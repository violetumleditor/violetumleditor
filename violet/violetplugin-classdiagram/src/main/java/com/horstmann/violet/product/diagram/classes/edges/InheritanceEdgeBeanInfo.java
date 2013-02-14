package com.horstmann.violet.product.diagram.classes.edges;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * The bean info for the ClassRelationshipEdge type.
 */
public class InheritanceEdgeBeanInfo extends SimpleBeanInfo
{

    @Override
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        try
        {
            PropertyDescriptor[] descriptors = new PropertyDescriptor[]
            {
                    new PropertyDescriptor("startLabel", InheritanceEdge.class),
                    new PropertyDescriptor("middleLabel", InheritanceEdge.class),
                    new PropertyDescriptor("endLabel", InheritanceEdge.class),
                    new PropertyDescriptor("bentStyle", InheritanceEdge.class),
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
