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

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.edge.SegmentedLineEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.property.ArrowHead;
import com.horstmann.violet.product.diagram.abstracts.property.BentStyle;

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
    
    
    @Override
    public Line2D getConnectionPoints()
    {
    
    	INode start = getStart();
    	INode end = getEnd();
		if (SynchronizationBarNode.class.isInstance(start) || SynchronizationBarNode.class.isInstance(end)) {
			Point2D startLocationOnGraph = start.getLocationOnGraph();
			Point2D endLocationOnGraph = end.getLocationOnGraph();
			
			Point2D relativeStarting = start.getConnectionPoint(this);
			Point2D relativeEnding = end.getConnectionPoint(this);
			
			Point2D p1 = new Point2D.Double(
					startLocationOnGraph.getX() - start.getLocation().getX() + relativeStarting.getX(),
					startLocationOnGraph.getY() - relativeStarting.getY() + start.getBounds().getHeight() + start.getLocation().getY()
					);

			Point2D p2 = new Point2D.Double(
					endLocationOnGraph.getX() - end.getLocation().getX() + relativeEnding.getX(),
					endLocationOnGraph.getY() - relativeEnding.getY() + end.getBounds().getHeight() + end.getLocation().getY()
					);
			
			return new Line2D.Double(
					p1,
					p2
					);
    		
    		
    	}
    	return super.getConnectionPoints();
    	
       }


}
