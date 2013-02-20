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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.Timer;

/**
 * This panel scrolls its content vertically
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class VerticalAutoScrollPane extends JScrollPane implements ActionListener
{

    public VerticalAutoScrollPane()
    {
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    }

    public void reset()
    {
        getVerticalScrollBar().setValue(0);
    }

    public void animate()
    {
        if (timer != null)
        {
            timer.stop();
        }
        timer = new Timer(50, this);
        timer.setInitialDelay(2000);
        timer.start();
    }

    public void actionPerformed(ActionEvent e)
    {
        JScrollBar verticalScrollBar = getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getValue() + 2);
        if (verticalScrollBar.getValue() >= verticalScrollBar.getMaximum())
        {
            timer.stop();
        }
    }

    private Timer timer;

}
