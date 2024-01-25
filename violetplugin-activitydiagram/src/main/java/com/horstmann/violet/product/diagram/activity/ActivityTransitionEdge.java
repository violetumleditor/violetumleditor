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

package com.horstmann.violet.product.diagram.activity;

import java.awt.geom.Point2D;

import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.edge.SegmentedLineEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.property.ArrowHead;

/**
 * An edge that is shaped like a line with up to three segments with an arrowhead
 */
public class ActivityTransitionEdge extends SegmentedLineEdge
{


    @Override
    public ArrowHead getEndArrowHead()
    {
        return ArrowHead.V;
    }
    
    @Override
    public Direction getDirection(INode node)
    {
        if (SynchronizationBarNode.class.isInstance(getStart()) || SynchronizationBarNode.class.isInstance(getEnd())) {
        	if (node.equals(getStart())) {
        		Point2D p1 = node.getLocationOnGraph();
        		Point2D p2 = getEnd().getLocationOnGraph();
        		if (p1.getY() < p2.getY()) {
        			return Direction.NORTH;
        		}
       		    if (p1.getY() > p2.getY()) {
        			return Direction.SOUTH;
        		}
        	}
        	if (node.equals(getEnd())) {
        		Point2D p1 = node.getLocationOnGraph();
        		Point2D p2 = getStart().getLocationOnGraph();
        		if (p1.getY() < p2.getY()) {
        			return Direction.NORTH;
        		}
       		    if (p1.getY() > p2.getY()) {
        			return Direction.SOUTH;
        		}
        	}
        }
        return super.getDirection(node);
    }
    


}
