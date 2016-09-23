package com.horstmann.violet.product.diagram.object;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class ObjectDiagramConstant
{
    public static final String OBJECT_DIAGRAM_STRINGS = "properties.ObjectDiagramGraphStrings";
    public static final ResourceBundle OBJECT_DIAGRAM_RESOURCE = ResourceBundle.getBundle(OBJECT_DIAGRAM_STRINGS, Locale.getDefault());
}