package com.horstmann.violet.product.diagram.usecase;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class UseCaseDiagramConstant
{
    public static final String USE_CASE_DIAGRAM_STRINGS = "properties.UseCaseDiagramGraphStrings";
    public static final ResourceBundle USE_CASE_DIAGRAM_RESOURCE = ResourceBundle.getBundle(USE_CASE_DIAGRAM_STRINGS, Locale.getDefault());
}