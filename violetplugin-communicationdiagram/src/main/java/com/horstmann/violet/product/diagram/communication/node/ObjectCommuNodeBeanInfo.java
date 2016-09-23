package com.horstmann.violet.product.diagram.communication.node;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
/**
 * 
 * @author Alexandre de Pellegrin / Cays S. Horstmann
 *
 */
public class ObjectCommuNodeBeanInfo extends SimpleBeanInfo {
	 public PropertyDescriptor[] getPropertyDescriptors()
	    {
	        try
	        {
	            PropertyDescriptor nameDescriptor = new PropertyDescriptor("name", ObjectCommuNode.class);
	            nameDescriptor.setValue("priority", new Integer(1));
	               
	            return new PropertyDescriptor[]
	            {
	                    nameDescriptor,  
	                   
	            };
	        }
	        catch (IntrospectionException exception)
	        {
	            return null;
	        }
	    }
}
