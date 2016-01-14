package com.horstmann.violet.product.diagram.activity;

import com.horstmann.violet.framework.util.ResourceManager;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 03.01.2016
 */
public class ActivityResource extends ResourceManager
{
    protected ActivityResource(String baseName) {
        super(baseName);
    }

    public final static ResourceManager ACTIVITY = new ActivityResource("properties.ActivityDiagramGraphStrings");
}
