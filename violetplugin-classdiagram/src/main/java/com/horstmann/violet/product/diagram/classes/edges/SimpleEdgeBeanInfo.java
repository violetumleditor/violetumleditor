package com.horstmann.violet.product.diagram.classes.edges;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * Created by Adrian Bobrowski on 27.12.2015.
 */
public class SimpleEdgeBeanInfo extends SimpleBeanInfo
{
    @Override
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        try
        {
            PropertyDescriptor[] descriptors = new PropertyDescriptor[]
            {
                new PropertyDescriptor("startLabel", SimpleEdge.class),
                new PropertyDescriptor("middleLabel", SimpleEdge.class),
                new PropertyDescriptor("endLabel", SimpleEdge.class),
                new PropertyDescriptor("bentStyle", SimpleEdge.class),
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
