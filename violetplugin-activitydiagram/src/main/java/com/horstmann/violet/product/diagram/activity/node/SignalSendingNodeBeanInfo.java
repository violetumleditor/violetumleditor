package com.horstmann.violet.product.diagram.activity.node;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class SignalSendingNodeBeanInfo extends SimpleBeanInfo
{
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        try
        {
            PropertyDescriptor nameDescriptor = new PropertyDescriptor("signal", SignalSendingNode.class);
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
