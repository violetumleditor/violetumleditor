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

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideDiamond;
import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.common.node.ColorableNode;
import com.horstmann.violet.product.diagram.activity.ActivityDiagramConstant;

/**
 * A decision node_old in an activity diagram.
 */
public class DecisionNode extends ColorableNode
{
    /**
     * Construct a decision node_old with a default size
     */
    public DecisionNode()
    {
        super();
        createContentStructure();
    }

    protected DecisionNode(DecisionNode node) throws CloneNotSupportedException
    {
        super(node);
        createContentStructure();
    }

    @Override
    protected DecisionNode copy() throws CloneNotSupportedException
    {
        return new DecisionNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        EmptyContent content = new EmptyContent();
        content.setMinHeight(MIN_HEIGHT);
        content.setMinWidth(MIN_WIDTH);

        ContentInsideShape contentInsideShape = new ContentInsideDiamond(content, DIAMOND_DEGREES);

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());

        setTextColor(super.getTextColor());
    }

    @Override
    public String getToolTip()
    {
        return ActivityDiagramConstant.ACTIVITY_DIAGRAM_RESOURCE.getString("tooltip.decision_node");
    }

    @Override
    public Point2D getConnectionPoint(IEdge edge)
    {
        Rectangle2D bounds = getBounds();

        double x = bounds.getCenterX();
        double y = bounds.getCenterY();

        Direction direction = edge.getDirection(this).getNearestCardinalDirection();
        if(direction.equals(Direction.NORTH))
        {
            y = bounds.getMaxY();
        }
        else if(direction.equals(Direction.SOUTH))
        {
            y = bounds.getY();
        }
        else if(direction.equals(Direction.EAST))
        {
            x = bounds.getX();
        }
        else
        {
            x = bounds.getMaxX();
        }
        return new Point2D.Double(x, y);
    }

    @Override
    public boolean addConnection(IEdge edge)
    {
        return edge.getEndNode() != null && this != edge.getEndNode();
    }

    private static final int DIAMOND_DEGREES = 60;
    private static final int MIN_WIDTH = 30;
    private static final int MIN_HEIGHT = 20;
}
