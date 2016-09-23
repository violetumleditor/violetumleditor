package com.horstmann.violet.product.diagram.sequence;

import com.horstmann.violet.framework.plugin.AbstractDiagramPlugin;

/**
 * Describes sequence diagram graph type
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class SequenceDiagramPlugin extends AbstractDiagramPlugin
{
    public SequenceDiagramPlugin()
    {
        super(SequenceDiagramGraph.class, SequenceDiagramConstant.SEQUENCE_DIAGRAM_STRINGS);
    }

    @Override
    public String getVersion()
    {
        return "2.1.0";
    }
}
