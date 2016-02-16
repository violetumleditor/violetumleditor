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

package com.horstmann.violet.product.diagram.sequence.nodes;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.sequence.edges.CallEdge;
import com.horstmann.violet.product.diagram.sequence.edges.ReturnEdge;

/**
 * An activation bar in a sequence diagram. This activation bar is hang on a lifeline (implicit parameter)
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 */
public class ActivationBarNode extends ColorableNode
{
    protected static class ActivationBarShape implements ContentInsideCustomShape.ShapeCreator
    {
        @Override
        public Shape createShape(double contentWidth, double contentHeight)
        {
            return new Rectangle2D.Double(0,0, DEFAULT_WIDTH, contentHeight);
        }
    }

    public ActivationBarNode()
    {
        super();
        createContentStructure();
    }

    protected ActivationBarNode(ActivationBarNode node) throws CloneNotSupportedException
    {
        super(node);
        createContentStructure();
    }

    @Override
    public void deserializeSupport()
    {
        super.deserializeSupport();
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
        return new ActivationBarNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        activationsGroup = new RelativeLayout();
        activationsGroup.setMinHeight(DEFAULT_HEIGHT);
        activationsGroup.setMinWidth(DEFAULT_WIDTH);

        EmptyContent padding = new EmptyContent();
        padding.setMinHeight(DEFAULT_CHILD_VERTICAL_MARGIN);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(padding);
        verticalLayout.add(activationsGroup);
        verticalLayout.add(padding);

        ContentInsideShape contentInsideShape = new ContentInsideCustomShape(verticalLayout, new ActivationBarShape());

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());

