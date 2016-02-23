package com.horstmann.violet.product.diagram.communication.edge;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
/**
 * 
 * @author Artur Ratajczak
 *
 */
public class DirectionEdgeBeanInfo extends SimpleBeanInfo {
	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor[] descriptors = new PropertyDescriptor[] {
					new PropertyDescriptor("SequenceNumber", DirectionEdge.class),
					new PropertyDescriptor("SequentialLoop", DirectionEdge.class),
					new PropertyDescriptor("ConcurrentLoop", DirectionEdge.class),
					new PropertyDescriptor("Message", DirectionEdge.class),
					new PropertyDescriptor("bentStyle", DirectionEdge.class), };
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
