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

package com.horstmann.violet.eclipseplugin.editors;

import java.awt.Color;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * This class picks Eclipse colors to allow to integrate this software as a plugin. By the way, it will respect as long as possible
 * Eclipse look and feel.
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class EclipseColorPicker
{

    /**
     * Default constructor
     * 
     * @param display current workspace display instance
     */
    public EclipseColorPicker(Display display)
    {
        this.display = display;
    }

    /**
     * @return background color
     */
    public Color getBackGroundColor()
    {
        if (this.backgroundColor == null)
        {
            RGB rgb = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND).getRGB();
            this.backgroundColor = new Color(rgb.red, rgb.green, rgb.blue);
        }
        return this.backgroundColor;
    }

    /**
     * @return foreground color
     */
    public Color getForeGroundColor()
    {
        if (this.foregroundColor == null)
        {
            RGB rgb = display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND).getRGB();
            this.foregroundColor = new Color(rgb.red, rgb.green, rgb.blue);
        }
        return this.foregroundColor;
    }

    /**
     * @return title background color
     */
    public Color getTitleBgColor()
    {
        if (this.titleBgColor == null)
        {
            RGB rgb = display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND).getRGB();
            this.titleBgColor = new Color(rgb.red, rgb.green, rgb.blue);
        }
        return this.titleBgColor;
    }

    /**
     * @return title background gradiant color
     */
    public Color getTitleBgColorGradient()
    {
        if (this.titleBgColorGradient == null)
        {
            RGB rgb = display.getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT).getRGB();
            this.titleBgColorGradient = new Color(rgb.red, rgb.green, rgb.blue);
        }
        return this.titleBgColorGradient;
    }

    /**
     * @return title foreground color
     */
    public Color getTitleFgColor()
    {
        if (this.titleFgColor == null)
        {
            RGB rgb = display.getSystemColor(SWT.COLOR_TITLE_FOREGROUND).getRGB();
            this.titleFgColor = new Color(rgb.red, rgb.green, rgb.blue);
        }
        return this.titleFgColor;
    }

    /**
     * @return border color
     */
    public Color getBorderColor()
    {
        if (this.borderColor == null)
        {
            RGB rgb = display.getSystemColor(SWT.COLOR_WIDGET_BORDER).getRGB();
            this.borderColor = new Color(rgb.red, rgb.green, rgb.blue);
        }
        return this.borderColor;
    }

    /**
     * @return light shadow color
     */
    public Color getLightShadowColor()
    {
        if (this.lightShadowColor == null)
        {
            RGB rgb = display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW).getRGB();
            this.lightShadowColor = new Color(rgb.red, rgb.green, rgb.blue);
        }
        return this.lightShadowColor;
    }

    /**
     * @return normal shadow color
     */
    public Color getNormalShadowColor()
    {
        if (this.normalShadowColor == null)
        {
            RGB rgb = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW).getRGB();
            this.normalShadowColor = new Color(rgb.red, rgb.green, rgb.blue);
        }
        return this.normalShadowColor;
    }

    private Display display;
    private Color backgroundColor;
    private Color foregroundColor;
    private Color titleBgColor;
    private Color titleBgColorGradient;
    private Color titleFgColor;
    private Color borderColor;
    private Color lightShadowColor;
    private Color normalShadowColor;

}
