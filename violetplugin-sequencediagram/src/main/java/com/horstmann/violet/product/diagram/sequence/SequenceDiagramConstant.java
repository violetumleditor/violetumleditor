package com.horstmann.violet.product.diagram.sequence;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class SequenceDiagramConstant
{
    public static final String SEQUENCE_DIAGRAM_STRINGS = "properties.SequenceDiagramGraphStrings";
    public static final ResourceBundle SEQUENCE_DIAGRAM_RESOURCE = ResourceBundle.getBundle(SEQUENCE_DIAGRAM_STRINGS, Locale.getDefault());
}
