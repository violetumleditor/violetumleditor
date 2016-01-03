package com.horstmann.violet.framework.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Adrian Bobrowski on 03.01.2016.
 */
public abstract class ResourceManager
{
    protected ResourceManager(String baseName)
    {
        resourceBundle = ResourceBundle.getBundle(baseName, Locale.getDefault());
    }

    public final String getResourceString(String key)
    {
        return resourceBundle.getString(key);
    }

    private ResourceBundle resourceBundle;
}
