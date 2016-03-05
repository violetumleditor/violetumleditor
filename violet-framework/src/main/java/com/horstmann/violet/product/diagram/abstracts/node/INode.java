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

package com.horstmann.violet.product.diagram.abstracts.node;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.ConnectedEdgeListener;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.IIdentifiable;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;

/**
 * A node_old in a graph. To be more precise, a node_old is an graphical entity that represents a class, a sequence, a state or all other
 * type of entities that can or not handle edges.
 * 
 * @author Cay Horstmann
 */
public interface INode extends Serializable, Cloneable, IIdentifiable, ConnectedEdgeListener
{

    void reconstruction();
    
    /**
     * Checks whether to add an edge that originates at this node_old.
     * 
     * @param e the edge to add
     * @return true if the edge was added
     */
    boolean addConnection(IEdge e);

    /**
     * Notifies this node_old that an edge is being removed.
     * 
     * @param g the ambient graph
     * @param e the edge to be removed
     */
    void removeConnection(IEdge e);

    /**
     * Adds a node_old as a child node_old to this node_old.
     * 
     * @param n the child node_old
     * @param p the point at which the node_old is being added
     * @return true if this node_old accepts the given node_old as a child
     */
    boolean addChild(INode n, Point2D p);

    /**
     * Adds a child node_old and fires the graph modification event.
     * @param node the child node_old to add
     * @param index the position at which to add the child
     * @return true if this node_old accepts the given node_old as a child
     */
    boolean addChild(INode node, int index);

    /**
     * Notifies this node_old that a node_old is being removed.
     * 
     * @param g the ambient graph
     * @param n the node_old to be removed
     */
    void removeChild(INode n);

    /**
     * Gets the children of this node_old.
     * 
     * @return an unmodifiable list of the children
     */
    List<INode> getChildren();

    /**
     * Gets the parent of this node_old.
     * 
     * @return the parent node_old, or null if the node_old has no parent
     */
    INode getParent();

    /**
     * Gets the three parents of this node_old.
     *
     * @return the three parents node_old, or null if the node_old has no parent
     */
    List<INode> getParents();

    /**
     * Sets node_old's parent (for decoder)
     * 
     * @param parentNode p
     */
    void setParent(INode parentNode);

    /**
     * Sets the graph that contains this node_old.
     * @param g the graph
     */
    void setGraph(IGraph g);

    /**
     * Gets the graph that contains this node_old, or null if this node_old is not contained in any graph.
     * @return
     */
    IGraph getGraph();

    /**
     * Translates the node_old by a given amount
     * 
     * @param dx the amount to translate in the x-direction
     * @param dy the amount to translate in the y-direction
     */
    void translate(double dx, double dy);
    
    /**
     * Tests whether the node_old contains a point.
     * 
     * @param aPoint the point to test
     * @return true if this node_old contains aPoint
     */
    boolean contains(Point2D aPoint);

    /**
     * Get the best connection point to connect this node_old with another node_old. This should be a point on the boundary of the shape of
     * this node_old.
     * 
     * @param d the direction from the center of the bounding rectangle towards the boundary
     * @return the recommended connection point
     */
    Point2D getConnectionPoint(IEdge e);

    /**
     * Set or change node_old location
     * @param aPoint
     */
    void setLocation(Point2D aPoint);

    /**
     * Gets the location of this node_old on its parent (i.e. relative location)
     * @return the location
     */
    Point2D getLocation();

    /**
     * Gets the location of this node_old on the whole graph. (i.e. absolute location)
     * @return
     */
    Point2D getLocationOnGraph();

    /**
     * Get the visual bounding rectangle of the shape of this node_old
     * 
     * @return the bounding rectangle
     */
    Rectangle2D getBounds();

    /**
     * Draw the node_old.
     * 
     * @param g2 the graphics context
     * @param grid the grid to snap to
     */
    void draw(Graphics2D g2);


    /**
        
     * Gets the z-order. Nodes with higher z-order are drawn above those with lower z-order.
     * @return the z-order.
     */
    int getZ();
    
    /**
     * Sets the z-order.
     * @param z the desired z-order.
     */
    void setZ(int z);
    
    
    /**
     * Gets current node_old tool tip
     * @return
     */
    String getToolTip();
    
    /**
     * @return a deep copy of this object
     */
    INode clone();
}
