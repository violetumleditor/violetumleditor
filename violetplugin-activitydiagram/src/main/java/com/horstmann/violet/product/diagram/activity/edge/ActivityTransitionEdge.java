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

package com.horstmann.violet.product.diagram.activity.edge;

import java.awt.geom.Point2D;

import com.horstmann.violet.product.diagram.activity.ActivityDiagramConstant;
import com.horstmann.violet.product.diagram.property.ArrowheadChoiceList;
import com.horstmann.violet.product.diagram.property.BentStyleChoiceList;
import com.horstmann.violet.product.diagram.property.LineStyleChoiceList;
import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.common.edge.LabeledLineEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.activity.node.SynchronizationBarNode;

/**
 * An edge that is shaped like a line with up to three segments with an arrowhead
 */
public class ActivityTransitionEdge extends LabeledLineEdge
{
    public ActivityTransitionEdge()
    {
        super();
        setStartArrowhead(ArrowheadChoiceList.NONE);
        setEndArrowhead(ArrowheadChoiceList.V);
        setLineStyle(LineStyleChoiceList.SOLID);
    }

    protected ActivityTransitionEdge(ActivityTransitionEdge cloned)
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
    protected ActivityTransitionEdge copy() throws CloneNotSupportedException
    {
        return new ActivityTransitionEdge(this);
    }

    @Override
    public String getToolTip()
    {
        return ActivityDiagramConstant.ACTIVITY_DIAGRAM_RESOURCE.getString("tooltip.transition_edge");
    }

    @Override
    public Direction getDirection(INode node)
    {
        BentStyleChoiceList bStyle = ((BentStyleChoiceList)getBentStyleChoiceList());
        Direction straightDirection = super.getDirection(node);
        double x = straightDirection.getX();
        double y = straightDirection.getY();


        if (node.equals(getStartNode()))
        {
            if (BentStyleChoiceList.HV.equals(bStyle) || BentStyleChoiceList.HVH.equals(bStyle))
            {
                return (x >= 0) ? Direction.EAST : Direction.WEST;
            }
            if (BentStyleChoiceList.VH.equals(bStyle) || BentStyleChoiceList.VHV.equals(bStyle))
            {
                return (y >= 0) ? Direction.SOUTH : Direction.NORTH;
            }
        }
        if (node.equals(getEndNode()))
        {
            if (BentStyleChoiceList.HV.equals(bStyle) || BentStyleChoiceList.VHV.equals(bStyle))
            {
                return (y >= 0) ? Direction.SOUTH : Direction.NORTH;
            }
            if (BentStyleChoiceList.VH.equals(bStyle) || BentStyleChoiceList.HVH.equals(bStyle))
            {
                return (x >= 0) ? Direction.EAST : Direction.WEST;
            }
        }
        if (SynchronizationBarNode.class.isInstance(getStartNode()) ||
            SynchronizationBarNode.class.isInstance(getEndNode()))
        {
            SynchronizationBarNode synchronizationBarNode;

            if (SynchronizationBarNode.class.isInstance(getStartNode()))
            {
                synchronizationBarNode = (SynchronizationBarNode)getStartNode();
            }
            else
            {
                synchronizationBarNode = (SynchronizationBarNode)getEndNode();
            }

            Point2D p1 = node.getLocationOnGraph();
            Point2D p2;

            if (node.equals(getStartNode()))
            {
                p2 = getEndNode().getLocationOnGraph();
            }
            else
            {
                p2 = getStartNode().getLocationOnGraph();
            }

            if(SynchronizationBarNode.HORIZONTAL == synchronizationBarNode.getStretch())
            {
                if (p1.getY() > p2.getY())
                {
                    return Direction.SOUTH;
                }
                else
                {
                    return Direction.NORTH;
                }
            }
            else
            {
                if (p1.getX() > p2.getX())
                {
                    return Direction.EAST;
                }
                else
                {
                    return Direction.WEST;
                }
            }
        }
        return straightDirection;
    }
}
