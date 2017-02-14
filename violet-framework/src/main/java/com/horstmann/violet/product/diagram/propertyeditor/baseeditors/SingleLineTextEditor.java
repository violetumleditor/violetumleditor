package com.horstmann.violet.product.diagram.propertyeditor.baseeditors;

import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * A property editor for the SingleLineText type.
 */
public class SingleLineTextEditor extends LineTextEditor
{
    /**
     * Single line edited text.
     */
    private SingleLineText source;

    @Override
    protected void setSourceEditor()
    {
        this.source = (SingleLineText) getValue();
    }

    @Override
    protected LineText getSourceEditor()
    {
        return this.source;
    }

    @Override
    protected JTextComponent createTextComponent()
    {
        return new JTextField(COLUMNS);
    }

}