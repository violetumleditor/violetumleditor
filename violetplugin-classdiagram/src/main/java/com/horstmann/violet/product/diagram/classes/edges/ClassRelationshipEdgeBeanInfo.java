package com.horstmann.violet.product.diagram.classes.edges;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * The bean info for the ClassRelationshipEdge type.
 */
public class ClassRelationshipEdgeBeanInfo extends SimpleBeanInfo
{
    
    @Override
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        try
        {
            PropertyDescriptor[] descriptors = new PropertyDescriptor[]
            {
                    new PropertyDescriptor("startArrowHead", ClassRelationshipEdge.class),
                    new PropertyDescriptor("startLabel", ClassRelationshipEdge.class),
                    new PropertyDescriptor("middleLabel", ClassRelationshipEdge.class),
                    new PropertyDescriptor("endLabel", ClassRelationshipEdge.class),
                    new PropertyDescriptor("endArrowHead", ClassRelationshipEdge.class),
                    new PropertyDescriptor("bentStyle", ClassRelationshipEdge.class),
                    new PropertyDescriptor("lineStyle", ClassRelationshipEdge.class),
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
