package com.horstmann.violet.product.diagram.classes;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class ClassDiagramConstant
{
    public static final String CLASS_DIAGRAM_STRINGS = "properties.ClassDiagramGraphStrings";
    public static final ResourceBundle CLASS_DIAGRAM_RESOURCE = ResourceBundle.getBundle(CLASS_DIAGRAM_STRINGS, Locale.getDefault());
}
