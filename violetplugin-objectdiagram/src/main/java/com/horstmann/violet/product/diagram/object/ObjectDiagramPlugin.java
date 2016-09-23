package com.horstmann.violet.product.diagram.object;

import com.horstmann.violet.framework.plugin.AbstractDiagramPlugin;

/**
 * Describes object diagram graph type
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class ObjectDiagramPlugin extends AbstractDiagramPlugin
{
    public ObjectDiagramPlugin()
    {
        super(ObjectDiagramGraph.class, ObjectDiagramConstant.OBJECT_DIAGRAM_STRINGS);
    }

    @Override
    public String getVersion()
    {
        return "2.0.2";
    }
}