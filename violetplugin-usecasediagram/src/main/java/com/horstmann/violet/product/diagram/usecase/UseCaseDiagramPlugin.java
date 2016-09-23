package com.horstmann.violet.product.diagram.usecase;

import com.horstmann.violet.framework.plugin.AbstractDiagramPlugin;

/**
 * Describes use case diagram graph type
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class UseCaseDiagramPlugin extends AbstractDiagramPlugin
{
    public UseCaseDiagramPlugin()
    {
        super(UseCaseDiagramGraph.class, UseCaseDiagramConstant.USE_CASE_DIAGRAM_STRINGS);
    }

    @Override
    public String getVersion()
    {
        return "2.0.0";
    }
}