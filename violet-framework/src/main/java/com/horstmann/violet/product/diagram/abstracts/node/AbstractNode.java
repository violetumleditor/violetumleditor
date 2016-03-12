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

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.horstmann.violet.framework.graphics.content.Content;
import com.horstmann.violet.product.diagram.abstracts.AbstractGraph;
import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.Id;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;

/**
 * A class that supplies convenience implementations for a number of methods in the Node interface
 * 
 * @author Cay Horstmann
 */
public abstract class AbstractNode implements INode
{
    private static class NodeGraph extends AbstractGraph
    {
        @Override
        public List<INode> getNodePrototypes() {
            return new ArrayList<INode>();
        }

        @Override
        public List<IEdge> getEdgePrototypes() {
            return new ArrayList<IEdge>();
        }
    }

    /**
     * Constructs a node_old with no parents or children at location (0, 0).
     */
    public AbstractNode() {
        this.id = new Id();
        this.revision = new Integer(0);
        this.location = new Point2D.Double(0, 0);
        this.children = new ArrayList<INode>();
    }

    /**
     * copy Constructs
     */
    protected AbstractNode(AbstractNode node) throws CloneNotSupportedException
    {
        if (null == node)
        {
            throw new CloneNotSupportedException("node can't be null");
        }
        this.id = node.getId().clone();
        this.revision = new Integer(0);
        this.children = new ArrayList<INode>();
        this.location = (Point2D.Double) node.getLocation().clone();
        for (INode child : node.getChildren()) {
            INode clonedChild = child.clone();
            clonedChild.setParent(this);
            this.children.add(clonedChild);
        }
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
        getContent().refresh();
    }

