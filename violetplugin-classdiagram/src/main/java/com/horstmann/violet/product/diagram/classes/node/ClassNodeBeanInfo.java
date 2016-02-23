package com.horstmann.violet.product.diagram.classes.node;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 20.02.2016
 */
public class ClassNodeBeanInfo extends SimpleBeanInfo
{
    /*
     * (non-Javadoc)
     *
     * @see java.beans.BeanInfo#getPropertyDescriptors()
     */
    public PropertyDescriptor[] getPropertyDescriptors()
    {
        try
        {
            PropertyDescriptor nameDescriptor = new PropertyDescriptor("name", ClassNode.class);
            nameDescriptor.setValue("priority", new Integer(1));
            PropertyDescriptor attributesDescriptor = new PropertyDescriptor("attributes", ClassNode.class);
            attributesDescriptor.setValue("priority", new Integer(2));
            PropertyDescriptor methodsDescriptor = new PropertyDescriptor("methods", ClassNode.class);
            methodsDescriptor.setValue("priority", new Integer(3));

            PropertyDescriptor backgroundColorDescriptor = new PropertyDescriptor("backgroundColor", ClassNode.class);
            backgroundColorDescriptor.setValue("priority", new Integer(4));
            return new PropertyDescriptor[]
                    {
                            nameDescriptor,
                            attributesDescriptor,
                            methodsDescriptor,
                            backgroundColorDescriptor
                    };
        }
        catch (IntrospectionException exception)
        {
            return null;
        }
    }
}
