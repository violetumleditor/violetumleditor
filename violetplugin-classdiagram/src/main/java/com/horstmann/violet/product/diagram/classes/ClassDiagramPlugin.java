package com.horstmann.violet.product.diagram.classes;

import com.horstmann.violet.framework.plugin.AbstractDiagramPlugin;

/**
 * Describes class diagram graph type
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class ClassDiagramPlugin extends AbstractDiagramPlugin
{
    public ClassDiagramPlugin()
    {
        super(ClassDiagramGraph.class, ClassDiagramConstant.CLASS_DIAGRAM_STRINGS);
    }

    @Override
    public String getVersion()
    {
        return "2.3.0";
    }
}
