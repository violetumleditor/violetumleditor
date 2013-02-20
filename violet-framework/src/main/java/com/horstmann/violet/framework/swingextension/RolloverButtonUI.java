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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

import com.horstmann.violet.framework.theme.ThemeManager;

public class RolloverButtonUI extends BasicButtonUI
{

    public RolloverButtonUI(Color rolloverColor, Color rolloverBorderColor, Color defaultColor)
    {
        super();
        this.defaultColor = defaultColor;
        this.rolloverColor = rolloverColor;
        this.rolloverBorderColor = rolloverBorderColor;
    }

    protected void installDefaults(AbstractButton b)
    {
        super.installDefaults(b);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setRolloverEnabled(true);
        b.setFont(b.getFont().deriveFont(Font.PLAIN));
        b.setBorder(new EmptyBorder(VGAP, HGAP, VGAP, HGAP));
    }

    // public Dimension getPreferredSize(JComponent c)
    // {
    // Dimension preferredSize = super.getPreferredSize(c);
    // return new Dimension((int) preferredSize.getWidth() + 2 * HGAP, (int) preferredSize.getHeight() + 2 * VGAP);
    // }

    public void paint(Graphics g, JComponent c)
    {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();
        clearTextShiftOffset();

        Color borderColor = defaultColor;
        Color startColor = defaultColor;
        Color endColor = defaultColor;

        if (model.isArmed() && model.isPressed())
        {
            borderColor = rolloverBorderColor;
            startColor = rolloverColor;
            endColor = rolloverColor.brighter().brighter();
        }
        else if (model.isRollover())
        {
            borderColor = rolloverBorderColor;
            startColor = rolloverColor.brighter().brighter();
            endColor = rolloverColor;
        }

        paintBackground(g, startColor, endColor, c);
        paintBorder(g, borderColor, c);
        paintText(g, c);

    }

    private void paintText(Graphics g, JComponent c)
    {
        AbstractButton b = (AbstractButton) c;
        String text = b.getText();
        Color currentColor = g.getColor();
        g.setColor(rolloverBorderColor);
        FontMetrics fontMetrics = g.getFontMetrics();
        Rectangle2D stringBounds = fontMetrics.getStringBounds(text, g);
        g.setFont(ThemeManager.getInstance().getTheme().getToggleButtonFont());
        g.drawString(text, (int) (c.getWidth() / 2 - stringBounds.getWidth() / 2), (int) (c.getHeight() / 2
                + stringBounds.getHeight() / 2 - 2));
        g.setColor(currentColor);
    }

    private void paintBackground(Graphics g, Color startColor, Color endColor, JComponent c)
    {

        Graphics2D g2 = (Graphics2D) g;
        Paint currentPaint = g2.getPaint();
        GradientPaint paint = new GradientPaint(c.getWidth() / 2, -c.getHeight() * 2, startColor, c.getWidth() / 2, c.getHeight()
                + c.getHeight() / 4, endColor);
        g2.setPaint(paint);
        g2.fillRect(0, 0, c.getWidth(), c.getHeight());
        g2.setPaint(currentPaint);
    }

    private void paintBorder(Graphics g, Color borderColor, JComponent c)
    {
        Color currentColor = g.getColor();
        g.setColor(borderColor);
        g.drawRect(0, 0, c.getWidth() - 1, c.getHeight() - 1);
        g.setColor(currentColor);
    }

    private Color defaultColor;
    private Color rolloverColor;
    private Color rolloverBorderColor;

    private static final int HGAP = 6;
    private static final int VGAP = 2;

}
