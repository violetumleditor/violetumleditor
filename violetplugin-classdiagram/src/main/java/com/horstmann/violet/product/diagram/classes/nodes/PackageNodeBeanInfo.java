package com.horstmann.violet.product.diagram.classes.nodes;


import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * The bean info for the PackageNode type.
 */
public class PackageNodeBeanInfo extends SimpleBeanInfo
{

    @Override
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        try
        {
            PropertyDescriptor nameDescriptor = new PropertyDescriptor("name", PackageNode.class);
            nameDescriptor.setValue("priority", 1);
            PropertyDescriptor contentDescriptor = new PropertyDescriptor("content", PackageNode.class);
            contentDescriptor.setValue("priority", 2);
            return new PropertyDescriptor[]
            {
                    nameDescriptor,
                    contentDescriptor
            };
        }
        catch (IntrospectionException exception)
        {
            return null;
        }
    }
}
