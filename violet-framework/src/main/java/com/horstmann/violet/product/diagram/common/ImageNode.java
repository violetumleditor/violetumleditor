/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.product.diagram.common;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.beans.Transient;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.imageio.ImageIO;

import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.product.diagram.abstracts.node.CropInsets;
import com.horstmann.violet.product.diagram.abstracts.node.ICroppableNode;
import com.horstmann.violet.product.diagram.abstracts.node.IResizableNode;
import com.horstmann.violet.product.diagram.abstracts.node.RectangularNode;
import com.horstmann.violet.product.diagram.abstracts.node.ResizeDirection;
import com.horstmann.violet.product.diagram.abstracts.property.MultiLineString;

/**
 * A node in a diagram represented by an image
 */
public class ImageNode extends RectangularNode implements IResizableNode, ICroppableNode
{
    /**
     * Default construct a note node with a default size and color
     */
    public ImageNode(Image img)
    {
        text = new MultiLineString();
        text.setJustification(MultiLineString.Justification.CENTER);
        this.setImage(img);
    }

    /**
     * For internal use only.
     */
    public ImageNode()
    {
    	ResourceBundleInjector.getInjector().inject(this);
    	text = new MultiLineString();
        text.setJustification(MultiLineString.Justification.CENTER);
    }

    /**
     * Returns the current image displayed by this node.
     * <p>
     * If no image has been pasted or loaded from a file yet, returns the
     * default placeholder icon injected from the resource bundle.
     * </p>
     *
     * @return the current {@link Image}, never {@code null} in normal use
     */
    @Transient
    public Image getImage()
    {
        return this.image;
    }

    /**
     * JavaBeans persistence property used to store image pixels reliably.
     */
    public String getImageBase64()
    {
        if (!(this.image instanceof BufferedImage))
        {
            return "";
        }
        try
        {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ImageIO.write((BufferedImage) this.image, "png", bytes);
            return Base64.getEncoder().encodeToString(bytes.toByteArray());
        }
        catch (IOException e)
        {
            throw new RuntimeException("Unable to encode image for persistence", e);
        }
    }

    /**
     * JavaBeans persistence setter paired with getImageBase64().
     */
    public void setImageBase64(String imageBase64)
    {
        if (imageBase64 == null || imageBase64.isEmpty())
        {
            setImage((Image) null);
            return;
        }
        try
        {
            byte[] bytes = Base64.getDecoder().decode(imageBase64);
            BufferedImage decoded = ImageIO.read(new ByteArrayInputStream(bytes));
            setImage(decoded);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Unable to decode persisted image", e);
        }
    }

    /**
     * Sets current image.
      * The image is immediately converted to {@link BufferedImage} so diagram
      * persistence remains stable regardless of the concrete AWT implementation
      * supplied by the OS clipboard or elsewhere.
     *
     * @param img the source image (any AWT Image subtype)
     */
    public void setImage(Image img)
    {
        this.imageIcon = null; // invalidate scaled-icon cache
        if (img == null)
        {
            this.image = null;
            return;
        }
        if (img instanceof BufferedImage)
        {
            this.image = (BufferedImage) img;
            return;
        }
        // Force loading so getWidth/getHeight are available
        ImageIcon loader = new ImageIcon(img);
        int w = loader.getIconWidth();
        int h = loader.getIconHeight();
        if (w <= 0 || h <= 0)
        {
            // Cannot determine dimensions; store a 1×1 transparent placeholder
            // so that the field always holds a BufferedImage.
            this.image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            return;
        }
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        this.image = bi;
    }

    @Override
    public Rectangle2D getBounds()
    {
        return getCroppedBounds();
    }

    @Override
    public Point2D getLocationOnGraph()
    {
        Point2D base = super.getLocationOnGraph();
        CropInsets ci = getCropInsets();
        if (ci == null || ci.isEmpty())
        {
            return base;
        }
        return new Point2D.Double(base.getX() + ci.getLeft(), base.getY() + ci.getTop());
    }

