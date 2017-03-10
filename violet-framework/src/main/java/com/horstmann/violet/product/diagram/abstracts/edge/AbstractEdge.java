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

import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.IGridSticker;
import com.horstmann.violet.product.diagram.abstracts.Id;
import com.horstmann.violet.product.diagram.abstracts.node.INode;

/**
 * A class that supplies convenience implementations for a number of methods in the Edge interface
 */
public abstract class AbstractEdge implements IEdge
{
    public AbstractEdge()
    {
        this.id = new Id();
        this.revision = 0;
        this.startNode = null;
        this.startLocation = null;
        this.endNode = null;
        this.endLocation = null;
        this.transitionPoints = new Point2D[] {};
    }

    protected AbstractEdge(AbstractEdge cloned) throws CloneNotSupportedException
    {
        refreshContactPoints();
    }

    @Override
    public final void reconstruction()
    {
        beforeReconstruction();
        createContentStructure();
        afterReconstruction();
    }

    protected void beforeReconstruction()
    {
    }

    protected void afterReconstruction()
    {
        refreshContactPoints();
    }

    protected abstract void createContentStructure();

    
    @Override
    public final AbstractEdge clone()
    {
        try {
            return (AbstractEdge) copy();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    protected IEdge copy() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException("You can't clone abstract class");
    }

    @Override
    public String getToolTip()
    {
        return "";
    }

    @Override
    public final void setStartNode(INode startingNode)
    {
        this.startNode = startingNode;
        refreshContactPoints();
    }

    @Override
    public final INode getStartNode()
    {
        return startNode;
    }

    @Override
    public final void setEndNode(INode endingNode)
    {
        this.endNode = endingNode;
        refreshContactPoints();
    }

    @Override
    public final INode getEndNode()
    {
        return endNode;
    }

    @Override
    public final void setStartLocation(Point2D startLocation)
    {
        this.startLocation = startLocation;
        refreshContactPoints();
    }

    @Override
    public final Point2D getStartLocation()
    {
        return startLocation;
    }

    @Override
    public final Point2D getStartLocationOnGraph()
    {
        if(null == startNode || null == startLocation)
        {
            return null;
        }

        Point2D nodeLocationOnGraph = startNode.getLocationOnGraph();
        return new Point2D.Double(
                nodeLocationOnGraph.getX() + startLocation.getX(),
                nodeLocationOnGraph.getY() + startLocation.getY()
        );
    }

    @Override
    public final void setEndLocation(Point2D endLocation)
    {
        this.endLocation = endLocation;
        refreshContactPoints();
    }

    @Override
    public final Point2D getEndLocation()
    {
        return this.endLocation;
    }

    @Override
    public final Point2D getEndLocationOnGraph()
    {
        if(null == endNode || null == endLocation)
        {
            return null;
        }

        Point2D nodeLocationOnGraph = endNode.getLocationOnGraph();
        return new Point2D.Double(
                nodeLocationOnGraph.getX() + endLocation.getX(),
                nodeLocationOnGraph.getY() + endLocation.getY()
        );
    }
    
    @Override
    public void setTransitionPoints(Point2D[] transitionPoints)
    {
        if(null == transitionPoints)
        {
            transitionPoints = new Point2D[] {};
        }
        this.transitionPoints = transitionPoints;
        refreshContactPoints();
    }
    
    @Override
    public final Point2D[] getTransitionPoints()
    {
        return this.transitionPoints;
    }
    
    @Override
    public boolean isTransitionPointsSupported()
    {
        return false;
    }
    
    @Override
    public void clearTransitionPoints() {
    	this.transitionPoints = new Point2D[] {};
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
    public Direction getDirection(INode node)
    {
        Point2D startLocationOnGraph = startNode.getLocationOnGraph();
        Point2D endLocationOnGraph = endNode.getLocationOnGraph();

        Point2D startCenter = new Point2D.Double(
                startLocationOnGraph.getX() + startNode.getBounds().getWidth() / 2,
                startLocationOnGraph.getY() + startNode.getBounds().getHeight() / 2
        );
        Point2D endCenter = new Point2D.Double(
                endLocationOnGraph.getX() + endNode.getBounds().getWidth() / 2,
                endLocationOnGraph.getY() + endNode.getBounds().getHeight() / 2
        );

        if(null !=this.contactPoints && this.contactPoints.length > 1)
        {
            if (startNode.equals(node))
            {
                return new Direction(startCenter,this.contactPoints[1]);
            }
            else if (endNode.equals(node))
            {
                return new Direction(endCenter,this.contactPoints[this.contactPoints.length - 2]);
            }
        }

        return new Direction(0,0);
    }

    @Override
    public Line2D getConnectionPoints()
    {
        Point2D startLocationOnGraph = startNode.getLocationOnGraph();
        Point2D endLocationOnGraph = endNode.getLocationOnGraph();

        Point2D relativeStarting = startNode.getConnectionPoint(this);
        Point2D relativeEnding = endNode.getConnectionPoint(this);

        //p = getGraph().getGridSticker().snap(p);

        
        Point2D p1 = new Point2D.Double(
		        startLocationOnGraph.getX() - relativeStarting.getX() + startNode.getBounds().getWidth() + startNode.getLocation().getX(),
		        startLocationOnGraph.getY() - relativeStarting.getY() + startNode.getBounds().getHeight() + startNode.getLocation().getY()
		);
        p1 = startNode.getGraph().getGridSticker().snap(p1);
		Point2D p2 = new Point2D.Double(
		        endLocationOnGraph.getX() - relativeEnding.getX() + endNode.getBounds().getWidth() + endNode.getLocation().getX(),
		        endLocationOnGraph.getY() - relativeEnding.getY() + endNode.getBounds().getHeight() + endNode.getLocation().getY()
		);
		p2 = endNode.getGraph().getGridSticker().snap(p2);
		return new Line2D.Double(
                p1,
                p2
        );
    }

    @Override
    public final Id getId()
    {
    	return this.id;
    }

    @Override
    public final void setId(Id id)
    {
        this.id = id;
    }

    @Override
    public final Integer getRevision()
    {
    	return this.revision;
    }

    @Override
    public final void setRevision(Integer newRevisionNumber)
    {
        if(null == newRevisionNumber)
        {
            throw new NullPointerException("newRevisionNumber can't be null");
        }
        if(0 > newRevisionNumber)
        {
            throw new IllegalArgumentException("newRevisionNumber can't be negative number");
        }
        this.revision = newRevisionNumber;
    }

    @Override
    public final void incrementRevision()
    {
        ++this.revision;
    }


    protected void updateContactPoints()
    {
        Line2D connectionPoints = getConnectionPoints();

        contactPoints = new Point2D[]{
                connectionPoints.getP1(),
                connectionPoints.getP2()
        };
    }

    private void refreshContactPoints()
    {
        if(null != startNode && null != endNode && null != startLocation && null != endLocation)
        {
            updateContactPoints();
        }
    }

    /** Points of contact path */
    protected transient Point2D[] contactPoints;

    /** Edge's current id (unique in all the graph) */
    private Id id;

    /** Edge's current revision */
    private Integer revision;

    /** The node_old where the edge starts */
    private INode startNode;

    /** The point inside the starting node_old where this edge begins */
    private Point2D startLocation;

    /** The node_old where the edge ends */
    private INode endNode;

    /** The point inside the ending node_old where this edge ends */
    private Point2D endLocation;
    
    /** Points for free path */
    private Point2D[] transitionPoints;
}
