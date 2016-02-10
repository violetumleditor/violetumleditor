package com.horstmann.violet.product.diagram.classes.edges;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class UseEdgeBeanInfo extends SimpleBeanInfo {

	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor[] descriptors = new PropertyDescriptor[] {
					new PropertyDescriptor("startLabel", UseEdge.class),
					new PropertyDescriptor("middleLabel", UseEdge.class),
					new PropertyDescriptor("endLabel", UseEdge.class),
					new PropertyDescriptor("bentStyle", UseEdge.class), };
			for (int i = 0; i < descriptors.length; i++) {
				descriptors[i].setValue("priority", new Integer(i));
			}
			return descriptors;
		} catch (IntrospectionException exception) {
			exception.printStackTrace();
			return null;
		}
	}

}
