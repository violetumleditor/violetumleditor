package com.horstmann.violet.product.diagram.sequence.edge;

import com.horstmann.violet.product.diagram.common.edge.LabeledLineEdge;
import com.horstmann.violet.product.diagram.property.ArrowheadChoiceList;
import com.horstmann.violet.product.diagram.property.LineStyleChoiceList;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 04.03.2016
 */
public abstract class CallEdge extends LabeledLineEdge
{
    public CallEdge()
    {
        setStartArrowhead(ArrowheadChoiceList.NONE);
        setLineStyle(LineStyleChoiceList.SOLID);
    }

    protected CallEdge(CallEdge clone)
    {
        super(clone);
    }

    @Override
    public void deserializeSupport()
    {
        super.deserializeSupport();
        setStartArrowhead(ArrowheadChoiceList.NONE);
        setLineStyle(LineStyleChoiceList.SOLID);
    }

    @Override
    protected void updateContactPoints()
    {
        Line2D connectionPoints = getConnectionPoints();

        if (null != getStartNode().getParent() &&
            null != getEndNode().getParent() &&
            getStartNode().getParents().get(0) == getEndNode().getParents().get(0))
        {
            contactPoints = new Point2D[4];
            contactPoints[0] = connectionPoints.getP1();
            contactPoints[3] = connectionPoints.getP2();
            contactPoints[1] = new Point2D.Double(contactPoints[3].getX() + LOOP_GAP, contactPoints[0].getY());
            contactPoints[2] = new Point2D.Double(contactPoints[3].getX() + LOOP_GAP, contactPoints[3].getY());
        }
        else
        {
            contactPoints = new Point2D[2];
            contactPoints[0] = connectionPoints.getP1();
            contactPoints[1] = connectionPoints.getP2();
        }
    }

    @Override
    public Line2D getConnectionPoints()
    {
        return new Line2D.Double(
                getStartNode().getConnectionPoint(this),
                getEndNode().getConnectionPoint(this)
        );
    }

    /** Horizintal gap used to connected two activation bars on the same lifeline */
    private static int LOOP_GAP = 15;
}
