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

package com.horstmann.violet.framework.injection.resources;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Collects resource bundles into one place.
 */
public final class ResourceBundleConstant
{
    public static final String OTHER_STRINGS = "properties.OtherStrings";
    public static final String FILE_STRINGS = "properties.FileStrings";
    public static final String GENERIC_STRINGS = "properties.%Strings";
    public static final String NODE_AND_EDGE_STRINGS = "properties.NodeAndEdgeStrings";
    public static final String EDITOR_STRINGS = "properties.EditorStrings";
    public static final String TEXT_EDITOR_STRINGS = "properties.TextEditorStrings";

    public static final ResourceBundle NODE_AND_EDGE_RESOURCE = ResourceBundle.getBundle(NODE_AND_EDGE_STRINGS, Locale.getDefault());
    public static final ResourceBundle EDITOR_RESOURCE = ResourceBundle.getBundle(EDITOR_STRINGS, Locale.getDefault());
    public static final ResourceBundle FILE_RESOURCE = ResourceBundle.getBundle(FILE_STRINGS, Locale.getDefault());
    public static final ResourceBundle TEXT_EDITOR_RESOURCE = ResourceBundle.getBundle(TEXT_EDITOR_STRINGS, Locale.getDefault());
    public static final ResourceBundle OTHER_RESOURCE = ResourceBundle.getBundle(OTHER_STRINGS, Locale.getDefault());

    private ResourceBundleConstant()
    {
    }
}
