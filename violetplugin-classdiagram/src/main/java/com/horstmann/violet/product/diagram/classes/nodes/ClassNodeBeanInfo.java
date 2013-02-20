package com.horstmann.violet.product.diagram.classes.nodes;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * The bean info for the ClassNode type.
 */
public class ClassNodeBeanInfo extends SimpleBeanInfo
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
            PropertyDescriptor nameDescriptor = new PropertyDescriptor("name", ClassNode.class);
            nameDescriptor.setValue("priority", new Integer(1));
            PropertyDescriptor attributesDescriptor = new PropertyDescriptor("attributes", ClassNode.class);
            attributesDescriptor.setValue("priority", new Integer(2));
            PropertyDescriptor methodsDescriptor = new PropertyDescriptor("methods", ClassNode.class);
            methodsDescriptor.setValue("priority", new Integer(3));
            return new PropertyDescriptor[]
            {
                    nameDescriptor,
                    attributesDescriptor,
                    methodsDescriptor
            };
        }
        catch (IntrospectionException exception)
        {
            return null;
        }
    }
}
