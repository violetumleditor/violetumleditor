package com.horstmann.violet.product.diagram.classes.node;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * The bean info for the ClassNode type.
 */
public class EnumNodeBeanInfo extends SimpleBeanInfo
{
    /*
     * (non-Javadoc)
     * 
     * @see java.beans.BeanInfo#getPropertyDescriptors()
     */
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        try
        {
            PropertyDescriptor nameDescriptor = new PropertyDescriptor("name", EnumNode.class);
            nameDescriptor.setValue("priority", new Integer(1));
            PropertyDescriptor attributesDescriptor = new PropertyDescriptor("attributes", EnumNode.class);
            attributesDescriptor.setValue("priority", new Integer(2));
            return new PropertyDescriptor[]
            {
                    nameDescriptor,
                    attributesDescriptor
            };
        }
        catch (IntrospectionException exception)
        {
            return null;
        }
    }
}
