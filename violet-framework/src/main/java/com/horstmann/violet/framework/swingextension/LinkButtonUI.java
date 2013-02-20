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

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.plaf.basic.BasicButtonUI;

public class LinkButtonUI extends BasicButtonUI
{

    public LinkButtonUI()
    {
        super();
    }

    protected void installDefaults(AbstractButton b)
    {
        super.installDefaults(b);
        b.setOpaque(false);
        b.setBorderPainted(false);
        b.setRolloverEnabled(true);
        b.setFont(b.getFont().deriveFont(Font.PLAIN));
    }

    protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text)
    {
        super.paintText(g, b, textRect, text);
        ButtonModel model = b.getModel();
        if (model.isRollover())
        {
            g.drawLine((int) (textRect.getX() + 1), (int) (textRect.getY() + textRect.getHeight() - 1), (int) (textRect.getX()
                    + textRect.getWidth() - 1), (int) (textRect.getY() + textRect.getHeight() - 1));
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

}
