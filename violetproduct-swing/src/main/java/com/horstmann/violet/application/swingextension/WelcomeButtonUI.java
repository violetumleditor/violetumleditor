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

package com.horstmann.violet.application.swingextension;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

import com.horstmann.violet.framework.theme.ThemeManager;

public class WelcomeButtonUI extends BasicButtonUI
{

    public WelcomeButtonUI()
    {
        super();
    }

    protected void installDefaults(AbstractButton b)
    {
        super.installDefaults(b);
        b.setOpaque(false);
        b.setBorderPainted(false);
        b.setRolloverEnabled(true);
        b.setFont(ThemeManager.getInstance().getTheme().getWelcomeBigFont());
        b.setBorder(new EmptyBorder(0, 0, 0, 0));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }


    
    protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text)
    {

        ButtonModel model = b.getModel();
        if (model.isRollover())
        {
            b.setForeground(ThemeManager.getInstance().getTheme().getWelcomeBigRolloverForegroundColor());
        }
        else
        {
            b.setForeground(ThemeManager.getInstance().getTheme().getWelcomeBigForegroundColor());
        }
        super.paintText(g, b, textRect, text);
    }

}
