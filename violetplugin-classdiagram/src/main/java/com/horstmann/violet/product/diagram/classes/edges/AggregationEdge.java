package com.horstmann.violet.product.diagram.classes.edges;

import com.horstmann.violet.product.diagram.abstracts.property.ArrowHead;
import com.horstmann.violet.product.diagram.abstracts.property.LineStyle;

public class AggregationEdge extends ClassRelationshipEdge
{

    @Override
    public ArrowHead getStartArrowHead()
    {
        return ArrowHead.NONE;
    }
    
    @Override
    public ArrowHead getEndArrowHead()
    {
        return ArrowHead.DIAMOND;
    }

    @Override
    public LineStyle getLineStyle()
    {
        return LineStyle.SOLID;
    }
    
}
