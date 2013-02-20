package com.horstmann.violet.framework.injection.resources;

import java.awt.Image;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;

/**
 * This class can inject external values into object attributes. To do that, it uses the {@link ResourceBundleBean}
 * annotation. Injected objects are created using the {@link ResourceFactory}
 * 
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class ResourceBundleInjector
{

    /**
     * Unique instance
     */
    private static ResourceBundleInjector instance;

    /**
     * Singleton constructor
     */
    private ResourceBundleInjector()
    {
        // Singleton pattern
    }

    /**
     * @return injector instance
     */
    public static ResourceBundleInjector getInjector()
    {
        if (ResourceBundleInjector.instance == null)
        {
            ResourceBundleInjector.instance = new ResourceBundleInjector();
        }
        return ResourceBundleInjector.instance;
    }

    /**
     * Injects values into object attributes if they are correctly annoted
     * 
     * @param o which need injection
     */
    public void inject(Object o)
    {
        ResourceBundle classResourceBundle = getPropertyFile(o);
        Class<?> referencedClass = getReferencedClass(o);
        ResourceFactory classResourceFactory = new ResourceFactory(classResourceBundle, referencedClass);
        // Injects on constructors
        for (Constructor<?> aConstructor : o.getClass().getDeclaredConstructors())
        {
            for (Annotation annotation : aConstructor.getAnnotations())
            {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType.equals(ResourceBundleBean.class))
                {
                    ResourceBundleBean constructorAnnotation = (ResourceBundleBean) annotation;
                    injectState(o, constructorAnnotation, classResourceFactory);
                }
            }
        }
        // Injects on fields
        for (Field aField : o.getClass().getDeclaredFields())
        {
            for (Annotation annotation : aField.getAnnotations())
            {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType.equals(ResourceBundleBean.class))
                {
                    ResourceBundleBean propertyAnnotation = (ResourceBundleBean) annotation;
                    try
                    {
                        aField.setAccessible(true);
                        if (!propertyAnnotation.resourceReference().equals(Object.class))
                        {
                            ResourceBundle fieldResourceBundle = ResourceBundle.getBundle(propertyAnnotation.resourceReference()
                                    .getName(), Locale.getDefault());
                            ResourceFactory fieldResourceFactory = new ResourceFactory(fieldResourceBundle, o.getClass());
                            injectValue(o, aField, propertyAnnotation, fieldResourceFactory);
                        }
                        else
                        {
                            injectValue(o, aField, propertyAnnotation, classResourceFactory);
                        }
                    }
                    catch (IllegalAccessException e)
                    {
                        throw new RuntimeException("Error while injecting external property values", e);
                    }
                }
            }
        }
    }

    /**
     * Gets the correct property file.
     * 
     * @param o object which need injection
     * @return resource bundle found
     */
    private ResourceBundle getPropertyFile(Object o)
    {
        Class<?> referencedClass = getReferencedClass(o);
        return ResourceBundle.getBundle(referencedClass.getName(), Locale.getDefault());
    }

    /**
     * Gets the referenced class
     * 
     * @param o object which need injection
     * @return the corresponding class or the class declared in ResourceBundleBean.resourceReference()
     */
    private Class<?> getReferencedClass(Object o)
    {
        for (Annotation annotation : o.getClass().getAnnotations())
        {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (annotationType.equals(ResourceBundleBean.class))
            {
                ResourceBundleBean propertyAnnotation = (ResourceBundleBean) annotation;
                Class ref = propertyAnnotation.resourceReference();
                if (!Object.class.equals(ref))
                {
                    return ref;
                }
            }
        }
        return o.getClass();
    }
    
    /**
     * Injects values
     * 
     * @param concernedObject = object which need injection
     * @param field = field to inject
     * @param annotation = current field annotation
     * @param resourceFactory = resource factory belonging to the concernedObject
     * @throws IllegalAccessException if field is not accessible
     */
    private void injectValue(Object concernedObject, Field field, ResourceBundleBean annotation,
            ResourceFactory resourceFactory) throws IllegalAccessException
    {
        String propertyPrefix = (!"".equals(annotation.key()) ? annotation.key() : field.getName());
        Class<?> fieldType = field.getType();
        if (fieldType.equals(String.class))
        {
            String value = resourceFactory.createString(propertyPrefix);
            field.set(concernedObject, value);
        }
        if (fieldType.equals(ImageIcon.class))
        {
            ImageIcon value = resourceFactory.createIcon(propertyPrefix);
            field.set(concernedObject, value);
        }
        if (fieldType.equals(Image.class))
        {
            Image value = resourceFactory.createImage(propertyPrefix);
            field.set(concernedObject, value);
        }
        if (fieldType.equals(JButton.class))
        {
            JButton value = resourceFactory.createButton(propertyPrefix);
            field.set(concernedObject, value);
        }
        if (fieldType.equals(JMenu.class))
        {
            JMenu value = resourceFactory.createMenu(propertyPrefix);
            field.set(concernedObject, value);
        }
        if (fieldType.equals(JMenuItem.class))
        {
            JMenuItem value = resourceFactory.createMenuItem(propertyPrefix);
            field.set(concernedObject, value);
        }
        if (fieldType.equals(JCheckBoxMenuItem.class))
        {
            JMenuItem value = resourceFactory.createCheckBoxMenuItem(propertyPrefix);
            field.set(concernedObject, value);
        }
    }

    /**
     * Configures an object
     * 
     * @param concernedObject
     * @param annotation
     * @param resourceFactory
     */
    private void injectState(Object concernedObject, ResourceBundleBean annotation, ResourceFactory resourceFactory)
    {
        Class<?> clazz = concernedObject.getClass();
        String propertyPrefix = annotation.key();
        if ("".equals(propertyPrefix))
        {
            return;
        }
        if (JMenu.class.isAssignableFrom(clazz))
        {
            resourceFactory.configureMenu((JMenu) concernedObject, propertyPrefix);
        }
    }

}
