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

package com.horstmann.violet.product.diagram.sequence;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.List;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.property.string.LineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.LargeSizeDecorator;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.OneLineString;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.UnderlineDecorator;
import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.property.string.SingleLineText;

/**
 * An object node_old in a scenario diagram.
 */
public class LifelineNode extends ColorableNode
{
    /**
     * Construct an object node_old with a default size
     */
    public LifelineNode()
    {
        super();

        name = new SingleLineText(new LineText.Converter(){
            @Override
            public OneLineString toLineString(String text)
            {
            return new UnderlineDecorator(new OneLineString(text));
            }
        });
        createContentStructure();
    }

    protected LifelineNode(LifelineNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        createContentStructure();
    }

    @Override
    public void deserializeSupport()
    {
        super.deserializeSupport();
        name.setConverter(nameConverter);
        name.deserializeSupport();

        for(INode child : getChildren())
        {
            if (child instanceof ActivationBarNode)
            {
                activationsGroup.add(((ActivationBarNode) child).getContent());
            }
        }
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new LifelineNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        TextContent nameContent = new TextContent(name);
        nameContent.setMinHeight(DEFAULT_TOP_HEIGHT);
        nameContent.setMinWidth(DEFAULT_WIDTH);

        ContentInsideShape contentInsideShape = new ContentInsideRectangle(nameContent);

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());

