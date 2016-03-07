package com.horstmann.violet.product.diagram.activity;

import com.horstmann.violet.framework.plugin.AbstractDiagramPlugin;
import com.horstmann.violet.framework.plugin.IDiagramPlugin;
import com.horstmann.violet.product.diagram.abstracts.IGraph;

/**
 * Describes activity diagram graph type
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class ActivityDiagramPlugin extends AbstractDiagramPlugin
{
    public ActivityDiagramPlugin()
    {
        super(ActivityDiagramGraph.class, ActivityDiagramConstant.ACTIVITY_DIAGRAM_STRINGS);
    }

    @Override
    public String getVersion()
    {
        return "2.1.3";
    }
}
