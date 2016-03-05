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

package com.horstmann.violet.product.diagram.sequence.node;

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
import com.horstmann.violet.product.diagram.sequence.SequenceDiagramConstant;
import com.horstmann.violet.product.diagram.sequence.edge.CallEdge;
import com.horstmann.violet.product.diagram.sequence.edge.ReturnEdge;

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
            return new Rectangle2D.Double(0,0, WIDTH, contentHeight);
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
                child.deserializeSupport();
                activationsGroup.add(((ActivationBarNode) child).getContent());
                onChildChangeLocation(child);
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

        EmptyContent padding = new EmptyContent();
        padding.setMinHeight(CHILD_VERTICAL_MARGIN);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(padding);
        verticalLayout.add(activationsGroup);
        verticalLayout.add(padding);
        verticalLayout.setMinHeight(MIN_HEIGHT);
        verticalLayout.setMinWidth(WIDTH);

        ContentInsideShape contentInsideShape = new ContentInsideCustomShape(verticalLayout, new ActivationBarShape());

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());

        setTextColor(getTextColor());
    }

    @Override
    public String getToolTip()
    {
        return SequenceDiagramConstant.SEQUENCE_DIAGRAM_RESOURCE.getString("activation_bar_node.tooltip");
    }

    @Override
    public void removeChild(INode node)
    {
        activationsGroup.remove(((ActivationBarNode) node).getContent());
        super.removeChild(node);
        refreshSize();
    }

    @Override
    public boolean addChild(INode node, Point2D point)
    {
        if (!(node instanceof ActivationBarNode))
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
        refreshSize();

        return true;
    }

    @Override
    protected void onChildChangeLocation(INode child)
    {
        activationsGroup.setPosition(((AbstractNode) child).getContent(), getChildRelativeLocation(child));
    }

    protected Point2D getChildRelativeLocation(INode node)
    {
        Point2D nodeLocation = node.getLocation();

        if(CHILD_LEFT_MARGIN != nodeLocation.getX() || CHILD_VERTICAL_MARGIN > nodeLocation.getY())
        {
            nodeLocation.setLocation(CHILD_LEFT_MARGIN, Math.max(nodeLocation.getY(), CHILD_VERTICAL_MARGIN));
            node.setLocation(nodeLocation);
        }

        return new Point2D.Double(nodeLocation.getX() + CHILD_LEFT_MARGIN, nodeLocation.getY() - CHILD_VERTICAL_MARGIN);
    }

    @Override
    public void onConnectedEdge(IEdge connectedEdge)
    {
        refreshPositionAndSize();
    }

    @Override
    public boolean addConnection(IEdge edge)
    {
        if(null == edge.getEndNode())
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
                   edge.getStartNode() == connectedEdge.getEndNode() &&
                   edge.getEndNode() == connectedEdge.getStartNode())
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
            x+= WIDTH;
        }

        if(edge instanceof CallEdge)
        {
            if (edge.getEndNode() instanceof LifelineNode)
            {
                y += CALL_Y_GAP / 2;
            }
            else if (null != edge.getStartNode().getParent() &&
                     null != edge.getEndNode().getParent() &&
                     edge.getStartNode().getParents().get(0) == edge.getEndNode().getParents().get(0))
            {
                if (0 < edgeDirection.getX())
                {
                    x += WIDTH;
                }
                if(this == edge.getStartNode())
                {
                    y += edge.getEndNode().getLocation().getY() - CALL_Y_GAP /2;
                }
            }
            else if(this == edge.getStartNode())
            {
                y = edge.getEndNode().getLocationOnGraph().getY();
            }
        }
        else if(edge instanceof ReturnEdge)
        {
            if(this == edge.getStartNode())
            {
                y += getContent().getHeight();
            }
            else if(this == edge.getEndNode())
            {
                y = edge.getStartNode().getLocationOnGraph().getY() + edge.getStartNode().getBounds().getHeight();
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

    private void refreshPosition()
    {
        setLocation(calculateLocation());
    }

    private void refreshSize()
    {
        activationsGroup.setMinHeight((int)Math.max(calculateHeight(), MIN_HEIGHT));
    }

    private void refreshPositionAndSize()
    {
        refreshPosition();
        refreshSize();
    }

    private Point2D calculateLocation()
    {
        double y = this.getLocation().getY();

        for (IEdge edge : getGraph().getAllEdges())
        {
            if (edge instanceof CallEdge && edge.getEndNode() instanceof ActivationBarNode)
            {
                if (edge.getStartNode() == this)
                {
                    y = Math.min(y, edge.getEndNode().getLocationOnGraph().getY() - 5);
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
                if (edge.getStartNode() == this && edge.getStartNode() != getParent())
                {
                    INode endingNode = edge.getEndNode();
                    if (endingNode instanceof ActivationBarNode)
                    {
                        Rectangle2D endingNodeBounds = endingNode.getBounds();
                        double newHeight = endingNodeBounds.getHeight() + (endingNode.getLocationOnGraph().getY() - this.getLocationOnGraph().getY());
                        height = Math.max(height, newHeight);
                    }
                }
            }
        }
        return Math.max(MIN_HEIGHT, height);
    }

    private boolean isReturnEdgeAcceptable(ReturnEdge edge)
    {
        INode start = edge.getStartNode();
        INode end = edge.getEndNode();

        if (null != start.getParent() &&
            null != end.getParent() &&
            start.getParents().get(0) == end.getParents().get(0))
        {
            return false;
        }

        for (IEdge connectedEdge : getConnectedEdges())
        {
            if(start == connectedEdge.getEndNode() && end == connectedEdge.getStartNode())
            {
                return true;
            }
        }
        return false;
    }

    private boolean isCallEdgeAcceptable(CallEdge edge)
    {
        INode start = edge.getStartNode();
        INode end = edge.getEndNode();

        for (IEdge connectedEdge : getConnectedEdges())
        {
            if(start == connectedEdge.getStartNode() && end == connectedEdge.getEndNode() ||
               end == connectedEdge.getStartNode() && start == connectedEdge.getEndNode() )
            {
                return false;
            }
        }
        if (start instanceof ActivationBarNode && end instanceof ActivationBarNode)
        {
            if(start.getParents().get(0) != end.getParents().get(0))
            {
                return true;
            }
            else if(start == end)
            {
                ActivationBarNode newActivationBar = new ActivationBarNode();
                Point2D location = edge.getStartLocation();
                Point2D newActivationBarLocation = new Point2D.Double(location.getX(), location.getY() + CALL_Y_GAP / 2);
                start.addChild(newActivationBar, newActivationBarLocation);
                edge.setEndNode(newActivationBar);

                return true;
            }
            else if(end.getParents().contains(start))
            {
                return true;
            }
            else if(start.getParents().contains(end))
            {
                return false;
            }
        }

        if (end instanceof LifelineNode)
        {
            if(start instanceof ActivationBarNode && end != start.getParents().get(0))
            {
                if(edge.getEndLocation().getY() < end.getBounds().getY() + LifelineNode.TOP_HEIGHT)
                {
                    edge.setCenterLabel(CENTER_LABEL);
                    return true;
                }

                ActivationBarNode newActivationBar = new ActivationBarNode();
                Point2D location = edge.getEndLocation();
                Point2D newActivationBarLocation = new Point2D.Double(location.getX(), location.getY());
                end.addChild(newActivationBar, newActivationBarLocation);
                edge.setEndNode(newActivationBar);
                return true;
            }
        }
        return false;
    }

    private transient RelativeLayout activationsGroup = null;
    
    public static final int WIDTH = 16;
    public static final int MIN_HEIGHT = 15;
    public static final int CHILD_LEFT_MARGIN = 6;
    public static final int CHILD_VERTICAL_MARGIN = 10;
    public static final int CALL_Y_GAP = 20;

    public static final String CENTER_LABEL = "«create»";
}
