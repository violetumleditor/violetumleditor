package com.horstmann.violet.framework.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 03.01.2016
 */
public class ResourceManager
{
    public ResourceManager()
    {
        this.resourceBundles = new ArrayList<ResourceBundle>();
    }

    public ResourceManager(ResourceBundle resourceBundle)
    {
        this();
        addResource(resourceBundle);
    }

    public ResourceManager(String baseName)
    {
        this(ResourceBundle.getBundle(baseName, Locale.getDefault()));
    }

    public final void addResource(String baseName)
    {
        addResource(ResourceBundle.getBundle(baseName, Locale.getDefault()));
    }

    public final void addResource(ResourceBundle resourceBundle)
    {
        this.resourceBundles.add(0,resourceBundle);
    }

    public final String getString(String key)
    {
        while(true)
        {
            for (ResourceBundle resourceBundle : resourceBundles)
            {
                try
                {
                    return resourceBundle.getString(key);
                }
                catch (Exception ignored)
                {}
            }
            int indexOfSeparator = key.indexOf('.');
            if (-1 == indexOfSeparator)
            {
                break;
            }
            key = key.substring(indexOfSeparator+1);
        }

        return key;
    }

    private List<ResourceBundle> resourceBundles;
}
