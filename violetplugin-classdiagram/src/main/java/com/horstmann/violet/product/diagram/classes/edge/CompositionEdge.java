package com.horstmann.violet.product.diagram.classes.edge;

import com.horstmann.violet.product.diagram.classes.ClassDiagramConstant;
import com.horstmann.violet.product.diagram.property.ArrowheadChoiceList;
import com.horstmann.violet.product.diagram.property.LineStyleChoiceList;
import com.horstmann.violet.product.diagram.common.edge.LabeledLineEdge;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 23.02.2016
 */
public class CompositionEdge extends LabeledLineEdge
{
    public CompositionEdge()
    {
        super();
        setStartArrowhead(ArrowheadChoiceList.NONE);
        setEndArrowhead(ArrowheadChoiceList.DIAMOND_BLACK);
        setLineStyle(LineStyleChoiceList.SOLID);
    }

    protected CompositionEdge(CompositionEdge cloned)
    {
        super(cloned);
    }

    @Override
    public void deserializeSupport()
    {
        super.deserializeSupport();
        setStartArrowhead(ArrowheadChoiceList.NONE);
        setEndArrowhead(ArrowheadChoiceList.DIAMOND_BLACK);
        setLineStyle(LineStyleChoiceList.SOLID);
    }

    @Override
    protected CompositionEdge copy() throws CloneNotSupportedException
    {
        return new CompositionEdge(this);
    }

    @Override
    public String getToolTip()
    {
        return ClassDiagramConstant.CLASS_DIAGRAM_RESOURCE.getString("composition_edge.tooltip");
    }
}
