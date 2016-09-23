package com.horstmann.violet.product.diagram.communication;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 
 * @author Artur Ratajczak
 *
 */
public abstract class CommunicationDiagramConstant
{
	public static final String COMMUNICATION_DIAGRAM_STRINGS = "properties.CommunicationDiagramGraphStrings";
	public static final ResourceBundle COMMUNICATION_DIAGRAM_RESOURCE = ResourceBundle.getBundle(COMMUNICATION_DIAGRAM_STRINGS, Locale.getDefault());
}
