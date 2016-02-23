package com.horstmann.violet.product.diagram.activity.node;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class SignalReceiptNodeBeanInfo extends SimpleBeanInfo
{
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        try
        {
            PropertyDescriptor nameDescriptor = new PropertyDescriptor("signal", SignalReceiptNode.class);
            nameDescriptor.setValue("priority", new Integer(0));

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
