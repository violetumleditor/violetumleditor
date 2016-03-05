package com.horstmann.violet.product.diagram.object.edge;

import com.horstmann.violet.product.diagram.abstracts.edge.bentstyle.BentStyle;
import com.horstmann.violet.product.diagram.common.edge.LabeledLineEdge;
import com.horstmann.violet.product.diagram.object.ObjectDiagramConstant;
import com.horstmann.violet.product.diagram.property.ArrowheadChoiceList;
import com.horstmann.violet.product.diagram.property.LineStyleChoiceList;

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
        setBentStyle(BentStyle.STRAIGHT);
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
        setBentStyle(BentStyle.STRAIGHT);
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
        return ObjectDiagramConstant.OBJECT_DIAGRAM_RESOURCE.getString("association_edge.tooltip");
    }
}
