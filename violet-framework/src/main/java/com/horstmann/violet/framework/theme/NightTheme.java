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
import java.util.HashMap;
import java.util.Map;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;

import com.pagosoft.plaf.PgsLookAndFeel;
import com.pagosoft.plaf.PgsTheme;
import com.pagosoft.plaf.PlafOptions;

/**
 * Implements Vista Blue theme.
 */
public class NightTheme extends AbstractTheme
{

	/* (non-Javadoc)
	 * @see com.horstmann.violet.framework.theme.ITheme#getThemeInfo()
	 */
	@Override
	public ThemeInfo getThemeInfo() {
		return new ThemeInfo("Night", NightTheme.class, PgsLookAndFeel.class);
	}
	
    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.AbstractTheme#configure()
     */
    @Override
    protected void configure()
    {
    	UIDefaults defaults = UIManager.getDefaults();
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("MenuItem.background", new Color(128, 128, 128));
        m.put("MenuBar.background", new Color(128, 128, 128));
        defaults.putAll(m);
        BlackTheme vistaTheme = new BlackTheme()
        {
            public ColorUIResource getMenuBackground()
            {
                return new ColorUIResource(new Color(128, 128, 128));
            }

            public ColorUIResource getSecondary3()
            {
                return new ColorUIResource(new Color(95, 95, 95));
            }
            
            public ColorUIResource getWindowBackground()
            {
            	return new ColorUIResource(new Color(128, 128, 128));
            }
        };

        PgsLookAndFeel.setCurrentTheme(vistaTheme);
    }
	
    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getWhiteColor()
     */
    public Color getWhiteColor()
    {
        return Color.WHITE;
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getBlackColor()
     */
    public Color getBlackColor()
    {
        return Color.BLACK;
    }
    
    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getGridBackgroundColor()
     */
	public Color getGridBackgroundColor() {
		return new Color(80, 80, 80);
	}

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getGridColor()
     */
    public Color getGridColor()
    {
        return new Color(90, 90, 90);
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getBackgroundColor()
     */
    public Color getBackgroundColor()
    {
        return new Color(95, 95, 95);
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getMenubarFont()
     */
    public Font getMenubarFont()
    {
        return MetalLookAndFeel.getMenuTextFont();
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getMenubarBackgroundColor()
     */
    public Color getMenubarBackgroundColor()
    {
        return new Color(75, 75, 75);
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getMenubarForegroundColor()
     */
    public Color getMenubarForegroundColor()
    {
        return new Color(128, 128, 128);
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getRolloverButtonDefaultColor()
     */
    public Color getRolloverButtonDefaultColor()
    {
        return getMenubarBackgroundColor();
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getRolloverButtonRolloverBorderColor()
     */
    public Color getRolloverButtonRolloverBorderColor()
    {
        return getMenubarForegroundColor();
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getRolloverButtonRolloverColor()
     */
    public Color getRolloverButtonRolloverColor()
    {
        return getMenubarBackgroundColor();
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getSidebarBackgroundEndColor()
     */
    public Color getSidebarBackgroundEndColor()
    {
        return new Color(75, 75, 75);
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getSidebarBackgroundStartColor()
     */
    public Color getSidebarBackgroundStartColor()
    {
        return new Color(75, 75, 75);
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getSidebarBorderColor()
     */
    public Color getSidebarBorderColor()
    {
        return getBackgroundColor();
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getSidebarElementBackgroundColor()
     */
    public Color getSidebarElementBackgroundColor()
    {
        return getBackgroundColor();
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getSidebarElementTitleBackgroundEndColor()
     */
    public Color getSidebarElementTitleBackgroundEndColor()
    {
        return new Color(0, 0, 0);
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getSidebarElementTitleBackgroundStartColor()
     */
    public Color getSidebarElementTitleBackgroundStartColor()
    {
        return new Color(0, 0, 0);
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getSidebarElementForegroundColor()
     */
    public Color getSidebarElementForegroundColor()
    {
        return new Color(160, 160, 160);
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getSidebarElementTitleOverColor()
     */
    public Color getSidebarElementTitleOverColor()
    {
        return new Color(190, 190, 190);
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getStatusbarBackgroundColor()
     */
    public Color getStatusbarBackgroundColor()
    {
        return new Color(75,75,75);
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getStatusbarBorderColor()
     */
    public Color getStatusbarBorderColor()
    {
        return getMenubarBackgroundColor();
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getToggleButtonFont()
     */
    public Font getToggleButtonFont()
    {
        return MetalLookAndFeel.getMenuTextFont().deriveFont(Font.PLAIN);
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getToggleButtonSelectedBorderColor()
     */
    public Color getToggleButtonSelectedBorderColor()
    {
        return new Color(100, 100, 100);
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getToggleButtonSelectedColor()
     */
    public Color getToggleButtonSelectedColor()
    {
        return new Color(85, 85, 85);
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getToggleButtonUnselectedColor()
     */
    public Color getToggleButtonUnselectedColor()
    {
        return getSidebarElementBackgroundColor();
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getWelcomeBigFont()
     */
    public Font getWelcomeBigFont()
    {
        return MetalLookAndFeel.getWindowTitleFont().deriveFont((float) 28.0);
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getWelcomeSmallFont()
     */
    public Font getWelcomeSmallFont()
    {
        return MetalLookAndFeel.getWindowTitleFont().deriveFont((float) 12.0).deriveFont(Font.PLAIN);
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getWelcomeBackgroundEndColor()
     */
    public Color getWelcomeBackgroundEndColor()
    {
        return new Color(90, 90, 90);
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getWelcomeBackgroundStartColor()
     */
    public Color getWelcomeBackgroundStartColor()
    {
        return new Color(60, 60, 60);
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getWelcomeBigForegroundColor()
     */
    public Color getWelcomeBigForegroundColor()
    {
        return Color.WHITE;
    }

    /* (non-Javadoc)
     * @see com.horstmann.violet.framework.theme.ITheme#getWelcomeBigRolloverForegroundColor()
     */
    public Color getWelcomeBigRolloverForegroundColor()
    {
        return new Color(75, 75, 75);
    }

    
    /**
     * The Class BlackTheme.
     */
    private class BlackTheme extends PgsTheme
    {
        
        /**
         * Instantiates a new black theme.
         */
        public BlackTheme()
        {
            super("Black");

            setSecondary3(new ColorUIResource(0x808080));
            setSecondary2(new ColorUIResource(0x4b4b4b));
            setSecondary1(new ColorUIResource(0xa0a0a0));

            setPrimary1(new ColorUIResource(0x000000));
            setPrimary2(new ColorUIResource(0xa0a0a0));
            setPrimary3(new ColorUIResource(0xa0a0a0));

            setBlack(new ColorUIResource(Color.BLACK));
            setWhite(new ColorUIResource(Color.WHITE));

            PlafOptions.setOfficeScrollBarEnabled(true);
            PlafOptions.setVistaStyle(true);
            PlafOptions.useBoldFonts(false);

            setDefaults(new Object[]
            {
                    "MenuBar.isFlat",
                    Boolean.FALSE,
                    "MenuBar.gradientStart",
                    new ColorUIResource(70, 70, 70),
                    "MenuBar.gradientMiddle",
                    new ColorUIResource(35, 35, 35),
                    "MenuBar.gradientEnd",
                    new ColorUIResource(0, 0, 0),

                    "MenuBarMenu.isFlat",
                    Boolean.FALSE,
                    "MenuBarMenu.foreground",
                    getWhite(),
                    "MenuBarMenu.rolloverBackground.gradientStart",
                    new ColorUIResource(105, 105, 105),
                    "MenuBarMenu.rolloverBackground.gradientMiddle",
                    new ColorUIResource(70, 70, 70),
                    "MenuBarMenu.rolloverBackground.gradientEnd",
                    new ColorUIResource(35, 35, 35),
                    "MenuBarMenu.selectedBackground.gradientStart",
                    new ColorUIResource(105, 105, 105),
                    "MenuBarMenu.selectedBackground.gradientMiddle",
                    new ColorUIResource(70, 70, 70),
                    "MenuBarMenu.selectedBackground.gradientEnd",
                    new ColorUIResource(35, 35, 35),
                    "MenuBarMenu.rolloverBorderColor",
                    getPrimary3(),
                    "MenuBarMenu.selectedBorderColor",
                    getPrimary3(),

                    "Menu.gradientStart",
                    getPrimary3(),
                    "Menu.gradientEnd",
                    getPrimary3(),
                    "Menu.gradientMiddle",
                    getPrimary3(),
                    "Menu.isFlat",
                    Boolean.FALSE,

                    "MenuItem.gradientStart",
                    getPrimary3(),
                    "MenuItem.gradientEnd",
                    getPrimary3(),
                    "MenuItem.gradientMiddle",
                    getPrimary3(),
                    "MenuItem.isFlat",
                    Boolean.FALSE,

                    "CheckBoxMenuItem.gradientStart",
                    getPrimary3(),
                    "CheckBoxMenuItem.gradientEnd",
                    getPrimary3(),
                    "CheckBoxMenuItem.gradientMiddle",
                    getPrimary3(),
                    "CheckBoxMenuItem.isFlat",
                    Boolean.FALSE,

                    "RadioButtonMenuItem.gradientStart",
                    getPrimary3(),
                    "RadioButtonMenuItem.gradientEnd",
                    getPrimary3(),
                    "RadioButtonMenuItem.gradientMiddle",
                    getPrimary3(),
                    "RadioButtonMenuItem.isFlat",
                    Boolean.FALSE,

                    "Button.rolloverGradientStart",
                    getPrimary3(),
                    "Button.rolloverGradientEnd",
                    getPrimary3(),
                    "Button.selectedGradientStart",
                    getPrimary3(),
                    "Button.selectedGradientEnd",
                    getPrimary3(),
                    "Button.rolloverVistaStyle",
                    Boolean.TRUE,
                    "glow",
                    getPrimary1(),

                    "ToggleButton.rolloverGradientStart",
                    getPrimary3(),
                    "ToggleButton.rolloverGradientEnd",
                    getPrimary3(),
                    "ToggleButton.selectedGradientStart",
                    getPrimary3(),
                    "ToggleButton.selectedGradientEnd",
                    getPrimary3(),

                    "TabbedPane.selected",
                    new ColorUIResource(128, 128, 128),
                    "TabbedPane.background",
                    new ColorUIResource(Color.WHITE),
                    "TabbedPane.selectedForeground",
                    new ColorUIResource(Color.BLACK),

            });
        }
    }
    
}
