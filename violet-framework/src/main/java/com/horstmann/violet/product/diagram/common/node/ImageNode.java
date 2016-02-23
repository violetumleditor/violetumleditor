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

package com.horstmann.violet.product.diagram.common.node;
//TODO Czy ta klasa jest wogole potrzebna

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;

import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.framework.property.text.MultiLineText;

/**
 * A node_old in a diagram represented by an image
 */
public class ImageNode extends ColorableNode
{
    /**
     * Default construct a note node_old with a default size and color
     */
    public ImageNode(Image img)
    {
        text = new MultiLineText();
//        text.setAlignment(MultiLineText.RIGHT);
        this.setImage(img);
    }

    /**
     * For internal use only.
     */
    public ImageNode()
    {
    	ResourceBundleInjector.getInjector().inject(this);
    	text = new MultiLineText();
//        text.setAlignment(MultiLineText.RIGHT);
    }

    /**
     * Sets current image
     * 
     * @param img
     */
    public void setImage(Image img)
    {
        this.imageIcon = new ImageIcon(img);
    }

    @Override
    public Rectangle2D getBounds()
    {
        Rectangle2D b = text.getBounds();
        Point2D currentLocation = getLocation();
        double x = currentLocation.getX();
        double y = currentLocation.getY();
        double w = Math.max(b.getWidth(), this.imageIcon.getIconWidth());
        double h = b.getHeight() + this.imageIcon.getIconHeight();
        Rectangle2D currentBounds = new Rectangle2D.Double(x, y, w, h);
        Rectangle2D snapperBounds = getGraph().getGridSticker().snap(currentBounds);
        return snapperBounds;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.horstmann.violet.product.diagram.abstracts.AbstractNode#checkRemoveEdge(com.horstmann.violet.product.diagram.abstracts
     * .Edge)
     */
    public void removeConnection(IEdge e)
    {
        if (e.getStartNode() == this) getGraph().removeNode(e.getEndNode());
    }

    /**
     * Gets the value of the text property.
     * 
     * @return the text inside the note
     */
    public MultiLineText getText()
    {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param newValue the text inside the note
     */
    public void setText(MultiLineText newValue)
    {
        text = newValue;
    }
    
    
    public ImageIcon getImageIcon() {
		return imageIcon;
	}

	public void setImageIcon(ImageIcon imageIcon) {
		this.imageIcon = imageIcon;
	}

	/*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.product.diagram.abstracts.AbstractNode#draw(java.awt.Graphics2D)
     */
    public void draw(Graphics2D g2)
    {
        // Backup current color;
        Color oldColor = g2.getColor();
        // Draw image
        Rectangle2D bounds = getBounds();
        g2.drawImage(this.imageIcon.getImage(), (int) bounds.getCenterX() - this.imageIcon.getIconWidth() / 2, (int) bounds.getY(),
                this.imageIcon.getImageObserver());
        // Draw text
        g2.setColor(getTextColor());
        Rectangle2D b = text.getBounds();
        Rectangle2D textBounds = new Rectangle2D.Double(bounds.getX(), bounds.getY() + this.imageIcon.getIconHeight(),
                b.getWidth(), b.getHeight());
        text.draw(g2, textBounds);
        // Restore first color
        g2.setColor(oldColor);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.product.diagram.abstracts.ColorableNode#getShape()
     */
    public Shape getShape()
    {
        Rectangle2D bounds = getBounds();
        GeneralPath path = new GeneralPath();
        path.moveTo((float) bounds.getX(), (float) bounds.getY());
        path.lineTo((float) bounds.getMaxX(), (float) bounds.getY());
        path.lineTo((float) bounds.getMaxX(), (float) bounds.getY());
        path.lineTo((float) bounds.getMaxX(), (float) bounds.getMaxY());
        path.lineTo((float) bounds.getX(), (float) bounds.getMaxY());
        path.closePath();
        return path;
    }

    /**
     * This method should be kept as private as long as it is used for serialization purpose
     * 
     * @return image content as an array
     * @throws InterruptedException
     */
    public String getImageContent() throws InterruptedException
    {
        Image img = this.imageIcon.getImage();
        int width = this.imageIcon.getIconWidth();
        int height = this.imageIcon.getIconHeight();
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
        return this.imageIcon.getIconWidth();
    }

    /**
     * @return current image height
     */
    public int getImageHeight()
    {
        return this.imageIcon.getIconHeight();
    }

    /**
     * This method should be kept as private as long as it is used for serialization purpose. Replaces current imageIcon by a new
     * one created with the image content guven is parameters
     * 
     * @param pixels image content
     * @param width image width
     * @param height image height
     */
    @SuppressWarnings("unused")
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
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.product.diagram.abstracts.ColorableNode#clone()
     */

    //TODO cos z tym zrobic
//    public ImageNode clone()
//    {
//        ImageNode cloned = (ImageNode) super.clone();
//        cloned.text = text.clone();
//        cloned.imageIcon = imageIcon;
//        return cloned;
//    }

    private static final String PIXEL_SEPARATOR = ":";

    @ResourceBundleBean(key = "imagenode.icon")
    private ImageIcon imageIcon;

    private MultiLineText text;
}
