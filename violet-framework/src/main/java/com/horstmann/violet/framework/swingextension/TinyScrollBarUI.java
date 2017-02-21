/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2008 Cay S. Horstmann (http://horstmann.com)
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

import com.horstmann.violet.framework.theme.ThemeManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * Scroll bar style used for status and tool bars
 * 
 * @author Alexandre de Pellegrin
 *
 */
public class TinyScrollBarUI extends BasicScrollBarUI
{

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.plaf.basic.BasicScrollBarUI#installUI(javax.swing.JComponent)
     */
    @Override
    public void installUI(JComponent c)
    {
        super.installUI(c);
        scrollbar.setUnitIncrement(DEFAULT_SCROLLUNIT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.plaf.basic.BasicScrollBarUI#getPreferredSize(javax.swing.JComponent)
     */
    public Dimension getPreferredSize(JComponent c)
    {
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL)
        {
            return new Dimension(DEFAULT_THICKNESS, scrollbar.getHeight());
        }
        else
        {
            return new Dimension(scrollbar.getWidth(), DEFAULT_THICKNESS);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.plaf.basic.BasicScrollBarUI#layoutVScrollbar(javax.swing.JScrollBar)
     */
    @Override
    protected void layoutVScrollbar(JScrollBar sb)
    {
        Rectangle vr = new Rectangle();
        SwingUtilities.calculateInnerArea(sb, vr);
        decrButton.setBounds(vr.x, vr.y, 0, 0);
        incrButton.setBounds(vr.x, vr.height, 0, 0);
        decrButton.setPreferredSize(new Dimension(0, 0));
        incrButton.setPreferredSize(new Dimension(0, 0));
        trackRect.setBounds(vr.x, vr.y, vr.width, vr.height);

        int max = sb.getMaximum();
        int min = sb.getMinimum();
        int value = sb.getValue();
        int extent = sb.getVisibleAmount();
        if (max == min)
        {
            thumbRect.x = trackRect.x;
            thumbRect.y = trackRect.y;
            thumbRect.width = trackRect.width;
            thumbRect.height = getMinimumThumbSize().height;
        }
        else
        {
            thumbRect.x = trackRect.x;
            thumbRect.y = trackRect.y + value * trackRect.height / (max - min);
            thumbRect.width = trackRect.width;
            thumbRect.height = extent * trackRect.height / (max - min);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.plaf.basic.BasicScrollBarUI#layoutHScrollbar(javax.swing.JScrollBar)
     */
    @Override
    protected void layoutHScrollbar(JScrollBar sb)
    {
        Rectangle vr = new Rectangle();
        SwingUtilities.calculateInnerArea(sb, vr);
        decrButton.setBounds(vr.x, vr.y, 0, 0);
        incrButton.setBounds(vr.width, vr.y, 0, 0);
        decrButton.setPreferredSize(new Dimension(0, 0));
        incrButton.setPreferredSize(new Dimension(0, 0));
        trackRect.setBounds(vr.x, vr.y, vr.width, vr.height);

        int max = sb.getMaximum();
        int min = sb.getMinimum();
        int value = sb.getValue();
        int extent = sb.getVisibleAmount();
        if (max == min)
        {
            thumbRect.x = trackRect.x;
            thumbRect.y = trackRect.y;
            thumbRect.width = getMinimumThumbSize().width;
            thumbRect.height = trackRect.height;
        }
        else
        {
            thumbRect.x = trackRect.x + value * trackRect.width / (max - min);
            ;
            thumbRect.y = trackRect.y;
            thumbRect.width = extent * trackRect.width / (max - min);
            thumbRect.height = trackRect.height;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.plaf.basic.BasicScrollBarUI#paintThumb(java.awt.Graphics, javax.swing.JComponent, java.awt.Rectangle)
     */
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds)
    {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled())
        {
            return;
        }

        int w = thumbBounds.width;
        int h = thumbBounds.height;

        g.translate(thumbBounds.x, thumbBounds.y);

        Color thumbCustomColor = ThemeManager.getInstance().getTheme().getToggleButtonSelectedBorderColor();
        g.setColor(thumbCustomColor);
        g.drawRect(0, 0, w, h);
        g.fillRect(0, 0, w, h);

        g.translate(-thumbBounds.x, -thumbBounds.y);
    }

    /**
     * Default scroll unit when using mouse wheel upon this scroll bar
     */
    private static final int DEFAULT_SCROLLUNIT = 20;

    /**
     * Default bar tickness
     */
    private static final int DEFAULT_THICKNESS = 4;
}