        setTextColor(getTextColor());
    }

    @Override
    public void setTextColor(Color textColor)
    {
        name.setTextColor(textColor);
        super.setTextColor(textColor);
    }

    @Override
    public void removeChild(INode node)
    {
        activationsGroup.remove(((ActivationBarNode) node).getContent());
        super.removeChild(node);
    }

    @Override
    public boolean addChild(INode node, Point2D p)
    {
        List<INode> activations = getChildren();
        if (!(node instanceof ActivationBarNode)) return false;
        if (activations.contains(node)) return true;

        addChild(node, activations.size());
        node.setGraph(getGraph());
        node.setParent(this);

        node.setLocation(p);

        ActivationBarNode activationBarNode = (ActivationBarNode) node;
        activationBarNode.setTextColor(getTextColor());
        activationBarNode.setBackgroundColor(getBackgroundColor());
        activationBarNode.setBorderColor(getBorderColor());
        activationsGroup.add(activationBarNode.getContent());

        return true;
    }





    /**
     * Sets the name property value.
     * 
     * @param newValue the name of this object
     */
    public void setName(SingleLineText newValue)
    {
        name.setText(newValue.toEdit());
    }

    /**
     * Gets the name property value.
     * 
     * @return the name of this object
     */
    public SingleLineText getName()
    {
        return name;
    }

    public boolean addConnection(IEdge e)
    {
        return false;
    }


    /**
     * Looks for the node_old which is located just after the given point
     * 
     * @param p
     * @return the node_old we found or null if there's no node_old after this point
     */
    private INode getNearestNodeAfterThisPoint(Point2D p)
    {
        double y = p.getY();
        INode nearestNodeAfterThisPoint = null;
        // Step 1 : we look for the closest node_old
        for (INode childNode : getChildren())
        {
            if (nearestNodeAfterThisPoint == null)
            {
                nearestNodeAfterThisPoint = childNode;
            }
            Point2D childLocation = childNode.getLocation();
            Point2D nearestNodeLocation = nearestNodeAfterThisPoint.getLocation();
            double childY = childLocation.getY();
            double nearestY = nearestNodeLocation.getY();
            double currentNodeGap = childY - y;
            double nearestNodeGap = nearestY - y;
            if (currentNodeGap > 0 && Math.abs(currentNodeGap) < Math.abs(nearestNodeGap))
            {
                nearestNodeAfterThisPoint = childNode;
            }
        }
        // Step 2 : if nothing found, we return null
        if (nearestNodeAfterThisPoint == null)
        {
            return null;
        }
        // Step 3 : as by default we set the first child node_old as the nearest one
        // We check if it is not before p
        Point2D nearestChildLocation = nearestNodeAfterThisPoint.getLocation();
        if (y > nearestChildLocation.getY())
        {
            return null;
        }
        // Step 4 : we return the closest node_old after p
        return nearestNodeAfterThisPoint;
    }

    @Override
    public Point2D getConnectionPoint(IEdge e)
    {
        Direction d = e.getDirection(this);
        Point2D locationOnGraph = getLocationOnGraph();
        Rectangle2D topRectBounds = getTopRectangle();
        if (d.getX() > 0)
        {
            return new Point2D.Double(locationOnGraph.getX(), locationOnGraph.getY() + topRectBounds.getHeight() / 2);
        }
        return new Point2D.Double(locationOnGraph.getX() + topRectBounds.getWidth(), locationOnGraph.getY()
                + topRectBounds.getHeight() / 2);
    }

    @Override
    public Point2D getLocation()
    {
        IGraph currentGraph = getGraph();
        if (currentGraph == null)
        {
            return new Point2D.Double(0, 0);
        }
        Collection<IEdge> edges = currentGraph.getAllEdges();
        for (IEdge edge : edges)
        {
            if (edge instanceof CallEdge)
            {
                INode endingNode = edge.getEnd();
                if (endingNode == this)
                {
                    INode startingNode = edge.getStart();
                    Point2D locationOnGraph = startingNode.getLocationOnGraph();
                    Point2D realLocation = super.getLocation();
                    Point2D fixedLocation = new Point2D.Double(realLocation.getX(), locationOnGraph.getY()
                            - getTopRectangleHeight() / 2 + ActivationBarNode.CALL_YGAP / 2);
                    return fixedLocation;
                }
            }
        }
        Point2D realLocation = super.getLocation();
        Point2D fixedLocation = new Point2D.Double(realLocation.getX(), 0);
        return fixedLocation;
    }

    /**
     * Returns the rectangle at the top of the object node_old.
     * 
     * @return the top rectangle
     */
    public Rectangle2D getTopRectangle()
    {
        double topWidth = getTopRectangleWidth();
        double topHeight = getTopRectangleHeight();
        Rectangle2D topRectangle = new Rectangle2D.Double(0, 0, topWidth, topHeight);
        Rectangle2D snappedRectangle = getGraph().getGridSticker().snap(topRectangle);
        return snappedRectangle;
    }

    private double getTopRectangleHeight()
    {
        Rectangle2D bounds = name.getBounds();
        double topHeight = Math.max(bounds.getHeight(), DEFAULT_TOP_HEIGHT);
        return topHeight;
    }

    private double getTopRectangleWidth()
    {
        Rectangle2D bounds = name.getBounds();
        double topWidth = Math.max(bounds.getWidth(), DEFAULT_WIDTH);
        return topWidth;
    }

    @Override
    public Rectangle2D getBounds()
    {
        double topRectWidth = getTopRectangle().getWidth();
        double height = getLocalHeight();
        Point2D nodeLocation = getLocation();
        Rectangle2D bounds = new Rectangle2D.Double(nodeLocation.getX(), nodeLocation.getY(), topRectWidth, height);
        Rectangle2D scaledBounds = getScaledBounds(bounds);
        Rectangle2D snappedBounds = getGraph().getGridSticker().snap(scaledBounds);
        return snappedBounds;
    }

    public double getLocalHeight()
    {
        double topRectHeight = getTopRectangle().getHeight();
        double height = topRectHeight; // default initial height
        List<INode> children = getChildren();
        for (INode n : children)
        {
            if (n.getClass().isAssignableFrom(ActivationBarNode.class))
            {
                // We are looking for the last activation bar node_old to get the total height needed
                height = Math.max(height, n.getBounds().getMaxY());
            }
        }
        height = height + ActivationBarNode.CALL_YGAP * 2;
        return height;
    }

    private Rectangle2D getScaledBounds(Rectangle2D bounds)
    {
        double x = bounds.getX();
        double y = bounds.getY();
        double w = bounds.getWidth();
        double h = bounds.getHeight();
        double diffY = this.maxYOverAllLifeLineNodes - bounds.getMaxY();
        if (diffY > 0)
        {
            h = h + diffY;
        }
        return new Rectangle2D.Double(x, y, w, h);
    }

    @Override
    public Rectangle2D getShape()
    {
        Point2D currentLocation = getLocation();
        Rectangle2D topRectangle = getTopRectangle();
        double x = currentLocation.getX();
        double y = currentLocation.getY();
        double w = topRectangle.getWidth();
        double h = topRectangle.getHeight();
        return new Rectangle2D.Double(x, y, w, h);
    }

    public void draw(Graphics2D g2)
    {
        super.draw(g2);

        // Backup current color;
        Color oldColor = g2.getColor();

        // Perform drawing
        Rectangle2D top = getShape();
        g2.setColor(getBackgroundColor());
        g2.fill(top);
        g2.setColor(getBorderColor());
        g2.draw(top);
        g2.setColor(getTextColor());
        name.draw(g2, top);
        double xmid = top.getCenterX();
        Line2D line = new Line2D.Double(xmid, top.getMaxY(), xmid, getMaxYOverAllLifeLineNodes());
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0.0f, new float[]
        {
                5.0f,
                5.0f
        }, 0.0f));
        g2.setColor(getBorderColor());
        g2.draw(line);
        g2.setStroke(oldStroke);

        // Restore first color
        g2.setColor(oldColor);
        // Draw its children
        for (INode node : getChildren())
        {
            node.draw(g2);
        }
    }

    private double getMaxYOverAllLifeLineNodes()
    {
        double maxY = this.getLocalHeight();
        IGraph graph = getGraph();
        if (graph == null)
        {
            return maxY;
        }
        Collection<INode> nodes = graph.getAllNodes();
        for (INode node : nodes)
        {
            if (!node.getClass().isAssignableFrom(LifelineNode.class))
            {
                continue;
            }
            LifelineNode aLifeLineNode = (LifelineNode) node;
            double localY = aLifeLineNode.getLocalHeight() + aLifeLineNode.getLocationOnGraph().getY();
            maxY = Math.max(maxY, localY);
        }
        this.maxYOverAllLifeLineNodes = maxY;
        return maxY;
    }

    public boolean contains(Point2D p)
    {
        Rectangle2D bounds = getBounds();
        return bounds.getX() <= p.getX() && p.getX() <= bounds.getX() + bounds.getWidth();
    }


    private SingleLineText name;

    private transient VerticalGroupContent activationsGroup = null;

    private transient double maxYOverAllLifeLineNodes = 0;
    private static int DEFAULT_TOP_HEIGHT = 60;
    private static int DEFAULT_WIDTH = 80;
    private static int DEFAULT_HEIGHT = 120;

    private static LineText.Converter nameConverter = new LineText.Converter(){
        @Override
        public OneLineString toLineString(String text)
        {
            return new LargeSizeDecorator(new UnderlineDecorator(new OneLineString(text)));
        }
    };
}