        setTextColor(getTextColor());
    }

    @Override
    public void removeChild(INode node)
    {
        activationsGroup.remove(((ActivationBarNode) node).getContent());
        super.removeChild(node);
    }

    @Override
    public boolean addChild(INode node, Point2D point)
    {
        if (! (node instanceof ActivationBarNode))
        {
            return false;
        }

        addChild(node, getChildren().size());

        ActivationBarNode activationBarNode = (ActivationBarNode) node;
        activationBarNode.setTextColor(getTextColor());
        activationBarNode.setBackgroundColor(getBackgroundColor());
        activationBarNode.setBorderColor(getBorderColor());

        activationsGroup.add(activationBarNode.getContent());

        activationBarNode.setLocation(point);
        activationBarNode.setGraph(getGraph());
        activationBarNode.setParent(this);

        return true;
    }

    protected void onChildChangeLocation(INode child)
    {
        activationsGroup.setPosition(((AbstractNode) child).getContent(), getChildRelativeLocation(child));
    }

    protected Point2D getChildRelativeLocation(INode node)
    {
        Point2D nodeLocation = node.getLocation();
        if(DEFAULT_CHILD_VERTICAL_MARGIN > nodeLocation.getY() || DEFAULT_CHILD_LEFT_MARGIN != nodeLocation.getX())
        {
            nodeLocation.setLocation(DEFAULT_CHILD_LEFT_MARGIN, Math.max(nodeLocation.getY(), DEFAULT_CHILD_VERTICAL_MARGIN));
            node.setLocation(nodeLocation);
        }

        return new Point2D.Double(nodeLocation.getX()+DEFAULT_CHILD_LEFT_MARGIN, nodeLocation.getY()- DEFAULT_CHILD_VERTICAL_MARGIN);
    }

    @Override
    public boolean addConnection(IEdge edge)
    {
        if(null == edge.getEnd())
        {
            return false;
        }
        if (edge instanceof CallEdge)
        {
            return isCallEdgeAcceptable((CallEdge) edge);
        }
        if (edge instanceof ReturnEdge)
        {
            return isReturnEdgeAcceptable((ReturnEdge) edge);
        }
        return false;
    }

    @Override
    public void removeConnection(IEdge edge)
    {
        if (edge instanceof CallEdge)
        {
            for(IEdge connectedEdge : getConnectedEdges())
            {
                if(connectedEdge instanceof ReturnEdge &&
                   edge.getStart() == connectedEdge.getEnd() &&
                   edge.getEnd() == connectedEdge.getStart())
                {
                    getGraph().removeEdge(connectedEdge);
                    break;
                }
            }
        }
    }

    @Override
    public Point2D getConnectionPoint(IEdge edge)
    {
        Direction edgeDirection = edge.getDirection(this);
        Point2D startingNodeLocation = getLocationOnGraph();

        double x = startingNodeLocation.getX();
        double y = startingNodeLocation.getY();

        if (0 >= edgeDirection.getX())
        {
            x+=DEFAULT_WIDTH;
        }

        if(edge instanceof CallEdge)
        {
            if (edge.getEnd() instanceof LifelineNode)
            {
                y += CALL_YGAP / 2;
            }
            else if (null != edge.getStart().getParent() &&
                     null != edge.getEnd().getParent() &&
                     edge.getStart().getParents().get(0) == edge.getEnd().getParents().get(0))
            {
                if (0 < edgeDirection.getX())
                {
                    x += DEFAULT_WIDTH;
                }
                if(this == edge.getStart())
                {
                    y += edge.getEnd().getLocation().getY() - CALL_YGAP/2;
                }
            }
            else if(this == edge.getStart())
            {
                y = edge.getEnd().getLocationOnGraph().getY();
            }
        }
        else if(edge instanceof ReturnEdge)
        {
            if(this == edge.getStart())
            {
                y += getContent().getHeight();
            }
            else if(this == edge.getEnd())
            {
                y = edge.getStart().getLocationOnGraph().getY() + edge.getStart().getBounds().getHeight();
            }
        }
        return new Point2D.Double(x, y);
    }

    @Override
    public Rectangle2D getBounds()
    {
        refreshPositionAndSize();

        return super.getBounds();
    }

    private void refreshPositionAndSize()
    {
        setLocation(calculateLocation());
        activationsGroup.setMinHeight((int)Math.max(calculateHeight(), DEFAULT_HEIGHT ));
    }

    private Point2D calculateLocation()
    {
        double y = this.getLocation().getY();

        for (IEdge edge : getGraph().getAllEdges())
        {
            if (edge instanceof CallEdge)
            {
                if (edge.getStart() == this && edge.getStart() != getParent())
                {
                    y = Math.min(y, edge.getEnd().getLocationOnGraph().getY()-5);
                }
            }
        }
        return new Point.Double(this.getLocation().getX(), y);
    }

    private double calculateHeight()
    {
        double height = 0;
        for (IEdge edge : getGraph().getAllEdges())
        {
            if (edge instanceof CallEdge)
            {
                if (edge.getStart() == this && edge.getStart() != getParent())
                {
                    INode endingNode = edge.getEnd();
                    if (endingNode instanceof ActivationBarNode)
                    {
                        Rectangle2D endingNodeBounds = endingNode.getBounds();
                        double newHeight = endingNodeBounds.getHeight() + (endingNode.getLocationOnGraph().getY() - this.getLocationOnGraph().getY());
                        height = Math.max(height, newHeight);
                    }
                }
            }
        }
        return Math.max(DEFAULT_HEIGHT, height);
    }

    private boolean isReturnEdgeAcceptable(ReturnEdge edge)
    {
        INode start = edge.getStart();
        INode end = edge.getEnd();

        if (null != start.getParent() &&
            null != end.getParent() &&
            start.getParents().get(0) == end.getParents().get(0))
        {
            return false;
        }

        for (IEdge connectedEdge : getConnectedEdges())
        {
            if(start == connectedEdge.getEnd() && end == connectedEdge.getStart())
            {
                return true;
            }
        }
        return false;
    }

    private boolean isCallEdgeAcceptable(CallEdge edge)
    {
        INode start = edge.getStart();
        INode end = edge.getEnd();

        for (IEdge connectedEdge : getConnectedEdges())
        {
            if(start == connectedEdge.getStart() && end == connectedEdge.getEnd() ||
               end == connectedEdge.getStart() && start == connectedEdge.getEnd() )
            {
                return false;
            }
        }
        if (start instanceof ActivationBarNode && end instanceof ActivationBarNode)
        {
            if(start == end)
            {
                ActivationBarNode newActivationBar = new ActivationBarNode();
                Point2D location = edge.getStartLocation();
                Point2D newActivationBarLocation = new Point2D.Double(location.getX(), location.getY() + CALL_YGAP / 2);
                start.addChild(newActivationBar, newActivationBarLocation);
                edge.setEnd(newActivationBar);
                return true;
            }
            return ((start.getParents().get(0) != end.getParents().get(0)) || (start == end.getParent()));
        }

        if (end instanceof LifelineNode)
        {
            if(start instanceof ActivationBarNode && end != start.getParents().get(0))
            {
                if(edge.getEndLocation().getY() < end.getBounds().getY() + LifelineNode.DEFAULT_TOP_HEIGHT)
                {
                    return true;
                }

                ActivationBarNode newActivationBar = new ActivationBarNode();
                Point2D location = edge.getEndLocation();
                Point2D newActivationBarLocation = new Point2D.Double(location.getX(), location.getY());
                end.addChild(newActivationBar, newActivationBarLocation);
                edge.setEnd(newActivationBar);
                return true;
            }
        }
        return false;
    }


    private transient RelativeLayout activationsGroup = null;
    
    /** Default with */
    public static final int DEFAULT_WIDTH = 16;
    public static final int DEFAULT_CHILD_LEFT_MARGIN = 5;
    public static final int DEFAULT_CHILD_VERTICAL_MARGIN = 10;

    /** Default height */
    private static final int DEFAULT_HEIGHT = 20;

    /** Default vertical gap between two call nodes and a call node_old and an implicit node_old */
    public static final int CALL_YGAP = 20;
}
