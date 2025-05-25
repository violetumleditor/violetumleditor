package com.horstmann.violet.product.diagram.classes.edges;

import com.horstmann.violet.product.diagram.abstracts.edge.SegmentedLineEdge;
import com.horstmann.violet.product.diagram.abstracts.property.ArrowHead;
import com.horstmann.violet.product.diagram.abstracts.property.LineStyle;

public class AssociationEdge extends SegmentedLineEdge
{

    @Override
    public ArrowHead getStartArrowHead()
    {
        return ArrowHead.NONE;
    }
    
    @Override
    public ArrowHead getEndArrowHead()
    {
        return ArrowHead.V;
    }

    @Override
    public LineStyle getLineStyle()
    {
        return LineStyle.SOLID;
    }
    
}
