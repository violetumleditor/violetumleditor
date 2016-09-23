package com.horstmann.violet.product.diagram.state;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class StateDiagramConstant
{
    public static final String STATE_DIAGRAM_STRINGS = "properties.StateDiagramGraphStrings";
    public static final ResourceBundle STATE_DIAGRAM_RESOURCE = ResourceBundle.getBundle(STATE_DIAGRAM_STRINGS, Locale.getDefault());
}