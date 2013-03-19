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
import java.awt.Font;

import javax.swing.plaf.metal.MetalLookAndFeel;

import com.horstmann.violet.framework.theme.AbstractTheme;
import com.horstmann.violet.framework.theme.ThemeInfo;


/**
 * Eclipse theme used when the software run as an embedded plugin
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class EclipseTheme extends AbstractTheme {

	/**
	 * Default constructor
	 * 
	 * @param colorManager
	 *            handles color from the Eclipse environement
	 */
	public EclipseTheme(EclipseColorPicker colorManager) {
		this.colorManager = colorManager;
	}

	
	@Override
	public ThemeInfo getThemeInfo() {
		return new ThemeInfo("Eclipse", EclipseTheme.class, MetalLookAndFeel.class);
	}
	
	@Override
	protected void configure() {
	}

	public Color getBlackColor() {
		return Color.BLACK;
	}

	public Color getWhiteColor() {
		return Color.WHITE;
	}

	public Color getGridColor() {
		return new Color(240, 240, 240);
	}

	public Color getBackgroundColor() {
		return colorManager.getBackGroundColor();
	}

	public Font getMenubarFont() {
		return MetalLookAndFeel.getMenuTextFont();
	}

	public Color getMenubarBackgroundColor() {
		return colorManager.getBackGroundColor();
	}

	public Color getMenubarForegroundColor() {
		return colorManager.getForeGroundColor();
	}

	public Color getRolloverButtonDefaultColor() {
		return colorManager.getBackGroundColor();
	}

	public Color getRolloverButtonRolloverBorderColor() {
		return colorManager.getForeGroundColor();
	}

	public Color getRolloverButtonRolloverColor() {
		return colorManager.getBackGroundColor();
	}

	public Color getSidebarBackgroundEndColor() {
		return colorManager.getBackGroundColor();
	}

	public Color getSidebarBackgroundStartColor() {
		return colorManager.getBackGroundColor();
	}

	public Color getSidebarBorderColor() {
		return colorManager.getNormalShadowColor();
	}

	public Color getSidebarElementBackgroundColor() {
		return colorManager.getBackGroundColor();
	}

	public Color getSidebarElementTitleBackgroundEndColor() {
		return colorManager.getTitleBgColorGradient();
	}

	public Color getSidebarElementTitleBackgroundStartColor() {
		return colorManager.getTitleBgColor();
	}

	public Color getSidebarElementForegroundColor() {
		return colorManager.getTitleFgColor();
	}

	public Color getSidebarElementTitleOverColor() {
		return colorManager.getTitleFgColor().brighter();
	}

	public Color getStatusbarBackgroundColor() {
		return colorManager.getBackGroundColor();
	}

	public Color getStatusbarBorderColor() {
		return colorManager.getNormalShadowColor();
	}

	public Font getToggleButtonFont() {
		return MetalLookAndFeel.getMenuTextFont().deriveFont(Font.PLAIN);
	}

	public Color getToggleButtonSelectedBorderColor() {
		return new Color(107, 144, 188);
	}

	public Color getToggleButtonSelectedColor() {
		return new Color(192, 220, 242);
	}

	public Color getToggleButtonUnselectedColor() {
		return getSidebarElementBackgroundColor();
	}

	public Font getWelcomeSmallFont() {
		return MetalLookAndFeel.getWindowTitleFont().deriveFont((float) 12.0)
				.deriveFont(Font.PLAIN);
	}

	public Font getWelcomeBigFont() {
		return MetalLookAndFeel.getWindowTitleFont().deriveFont((float) 28.0);
	}

	public Color getWelcomeBackgroundEndColor() {
		return getSidebarBackgroundStartColor();
	}

	public Color getWelcomeBackgroundStartColor() {
		return getSidebarBackgroundEndColor().brighter();
	}

	public Color getWelcomeBigForegroundColor() {
		return Color.WHITE;
	}

	public Color getWelcomeBigRolloverForegroundColor() {
		return new Color(255, 203, 151);
	}

	/**
	 * Contains Eclipse colors
	 */
	private EclipseColorPicker colorManager;

}
