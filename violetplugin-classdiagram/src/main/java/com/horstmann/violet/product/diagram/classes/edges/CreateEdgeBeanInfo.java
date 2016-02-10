package com.horstmann.violet.product.diagram.classes.edges;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class CreateEdgeBeanInfo extends SimpleBeanInfo {
	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor[] descriptors = new PropertyDescriptor[] {
					new PropertyDescriptor("startLabel", CreateEdge.class),
					new PropertyDescriptor("middleLabel", CreateEdge.class),
					new PropertyDescriptor("endLabel", CreateEdge.class),
					new PropertyDescriptor("bentStyle", CreateEdge.class), };
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
