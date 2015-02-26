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

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;

/**
 * Currently supported JVM theme. This themes fakes swing composants to extract current look and feel theme colors
 * 
 * @author ALexandre de Pellegrin
 */
public class BasicTheme extends AbstractTheme
{

    /**
     * Default constructor
     * 
     * @param className
     */
    public BasicTheme(String className) throws ClassNotFoundException
    {
        this.lookAndFeelClass = (Class<? extends LookAndFeel>) Class.forName(className);
    }

	@Override
	public ThemeInfo getThemeInfo() {
		return new ThemeInfo("Basic", BasicTheme.class, this.lookAndFeelClass);
	}
    

    @Override
    protected void configure()
    {
        this.defaultGridColor = new Color(250, 250, 250);

        JMenuBar menuBar = new JMenuBar();
        this.basicMenubarBackground = menuBar.getBackground();
        this.basicMenubarForeground = menuBar.getForeground();
        this.basicMenubarFont = menuBar.getFont();

        JMenu menu = new JMenu();
        this.basicMenuBackground = menu.getBackground();
        this.basicMenuForeground = menu.getForeground();
        this.basicMenuFont = menu.getFont();

        JTextField textArea = new JTextField();
        this.basicWhite = textArea.getBackground();
        this.basicBlack = textArea.getForeground();

        JFrame frame = new JFrame();
        this.basicFrameBackground = frame.getBackground();
        frame.dispose();

        JProgressBar progressBar = new JProgressBar();
        this.basicProgressbarForeground = progressBar.getForeground();

        JTabbedPane pane = new JTabbedPane();
        this.basicTabbedPaneBackground = pane.getBackground();
    }

    public Color getBlackColor()
    {
        return this.basicBlack;
    }

    public Color getWhiteColor()
    {
        return this.basicWhite;
    }

    public Color getGridColor()
    {
        return this.defaultGridColor;
    }

    public Color getBackgroundColor()
    {
        return this.basicMenuBackground;
    }

    public Font getMenubarFont()
    {
        return this.basicMenubarFont;
    }

    public Color getMenubarBackgroundColor()
    {
        return this.basicMenubarBackground;
    }

    public Color getMenubarForegroundColor()
    {
        return this.basicMenubarForeground;
    }

    public Color getRolloverButtonDefaultColor()
    {
        return this.basicMenuBackground;
    }

    public Color getRolloverButtonRolloverBorderColor()
    {
        return this.basicMenuForeground;
    }

    public Color getRolloverButtonRolloverColor()
    {
        return this.basicMenuBackground;
    }

    public Color getSidebarBackgroundEndColor()
    {
        return this.basicTabbedPaneBackground;
    }

    public Color getSidebarBackgroundStartColor()
    {
        return this.basicTabbedPaneBackground;
    }

    public Color getSidebarBorderColor()
    {
        return this.basicMenuBackground;
    }

    public Color getSidebarElementBackgroundColor()
    {
        return this.basicMenuBackground;
    }

    public Color getSidebarElementTitleBackgroundEndColor()
    {
        return this.basicMenuBackground;
    }

    public Color getSidebarElementTitleBackgroundStartColor()
    {
        return this.basicMenuBackground.darker();
    }

    public Color getSidebarElementForegroundColor()
    {
        return this.basicFrameBackground;
    }

    public Color getSidebarElementTitleOverColor()
    {
        return this.basicFrameBackground.brighter();
    }

    public Color getStatusbarBackgroundColor()
    {
        return this.basicMenuBackground;
    }

    public Color getStatusbarBorderColor()
    {
        return this.basicMenuBackground;
    }

    public Font getToggleButtonFont()
    {
        return this.basicMenuFont.deriveFont(Font.PLAIN);
    }

    public Color getToggleButtonSelectedBorderColor()
    {
        return this.basicTabbedPaneBackground.darker();
    }

    public Color getToggleButtonSelectedColor()
    {
        return this.basicTabbedPaneBackground;
    }

    public Color getToggleButtonUnselectedColor()
    {
        return this.basicMenuBackground;
    }

    public Font getWelcomeSmallFont()
    {
        return this.basicMenuFont.deriveFont((float) 12.0).deriveFont(Font.PLAIN);
    }

    public Font getWelcomeBigFont()
    {
        return this.basicMenuFont.deriveFont((float) 28.0);
    }

    public Color getWelcomeBackgroundEndColor()
    {
        return this.basicMenuBackground;
    }

    public Color getWelcomeBackgroundStartColor()
    {
        return this.basicMenuBackground.brighter();
    }

    public Color getWelcomeBigForegroundColor()
    {
        return this.basicProgressbarForeground;
    }

    public Color getWelcomeBigRolloverForegroundColor()
    {
        return getWelcomeBigForegroundColor().darker();
    }

    /** Look and feel class name */
    private Class<? extends LookAndFeel> lookAndFeelClass;

    /** Default grid color */
    private Color defaultGridColor;

    /** Menubar bg color */
    private Color basicMenubarBackground;

    /** Menubar fg color */
    private Color basicMenubarForeground;

    /** Menubar font */
    private Font basicMenubarFont;

    /** Menu bg color */
    private Color basicMenuBackground;

    /** Menu fg color */
    private Color basicMenuForeground;

    /** Menu font */
    private Font basicMenuFont;

    /** Default white */
    private Color basicWhite;

    /** Default black */
    private Color basicBlack;

    /** Frame bg color */
    private Color basicFrameBackground;

    /** Progress bar color */
    private Color basicProgressbarForeground;

    /** Tabbed pane background color */
    private Color basicTabbedPaneBackground;



}
