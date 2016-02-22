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

package com.horstmann.violet.product.diagram.activity.nodes;

import java.awt.geom.Point2D;
import java.util.List;

import com.horstmann.violet.framework.graphics.content.Content;
import com.horstmann.violet.framework.graphics.content.ContentBackground;
import com.horstmann.violet.framework.graphics.content.ContentInsideShape;
import com.horstmann.violet.framework.graphics.content.EmptyContent;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.framework.property.choiceList.ChoiceList;
import com.horstmann.violet.framework.property.StretchStyle;
import com.horstmann.violet.framework.property.choiceList.TextChoiceList;
import com.horstmann.violet.product.diagram.activity.edges.ActivityTransitionEdge;

/**
 * A synchronization bar node_old in an activity diagram.
 */
public class SynchronizationBarNode extends ColorableNode
{
    public SynchronizationBarNode()
    {
        super();
        orientation = new TextChoiceList<StretchStrategy>(
                new String[]{"HORIZONTAL","VERTICAL"},
                new StretchStrategy[]{HORIZONTAL, VERTICAL}
        );
        orientation.setSelectedValue(HORIZONTAL);
        createContentStructure();
    }

    protected SynchronizationBarNode(SynchronizationBarNode node) throws CloneNotSupportedException
    {
        super(node);
        createContentStructure();
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new SynchronizationBarNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        currentStretch = ((StretchStrategy)this.orientation.getSelectedValue());

        content = new EmptyContent();
        currentStretch.setLength(content, LENGTH);
        currentStretch.setThickness(content, THICKNESS);

        ContentInsideShape contentInsideShape = new ContentInsideRectangle(content);
        ContentBackground contentBackground = new ContentBackground(contentInsideShape, getBorderColor());

        setContent(contentBackground);
    }

    @Override
    public void onConnectedEdge(IEdge connectedEdge)
    {
        refresh();
    }

    @Override
    public void removeConnection(IEdge e)
    {
        refresh();
    }

    @Override
    public boolean addConnection(IEdge e)
    {
        return e.getEndNode() != null && this != e.getEndNode();
    }

    @Override
    public Point2D getConnectionPoint(IEdge e)
    {
        Point2D defaultConnectionPoint = super.getConnectionPoint(e);
        double x = defaultConnectionPoint.getX();
        double y = defaultConnectionPoint.getY();

        if (ActivityTransitionEdge.class.isInstance(e))
        {
            if (this == e.getStartNode())
            {
                x = e.getStartNode().getConnectionPoint(e).getX();
            }
            else if (this == e.getEndNode())
            {
                x = e.getEndNode().getConnectionPoint(e).getX();
            }

            if(Direction.NORTH.equals(e.getDirection(this).getNearestCardinalDirection()))
            {
                y = defaultConnectionPoint.getY();
            }
            else if(Direction.SOUTH.equals(e.getDirection(this).getNearestCardinalDirection()))
            {
                y = defaultConnectionPoint.getY() + THICKNESS;
            }
        }

        return new Point2D.Double(x, y);
    }

    private void refresh()
    {
        List<IEdge> connectedEdges = getConnectedEdges();
        if (connectedEdges.size() > 0)
        {
            int count = 0;

            for (IEdge edge : connectedEdges)
            {
                Direction direction = edge.getDirection(this);
                if (currentStretch.getCountingDirection().equals(direction.getNearestCardinalDirection())) {
                    ++count;
                }
            }

            currentStretch.setLength(content, LENGTH + EXTRA_LENGTH * (Math.max(count, connectedEdges.size() - count) - 1));
            currentStretch.setThickness(content, THICKNESS);
        }
    }

    public StretchStyle getStretchStyle()
    {
        return currentStretch.getStretchStyle();
    }

    public ChoiceList getOrientation()
    {
        return orientation;
    }

    public void setOrientation(ChoiceList orientation)
    {
        double length = currentStretch.getLength(content);
        double thickness = currentStretch.getThickness(content);

        currentStretch = ((StretchStrategy)this.orientation.getSelectedValue());

        currentStretch.setLength(content, length);
        currentStretch.setThickness(content, thickness);

        this.orientation = orientation;
        getContent().refresh();
    }

    private ChoiceList orientation;

    private transient StretchStrategy currentStretch;
    private transient Content content = null;

    private static final int LENGTH = 100;
    private static final int THICKNESS = 5;
    private static final int EXTRA_LENGTH = 12;

    private interface StretchStrategy
    {
        void setLength(Content content, double length);
        double getLength(Content content);
        void setThickness(Content content, double thickness);
        double getThickness(Content content);
        Direction getCountingDirection();
        StretchStyle getStretchStyle();
    }

    private static final StretchStrategy HORIZONTAL = new StretchStrategy() {
        @Override
        public void setLength(Content content, double length) {
            content.setMinWidth(length);
        }

        @Override
        public double getLength(Content content) {
            return content.getWidth();
        }

        @Override
        public void setThickness(Content content, double thickness)
        {
            content.setMinHeight(thickness);
        }

        @Override
        public double getThickness(Content content) {
            return content.getHeight();
        }

        @Override
        public Direction getCountingDirection() {
            return Direction.NORTH;
        }

        @Override
        public StretchStyle getStretchStyle() {
            return StretchStyle.HORIZONTAL;
        }
    };
    private static final StretchStrategy VERTICAL = new StretchStrategy() {
        @Override
        public void setLength(Content content, double length) {
            content.setMinHeight(length);
        }

        @Override
        public double getLength(Content content) {
            return content.getHeight();
        }

        @Override
        public void setThickness(Content content, double thickness) {
            content.setMinWidth(thickness);
        }

        @Override
        public double getThickness(Content content) {
            return content.getWidth();
        }

        @Override
        public Direction getCountingDirection() {
            return Direction.EAST;
        }

        @Override
        public StretchStyle getStretchStyle() {
            return StretchStyle.VERTICAL;
        }
    };
}
