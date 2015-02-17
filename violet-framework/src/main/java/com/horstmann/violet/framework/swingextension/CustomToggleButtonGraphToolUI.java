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

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 * UI for CustomToggleButton
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class CustomToggleButtonGraphToolUI extends BasicPanelUI
{

    /**
     * Default constructor
     * 
     * @param selectedColor
     * @param selectedBorderColor
     * @param unselectedColor
     */
    public CustomToggleButtonGraphToolUI(Color selectedColor, Color selectedBorderColor, Color unselectedColor)
    {
        this.unselectedColor = unselectedColor;
        this.selectedColor = selectedColor;
        this.selectedBorderColor = selectedBorderColor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.plaf.basic.BasicPanelUI#installDefaults(javax.swing.JPanel)
     */
    protected void installDefaults(JPanel p)
    {
        p.setOpaque(false);

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.plaf.ComponentUI#paint(java.awt.Graphics, javax.swing.JComponent)
     */
    public void paint(Graphics g, JComponent c)
    {
        if (c instanceof CustomToggleButton)
        {
            boolean isSelected = ((CustomToggleButton) c).isSelected();
            if (isSelected)
            {
                paintSelectedBg(g, c);
                paintSelectedBorder(g, c);
            }
            if (!isSelected)
            {
                paintUnselectedBg(g, c);
            }
        }
        super.paint(g, c);
    }

    /**
     * Paints background for state set as 'selected'
     * 
     * @param g
     * @param c
     */
    private void paintSelectedBg(Graphics g, JComponent c)
    {

        Graphics2D g2 = (Graphics2D) g;
        Paint currentPaint = g2.getPaint();
        GradientPaint paint = new GradientPaint(c.getWidth() / 2, -c.getHeight() / 4, selectedColor.brighter().brighter(), c
                .getWidth() / 2, c.getHeight() + c.getHeight() / 4, selectedColor);
        g2.setPaint(paint);
        g2.fillRect(0, 0, c.getWidth(), c.getHeight());
        g2.setPaint(currentPaint);
    }

    /**
     * Paints background for state set as 'NOT selected'
     * 
     * @param g
     * @param c
     */
    private void paintUnselectedBg(Graphics g, JComponent c)
    {
        Graphics2D g2 = (Graphics2D) g;
        Color currentColor = g2.getColor();
        g2.setColor(unselectedColor);
        g2.fillRect(0, 0, c.getWidth(), c.getHeight());
        g2.setColor(currentColor);
    }

    /**
     * Paints border for state set as 'selected'
     * 
     * @param g
     * @param c
     */
    private void paintSelectedBorder(Graphics g, JComponent c)
    {
        Color currentColor = g.getColor();
        g.setColor(selectedBorderColor);
        g.drawRect(0, 0, c.getWidth() - 1, c.getHeight() - 1);
        g.setColor(currentColor);
    }

    private Color unselectedColor;
    private Color selectedColor;
    private Color selectedBorderColor;

}
