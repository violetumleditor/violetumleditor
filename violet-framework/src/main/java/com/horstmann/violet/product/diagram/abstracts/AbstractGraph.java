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

package com.horstmann.violet.product.diagram.abstracts;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.RectangularNode;
import com.horstmann.violet.product.diagram.common.NoteNode;

/**
 * A graph consisting of selectable nodes and edges.
 */
public abstract class AbstractGraph implements Serializable, Cloneable, IGraph
{
    /**
     * Constructs a graph with no nodes or edges.
     */
    public AbstractGraph()
    {
        nodes = new ArrayList<INode>();
        edges = new ArrayList<IEdge>();
    }

    @Override
    public INode findNode(Point2D p)
    {
        for (INode n : getAllNodes())
        {
            Point2D locationOnGraph = n.getLocationOnGraph();
            Rectangle2D bounds = n.getBounds();
            Rectangle2D boundsToCheck = new Rectangle2D.Double(locationOnGraph.getX(), locationOnGraph.getY(), bounds.getWidth(),
                    bounds.getHeight());
            if (boundsToCheck.contains(p))
            {
                return n;
            }
        }
        return null;
    }

    @Override
    public INode findNode(Id id)
    {
        for (INode n : getAllNodes())
        {
            if (n.getId().equals(id)) return n;
        }
        return null;
    }

    @Override
    public IEdge findEdge(Point2D p)
    {
        for (IEdge e : edges)
        {
            if (e.contains(p)) return e;
        }
        return null;
    }

