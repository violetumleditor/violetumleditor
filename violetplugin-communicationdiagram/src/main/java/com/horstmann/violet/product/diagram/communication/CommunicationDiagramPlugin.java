package com.horstmann.violet.product.diagram.communication;

import java.util.Locale;
import java.util.ResourceBundle;

import com.horstmann.violet.framework.plugin.AbstractDiagramPlugin;
import com.horstmann.violet.framework.plugin.IDiagramPlugin;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
/**
 * 
 * @author Artur Ratajczak
 *
 */
public class CommunicationDiagramPlugin extends AbstractDiagramPlugin
{
	public CommunicationDiagramPlugin()
	{
		super(CommunicationDiagramGraph.class, CommunicationDiagramConstant.COMMUNICATION_DIAGRAM_STRINGS);
	}

	@Override
	public String getProvider() {
		return "Artur Ratajczak / Adrian Bobrowski";
	}

	@Override
	public String getVersion()
	{
		return "1.0.3";
	}
}
