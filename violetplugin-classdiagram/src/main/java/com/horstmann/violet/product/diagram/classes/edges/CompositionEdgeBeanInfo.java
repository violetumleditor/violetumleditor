package com.horstmann.violet.product.diagram.classes.edges;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * The bean info for the ClassRelationshipEdge type.
 */
public class CompositionEdgeBeanInfo extends SimpleBeanInfo
{

    @Override
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        try
        {
            PropertyDescriptor[] descriptors = new PropertyDescriptor[]
            {
                    new PropertyDescriptor("startLabel", CompositionEdge.class),
                    new PropertyDescriptor("middleLabel", CompositionEdge.class),
                    new PropertyDescriptor("endLabel", CompositionEdge.class),
                    new PropertyDescriptor("bentStyle", CompositionEdge.class),
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