    @Override
//    public AbstractNode clone(){
    public final AbstractNode clone()
    {
        try {
            return (AbstractNode) copy();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    protected INode copy() throws CloneNotSupportedException
    {
        throw new CloneNotSupportedException("You can't clone abstract class");
    }

    @Override
    public String getToolTip()
    {
        return "";
    }

    protected abstract void createContentStructure();

    @Override
    public void draw(Graphics2D g2)
    {
        getContent().draw(g2, getLocationOnGraph());
    }

    @Override
    public Rectangle2D getBounds()
    {
        Point2D location = getLocation();
        Rectangle2D contentBounds = getContent().getBounds();
        return new Rectangle2D.Double(location.getX(), location.getY(), contentBounds.getWidth(), contentBounds.getHeight());
    }

    public boolean contains(Point2D p) {
        return getContent().contains(p);
    }

    /**
     * @return currently connected edges
     */
    protected List<IEdge> getConnectedEdges()
    {
        List<IEdge> connectedEdges = new ArrayList<IEdge>();
        IGraph currentGraph = getGraph();
        for (IEdge anEdge : currentGraph.getAllEdges()) {
            INode start = anEdge.getStartNode();
            INode end = anEdge.getEndNode();
            if (this.equals(start) || this.equals(end)) {
                connectedEdges.add(anEdge);
            }
        }
        return connectedEdges;
    }

    @Override
    public Point2D getLocation() {
        return this.location;
    }



    @Override
    public Point2D getLocationOnGraph()
    {
        INode parentNode = getParent();
        if (parentNode == null) {
            return getLocation();
        }
        Point2D parentLocationOnGraph = parentNode.getLocationOnGraph();
        Point2D relativeLocation = getLocation();
        Point2D result = new Point2D.Double(parentLocationOnGraph.getX() + relativeLocation.getX(), parentLocationOnGraph.getY()
                + relativeLocation.getY());
        return result;
    }

    @Override
    public void setLocation(Point2D point)
    {
        if (null == point) {
            throw new NullPointerException("Location can't be null");
        }
        this.location = point;

        if (null != parent) {
            if (parent instanceof AbstractNode) {
                ((AbstractNode) parent).onChildChangeLocation(this);
            }
        }
    }

    protected void onChildChangeLocation(INode child) {
    }

    @Override
    public Id getId() {
        return this.id;
    }

    @Override
    public void setId(Id id) {
        if (null == id) {
            throw new NullPointerException("Id can't be null");
        }
        this.id = id;
    }

    /**
     * List edges connected to the same side
     *
     * @param edge
     * @return ordered list of edges
     */
    private List<IEdge> getEdgesOnSameSide(IEdge edge)
    {
        // Step 1 : look for edges
        List<IEdge> result = new ArrayList<IEdge>();
        Direction d = edge.getDirection(this);

        if (d == null) return result;
        Direction cardinalDirectionToSearch = d.getNearestCardinalDirection();
        for (IEdge anEdge : getConnectedEdges()) {
            Direction edgeDirection = anEdge.getDirection(this);
            Direction nearestCardinalDirection = edgeDirection.getNearestCardinalDirection();
            if (cardinalDirectionToSearch.equals(nearestCardinalDirection)) {
                result.add(anEdge);
            }
            if (anEdge.getStartNode().equals(anEdge.getEndNode()) && anEdge.getStartNode().equals(this)) {
                // self loop
                result.add(anEdge);
            }
        }
        // Step 2: sort them
        if (Direction.NORTH.equals(cardinalDirectionToSearch) || Direction.SOUTH.equals(cardinalDirectionToSearch)) {
            Collections.sort(result, new Comparator<IEdge>() {
                @Override
                public int compare(IEdge e1, IEdge e2) {
                    Direction d1 = e1.getDirection(AbstractNode.this);
                    Direction d2 = e2.getDirection(AbstractNode.this);
                    double x1 = d1.getX();
                    double x2 = d2.getX();
                    return Double.compare(x1, x2);
                }
            });
        }
        if (Direction.EAST.equals(cardinalDirectionToSearch) || Direction.WEST.equals(cardinalDirectionToSearch)) {
            Collections.sort(result, new Comparator<IEdge>() {
                @Override
                public int compare(IEdge e1, IEdge e2) {
                    Direction d1 = e1.getDirection(AbstractNode.this);
                    Direction d2 = e2.getDirection(AbstractNode.this);
                    double y1 = d1.getY();
                    double y2 = d2.getY();
                    return Double.compare(y1, y2);
                }
            });
        }
        return result;
    }


    public Point2D getConnectionPoint(IEdge edge)
    {
        List<IEdge> edgesOnSameSide = getEdgesOnSameSide(edge);
        int position = edgesOnSameSide.indexOf(edge);
        int size = edgesOnSameSide.size();

        Direction edgeDirection = edge.getDirection(this);
        Point2D startingNodeLocation = getLocation();

        double x = startingNodeLocation.getX();
        double y = startingNodeLocation.getY();

        Direction nearestCardinalDirection = edgeDirection.getNearestCardinalDirection();
        if (Direction.NORTH.equals(nearestCardinalDirection))
        {
            x += getContent().getWidth() - (getContent().getWidth() / (size + 1)) * (position + 1);
            y += getContent().getHeight();
        }
        else if (Direction.SOUTH.equals(nearestCardinalDirection))
        {
            x += getContent().getWidth() - (getContent().getWidth() / (size + 1)) * (position + 1);
        }
        else if (Direction.EAST.equals(nearestCardinalDirection))
        {
            y += getContent().getHeight() - (getContent().getHeight() / (size + 1)) * (position + 1);
        }
        else if (Direction.WEST.equals(nearestCardinalDirection))
        {
            x += getContent().getWidth();
            y += getContent().getHeight() - (getContent().getHeight() / (size + 1)) * (position + 1);
        }
        return new Point2D.Double(x, y);
    }

    @Override
    public Integer getRevision() {
        return this.revision;
    }

    @Override
    public void setRevision(Integer newRevisionNumber) {
        if (null == newRevisionNumber) {
            throw new NullPointerException("Integer can't be null");
        }
        this.revision = newRevisionNumber;
    }

    @Override
    public void incrementRevision() {
        int i = getRevision().intValue() + 1;
        this.revision = new Integer(i);
    }

    @Override
    public void translate(double dx, double dy) {
        Point2D newLocation = new Point2D.Double(getLocation().getX() + dx, getLocation().getY() + dy);
        setLocation(newLocation);
    }

    @Override
    public boolean addConnection(IEdge e)
    {
        return e.getEndNode() != null;
    }

    @Override
    public void removeConnection(IEdge e) {
    }

    @Override
    public void removeChild(INode node)
    {
        if (node.getParent() != this) return;
        getChildren().remove(node);
    }

    @Override
    public boolean addChild(INode n, Point2D p)
    {
        return false;
    }

    @Override
    public INode getParent() {
        return parent;
    }

    @Override
    public List<INode> getParents()
    {
        List<INode> parents = new ArrayList<INode>();
        if(null != getParent())
        {
            parents.add(getParent());
            while (null != parents.get(0).getParent())
            {
                parents.add(0, parents.get(0).getParent());
            }
        }
        return parents;
    }

    @Override
    public void setParent(INode node)
    {
        parent = node;
    }

    @Override
    public List<INode> getChildren()
    {
    	return children;
    }

    @Override
    public boolean addChild(INode node, int index)
    {
        INode oldParent = node.getParent();
        if (oldParent != null) oldParent.removeChild(node);
        getChildren().add(index, node);
        node.setParent(this);
        node.setGraph(getGraph());
        return true;
    }

    public void onConnectedEdge(IEdge connectedEdge)
    {}

    /**
     * @return the shape to be used for computing the drop shadow
     */
    public Shape getShape()
    {
        return new Rectangle2D.Double(0, 0, 0, 0);
    }

    @Override
    public int getZ()
    {
        return z;
    }

    @Override
    public void setZ(int z)
    {
        this.z = z;
    }

    @Override
    public void setGraph(IGraph graph)
    {
        if(null == graph)
        {
            throw new NullPointerException("Graph can't be null");
        }
        this.graph = graph;
        for (INode aChild : getChildren()) {
        	aChild.setGraph(graph);
        }
    }

    @Override
    public IGraph getGraph()
    {
        if(null == graph)
        {
            return new NodeGraph();
        }
    	return this.graph;
    }

    public final Content getContent()
    {
        if(null == content)
        {
            reconstruction();
        }
        return content;
    }

    protected final void setContent(Content content)
    {
        this.content = content;
    }

    private transient Content content;

    private transient IGraph graph;
    private transient int z;

    /** Node's current id (unique in all the graph) */
    private Id id;

    /** Node's current revision */
    private Integer revision;

    private List<INode> children;
    private INode parent;

    private Point2D location;
}
