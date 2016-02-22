package com.horstmann.violet.product.diagram.common.edge;

import com.horstmann.violet.product.diagram.abstracts.edge.ArrowheadEdgeBeanInfo;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 27.12.2015
 */
public class BasePropertyEdgeBeanInfo extends ArrowheadEdgeBeanInfo
{
    public BasePropertyEdgeBeanInfo()
    {
        super(BasePropertyEdge.class);
    }

    protected BasePropertyEdgeBeanInfo(Class<?> beanClass)
    {
        super(beanClass);
    }

//    @Override
//    public PropertyDescriptor[] getPropertyDescriptors()
//    {
//        try
//        {
//            PropertyDescriptor[] descriptors = new PropertyDescriptor[]
//            {
//                new PropertyDescriptor("startLabel", BasePropertyEdge.class),
//                new PropertyDescriptor("middleLabel", BasePropertyEdge.class),
//                new PropertyDescriptor("endLabel", BasePropertyEdge.class),
//                new PropertyDescriptor("bentStyle", BasePropertyEdge.class),
//            };
//            for (int i = 0; i < descriptors.length; i++)
//            {
//                descriptors[i].setValue("priority", new Integer(i));
//            }
//            return descriptors;
//        }
//        catch (IntrospectionException exception)
//        {
//            exception.printStackTrace();
//            return null;
//        }
//    }
}
