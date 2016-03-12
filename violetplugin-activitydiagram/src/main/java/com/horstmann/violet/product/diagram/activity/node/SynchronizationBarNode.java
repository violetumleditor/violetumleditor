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

package com.horstmann.violet.product.diagram.activity.node;

import java.util.List;
import java.util.MissingResourceException;

import com.horstmann.violet.framework.graphics.content.Content;
import com.horstmann.violet.framework.graphics.content.ContentBackground;
import com.horstmann.violet.framework.graphics.content.ContentInsideShape;
import com.horstmann.violet.framework.graphics.content.EmptyContent;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRoundRectangle;
import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.common.node.ColorableNode;
import com.horstmann.violet.product.diagram.activity.ActivityDiagramConstant;
import com.horstmann.violet.product.diagram.property.choiceList.ChoiceList;
import com.horstmann.violet.product.diagram.property.choiceList.TextChoiceList;

/**
 * A synchronization bar node_old in an activity diagram.
 */
public class SynchronizationBarNode extends ColorableNode
{
    public SynchronizationBarNode()
    {
        super();
        orientation = new TextChoiceList<StretchStrategy>(
                STRETCH_KEYS,
                STRETCH_STRATEGIES
        );
        orientation.setSelectedValue(HORIZONTAL);
        selectedStretch = orientation.getSelectedPos();
        createContentStructure();
    }

    protected SynchronizationBarNode(SynchronizationBarNode node) throws CloneNotSupportedException
    {
        super(node);
        orientation = node.orientation.clone();
        selectedStretch = orientation.getSelectedPos();
        createContentStructure();
    }

    @Override
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();

        orientation = new TextChoiceList<StretchStrategy>(
                STRETCH_KEYS,
                STRETCH_STRATEGIES
        );
        orientation.setSelectedIndex(selectedStretch);
    }

    @Override
    protected SynchronizationBarNode copy() throws CloneNotSupportedException
    {
        return new SynchronizationBarNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        StretchStrategy currentStretch = getStretch();

        content = new EmptyContent();
        currentStretch.setLength(content, LENGTH);
        currentStretch.setThickness(content, THICKNESS);

        ContentInsideShape contentInsideShape = new ContentInsideRoundRectangle(content, THICKNESS);
        ContentBackground contentBackground = new ContentBackground(contentInsideShape, getBorderColor());

        setContent(contentBackground);
    }

    @Override
    public String getToolTip()
    {
        return ActivityDiagramConstant.ACTIVITY_DIAGRAM_RESOURCE.getString("tooltip.synchronization_node");
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

    private void refresh()
    {
        List<IEdge> connectedEdges = getConnectedEdges();
        if (connectedEdges.size() > 0)
        {
            int count = 0;
            StretchStrategy currentStretch = getStretch();

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

    public ChoiceList getOrientation()
    {
        return orientation;
    }

    public void setOrientation(ChoiceList orientation)
    {
        StretchStrategy currentStretch = getStretch();

        double length = currentStretch.getLength(content);
        double thickness = currentStretch.getThickness(content);
        this.orientation = orientation;
        selectedStretch = this.orientation.getSelectedPos();

        currentStretch = getStretch();
        currentStretch.setLength(content, length);
        currentStretch.setThickness(content, thickness);

        getContent().refresh();
    }
    public StretchStrategy getStretch()
    {
        return ((StretchStrategy)orientation.getSelectedValue());
    }

    private int selectedStretch;

    private transient ChoiceList orientation;
    private transient Content content;

    private static final int LENGTH = 100;
    private static final int THICKNESS = 6;
    private static final int EXTRA_LENGTH = 12;

    private interface StretchStrategy
    {
        void setLength(Content content, double length);
        double getLength(Content content);
        void setThickness(Content content, double thickness);
        double getThickness(Content content);
        Direction getCountingDirection();
    }

    public static final StretchStrategy HORIZONTAL = new StretchStrategy()
    {
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

    };
    public static final StretchStrategy VERTICAL = new StretchStrategy()
    {
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
    };

    private static final StretchStrategy[] STRETCH_STRATEGIES = new StretchStrategy[]{
            HORIZONTAL,
            VERTICAL
    };
    private static String[] STRETCH_KEYS = new String[]{
            "HORIZONTAL",
            "VERTICAL"
    };

    static
    {
        for(int i = 0; i <STRETCH_KEYS.length;++i)
        {
            try
            {
                STRETCH_KEYS[i] = ActivityDiagramConstant.ACTIVITY_DIAGRAM_RESOURCE.getString("orientation." + STRETCH_KEYS[i].toLowerCase());
            }
            catch (MissingResourceException ignored)
            {}
        }
    }
}
