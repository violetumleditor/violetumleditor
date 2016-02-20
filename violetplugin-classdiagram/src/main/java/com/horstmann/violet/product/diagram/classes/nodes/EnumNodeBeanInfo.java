package com.horstmann.violet.product.diagram.classes.nodes;


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
            PropertyDescriptor testDescriptor = new PropertyDescriptor("arrowheadChoiceList", EnumNode.class);
            testDescriptor.setValue("priority", new Integer(3));
            PropertyDescriptor test2Descriptor = new PropertyDescriptor("lineStyleChoiceList", EnumNode.class);
            test2Descriptor.setValue("priority", new Integer(4));
            return new PropertyDescriptor[]
            {
                    nameDescriptor,
                    attributesDescriptor,
                    testDescriptor,
                    test2Descriptor,
            };
        }
        catch (IntrospectionException exception)
        {
            return null;
        }
    }
}
