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
public class AssociationEdge extends LabeledLineEdge
{
    public AssociationEdge()
    {
        super();
        setStartArrowhead(ArrowheadChoiceList.NONE);
        setEndArrowhead(ArrowheadChoiceList.V);
        setLineStyle(LineStyleChoiceList.SOLID);
    }

    protected AssociationEdge(AssociationEdge cloned)
    {
        super(cloned);
    }

    @Override
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();
        setStartArrowhead(ArrowheadChoiceList.NONE);
        setEndArrowhead(ArrowheadChoiceList.V);
        setLineStyle(LineStyleChoiceList.SOLID);
    }

    @Override
    protected AssociationEdge copy() throws CloneNotSupportedException
    {
        return new AssociationEdge(this);
    }

    @Override
    public String getToolTip()
    {
        return ClassDiagramConstant.CLASS_DIAGRAM_RESOURCE.getString("tooltip.association_edge");
    }
}
