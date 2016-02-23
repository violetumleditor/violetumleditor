package com.horstmann.violet.framework.propertyeditor.baseeditors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditorSupport;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 22.02.2016
 */
public class BooleanEditor extends PropertyEditorSupport
{
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
        final Boolean value = (Boolean)getValue();
        final JComboBox comboBox = new JComboBox(KEYS);
        comboBox.setSelectedIndex(value ? 1:0);
        comboBox.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                setValue(VALUES[comboBox.getSelectedIndex()]);
            }
        });
        return comboBox;
    }

    public static final Boolean[] VALUES = new Boolean[]{
            false,
            true
    };

    public static final String[] KEYS = new String[]{
            "False",
            "True"
    };
}
