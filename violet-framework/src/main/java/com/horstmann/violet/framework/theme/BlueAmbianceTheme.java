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

package com.horstmann.violet.framework.theme;

import java.awt.Color;
import java.awt.Font;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.pagosoft.plaf.PgsLookAndFeel;
import com.pagosoft.plaf.themes.VistaTheme;

/**
 * Implements Vista Blue theme
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class BlueAmbianceTheme extends AbstractTheme
{


	@Override
	public ThemeInfo getThemeInfo() {
		return new ThemeInfo("Blue Ambiance", BlueAmbianceTheme.class, PgsLookAndFeel.class);
	}
	
	
    @Override
    protected void configure()
    {
    	VistaTheme vistaTheme = new VistaTheme()
        {
            public ColorUIResource getMenuBackground()
            {
                return new ColorUIResource(new Color(255, 255, 255));
            }

            public ColorUIResource getSecondary3()
            {
                return new ColorUIResource(new Color(224, 231, 242));
            }
        };
        PgsLookAndFeel.setCurrentTheme(vistaTheme);
    }

    
    
    public Color getWhiteColor()
    {
        return Color.WHITE;
    }

    public Color getBlackColor()
    {
        return Color.BLACK;
    }

    public Color getGridColor()
    {
        return new Color(250, 250, 250);
    }

    public Color getBackgroundColor()
    {
        return new Color(224, 231, 242);
    }

    public Font getMenubarFont()
    {
        return MetalLookAndFeel.getMenuTextFont();
    }

    public Color getMenubarBackgroundColor()
    {
        return new Color(73, 103, 145);
    }

    public Color getMenubarForegroundColor()
    {
        return Color.WHITE;
    }

    public Color getRolloverButtonDefaultColor()
    {
        return getMenubarBackgroundColor();
    }

    public Color getRolloverButtonRolloverBorderColor()
    {
        return getMenubarForegroundColor();
    }

    public Color getRolloverButtonRolloverColor()
    {
        return getMenubarBackgroundColor();
    }

    public Color getSidebarBackgroundEndColor()
    {
        return new Color(125, 156, 201);
    }

    public Color getSidebarBackgroundStartColor()
    {
        return getMenubarBackgroundColor();
    }

    public Color getSidebarBorderColor()
    {
        return getGridColor();
    }

    public Color getSidebarElementBackgroundColor()
    {
        return getBackgroundColor();
    }

    public Color getSidebarElementTitleBackgroundEndColor()
    {
        return getMenubarBackgroundColor();
    }

    public Color getSidebarElementTitleBackgroundStartColor()
    {
        return getMenubarBackgroundColor().darker();
    }

    public Color getSidebarElementForegroundColor()
    {
        return getBackgroundColor();
    }

    public Color getSidebarElementTitleOverColor()
    {
        return getBackgroundColor().brighter();
    }

    public Color getStatusbarBackgroundColor()
    {
        return getMenubarBackgroundColor();
    }

    public Color getStatusbarBorderColor()
    {
        return getMenubarBackgroundColor();
    }

    public Font getToggleButtonFont()
    {
        return MetalLookAndFeel.getMenuTextFont().deriveFont(Font.PLAIN);
    }

    public Color getToggleButtonSelectedBorderColor()
    {
        return new Color(247, 154, 24);
    }

    public Color getToggleButtonSelectedColor()
    {
        return new Color(255, 203, 107);
    }

    public Color getToggleButtonUnselectedColor()
    {
        return getSidebarElementBackgroundColor();
    }

    public Font getWelcomeBigFont()
    {
        return MetalLookAndFeel.getWindowTitleFont().deriveFont((float) 28.0);
    }

    public Font getWelcomeSmallFont()
    {
        return MetalLookAndFeel.getWindowTitleFont().deriveFont((float) 12.0).deriveFont(Font.PLAIN);
    }

    public Color getWelcomeBackgroundEndColor()
    {
        return getSidebarBackgroundStartColor();
    }

    public Color getWelcomeBackgroundStartColor()
    {
        return getSidebarBackgroundEndColor().brighter();
    }

    public Color getWelcomeBigForegroundColor()
    {
        return Color.WHITE;
    }

    public Color getWelcomeBigRolloverForegroundColor()
    {
        return new Color(255, 203, 151);
    }

}
