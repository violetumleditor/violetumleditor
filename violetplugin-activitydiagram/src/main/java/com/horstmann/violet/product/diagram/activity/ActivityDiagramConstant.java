package com.horstmann.violet.product.diagram.activity;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class ActivityDiagramConstant
{
    public static final String ACTIVITY_DIAGRAM_STRINGS = "properties.ActivityDiagramGraphStrings";
    public static final ResourceBundle ACTIVITY_DIAGRAM_RESOURCE = ResourceBundle.getBundle(ACTIVITY_DIAGRAM_STRINGS, Locale.getDefault());
}