    @Override
    public IEdge findEdge(Id id)
    {
        for (IEdge e : edges)
        {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }
    
    


    @Override
    public void draw(Graphics2D g2)
    {
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        List<INode> specialNodes = new ArrayList<INode>();

        int count = 0;
        int z = 0;
        Collection<INode> nodes = getAllNodes();
        while (count < nodes.size())
        {
            for (INode n : nodes)
            {

                if (n.getZ() == z)
                {
                    if (n instanceof NoteNode)
                    {
                        specialNodes.add(n);
                    }
                    else
                    {
                        n.draw(g2);
                    }
                    count++;
                }
            }
            z++;
        }

        for (int i = 0; i < edges.size(); i++)
        {
            IEdge e = (IEdge) edges.get(i);
            e.draw(g2);
        }
        // Special nodes are always drawn upon other elements
        for (INode n : specialNodes)
        {
            // Translate g2 if node has parent
            Point2D nodeLocationOnGraph = n.getLocationOnGraph();
            Point2D nodeLocation = n.getLocation();
            Point2D g2Location = new Point2D.Double(nodeLocationOnGraph.getX() - nodeLocation.getX(), nodeLocationOnGraph.getY()
                    - nodeLocation.getY());
            g2.translate(g2Location.getX(), g2Location.getY());
            n.draw(g2);
            // Restore g2 original location
            g2.translate(-g2Location.getX(), -g2Location.getY());
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.product.diagram.abstracts.IGraph#getBounds()
     */
    public Rectangle2D getClipBounds()
    {
        Rectangle2D r = minBounds;
        for (INode n : nodes)
        {
            Rectangle2D b = n.getBounds();
            if (r == null) r = b;
            else r.add(b);
        }
        for (IEdge e : edges)
        {
            r.add(e.getBounds());
        }
        return r == null ? new Rectangle2D.Double() : new Rectangle2D.Double(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    @Override
    public void setBounds(Rectangle2D newValue)
    {
        minBounds = newValue;
    }

    @Override
    public abstract List<INode> getNodePrototypes();

    @Override
    public abstract List<IEdge> getEdgePrototypes();

    @Override
    public Collection<INode> getAllNodes()
    {
        List<INode> fifo = new ArrayList<INode>();
        List<INode> allNodes = new ArrayList<INode>();
        fifo.addAll(nodes);
        allNodes.addAll(nodes);
        while (!fifo.isEmpty())
        {
            INode nodeToInspect = fifo.remove(0);
            List<INode> children = nodeToInspect.getChildren();
            fifo.addAll(children);
            allNodes.addAll(children);
        }
        // Let's have children first
        Collections.reverse(allNodes);
        return Collections.unmodifiableCollection(allNodes);
    }

    @Override
    public Collection<IEdge> getAllEdges()
    {
        return Collections.unmodifiableCollection(edges);
    }

    @Override
    public boolean addNode(INode newNode, Point2D p)
    {
        newNode.setId(new Id());
        newNode.setGraph(this);
        // Case 1 : Note node always attached to the graph
        if (newNode instanceof NoteNode)
        {
            newNode.setLocation(p);
            nodes.add(newNode);
            return true;
        }
        // Case 2 : attached to an existing node
        INode potentialParentNode = findNode(p);
        if (potentialParentNode != null)
        {
            Point2D parentLocationOnGraph = potentialParentNode.getLocationOnGraph();
            Point2D relativeLocation = new Point2D.Double(p.getX() - parentLocationOnGraph.getX(), p.getY()
                    - parentLocationOnGraph.getY());
            return potentialParentNode.addChild(newNode, relativeLocation);
        }
        // Case 3 : attached directly to the graph
        newNode.setLocation(p);
        newNode.setParent(null);
        nodes.add(newNode);
        return true;
    }

    @Override
    public void removeNode(INode... nodesToRemove)
    {
        // Step 1a : Remove nodes directly attach to the graph
        for (INode aNodeToRemove : nodesToRemove)
        {
            if (this.nodes.contains(aNodeToRemove))
            {
                this.nodes.remove(aNodeToRemove);
            }
        }
        // Step 1b : Remove nodes attach to other nodes as children
        for (INode aNode : getAllNodes())
        {
            for (INode aNodeToRemove : nodesToRemove)
            {
                List<INode> children = aNode.getChildren();
                if (children.contains(aNodeToRemove))
                {
                    aNode.removeChild(aNodeToRemove);
                }
            }
        }
        // Step 2 : Disconnect edges
        List<IEdge> edgesToRemove = new ArrayList<IEdge>();
        Collection<INode> allNodes = getAllNodes();
        for (IEdge anEdge : this.edges)
        {
            INode startingNode = anEdge.getStart();
            INode endingNode = anEdge.getEnd();
            boolean isEdgeStillConnected = (allNodes.contains(startingNode) && allNodes.contains(endingNode));
            if (!isEdgeStillConnected)
            {
                edgesToRemove.add(anEdge);
            }
        }
        IEdge[] edgesToRemoveAsArray = edgesToRemove.toArray(new IEdge[edgesToRemove.size()]);
        removeEdge(edgesToRemoveAsArray);
    }

    @Override
    public boolean connect(IEdge e, INode start, Point2D startLocation, INode end, Point2D endLocation, Point2D[] transitionPoints)
    {
        // Step 1 : find if nodes exist
        Collection<INode> allNodes = getAllNodes();
        if (start != null && !allNodes.contains(start))
        {
            addNode(start, start.getLocation());
        }
        if (end != null && !allNodes.contains(end))
        {
            addNode(end, end.getLocation());
        }
        e.setStart(start);
        e.setStartLocation(startLocation);
        e.setEnd(end);
        e.setEndlocation(endLocation);
        e.setTransitionPoints(transitionPoints);
        if (start.addConnection(e))
        {
            e.setId(new Id());
            edges.add(e);
            return true;
        }
        return false;
    }

    @Override
    public void removeEdge(IEdge... edgesToRemove)
    {
        for (IEdge anEdgeToRemove : edgesToRemove)
        {
            INode startingNode = anEdgeToRemove.getStart();
            INode endingNode = anEdgeToRemove.getEnd();
            startingNode.removeConnection(anEdgeToRemove);
            endingNode.removeConnection(anEdgeToRemove);
            this.edges.remove(anEdgeToRemove);
        }
    }

    @Override
    public IGridSticker getGridSticker()
    {
        if (this.gridSticker == null)
        {
            return new IGridSticker()
            {
                @Override
                public Rectangle2D snap(Rectangle2D r)
                {
                    return r;
                }

                @Override
                public Point2D snap(Point2D p)
                {
                    return p;
                }
            };
        }
        return this.gridSticker;
    }

    @Override
    public void setGridSticker(IGridSticker positionCorrector)
    {
        this.gridSticker = positionCorrector;
    }

    private ArrayList<INode> nodes;
    private ArrayList<IEdge> edges;
    private transient Rectangle2D minBounds;
    private transient IGridSticker gridSticker;
}
