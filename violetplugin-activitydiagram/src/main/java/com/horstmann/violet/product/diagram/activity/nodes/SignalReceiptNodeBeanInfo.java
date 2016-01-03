package com.horstmann.violet.product.diagram.activity.nodes;


import com.horstmann.violet.product.diagram.activity.ActivityResource;

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
            nameDescriptor.setValue("priority", new Integer(1));
            nameDescriptor.setName(ActivityResource.ACTIVITY.getResourceString("signal.receipt.node.signal"));

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
