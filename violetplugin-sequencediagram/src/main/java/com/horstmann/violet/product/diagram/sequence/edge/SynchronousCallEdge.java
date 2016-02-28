/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.product.diagram.sequence.edge;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.horstmann.violet.product.diagram.common.edge.LabeledLineEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.sequence.SequenceDiagramConstant;
import com.horstmann.violet.product.diagram.sequence.node.ActivationBarNode;

/**
 * An edge that joins two call node. Typically, call edges are used in sequence diagram to represent calls between entities (call
 * node).
 */
public class SynchronousCallEdge extends LabeledLineEdge
{
    public SynchronousCallEdge() {}

    protected SynchronousCallEdge(SynchronousCallEdge clone)
    {
        super(clone);
    }

    @Override
    public SynchronousCallEdge copy()
    {
        return new SynchronousCallEdge(this);
    }

    @Override
    public String getToolTip()
    {
        return SequenceDiagramConstant.SEQUENCE_DIAGRAM_RESOURCE.getString("synchronous_call_edge.tooltip");
    }

    @Override
    public Line2D getConnectionPoints()
    {
        ArrayList<Point2D> points = getPoints();
        Point2D p1 = points.get(0);
        Point2D p2 = points.get(points.size() - 1);
        return new Line2D.Double(p1, p2);
    }

//    @Override
    public ArrayList<Point2D> getPoints()
    {
        INode endingNode = getEndNode();
        INode startingNode = getStartNode();
        if (isActivationBarsOnSameLifeline(startingNode, endingNode))
        {
            return getPointsForLoopOnActivationBarNode(startingNode, endingNode);
        }
        return getPointsForActivationBarsOnDifferentLifeLines(startingNode, endingNode);
    }

    private boolean isActivationBarsOnSameLifeline(INode startingNode, INode endingNode)
    {
        if (startingNode.getClass().isAssignableFrom(ActivationBarNode.class)
                && endingNode.getClass().isAssignableFrom(ActivationBarNode.class))
        {
            ActivationBarNode startingActivationBarNode = (ActivationBarNode) startingNode;
            ActivationBarNode endingActivationBarNode = (ActivationBarNode) endingNode;
            if (startingActivationBarNode.getParents().get(0) == endingActivationBarNode.getParents().get(0))
            {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Point2D> getPointsForActivationBarsOnDifferentLifeLines(INode startingNode, INode endingNode)
    {
        Point2D startingPoint = startingNode.getConnectionPoint(this);
        Point2D endingPoint = endingNode.getConnectionPoint(this);
        ArrayList<Point2D> a = new ArrayList<Point2D>();
        a.add(startingPoint);
        a.add(endingPoint);
        return a;
    }

    private ArrayList<Point2D> getPointsForLoopOnActivationBarNode(INode startingNode, INode endingNode)
    {
        ArrayList<Point2D> a = new ArrayList<Point2D>();
        Point2D startingPoint = startingNode.getConnectionPoint(this);
        Point2D endingPoint = endingNode.getConnectionPoint(this);
        Point2D p = startingPoint;
        Point2D q = new Point2D.Double(endingPoint.getX() + LOOP_GAP, p.getY());
        Point2D r = new Point2D.Double(q.getX(), endingPoint.getY());
        Point2D s = endingPoint;
        a.add(p);
        a.add(q);
        a.add(r);
        a.add(s);
        return a;
    }

    /** Horizintal gap used to connected two activation bars on the same lifeline */
    private static int LOOP_GAP = 15;
}
