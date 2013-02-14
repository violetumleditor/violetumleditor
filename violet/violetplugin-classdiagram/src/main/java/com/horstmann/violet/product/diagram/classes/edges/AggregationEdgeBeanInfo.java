package com.horstmann.violet.product.diagram.classes.edges;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * The bean info for the ClassRelationshipEdge type.
 */
public class AggregationEdgeBeanInfo extends SimpleBeanInfo
{

    @Override
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        try
        {
            PropertyDescriptor[] descriptors = new PropertyDescriptor[]
            {
                    new PropertyDescriptor("startLabel", AggregationEdge.class),
                    new PropertyDescriptor("middleLabel", AggregationEdge.class),
                    new PropertyDescriptor("endLabel", AggregationEdge.class),
                    new PropertyDescriptor("bentStyle", AggregationEdge.class),
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
