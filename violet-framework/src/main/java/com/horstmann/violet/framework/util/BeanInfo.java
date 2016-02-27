package com.horstmann.violet.framework.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 21.02.2016
 */
public abstract class BeanInfo extends SimpleBeanInfo
{
    protected BeanInfo(Class<?> beanClass)
    {
        this.beanClass = beanClass;
    }

    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        return new ArrayList<PropertyDescriptor>();
    }

    /**
     * (non-Javadoc)
     *
     * @see java.beans.BeanInfo#getPropertyDescriptors()
     */
    public final PropertyDescriptor[] getPropertyDescriptors()
    {
        List<PropertyDescriptor> propertyDescriptorList = createPropertyDescriptorList();
        while(true)
        {
            if (!(propertyDescriptorList.remove(null)))
            {
                break;
            }
        }
        return propertyDescriptorList.toArray(new PropertyDescriptor[propertyDescriptorList.size()]);
    }

    protected final PropertyDescriptor createPropertyDescriptor(String valueName, int priority)
    {
        return createPropertyDescriptor(valueName,valueName,priority);
    }

    protected final PropertyDescriptor createPropertyDescriptor(String valueName, String label, int priority)
    {
        try
        {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(valueName, beanClass);
            propertyDescriptor.setValue("label", label);
            propertyDescriptor.setValue("priority", new Integer(priority));

            return propertyDescriptor;
        }
        catch (IntrospectionException e)
        {
            return null;
        }
    }

    private final Class<?> beanClass;
}
