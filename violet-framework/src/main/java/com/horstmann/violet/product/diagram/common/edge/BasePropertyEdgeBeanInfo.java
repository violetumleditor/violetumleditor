package com.horstmann.violet.product.diagram.common.edge;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * Created by Adrian Bobrowski on 27.12.2015.
 */
public class BasePropertyEdgeBeanInfo extends SimpleBeanInfo
{
    @Override
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        try
        {
            PropertyDescriptor[] descriptors = new PropertyDescriptor[]
            {
                new PropertyDescriptor("startLabel", BasePropertyEdge.class),
                new PropertyDescriptor("middleLabel", BasePropertyEdge.class),
                new PropertyDescriptor("endLabel", BasePropertyEdge.class),
                new PropertyDescriptor("bentStyle", BasePropertyEdge.class),
            };
            for (int i = 0; i < descriptors.length; i++)
            {
                descriptors[i].setValue("priority", new Integer(i));
            }
            return descriptors;
        }
        catch (IntrospectionException exception)
        {
            exception.printStackTrace();
            return null;
        }
    }
}
