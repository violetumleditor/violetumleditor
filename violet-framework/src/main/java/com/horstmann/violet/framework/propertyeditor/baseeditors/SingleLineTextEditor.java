/**
 * 
 */
package com.horstmann.violet.framework.propertyeditor.baseeditors;

import com.horstmann.violet.framework.property.text.LineText;
import com.horstmann.violet.framework.property.text.SingleLineText;

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