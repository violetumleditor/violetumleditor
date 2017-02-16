package com.horstmann.violet.product.diagram.component;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class ComponentDiagramConstant
{
    public static final String COMPONENT_DIAGRAM_STRINGS = "properties.ComponentDiagramGraphStrings";
    public static final ResourceBundle COMPONENT_DIAGRAM_RESOURCE = ResourceBundle.getBundle(COMPONENT_DIAGRAM_STRINGS, Locale.getDefault());
}