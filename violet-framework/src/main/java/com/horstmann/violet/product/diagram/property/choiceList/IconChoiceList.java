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

package com.horstmann.violet.product.diagram.property.choiceList;

import javax.swing.Icon;

/**
 * Represents a static list for user choices.
 *
 * @param <V> the type of elements returned by list
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 20.02.2016
 */
public class IconChoiceList<V> extends ChoiceList<Icon, V>
{
    /**
     * Default constructor
     *
     * @param keys items to select
     * @param values items to select
     * @throws IllegalArgumentException
     */
    public IconChoiceList(Icon[] keys, V[] values)
    {
        super(keys, values);
    }

    /**
     * Copy constructor
     *
     * @param copyElement
     */
    protected IconChoiceList(IconChoiceList<V> copyElement)
    {
        super(copyElement);
    }

    public IconChoiceList clone()
    {
        return new IconChoiceList<V>(this);
    }
}
