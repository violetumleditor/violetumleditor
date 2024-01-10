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

import com.horstmann.violet.product.diagram.property.choiceList.ChoiceList;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditorSupport;

import javax.swing.*;

/**
 * A property editor for the ChoiceList type.
 * 
 * @author Alexandre de Pellegrin
 */
public class ChoiceListEditor extends PropertyEditorSupport
{
    /**
     * (non-Javadoc)
     *
     * @see java.beans.PropertyEditorSupport#supportsCustomEditor()
     */
    @Override
    public boolean supportsCustomEditor()
    {
        return true;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyEditorSupport#getCustomEditor()
     */
    @Override
    public Component getCustomEditor()
    {
        JPanel panel = new JPanel();

        final ChoiceList list = ((ChoiceList) getValue()).clone();
        final JComboBox comboBox = new JComboBox(list.getKeys());
        comboBox.setSelectedItem(list.getSelectedValue());
        comboBox.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                list.setSelectedKey(comboBox.getSelectedItem());
                setValue(list.getSelectedValue());
            }

        });
        panel.add(comboBox);
        return panel;
    }
}
