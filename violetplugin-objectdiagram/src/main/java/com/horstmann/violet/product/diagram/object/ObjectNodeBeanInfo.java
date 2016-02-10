package com.horstmann.violet.product.diagram.object;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * The bean info for the ObjectNode type.
 * @author Mateusz Mucha
 */
public class ObjectNodeBeanInfo extends SimpleBeanInfo {
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.BeanInfo#getPropertyDescriptors()
	 */
	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor titleDescriptor = new PropertyDescriptor("objectName", ObjectNode.class);
			titleDescriptor.setName("Object");
			titleDescriptor.setValue("priority", new Integer(1));
			
			PropertyDescriptor nameDescriptor = new PropertyDescriptor("className",ObjectNode.class);
			nameDescriptor.setName("Class");
			nameDescriptor.setValue("priority", new Integer(2));
			
			return new PropertyDescriptor[] { 
					titleDescriptor, 
					nameDescriptor 
			};
		} catch (IntrospectionException exception) {
			return null;
		}
	}
}
