package com.horstmann.violet.product.diagram.abstracts;

import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.List;

public interface IGraph
{

    /**
     * Gets the node_old types of a particular graph type.
     * 
     * @return a list of node_old prototypes
     */
    public abstract List<INode> getNodePrototypes();


    /**
     * Gets the edge types of a particular graph type.
     * 
     * @return a list of edge prototypes
     */
    public abstract List<IEdge> getEdgePrototypes();


    void deserializeSupport();

    /**
     * Gets ALL the node of this graph.
     * 
     * @return an unmodifiable collection of the node
     */
    public abstract Collection<INode> getAllNodes();


    /**
     * Gets ALL the edges of this graph.
     * 
     * @return an unmodifiable collection of the edges
     */
    public abstract Collection<IEdge> getAllEdges();


    /**
     * Removes one or more edges from this graph.
     * 
     * @param edgesToRemove
     */
    public abstract void removeEdge(IEdge... edgesToRemove);
    
    /**
     * Removes one or more node from this graph.
     * 
     * @param nodesToRemove
     */
    public abstract void removeNode(INode... nodesToRemove);    
    
    /**
     * Adds a node_old to the graph so that the top left corner of the bounding rectangle is at the given point.
     * This method is called by a decoder when reading a data file.
     * 
     * @param n the node_old to add
     * @param p the desired location
     */
    public abstract boolean addNode(INode n, Point2D p);


    /**
     * Adds an edge to this graph. 
     * 
     * @param e the new edge to add (don't forget to populate it withs the following node and points!)
     * @param start the start node_old of the edge
     * @param startLocation the point inside the start node_old where the edge begins
     * @param end the end node_old of the edge
     * @param endLocation the point inside the end node_old where the edge ends
     * @param list a points for edge that supports free path 
     * @return isOK as true if successfully connected
     */
    public abstract boolean connect(IEdge e, INode start, Point2D startLocation, INode end, Point2D endLocation, Point2D[] transitionPoints);


    /**
     * Finds a node_old by its id. This internal method should only be used by network features (for the moment because
     * node_old ids are still generated automatically)
     * 
     * @param id 
     * @return the found node_old or null if no one found
     */
    public abstract INode findNode(Id id);


    /**
     * Finds a node_old containing the given point.
     * 
     * @param p a point
     * @return a node_old containing p or null if no node contain p
     */
    public abstract INode findNode(Point2D p);

    /**
     * Finds an adge by its id. This internal method should only be used by network features (for the moment because
     * edge ids are still generated automatically)
     * 
     * @param id
     * @return the found edge or null if no one found
     */
    public abstract IEdge findEdge(Id id);


    /**
     * Finds an edge containing the given point.
     * 
     * @param p a point
     * @return an edge containing p or null if no edges contain p
     */
    public abstract IEdge findEdge(Point2D p);

    /**
     * Draws the graph
     * 
     * @param g2 the graphics context
     */
    public abstract void draw(Graphics2D g2);


    /**
     * Gets the smallest rectangle enclosing the graph
     * 
     * @return the bounding rectangle
     */
    public abstract Rectangle2D getClipBounds();
    
    
    /**
     * Sets desired bound
     * 
     * @param newValue
     */
    public abstract void setBounds(Rectangle2D newValue);
    
    
    /**
     * Returns the corrector used to stick points on the grid
     */
    public IGridSticker getGridSticker();

    /**
     * Sets the corrector used to stick points on the grid
     * 
     * @param newGrid
     */
    public void setGridSticker(IGridSticker newCorrector);
    
    
}