package com.horstmann.violet.web.property;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.horstmann.violet.framework.injection.resources.ResourceBundleConstant;
import com.horstmann.violet.framework.util.SerializableEnumeration;

import eu.webtoolkit.jwt.WCompositeWidget;
import eu.webtoolkit.jwt.WWidget;

public abstract class AbstractPropertyEditorWidget<T> extends WCompositeWidget {
	
	private List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();
	
	private T oldValue;
	
	private T newValue;
	
	private Object bean;
	
	private PropertyDescriptor propertyDescriptor;
	
	public AbstractPropertyEditorWidget(Object bean, PropertyDescriptor propertyDescriptor) {
		this.bean = bean;
		this.propertyDescriptor = propertyDescriptor;
		setImplementation(getCustomEditor());
	}

	protected Object getBean() {
		return this.bean;
	}
	
	protected PropertyDescriptor getPropertyDescriptor() {
		return this.propertyDescriptor;
	}
	
	protected String getPropertyTitle() {
		ResourceBundle rs = ResourceBundle.getBundle(ResourceBundleConstant.NODE_AND_EDGE_STRINGS, Locale.getDefault());
		// Try to extract title from resource bundle
		String title = this.propertyDescriptor.getName();
		try {
			String translatedTitle = rs.getString(title.toLowerCase());
			if (translatedTitle != null)
				title = translatedTitle;
		} catch (MissingResourceException e) {
			// Nothing to do
		}

		// Upper case the first character
		title = title.substring(0, Math.min(1, title.length())).toUpperCase() + title.substring(Math.min(1, title.length()), title.length());
		return title;
	}

	
    /**
     * A PropertyEditor may choose to make available a full custom Component
     * that edits its property value.  It is the responsibility of the
     * PropertyEditor to hook itself up to its editor Component itself and
     * to report property value changes by firing a PropertyChange event.
     * <P>
     * The higher-level code that calls getCustomEditor may either embed
     * the Component in some larger property sheet, or it may put it in
     * its own individual dialog, or ...
     *
     * @return A widget Component that will allow a human to directly
     *      edit the current property value.  May be null if this is
     *      not supported.
     */
    protected abstract WWidget getCustomEditor();
    
    /**
     * Called when setValue() is invoked to refreshUp editor content
     */
    protected abstract void updateCustomEditor();
	
	/**
     * Set (or change) the object that is to be edited.  Primitive types such
     * as "int" must be wrapped as the corresponding object type such as
     * "java.lang.Integer".
     *
     * @param value The new target object to be edited.  Note that this
     *     object should not be modified by the PropertyEditor, rather
     *     the PropertyEditor should create a new object to hold any
     *     modified value.
     */
    public void setValue(T value) {
    	this.newValue = value;
    	firePropertyChanged(this.oldValue, this.newValue);
    	if (isKnownImmutable(this.propertyDescriptor.getPropertyType()))
        {
            try
            {
                value = (T) value.getClass().getMethod("clone").invoke(value);
            }
            catch (Throwable t)
            {
                t.printStackTrace();
            	// we tried
            }
        }
    	this.oldValue = value;
    	updateCustomEditor();
    }
    
    private boolean isKnownImmutable(Class<?> type)
    {
        if (type.isPrimitive()) return true;
        if (SerializableEnumeration.class.isAssignableFrom(type)) return true;
        if (Cloneable.class.isAssignableFrom(type)) return true;
        return false;
    }

    /**
     * Gets the property value.
     *
     * @return The value of the property.  Primitive types such as "int" will
     * be wrapped as the corresponding object type such as "java.lang.Integer".
     */

    public T getValue() {
    	return this.newValue;
    }
	
	
	/**
     * Adds a listener for the value change.
     * When the property editor changes its value
     * it should fire a {@link PropertyChangeEvent}
     * on all registered {@link PropertyChangeListener}s,
     * specifying the {@code null} value for the property name
     * and itself as the source.
     *
     * @param listener  the {@link PropertyChangeListener} to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
    	this.listeners.add(listener);
    }

    /**
     * Removes a listener for the value change.
     *
     * @param listener  the {@link PropertyChangeListener} to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
    	this.listeners.remove(listener);
    }
    
    /**
     * Fires property change event
     * 
     * @param oldValue
     * @param newValue
     */
    private void firePropertyChanged(T oldValue, T newValue) {
    	PropertyChangeEvent event = new PropertyChangeEvent(this.bean, this.propertyDescriptor.getName(), oldValue, newValue);
    	for (PropertyChangeListener aListener : this.listeners) {
    		aListener.propertyChange(event);
    	}
    }
    
    

}
