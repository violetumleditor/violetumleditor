package com.horstmann.violet.framework.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PropertyUtils
{

    public static void copyProperties(Object fromBean, Object toBean) {
        try
        {
            BeanInfo info = Introspector.getBeanInfo(fromBean.getClass());
            PropertyDescriptor[] descriptors = (PropertyDescriptor[]) info.getPropertyDescriptors().clone();
            for (int i = 0; i < descriptors.length; i++) {
                PropertyDescriptor prop = descriptors[i];
                Method getter = prop.getReadMethod();
                Method setter = prop.getWriteMethod();
                if (getter != null && setter != null) {
                    Object value = getter.invoke(fromBean, new Object[] {});
                    setter.invoke(toBean, new Object[] {value});
                }
            }
        }
        catch (IntrospectionException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }

    public static void setProperty(Object bean, String propertyName, Object propertyValue) 
    {
        try
        {
            BeanInfo info = Introspector.getBeanInfo(bean.getClass());
            for (PropertyDescriptor prop : info.getPropertyDescriptors()) 
            {
                if (prop.getName().equals(propertyName))
                {
                    Method setter = prop.getWriteMethod();
                    if (setter != null) 
                        setter.invoke(bean, propertyValue);
                    return;
                }
            }
        }
        catch (IntrospectionException e)
        {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }    
}
