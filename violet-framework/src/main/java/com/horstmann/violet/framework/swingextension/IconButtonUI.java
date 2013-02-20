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

package com.horstmann.violet.framework.swingextension;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.AreaAveragingScaleFilter;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.awt.image.ReplicateScaleFilter;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.basic.BasicButtonUI;

public class IconButtonUI extends BasicButtonUI
{

    public IconButtonUI()
    {
        super();
    }
    
    public IconButtonUI(double scalingValue) {
        this.scalingValue = scalingValue;
    }

    protected void installDefaults(AbstractButton b)
    {
        super.installDefaults(b);
        b.setOpaque(false);
        b.setBorderPainted(false);
        b.setRolloverEnabled(true);
        b.setBorder(new EmptyBorder(0, 0, 0, 0));
        prepareIcons(b);
    }
    
    @Override
    public void installUI(JComponent c)
    {
        AbstractButton b = (AbstractButton) c;
        this.originalIcon = b.getIcon();
        super.installUI(c);
    }
    
    @Override
    public void uninstallUI(JComponent c)
    {
        AbstractButton b = (AbstractButton) c;
        b.setIcon(this.originalIcon);
        super.uninstallUI(c);
    }

    public void paint(Graphics g, JComponent c)
    {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();

        if (model.isArmed() && model.isPressed())
        {
            b.setIcon(darkerIcon);
        }
        else if (model.isRollover())
        {
            b.setIcon(brighterIcon);
        }
        else
        {
            b.setIcon(baseIcon);
        }
        super.paint(g, c);
    }

    /**
     * Prepares icons to simulate button behaviour
     */
    private void prepareIcons(AbstractButton b)
    {
        baseIcon = b.getIcon();

        Image baseImage = getImage(baseIcon);
        if (this.scalingValue > 0) {
            baseImage = getScaledImage(baseImage,(int) (baseIcon.getIconWidth() * this.scalingValue), (int) (baseIcon.getIconHeight() * this.scalingValue));
            baseIcon = getIcon(baseImage);
        }
        Image brightenImage = changeBrightness(baseImage, true, 30);
        Image darkenImage = changeBrightness(baseImage, false, 20);

        brighterIcon = getIcon(brightenImage);
        darkerIcon = getIcon(darkenImage);
    }

    /**
     * Creates an image from an icon.
     */
    private Image getImage(Icon icon)
    {
        if (icon instanceof ImageIcon)
        {
            return ((ImageIcon) icon).getImage();
        }
        else
        {
            BufferedImage buffer = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = buffer.getGraphics();
            icon.paintIcon(new JLabel(), g, 0, 0);
            g.dispose();
            return buffer;
        }
    }
    
    /**
     * @param img
     * @return an icon from an image
     */
    private Icon getIcon(Image img)
    {
        return new IconUIResource(new ImageIcon(img));
    }
    
    /**
     * @param img to scale
     * @param newWidth desired width
     * @param newHeight desired height
     * @return a scaled image
     */
    private Image getScaledImage(Image img, int newWidth, int newHeight) {
        ImageFilter filter = new AreaAveragingScaleFilter(newWidth, newHeight);
        ImageProducer prod = new FilteredImageSource(img.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(prod);
    }

    /**
     * Change the brightness of an image
     */
    private Image changeBrightness(Image img, final boolean brighten, final int percent)
    {
        ImageFilter filter = new RGBImageFilter()
        {

            /*
             * (non-Javadoc)
             * 
             * @see java.awt.image.RGBImageFilter#filterRGB(int, int, int)
             */
            public int filterRGB(int x, int y, int rgb)
            {
                return (rgb & 0xff000000) | (filter(rgb >> 16) << 16) | (filter(rgb >> 8) << 8) | (filter(rgb));
            }

            /**
             * Brighens or darkens a single r/g/b value.
             */
            private int filter(int color)
            {
                color = color & 0xff;
                if (brighten)
                {
                    color = (255 - ((255 - color) * (100 - percent) / 100));
                }
                else
                {
                    color = (color * (100 - percent) / 100);
                }
                if (color < 0) color = 0;
                if (color > 255) color = 255;
                return color;
            }

        };
        ImageProducer prod = new FilteredImageSource(img.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(prod);
    }

    private Icon baseIcon;
    private Icon darkerIcon;
    private Icon brighterIcon;
    private Icon originalIcon;
    
    /**
     * Allows to scale icon image 
     */
    private double scalingValue = -1;

}
