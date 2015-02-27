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
 * Implements Vista Blue theme
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class DarkBlueTheme extends AbstractTheme
{

	@Override
	public ThemeInfo getThemeInfo() {
		return new ThemeInfo("Dark Blue", DarkBlueTheme.class, PgsLookAndFeel.class);
	}
	
    @Override
    protected void configure()
    {
    	UIDefaults defaults = UIManager.getDefaults();
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("MenuItem.background", new Color(255, 255, 255));
        m.put("MenuBar.background", new Color(255, 255, 255));
        defaults.putAll(m);
        BlackTheme vistaTheme = new BlackTheme()
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
        return new Color(67, 87, 113);
    }

    public Font getMenubarFont()
    {
        return MetalLookAndFeel.getMenuTextFont();
    }

    public Color getMenubarBackgroundColor()
    {
        return new Color(242, 241, 240);
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
        return new Color(50, 50, 50);
    }

    public Color getSidebarBackgroundStartColor()
    {
        return new Color(90, 90, 90);
    }

    public Color getSidebarBorderColor()
    {
        return getBackgroundColor();
    }

    public Color getSidebarElementBackgroundColor()
    {
        return getBackgroundColor();
    }

    public Color getSidebarElementTitleBackgroundEndColor()
    {
        return new Color(54, 69, 89);
    }

    public Color getSidebarElementTitleBackgroundStartColor()
    {
        return new Color(54, 69, 89);
    }

    public Color getSidebarElementForegroundColor()
    {
        return Color.WHITE;
    }

    public Color getSidebarElementTitleOverColor()
    {
        return Color.WHITE;
    }

    public Color getStatusbarBackgroundColor()
    {
        return new Color(100, 100, 100);
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
        return new Color(120, 120, 120);
    }

    public Color getWelcomeBackgroundStartColor()
    {
        return new Color(150, 150, 150);
    }

    public Color getWelcomeBigForegroundColor()
    {
        return Color.WHITE;
    }

    public Color getWelcomeBigRolloverForegroundColor()
    {
        return new Color(255, 203, 151);
    }

    
    private class BlackTheme extends PgsTheme
    {
        public BlackTheme()
        {
            super("Black");

            setSecondary3(new ColorUIResource(new Color(224, 231, 242)));
            setSecondary2(new ColorUIResource(0xFDFDFD));
            setSecondary1(new ColorUIResource(0x8E8F8F));

            setPrimary1(new ColorUIResource(0x3c7fb1));
            setPrimary2(new ColorUIResource(0xaadcf8));
            setPrimary3(new ColorUIResource(0xdff2fc));

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
                    new ColorUIResource(70, 70, 70),
                    "MenuBar.gradientEnd",
                    new ColorUIResource(70, 70, 70),

                    "MenuBarMenu.isFlat",
                    Boolean.FALSE,
                    "MenuBarMenu.foreground",
                    getWhite(),
                    "MenuBarMenu.rolloverBackground.gradientStart",
                    new ColorUIResource(130, 130, 130),
                    "MenuBarMenu.rolloverBackground.gradientMiddle",
                    new ColorUIResource(130, 130, 130),
                    "MenuBarMenu.rolloverBackground.gradientEnd",
                    new ColorUIResource(130, 130, 130),
                    "MenuBarMenu.selectedBackground.gradientStart",
                    new ColorUIResource(130, 130, 130),
                    "MenuBarMenu.selectedBackground.gradientMiddle",
                    new ColorUIResource(130, 130, 130),
                    "MenuBarMenu.selectedBackground.gradientEnd",
                    new ColorUIResource(130, 130, 130),
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
                    new ColorUIResource(253, 236, 178),
                    "TabbedPane.background",
                    new ColorUIResource(Color.WHITE),
                    "TabbedPane.selectedForeground",
                    new ColorUIResource(Color.BLACK),

            });
        }
    }
    
}
