package com.horstmann.violet.framework.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 21.02.2016
 */
public abstract class BeanInfo extends SimpleBeanInfo
{
    protected BeanInfo(Class<?> beanClass, ResourceBundle resourceBundle)
    {
        this(beanClass);
        addResourceBundle(resourceBundle);
    }

    protected BeanInfo(Class<?> beanClass)
    {
        this.beanClass = beanClass;
        resourceManager = new ResourceManager();
    }

    protected List<PropertyDescriptor> createPropertyDescriptorList()
    {
        return new ArrayList<PropertyDescriptor>();
    }

    protected final void addResourceBundle(ResourceBundle resourceBundle)
    {
        this.resourceManager.addResource(resourceBundle);
    }

    protected final void addResourceBundle(String baseName)
    {
        this.resourceManager.addResource(baseName);
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

    protected final PropertyDescriptor createPropertyDescriptor(String valueName, String resourceKey, int priority)
    {
    	PropertyDescriptor propertyDescriptor = null;
        try
        {
            propertyDescriptor = new PropertyDescriptor(valueName, beanClass);
            propertyDescriptor.setValue("label", resourceManager.getString(resourceKey));
            propertyDescriptor.setValue("priority", new Integer(priority));

        }
        catch (IntrospectionException exception)
        {   
            MessageFormat messageFormatter = new MessageFormat(CREATE_PROPERTY_DESCRIPTOR_FAIL_MESSAGE_FORMAT);
            String message = messageFormatter.format(new Object[]{valueName,resourceKey,exception.getMessage()});
            LOGGER.log(Level.WARNING, message);
        }
        return propertyDescriptor;
    }

    private static final Logger LOGGER = Logger.getLogger(BeanInfo.class.getName());
    private ResourceManager resourceManager;
    private final Class<?> beanClass;
    private final String CREATE_PROPERTY_DESCRIPTOR_FAIL_MESSAGE_FORMAT = "Create property descriptor fail valueName: {0} resourceKey: {1}\nIntrospectionException:{2}";
}
