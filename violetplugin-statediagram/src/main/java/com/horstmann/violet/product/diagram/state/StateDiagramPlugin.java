package com.horstmann.violet.product.diagram.state;

import com.horstmann.violet.framework.plugin.AbstractDiagramPlugin;

/**
 * Describes state diagram graph type
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class StateDiagramPlugin extends AbstractDiagramPlugin
{
    public StateDiagramPlugin()
    {
        super(StateDiagramGraph.class, StateDiagramConstant.STATE_DIAGRAM_STRINGS);
    }

    @Override
    public String getVersion()
    {
        return "2.1.1";
    }
}
