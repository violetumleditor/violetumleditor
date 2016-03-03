package com.horstmann.violet.product.diagram.classes.edges;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.property.ArrowHead;
import com.horstmann.violet.product.diagram.abstracts.property.BentStyle;
import com.horstmann.violet.product.diagram.abstracts.property.LineStyle;
import com.horstmann.violet.product.diagram.common.edge.BasePropertyEdge;

public class LollipopEdge extends BasePropertyEdge
{

    @Override
    public ArrowHead getStartArrowHead()
    {
        return ArrowHead.NONE;
    }

    @Override
    public ArrowHead getEndArrowHead()
    {
        return ArrowHead.NONE;
    }

    @Override
    public LineStyle getLineStyle()
    {
        return LineStyle.SOLID;
    }
    
 
    
}
