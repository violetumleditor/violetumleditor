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
     * @param list items to select
     */
    public ChoiceList(String[] list)
    {
        this.list = list;
        this.selectedPos = DEFAULT_SELECT;
    }

    /**
     * @return the item list
     */
    public String[] getList()
    {
        return list;
    }

    /**
     * Selects an item in the list
     * 
     * @param selected
     */
    public void setSelectedItem(String selected)
    {
        for (int i = 0; i < list.length; i++)
        {
            if (list[i].equals(selected))
            {
                selectedPos = i;
                return;
            }
        }
    }

    /**
     * @return current selected item
     */
    public String getSelectedItem()
    {
        return list[selectedPos];
    }

    /**
     * Item list for selection
     */
    private String[] list;

    /**
     * Index for current selected item
     */
    private int selectedPos;

    /**
     * Default selected item index
     */
    private static final int DEFAULT_SELECT = 0;

}
