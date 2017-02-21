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

package com.horstmann.violet.product.diagram.propertyeditor.baseeditors;

import com.horstmann.violet.framework.injection.resources.ResourceBundleConstant;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.MultiLineText;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * A property editor for the MultiLineText type.
 */
public class MultiLineTextEditor extends LineTextEditor
{
    /**
     * Multi line edited text.
     */
    private MultiLineText source;

    /**
     * Number of text lines.
     */
    private static final int ROWS;

    static
    {
        ROWS = Integer.parseInt(ResourceBundleConstant.TEXT_EDITOR_RESOURCE.getString("rows"));
    }

    @Override
    protected void setSourceEditor()
    {
        this.source = (MultiLineText) getValue();
    }

    @Override
    protected LineText getSourceEditor()
    {
        return this.source;
    }

    @Override
    protected JTextComponent createTextComponent()
    {
        return new JTextArea(ROWS, COLUMNS);
    }

    @Override
    protected JComponent createScrollPanel(final JTextComponent textComponent)
    {
        return new JScrollPane(textComponent);
    }

}
