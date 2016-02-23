package com.horstmann.violet.product.diagram.classes.node;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * The bean info for the InterfaceNode type.
 */
public class InterfaceNodeBeanInfo extends SimpleBeanInfo
{

    @Override
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        try
        {
            PropertyDescriptor nameDescriptor = new PropertyDescriptor("name", InterfaceNode.class);
            nameDescriptor.setValue("priority", new Integer(1));
            PropertyDescriptor methodsDescriptor = new PropertyDescriptor("methods", InterfaceNode.class);
            methodsDescriptor.setValue("priority", new Integer(2));
            return new PropertyDescriptor[]
            {
                    nameDescriptor,
                    methodsDescriptor
            };
        }
        catch (IntrospectionException exception)
        {
            return null;
        }
    }
}
