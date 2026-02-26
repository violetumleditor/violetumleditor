package com.horstmann.violet.framework.file.persistence;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public class ImageConverter extends AbstractSingleValueConverter {

    @Override
    @SuppressWarnings("rawtypes")
    public boolean canConvert(final Class type) {
        return BufferedImage.class.isAssignableFrom(type);
    }

    @Override
    public String toString(final Object obj) {
        String result = imageToString((BufferedImage) obj, "png");
        System.out.println("ImageConverter.toString: " + result);
        return result;
    }

    @Override
    public Object fromString(final String str) {
        return stringToImage(str);
    }


    private String imageToString(BufferedImage image, String format) {
        try {
            if (!(image instanceof BufferedImage)) {
                // Convertir en BufferedImage si ce n'est pas déjà le cas
                BufferedImage bufferedImage = new BufferedImage(
                    image.getWidth(null),
                    image.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB
                );
                bufferedImage.getGraphics().drawImage(image, 0, 0, null);
                image = bufferedImage;
            }
    
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write((BufferedImage) image, format, baos);
            byte[] imageBytes = baos.toByteArray();
            baos.close();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error converting image to string: " + e.getMessage(), e);
        }
    }

    private BufferedImage stringToImage(String base64String) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64String);
            ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
            BufferedImage image = ImageIO.read(bais);
            bais.close();
            return image;
        } catch (Exception e) {
            throw new RuntimeException("Error converting string to image: " + e.getMessage(), e);
        }
    }

}
