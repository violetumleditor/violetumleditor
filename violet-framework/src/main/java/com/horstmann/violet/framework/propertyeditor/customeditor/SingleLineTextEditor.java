/**
 * 
 */
package com.horstmann.violet.framework.propertyeditor.customeditor;

import com.horstmann.violet.product.diagram.abstracts.property.string.LineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.SingleLineText;

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