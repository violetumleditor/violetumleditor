/**
 * 
 */
package com.horstmann.violet.product.diagram.propertyeditor.baseeditors;

import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;

import javax.swing.*;
import javax.swing.text.JTextComponent;

public class SingleLineTextEditor extends LineTextEditor
{
    protected void setSourceEditor()
    {
        this.source = (SingleLineText) getValue();
    }
    protected LineText getSourceEditor()
    {
        return this.source;
    }
    protected JTextComponent createTextComponent()
    {
        return new JTextField(COLUMNS);
    }

    private SingleLineText source;
}