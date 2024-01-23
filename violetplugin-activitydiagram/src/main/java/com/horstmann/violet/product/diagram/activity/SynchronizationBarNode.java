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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.RectangularNode;

/**
 * A synchronization bar node in an activity diagram.
 */
public class SynchronizationBarNode extends RectangularNode
{

    @Override
    public boolean addConnection(IEdge e)
    {
        return e.getEnd() != null && this != e.getEnd();
    }

    @Override
    public Point2D getConnectionPoint(IEdge e)
    {
    	Point2D defaultConnectionPoint = super.getConnectionPoint(e);
        if (!ActivityTransitionEdge.class.isInstance(e))
        {
            return defaultConnectionPoint;
        }

        INode end = e.getEnd();
        INode start = e.getStart();
        if (this == start)
        {
            Point2D endConnectionPoint = end.getConnectionPoint(e);
            double y = defaultConnectionPoint.getY();
            double x = endConnectionPoint.getX();
            return new Point2D.Double(x, y);
        }
        if (this == end)
        {
        	Point2D startConnectionPoint = start.getConnectionPoint(e);
            double y = defaultConnectionPoint.getY();
            double x = startConnectionPoint.getX();
            return new Point2D.Double(x, y);
        }

        return defaultConnectionPoint;
    }

    @Override
    public Rectangle2D getBounds()
    {
        double w = DEFAULT_WIDTH;
        Point2D location = getLocation();
        double x = location.getX();
        double y = location.getY();
        List<INode> connectedNodes = getConnectedNodes();
        if (connectedNodes.size() > 0)
        {
        	double minX = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE;
            for (INode n : connectedNodes)
            {
                Rectangle2D b2 = n.getBounds();
                minX = Math.min(minX, b2.getX());
                maxX = Math.max(maxX, b2.getX() + b2.getWidth());
            }
            w = maxX - minX;
        }
        return new Rectangle2D.Double(x, y, EXTRA_WIDTH + w + EXTRA_WIDTH, DEFAULT_HEIGHT);
    }
    

    @Override
    public Point2D getLocation() {
    	Point2D defaultLocation = super.getLocation();
    	double x = defaultLocation.getX();
    	double y = defaultLocation.getY();
    	List<INode> connectedNodes = getConnectedNodes();
        if (connectedNodes.size() > 0)
        {
            double minX = Double.MAX_VALUE;
            for (INode n : connectedNodes)
            {
                Point2D p = n.getLocation();
                minX = Math.min(minX, p.getX());
            }

            x = minX - EXTRA_WIDTH;
        }
    	return new Point2D.Double(x, y);
    }
    

    /**
     * 
     * @return nodes which are connected (with edges) to this node
     */
    private List<INode> getConnectedNodes()
    {
    	List<INode> connectedNodes = new ArrayList<INode>();
        // needs to contain all incoming and outgoing edges
        for (IEdge e : getConnectedEdges())
        {
            if (e.getStart() == this) connectedNodes.add(e.getEnd());
            if (e.getEnd() == this) connectedNodes.add(e.getStart());
        }
        return connectedNodes;
    }

    @Override
    public void draw(Graphics2D g2)
    {
        super.draw(g2);

        // Backup current color;
        Color oldColor = g2.getColor();

        // Perform drawing
        g2.setColor(getBorderColor());
        g2.fill(getShape());

        // Restore first color
        g2.setColor(oldColor);
    }

    /**
     * @see java.lang.Object#clone()
     */
    @Override
    public SynchronizationBarNode clone()
    {
        return (SynchronizationBarNode) super.clone();
    }

    private static int DEFAULT_WIDTH = 100;
    private static int DEFAULT_HEIGHT = 4;
    private static int EXTRA_WIDTH = 12;
}
