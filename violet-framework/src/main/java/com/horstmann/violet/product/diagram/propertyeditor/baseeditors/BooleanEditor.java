package com.horstmann.violet.product.diagram.propertyeditor.baseeditors;

import com.horstmann.violet.framework.injection.resources.ResourceBundleConstant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditorSupport;
import java.util.Locale;
import java.util.ResourceBundle;

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
        JPanel panel = new JPanel();
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
        panel.add(comboBox);
        return panel;
    }

    public static final Boolean[] VALUES = new Boolean[]{
            false,
            true
    };

    public static String[] KEYS = new String[]{
            "False",
            "True"
    };

    static
    {
        ResourceBundle rs = ResourceBundle.getBundle(ResourceBundleConstant.EDITOR_STRINGS, Locale.getDefault());

        for(int i=0; i< KEYS.length; ++i)
        {
            try
            {
                KEYS[i] = rs.getString(("boolean." + KEYS[i]).toLowerCase());
            }catch (Exception ignored){}
        }
    }
}
