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

package com.horstmann.violet.product.diagram.state.nodes;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.horstmann.violet.framework.graphics.content.ContentBackground;
import com.horstmann.violet.framework.graphics.content.ContentBorder;
import com.horstmann.violet.framework.graphics.content.ContentInsideShape;
import com.horstmann.violet.framework.graphics.content.TextContent;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.framework.property.string.SingleLineText;

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
        condition = new SingleLineText();
        createContentStructure();
    }

    protected DecisionNode(DecisionNode node) throws CloneNotSupportedException
    {
        super(node);
        condition = node.condition.clone();
        createContentStructure();
    }

    @Override
    public void deserializeSupport()
    {
        condition.deserializeSupport();

        super.deserializeSupport();
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new DecisionNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        TextContent conditionContent = new TextContent(condition);
        conditionContent.setMinHeight(DEFAULT_HEIGHT);
        conditionContent.setMinWidth(DEFAULT_WIDTH);

        ContentInsideShape contentInsideShape = new ContentInsideCustomShape(conditionContent, new ContentInsideCustomShape.ShapeCreator() {
            @Override
            public Shape createShape(double contentWidth, double contentHeight) {
                double width = contentWidth + contentHeight * Math.sqrt(3);
                double height = contentHeight + contentWidth / Math.sqrt(3);

                GeneralPath diamond = new GeneralPath();
                diamond.moveTo(0, height/2);
                diamond.lineTo(width/2, 0);
                diamond.lineTo(width, height/2);
                diamond.lineTo(width/2, height);
                diamond.lineTo(0, height/2);
                return diamond;
            }
        });

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());

        setTextColor(super.getTextColor());
    }

    @Override
    public void setTextColor(Color textColor)
    {
        condition.setTextColor(textColor);
        super.setTextColor(textColor);
    }


    @Override
    public Point2D getConnectionPoint(IEdge e)
    {
        Rectangle2D b = getBounds();

        double x = b.getCenterX();
        double y = b.getCenterY();

        Direction d = e.getDirection(this);

        Direction nearestCardinalDirection = d.getNearestCardinalDirection();

        if (Direction.NORTH.equals(nearestCardinalDirection))
        {
            x = b.getCenterX();
            y = b.getMaxY();
        }
        if (Direction.SOUTH.equals(nearestCardinalDirection))
        {
            x = b.getCenterX();
            y = b.getY();
        }
        if (Direction.EAST.equals(nearestCardinalDirection))
        {
            x = b.getX();
            y = b.getCenterY();
        }
        if (Direction.WEST.equals(nearestCardinalDirection))
        {
            x = b.getMaxX();
            y = b.getCenterY();
        }
        return new Point2D.Double(x, y);
    }

    @Override
    public boolean addConnection(IEdge e)
    {
        return e.getEnd() != null && this != e.getEnd();
    }


    /**
     * Sets the condition property value.
     * 
     * @param newValue the branch condition
     */
    public void setCondition(SingleLineText newValue)
    {
        condition.setText(newValue.toEdit());
    }

    /**
     * Gets the condition property value.
     * 
     * @return the branch condition
     */
    public SingleLineText getCondition()
    {
        return condition;
    }

    /**
     * @see java.lang.Object#clone()
     */

    private SingleLineText condition;

    private static int DEFAULT_WIDTH = 30;
    private static int DEFAULT_HEIGHT = 20;
}
