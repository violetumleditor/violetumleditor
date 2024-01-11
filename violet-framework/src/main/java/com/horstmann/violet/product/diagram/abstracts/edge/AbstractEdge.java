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

package com.horstmann.violet.product.diagram.abstracts.edge;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.IGridSticker;
import com.horstmann.violet.product.diagram.abstracts.ISelectable;
import com.horstmann.violet.product.diagram.abstracts.Id;
import com.horstmann.violet.product.diagram.abstracts.node.INode;

/**
 * A class that supplies convenience implementations for a number of methods in the Edge interface
 */
public abstract class AbstractEdge implements IEdge
{
    public AbstractEdge()
    {
        // Nothing to do
    }

    @Override
    public void setStart(INode startingNode)
    {
        this.start = startingNode;
    }

    @Override
    public INode getStart()
    {
        return start;
    }

    @Override
    public void setEnd(INode endingNode)
    {
        this.end = endingNode;
    }

    @Override
    public INode getEnd()
    {
        return end;
    }

    @Override
    public void setStartLocation(Point2D startLocation)
    {
        this.startLocation = startLocation;
    }

    @Override
    public Point2D getStartLocation()
    {
        return startLocation;
    }

    @Override
    public final Point2D getStartLocationOnGraph()
    {
        if(null == start || null == startLocation)
        {
            return null;
        }

        Point2D nodeLocationOnGraph = start.getLocationOnGraph();
        return new Point2D.Double(
                nodeLocationOnGraph.getX() + startLocation.getX(),
                nodeLocationOnGraph.getY() + startLocation.getY()
        );
    }

    @Override
    public final void setEndLocation(Point2D endLocation)
    {
        this.endLocation = endLocation;
    }

    @Override
    public Point2D getEndLocation()
    {
        return this.endLocation;
    }

    @Override
    public final Point2D getEndLocationOnGraph()
    {
        if(null == end || null == endLocation)
        {
            return null;
        }

        Point2D nodeLocationOnGraph = end.getLocationOnGraph();
        return new Point2D.Double(
                nodeLocationOnGraph.getX() + endLocation.getX(),
                nodeLocationOnGraph.getY() + endLocation.getY()
        );
    }
    
    @Override
    public void setTransitionPoints(ITransitionPoint[] transitionPoints)
    {
        if(null == transitionPoints)
        {
            transitionPoints = new ITransitionPoint[] {};
        }
        this.transitionPoints = transitionPoints;
    }
    
    public final ITransitionPoint[] getTransitionPoints()
    {
        if (this.transitionPoints == null) {
        	this.transitionPoints = new ITransitionPoint[] {};
        }
    	return this.transitionPoints;
    }
    
    @Override
    public boolean isTransitionPointsSupported()
    {
        return false;
    }
    
    @Override
    public void clearTransitionPoints() {
    	this.transitionPoints = new ITransitionPoint[] {};
    }

    @Override
    public Rectangle2D getBounds()
    {
        Line2D conn = getConnectionPoints();
        Rectangle2D r = new Rectangle2D.Double();
        r.setFrameFromDiagonal(conn.getX1(), conn.getY1(), conn.getX2(), conn.getY2());
        return r;
    }
    
    @Override
    public Direction getDirection(INode node) {
        Rectangle2D startBounds = start.getBounds();
        Rectangle2D endBounds = end.getBounds();
        Point2D startLocationOnGraph = start.getLocationOnGraph();
        Point2D endLocationOnGraph = end.getLocationOnGraph();
        Point2D startCenter = new Point2D.Double(startLocationOnGraph.getX() + startBounds.getWidth() / 2, startLocationOnGraph.getY() + startBounds.getHeight() / 2);
        Point2D endCenter = new Point2D.Double(endLocationOnGraph.getX() + endBounds.getWidth() / 2, endLocationOnGraph.getY() + endBounds.getHeight() / 2);
        if (node.equals(start)) {
            if (isTransitionPointsSupported() && this.transitionPoints != null && this.transitionPoints.length > 0) {
                Point2D firstTransitionPoint = this.transitionPoints[0].toPoint2D();
                Direction fromStart = new Direction(firstTransitionPoint, startCenter);
                return fromStart;
            }
            Direction fromStart = new Direction(endCenter, startCenter);
            return fromStart;
        }
        if (node.equals(end)) {
            if (isTransitionPointsSupported() && this.transitionPoints != null && this.transitionPoints.length > 0) {
                Point2D lastTransitionPoint = this.transitionPoints[this.transitionPoints.length - 1].toPoint2D();
                Direction toEnd = new Direction(lastTransitionPoint, endCenter);
                return toEnd;
            }
            Direction toEnd = new Direction(startCenter, endCenter);
            return toEnd;
        }
        return null;
    }

