package com.horstmann.violet.product.diagram.common.edge;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 27.12.2015
 */
public class AdvancedPropertyEdgeBeanInfo extends SimpleBeanInfo
{
    @Override
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        try
        {
            PropertyDescriptor[] descriptors = new PropertyDescriptor[]
            {
                new PropertyDescriptor("startArrowHead", AdvancedPropertyEdge.class),
                new PropertyDescriptor("startLabel", AdvancedPropertyEdge.class),
                new PropertyDescriptor("middleLabel", AdvancedPropertyEdge.class),
                new PropertyDescriptor("endLabel", AdvancedPropertyEdge.class),
                new PropertyDescriptor("endArrowHead", AdvancedPropertyEdge.class),
                new PropertyDescriptor("lineStyle", AdvancedPropertyEdge.class),
                new PropertyDescriptor("bentStyle", AdvancedPropertyEdge.class),
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