    @Override
    public Rectangle2D getUncroppedBounds()
    {
        Point2D currentLocation = getLocation();
        double x = currentLocation.getX();
        double y = currentLocation.getY();
        double w = 0;
        double h = 0;
        if (this.image != null) {
            w = this.getImageWidth();
            h = this.getImageHeight();
        }
        Rectangle2D preferredSize = getPreferredSize();
        if (preferredSize != null) {
            if (preferredSize.getWidth() > 0) w = preferredSize.getWidth();
            if (preferredSize.getHeight() > 0) h = preferredSize.getHeight();
        }
        if (w <= 0) w = 100; // Use a sensible default if everything else is missing
        if (h <= 0) h = 100;
        return new Rectangle2D.Double(x, y, w, h);
    }



    /**
     * Gets the value of the text property.
     * 
     * @return the text inside the note
     */
    public MultiLineString getText()
    {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param newValue the text inside the note
     */
    public void setText(MultiLineString newValue)
    {
        text = newValue;
    }

    // ------------------------------------------------------------------
    // ICroppableNode
    // ------------------------------------------------------------------

    @Override
    public CropInsets getCropInsets()
    {
        return this.cropInsets;
    }

    @Override
    public void setCropInsets(CropInsets insets)
    {
        this.cropInsets = (insets != null) ? insets : new CropInsets();
    }

    @Override
    protected Rectangle2D getBoundsForConnectionPoint()
    {
        // AbstractEdge.getConnectionPoints() adds (locationOnGraph − location) =
        // (ci.left, ci.top) to every point returned here.  So to guarantee the
        // result lands exactly on the *snapped* visible bounds we subtract the
        // crop offset from the snapped origin:
        //   result_origin + (ci.left, ci.top) == getBounds().origin
        Rectangle2D b = getBounds();
        CropInsets ci = getCropInsets();
        double dx = (ci != null) ? ci.getLeft() : 0;
        double dy = (ci != null) ? ci.getTop()  : 0;
        return new Rectangle2D.Double(
                b.getX() - dx,
                b.getY() - dy,
                b.getWidth(),
                b.getHeight());
    }

    @Override
    public Map<ResizeDirection, Rectangle2D> getResizableDragPoints()
    {
        // Anchor resize handles to the corners of the visible (cropped) bounds
        Rectangle2D v = getBounds();
        int s = RESIZABLE_POINT_SIZE;
        Map<ResizeDirection, Rectangle2D> points = new LinkedHashMap<>();
        points.put(ResizeDirection.NW, makeVisibleHandle(v.getMinX(), v.getMinY(), s));
        points.put(ResizeDirection.NE, makeVisibleHandle(v.getMaxX(), v.getMinY(), s));
        points.put(ResizeDirection.SW, makeVisibleHandle(v.getMinX(), v.getMaxY(), s));
        points.put(ResizeDirection.SE, makeVisibleHandle(v.getMaxX(), v.getMaxY(), s));
        return points;
    }

    @Override
    public Map<ResizeDirection, Rectangle2D> getCropDragPoints()
    {
        // Use snapped bounds so crop handles align with the visible painted area
        Rectangle2D v = getBounds();
        int s = ICroppableNode.CROP_POINT_SIZE;
        Map<ResizeDirection, Rectangle2D> points = new LinkedHashMap<>();
        points.put(ResizeDirection.N, makeVisibleHandle(v.getCenterX(), v.getMinY(), s));
        points.put(ResizeDirection.S, makeVisibleHandle(v.getCenterX(), v.getMaxY(), s));
        points.put(ResizeDirection.W, makeVisibleHandle(v.getMinX(),    v.getCenterY(), s));
        points.put(ResizeDirection.E, makeVisibleHandle(v.getMaxX(),    v.getCenterY(), s));
        return points;
    }

    private static Rectangle2D makeVisibleHandle(double cx, double cy, int size)
    {
        return new Rectangle2D.Double(cx - size / 2.0, cy - size / 2.0, size, size);
    }
    
    
    private ImageIcon getImageIcon() {
    	if (this.imageIcon == null) 
    	{
            this.imageIcon = new ImageIcon(image);
    		Rectangle2D preferredSize = getPreferredSize();
    		if (preferredSize != null) {
    			int iconHeight = this.imageIcon.getIconHeight();
    			int iconWidth = this.imageIcon.getIconWidth();
    			if (iconWidth <= 0 || iconHeight <= 0) {
    				return this.imageIcon;
    			}
    			double iconRatio = (double) iconHeight / iconWidth;
    			double preferredWidth = preferredSize.getWidth();
    			double preferredHeight = preferredSize.getHeight();
    			double preferredRatio = preferredHeight / preferredWidth;
    			int targetWidth, targetHeight;
    			if (preferredRatio >= iconRatio) 
    			{
    				targetWidth = (int) Math.round((double) iconWidth * preferredHeight / iconHeight);
    				targetHeight = (int) preferredHeight;
    			}
    			else 
    			{
    				targetWidth = (int) preferredWidth;
    				targetHeight = (int) Math.round((double) iconHeight * preferredWidth / iconWidth);
    			}
    			if (targetWidth > 0 && targetHeight > 0) {
				this.imageIcon.setImage(scaleImageHighQuality(this.imageIcon.getImage(), iconWidth, iconHeight, targetWidth, targetHeight));
			}
    		}
    	}
		return this.imageIcon;
	}

	private Image scaleImageHighQuality(Image img, int srcWidth, int srcHeight, int targetWidth, int targetHeight) {
		targetWidth = Math.max(1, targetWidth);
		targetHeight = Math.max(1, targetHeight);
		BufferedImage result = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = result.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(img, 0, 0, targetWidth, targetHeight, null);
		g2d.dispose();
		return result;
	}


	
	@Override
	public void setPreferredSize(Rectangle2D size) {
		if (this.image != null && size.getWidth() > 0 && size.getHeight() > 0) {
			double iconRatio = (double) this.getImageHeight() / this.getImageWidth();
			double targetWidth = size.getWidth();
			double targetHeight = size.getHeight();
			if (targetHeight / targetWidth > iconRatio) {
				targetHeight = targetWidth * iconRatio;
			} else {
				targetWidth = targetHeight / iconRatio;
			}
			size = new Rectangle2D.Double(size.getX(), size.getY(), targetWidth, targetHeight);
		}
		super.setPreferredSize(size);
		this.imageIcon = null;
	}

	/*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.product.diagram.abstracts.AbstractNode#draw(java.awt.Graphics2D)
     */
    public void draw(Graphics2D g2)
    {
        // Backup state
        Color oldColor  = g2.getColor();
        Shape oldClip   = g2.getClip();
        Object oldInterpolation = g2.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
        Object oldRendering     = g2.getRenderingHint(RenderingHints.KEY_RENDERING);
        Object oldAntialiasing  = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        // High-quality rendering hints
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,     RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
        // Clip to the visible (cropped) bounds so only the uncropped area is painted
        Rectangle2D visibleBounds = getBounds();
        g2.clip(visibleBounds);
        // Draw image
        Rectangle2D bounds = getUncroppedBounds();
        if (this.image != null) {
            g2.drawImage(this.getImageIcon().getImage(),
                    (int) bounds.getX(),
                    (int) bounds.getY(),
                    (int) bounds.getWidth(),
                    (int) bounds.getHeight(),
                    this.getImageIcon().getImageObserver());
        }
        // Restore clip to draw text outside the cropped visible bounds
        g2.setClip(oldClip);

        // Draw text — draw it centered below the image.
        // If image is smaller than text, text remains centered relative to the image's center.
        g2.setColor(getTextColor());
        Rectangle2D textContentBounds = text.getBounds();
        double textWidth = Math.max(visibleBounds.getWidth(), textContentBounds.getWidth());
        double textX = visibleBounds.getCenterX() - textWidth / 2.0;
        
        Rectangle2D textBounds = new Rectangle2D.Double(
                textX,
                visibleBounds.getMaxY(),
                textWidth,
                textContentBounds.getHeight());
        text.draw(g2, textBounds);

        // Restore state
        g2.setColor(oldColor);
        if (oldInterpolation != null) g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldInterpolation);
        if (oldRendering     != null) g2.setRenderingHint(RenderingHints.KEY_RENDERING,     oldRendering);
        if (oldAntialiasing  != null) g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  oldAntialiasing);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.product.diagram.abstracts.RectangularNode#getShape()
     */
    public Shape getShape()
    {
        // Use visible (cropped) bounds so the selection outline matches the painted area
        Rectangle2D bounds = getBounds();
        GeneralPath path = new GeneralPath();
        path.moveTo((float) bounds.getX(),    (float) bounds.getY());
        path.lineTo((float) bounds.getMaxX(), (float) bounds.getY());
        path.lineTo((float) bounds.getMaxX(), (float) bounds.getMaxY());
        path.lineTo((float) bounds.getX(),    (float) bounds.getMaxY());
        path.closePath();
        return path;
    }