    @Override
    public Line2D getConnectionPoints()
    {
    	Point2D startLocationOnGraph = start.getLocationOnGraph();
        Point2D endLocationOnGraph = end.getLocationOnGraph();

        Point2D relativeStarting = start.getConnectionPoint(this);
        Point2D relativeEnding = end.getConnectionPoint(this);
        
        Point2D p1 = new Point2D.Double(
		        startLocationOnGraph.getX() - relativeStarting.getX() + start.getBounds().getWidth() + start.getLocation().getX(),
		        startLocationOnGraph.getY() - relativeStarting.getY() + start.getBounds().getHeight() + start.getLocation().getY()
		);
		Point2D p2 = new Point2D.Double(
		        endLocationOnGraph.getX() - relativeEnding.getX() + end.getBounds().getWidth() + end.getLocation().getX(),
		        endLocationOnGraph.getY() - relativeEnding.getY() + end.getBounds().getHeight() + end.getLocation().getY()
		);

        return new Line2D.Double(
                p1,
                p2
        );
       }

    @Override
    public Id getId()
    {
        if (this.id == null) {
        	this.id = new Id();
        }
    	return this.id;
    }

    @Override
    public void setId(Id id)
    {
        this.id = id;
    }


    @Override
    public AbstractEdge clone()
    {
        try
        {
            AbstractEdge cloned = (AbstractEdge) super.clone();
            cloned.id = new Id();
            return cloned;
        }
        catch (CloneNotSupportedException ex)
        {
            return null;
        }
    }

    @Override
    public Integer getRevision()
    {
        if (this.revision == null) {
        	this.revision =  0;
        }
    	return this.revision;
    }

    @Override
    public void setRevision(Integer newRevisionNumber)
    {
        this.revision = newRevisionNumber;
    }

    public void incrementRevision()
    {
        int i = getRevision().intValue();
        i++;
        this.revision = i;
    }

    /**
     * Sets edge tool tip
     * 
     * @param s
     */
    public void setToolTip(String s)
    {
        this.toolTip = s;
    }

    @Override
    public String getToolTip()
    {
        if (this.toolTip == null) {
        	this.toolTip = "";
        }
    	return this.toolTip;
    }
    
    @Override
    public List<Point2D> getSelectionPoints() {
    	Line2D line = getConnectionPoints();
    	if (line == null) {
    		return new ArrayList<>();
    	}
    	Point2D p1 = new Point2D.Double(line.getX1(), line.getY1());
    	Point2D p2 = new Point2D.Double(line.getX2(), line.getY2());
    	return Arrays.asList(p1, p2);
    }
    
	@Override
	public ISelectable getSelectableParent() {
		return null;
	}
	
	@Override
	public List<ISelectable> getSelectableChildren() {
		ITransitionPoint[] tpArray = getTransitionPoints();
		return Arrays.asList(tpArray);
	}
    

    /** The node where the edge starts */
    private INode start;

    /** The node where the edge ends */
    private INode end;

    /** The point inside the starting node where this edge begins */
    private Point2D startLocation;

    /** The point inside the ending node where this edge ends */
    private Point2D endLocation;
    
    /** Points for free path */
    private ITransitionPoint[] transitionPoints;

    /** Edge's current id (unique in all the graph) */
    private Id id;

    /** Edge's current revision */
    private Integer revision;

    /** Edge tool tip */
    private transient String toolTip;
}
