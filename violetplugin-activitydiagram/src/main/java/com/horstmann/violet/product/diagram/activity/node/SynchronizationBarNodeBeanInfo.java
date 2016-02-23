package com.horstmann.violet.product.diagram.activity.node;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class SynchronizationBarNodeBeanInfo extends SimpleBeanInfo
{
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        try
        {
            PropertyDescriptor orientationDescriptor = new PropertyDescriptor("orientation", SynchronizationBarNode.class);
            orientationDescriptor.setValue("priority", new Integer(0));

            return new PropertyDescriptor[]
            {
                    orientationDescriptor,
            };
        }
        catch (IntrospectionException exception)
        {
            return null;
        }
    }
}
