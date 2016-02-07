package ratajczak.violet.product.diagram.classes.nodes;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class ObjectNodeBeanInfo extends SimpleBeanInfo {
	 public PropertyDescriptor[] getPropertyDescriptors()
	    {
	        try
	        {
	            PropertyDescriptor nameDescriptor = new PropertyDescriptor("name", ObjectNode.class);
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
