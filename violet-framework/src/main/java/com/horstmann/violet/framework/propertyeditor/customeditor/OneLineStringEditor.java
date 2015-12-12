/**
 * 
 */
package com.horstmann.violet.framework.propertyeditor.customeditor;

import java.beans.PropertyEditorSupport;

public class OneLineStringEditor extends PropertyEditorSupport
{
    public String getAsText()
    {
        return (String) getValue();
    }

    public void setAsText(String s)
    {
        setValue(s);
    }
}