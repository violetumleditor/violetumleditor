package com.horstmann.violet.product.diagram.component;

import com.horstmann.violet.framework.plugin.AbstractDiagramPlugin;

/**
 * Describes component diagram graph type
 */
public class ComponentDiagramPlugin extends AbstractDiagramPlugin
{
    public ComponentDiagramPlugin()
    {
        super(ComponentDiagramGraph.class, ComponentDiagramConstant.COMPONENT_DIAGRAM_STRINGS);
    }

    @Override
    public String getVersion()
    {
        return "1.0.0";
    }
}