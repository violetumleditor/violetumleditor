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

package com.horstmann.violet.framework.propertyeditor.customeditor;

import com.horstmann.violet.framework.propertyeditor.CustomPropertyEditorSupport;
import com.horstmann.violet.framework.property.StretchStyle;

/**
 * A property editor for the LineStyleChoiceList type.
 */
public class StretchStyleEditor extends CustomPropertyEditorSupport
{
    /**
     * Default constructor
     */
    public StretchStyleEditor()
    {
        super(NAMES, VALUES);
    }

    /**
     * Type names
     */
    public static final String[] NAMES =
    {
        resourceBundle.getString("stretch_style.horizontal"),
        resourceBundle.getString("stretch_style.vertical")
    };

    /**
     * Corresponding objects
     */
    public static final Object[] VALUES =
    {
        StretchStyle.HORIZONTAL,
        StretchStyle.VERTICAL
    };
}
