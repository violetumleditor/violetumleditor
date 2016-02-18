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

package com.horstmann.violet.product.diagram.abstracts.property;

/**
 * Represents a static list for user choices.
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class ChoiceList
{
    /**
     * Default constructor
     *
     * @param keys items to select
     */
    public ChoiceList(String[] keys)
    {
        this(keys,keys);
    }

    /**
     * Default constructor
     *
     * @param keys items to select
     * @param values items to select
     * @throws IllegalArgumentException
     */
    public ChoiceList(String[] keys, Object[] values)
    {
        if(keys.length != values.length || keys.length < 1)
        {
            throw new IllegalArgumentException("keys and values must have the same length of");
        }

        this.values = values;
        this.keys = keys;
        this.selectedPos = 0;
    }

    /**
     * (non-Javadoc)
     * @see java.lang.Object#clone()
     *
     */
    public ChoiceList clone()
    {
        ChoiceList cloned = new ChoiceList(keys, values);
        cloned.selectedPos = selectedPos;

        return cloned;
    }

	/**
	 * @return the key list
	 */
	public String[] getKeys()
    {
		return keys;
	}

    /**
     * Selects an index in the list
     *
     * @param index
     */
    public void setSelectedIndex(int index)
    {
        selectedPos = index;
    }

    /**
     * @return current selected value
     */
    public Object getSelectedValue()
    {
        return values[selectedPos];
    }

    /**
     * @return current selected key
     */
    public String getSelectedKey()
    {
        return keys[selectedPos];
    }

    /**
     * @return current selected position
     */
    public int getSelectedPos()
    {
        return selectedPos;
    }

    /**
     * Index for current selected item
     */
    private int selectedPos;

    /**
     * Item keys list for selection
     */
    private final String[] keys;

    /**
     * Item values list for selection
     */
    private final Object[] values;
}
