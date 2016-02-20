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

package com.horstmann.violet.framework.propertyeditor.baseeditors;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import com.horstmann.violet.framework.property.string.LineText;
import com.horstmann.violet.framework.property.string.MultiLineText;

/**
 * A property editor for the MultiLineText type.
 */
public class MultiLineTextEditor extends LineTextEditor
{
    protected void setSourceEditor()
    {
        this.source = (MultiLineText) getValue();
    }
    protected LineText getSourceEditor()
    {
        return this.source;
    }
    protected JTextComponent createTextComponent()
    {
        return new JTextArea(ROWS, COLUMNS);
    }

    protected JComponent createScrollPanel(JTextComponent textComponent)
    {
        return new JScrollPane(textComponent);
    }

    private MultiLineText source;

    private static final int ROWS = 5;
}
