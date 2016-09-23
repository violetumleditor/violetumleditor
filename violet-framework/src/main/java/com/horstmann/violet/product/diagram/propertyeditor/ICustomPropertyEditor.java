package com.horstmann.violet.product.diagram.propertyeditor;

import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

public interface ICustomPropertyEditor
{

    public abstract JComponent getAWTComponent();

    /**
     * Adds a property change listener to the list of listeners.
     * 
     * @param listener the listener to add
     */
    public abstract void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Adds a property change listener to the list of listeners.
     * 
     * @param listener the listener to add
     */
    public abstract void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * @return true if the bean has a minimum of one editable property
     */
    public abstract boolean isEditable();

}