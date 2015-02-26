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

import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

/**
 * Java Metal based theme
 * 
 * @author ALexandre de Pellegrin
 * 
 */
public class ClassicMetalTheme extends AbstractTheme
{

    /**
     * Default constructor
     */
    public ClassicMetalTheme()
    {
        initializeLookAndFeel();
    }

    @Override
    public ThemeInfo getThemeInfo() {
    	return new ThemeInfo("Metal", ClassicMetalTheme.class, MetalLookAndFeel.class);
    }
    
    
    /**
     * Inits Metal laf
     */
    private void initializeLookAndFeel()
    {
        MetalTheme defaultMetalTheme = new DefaultMetalTheme();
        MetalLookAndFeel.setCurrentTheme(defaultMetalTheme);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.display.clipboard.theme.AbstractTheme#setup()
     */
    protected void configure()
    {
    }

    public Color getBlackColor()
    {
        return MetalLookAndFeel.getBlack();
    }

    public Color getWhiteColor()
    {
        return MetalLookAndFeel.getWhite();
    }

    public Color getGridColor()
    {
        return new Color(250, 250, 250);
    }

    public Color getBackgroundColor()
    {
        return MetalLookAndFeel.getMenuBackground();
    }

    public Font getMenubarFont()
    {
        return MetalLookAndFeel.getMenuTextFont();
    }

    public Color getMenubarBackgroundColor()
    {
        return MetalLookAndFeel.getMenuBackground();
    }

    public Color getMenubarForegroundColor()
    {
        return MetalLookAndFeel.getMenuForeground();
    }

    public Color getRolloverButtonDefaultColor()
    {
        return MetalLookAndFeel.getMenuBackground();
    }

    public Color getRolloverButtonRolloverBorderColor()
    {
        return MetalLookAndFeel.getMenuForeground();
    }

    public Color getRolloverButtonRolloverColor()
    {
        return MetalLookAndFeel.getMenuBackground();
    }

    public Color getSidebarBackgroundEndColor()
    {
        return MetalLookAndFeel.getMenuSelectedBackground();
    }

    public Color getSidebarBackgroundStartColor()
    {
        return MetalLookAndFeel.getMenuSelectedBackground();
    }

    public Color getSidebarBorderColor()
    {
        return MetalLookAndFeel.getMenuBackground();
    }

    public Color getSidebarElementBackgroundColor()
    {
        return MetalLookAndFeel.getMenuBackground();
    }

    public Color getSidebarElementTitleBackgroundEndColor()
    {
        return MetalLookAndFeel.getMenuBackground();
    }

    public Color getSidebarElementTitleBackgroundStartColor()
    {
        return MetalLookAndFeel.getMenuBackground().darker();
    }

    public Color getSidebarElementForegroundColor()
    {
        return MetalLookAndFeel.getWindowBackground();
    }

    public Color getSidebarElementTitleOverColor()
    {
        return MetalLookAndFeel.getWindowBackground().brighter();
    }

    public Color getStatusbarBackgroundColor()
    {
        return MetalLookAndFeel.getMenuBackground();
    }

    public Color getStatusbarBorderColor()
    {
        return MetalLookAndFeel.getMenuBackground();
    }

    public Font getToggleButtonFont()
    {
        return MetalLookAndFeel.getMenuTextFont().deriveFont(Font.PLAIN);
    }

    public Color getToggleButtonSelectedBorderColor()
    {
        return MetalLookAndFeel.getMenuSelectedBackground();
    }

    public Color getToggleButtonSelectedColor()
    {
        return MetalLookAndFeel.getMenuSelectedBackground();
    }

    public Color getToggleButtonUnselectedColor()
    {
        return MetalLookAndFeel.getMenuBackground();
    }

    public Font getWelcomeSmallFont()
    {
        return MetalLookAndFeel.getWindowTitleFont().deriveFont((float) 12.0).deriveFont(Font.PLAIN);
    }

    public Font getWelcomeBigFont()
    {
        return MetalLookAndFeel.getWindowTitleFont().deriveFont((float) 28.0);
    }

    public Color getWelcomeBackgroundEndColor()
    {
        return MetalLookAndFeel.getMenuBackground();
    }

    public Color getWelcomeBackgroundStartColor()
    {
        return MetalLookAndFeel.getMenuBackground().brighter();
    }

    public Color getWelcomeBigForegroundColor()
    {
        return MetalLookAndFeel.getMenuSelectedBackground();
    }

    public Color getWelcomeBigRolloverForegroundColor()
    {
        return getWelcomeBigForegroundColor().darker();
    }

}
