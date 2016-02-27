package com.horstmann.violet.framework.util;

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
    public ResourceManager(String baseName)
    {
        resourceBundle = ResourceBundle.getBundle(baseName, Locale.getDefault());
    }

    public final String getString(String key)
    {
        return resourceBundle.getString(key);
    }

//    public static final ResourceManager NODE_AND_EDGE = new ResourceManager("properties.NodeAndEdgeStrings");

    private ResourceBundle resourceBundle;
}
