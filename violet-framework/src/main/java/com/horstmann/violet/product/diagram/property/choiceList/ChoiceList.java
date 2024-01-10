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

/**
 * Represents a static list for user choices.
 *
 * @param <K> the type of elements keys list
 * @param <V> the type of elements returned by list
 *
 * @author Alexandre de Pellegrin
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 20.02.2016
 */
public abstract class ChoiceList<K, V> implements Cloneable
{
    /**
     * Default constructor
     *
     * @param keys items to select
     * @param values items to select
     * @throws IllegalArgumentException
     */
    public ChoiceList(K[] keys, V[] values)
    {
        if(keys.length != values.length || keys.length < 1)
        {
            throw new IllegalArgumentException("keys and values must have the same length of");
        }

        this.values = values;
        this.keys = keys;
        this.selectedKey = null;
    }

    /**
     * Copy constructor
     *
     * @param copyElement
     */
    protected ChoiceList(ChoiceList<K, V> copyElement)
    {
        this.values = copyElement.values;
        this.keys = copyElement.keys;
        this.selectedKey = copyElement.selectedKey;
    }

    public abstract ChoiceList clone();

    /**
	 * @return the key list
	 */
    public K[] getKeys()
    {
		return keys;
	}
    
    /**
	 * @return the value list
	 */
    public V[] getValues()
    {
		return values;
	}
    


    /**
     * Selects an value in the list
     *
     * @param value
     */
    public boolean setSelectedValue(V value)
    {
        if (value == null) {
            selectedKey = null;
            return false;
        }
        
        for(int i = 0; i<values.length; ++i)
        {
            if(value.equals(values[i]))
            {
                selectedKey = keys[i];
                return true;
            }
        }
        return false;
    }

    /**
     * Selects an value in the list
     *
     * @param value
     */
    public boolean setSelectedValueFromString(String value)
    {
        for(int i = 0; i<values.length; ++i)
        {
            if(value.equals(values[i].toString()))
            {
                selectedKey = keys[i];
                return true;
            }
        }
        return false;
    }


    /**
     * @return current selected value
     */
    public V getSelectedValue()
    {
        if (this.selectedKey == null) {
            return null;
        }

        int selectedPos = -1;
        for (int i = 0; i < keys.length; i++) {
            if (selectedKey.equals(keys[i])) {
                selectedPos = i;
                break;
            }
        }
        if (selectedPos != -1) {
            return values[selectedPos];
        } 
        return null;
    }

    public void setSelectedKey(K selectedKey) {
        this.selectedKey = selectedKey;
    }

    /**
     * @return current selected key
     */
    public K getSelectedKey()
    {
        return selectedKey;
    }

    /**
     * Key for current selected item
     */
    private K selectedKey;

    /**
     * Item keys list for selection
     */
    private final K[] keys;

    /**
     * Item values list for selection
     */
    private final V[] values;
}