    /**
     * This method should be kept as private as long as it is used for serialization purpose
     * 
     * @return image content as an array
     * @throws InterruptedException
     */
    @SuppressWarnings("unused")
    private String getImageContent() throws InterruptedException
    {
        Image img = this.getImageIcon().getImage();
        
        
        int width = this.getImageIcon().getIconWidth();
        int height = this.getImageIcon().getIconHeight();
        int[] pixels = new int[width * height];
        PixelGrabber pg = new PixelGrabber(img, 0, 0, width, height, pixels, 0, width);
        pg.grabPixels();
        StringBuilder result = new StringBuilder();
        for (int i : pixels)
        {
            result.append(i).append(PIXEL_SEPARATOR);
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }
    
    
    /**
     * @return current image width
     */
    public int getImageWidth()
    {
        return this.getImageIcon().getIconWidth();
    }

    /**
     * @return current image height
     */
    public int getImageHeight()
    {
        return this.getImageIcon().getIconHeight();
    }

    /**
     * This method should be kept as private as long as it is used for serialization purpose. Replaces current imageIcon by a new
     * one created with the image content guven is parameters
     * 
     * @param pixels image content
     * @param width image width
     * @param height image height
     */
/*     @SuppressWarnings("unused")
    public void setImageContent(String imageContent, int width, int height)
    {
        StringTokenizer tokenizer = new StringTokenizer(imageContent, PIXEL_SEPARATOR);
        int[] pixels = new int[tokenizer.countTokens()];
        int counter = 0;
        while (tokenizer.hasMoreTokens())
        {
            String aPixel = tokenizer.nextToken();
            pixels[counter] = Integer.parseInt(aPixel);
            counter++;
        }
        MemoryImageSource mis = new MemoryImageSource(width, height, pixels, 0, width);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image img = tk.createImage(mis);
        this.setImage(img);
    } */

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.product.diagram.abstracts.RectangularNode#clone()
     */
    public ImageNode clone()
    {
        ImageNode cloned = (ImageNode) super.clone();
        cloned.text = text.clone();
        cloned.image = image;
        cloned.cropInsets = (cropInsets != null) ? cropInsets.clone() : null;
        return cloned;
    }

    private static final String PIXEL_SEPARATOR = ":";

    private transient ImageIcon imageIcon;

    /** Crop offsets applied to the displayed image area. */
    private CropInsets cropInsets = new CropInsets();

    @ResourceBundleBean(key = "imagenode.icon")
    private Image image;
    
    private MultiLineString text;
}
