package com.horstmann.violet.framework.file.persistence;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * XStream converter for {@link BufferedImage} that deduplicates image data during
 * serialization. When the same image content appears more than once in a diagram,
 * the raw base-64 data is written only on the first occurrence (tagged with an
 * {@code imgId} attribute); subsequent occurrences emit only an {@code imgRef}
 * attribute that points back to the first occurrence, saving significant disk space.
 *
 * <p>The converter is <em>stateful</em>: a fresh instance must be created for every
 * {@code write} / {@code read} operation (which is already the case because
 * {@link XStreamBasedPersistenceService} creates a new XStream per call).</p>
 *
 * <p>The XML format is backward-compatible: old files that contain plain base-64
 * values (no {@code imgId} / {@code imgRef} attributes) are still read correctly.</p>
 */
public class ImageConverter implements Converter {

    /** Maps content-hash → assigned imgId (used during marshalling). */
    private final Map<String, String> hashToId = new HashMap<>();

    /** Maps imgId → decoded image (used during unmarshalling). */
    private final Map<String, BufferedImage> idToImage = new HashMap<>();

    private int counter = 0;

    // -------------------------------------------------------------------------
    // Converter contract
    // -------------------------------------------------------------------------

    @Override
    @SuppressWarnings("rawtypes")
    public boolean canConvert(final Class type) {
        return Image.class.isAssignableFrom(type);
    }

    @Override
    public void marshal(final Object source, final HierarchicalStreamWriter writer,
                        final MarshallingContext context) {
        BufferedImage image = toBufferedImage((Image) source);
        String base64 = imageToBase64(image);
        String hash = sha256Hex(base64);

        if (hashToId.containsKey(hash)) {
            // Duplicate image – only emit a lightweight reference.
            writer.addAttribute("imgRef", hashToId.get(hash));
        } else {
            // First occurrence – assign an ID, persist the data once.
            String id = "img-" + (++counter);
            hashToId.put(hash, id);
            writer.addAttribute("imgId", id);
            writer.setValue(base64);
        }
    }

    @Override
    public Object unmarshal(final HierarchicalStreamReader reader,
                            final UnmarshallingContext context) {
        String ref = reader.getAttribute("imgRef");
        if (ref != null) {
            // Reference to an already-decoded image.
            return idToImage.get(ref);
        }

        String base64 = reader.getValue();
        BufferedImage image = base64ToImage(base64);

        String id = reader.getAttribute("imgId");
        if (id != null) {
            idToImage.put(id, image);
        }
        return image;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private String imageToBase64(BufferedImage image) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] bytes = baos.toByteArray();
            baos.close();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Error converting image to base64: " + e.getMessage(), e);
        }
    }

    private BufferedImage base64ToImage(String base64) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            BufferedImage image = ImageIO.read(bais);
            bais.close();
            return image;
        } catch (Exception e) {
            throw new RuntimeException("Error converting base64 to image: " + e.getMessage(), e);
        }
    }

    /**
     * Converts any AWT {@link Image} to a {@link BufferedImage}.
     * If the source is already a BufferedImage it is returned as-is.
     */
    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        // Force loading so dimensions are available
        ImageIcon loader = new ImageIcon(img);
        int w = loader.getIconWidth();
        int h = loader.getIconHeight();
        if (w <= 0 || h <= 0) {
            // Absolute fallback: 1×1 transparent pixel
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return bi;
    }

    private String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            // Fallback: use the content directly as the key (still correct, just slower).
            return input;
        }
    }
}
