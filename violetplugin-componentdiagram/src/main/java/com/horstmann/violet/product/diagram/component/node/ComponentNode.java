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

package com.horstmann.violet.product.diagram.component.node;

import com.horstmann.violet.framework.graphics.content.ContentBackground;
import com.horstmann.violet.framework.graphics.content.ContentBorder;
import com.horstmann.violet.framework.graphics.content.ContentInsideShape;
import com.horstmann.violet.framework.graphics.content.TextContent;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INamedNode;
import com.horstmann.violet.product.diagram.common.node.ColorableNode;
import com.horstmann.violet.product.diagram.component.ComponentDiagramConstant;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

/**
 * A component node in a UML diagram.
 */
public class ComponentNode extends ColorableNode implements INamedNode
{
    /**
     * Construct a component node with a default size and color
     */
    public ComponentNode()
    {
        tooltip = ComponentDiagramConstant.COMPONENT_DIAGRAM_RESOURCE.getString("tooltip.component_node");
        text = new SingleLineText();
        text.setPadding(TOP_AND_BOTTOM_TEXT_PADDING,
                        LEFT_TEXT_PADDING,
                        TOP_AND_BOTTOM_TEXT_PADDING,
                        RIGHT_TEXT_PADDING);
        createContentStructure();
    }

    /**
     * Construct a component node by cloning it.
     */
    protected ComponentNode(ComponentNode node) throws CloneNotSupportedException
    {
        super(node);
        tooltip = ComponentDiagramConstant.COMPONENT_DIAGRAM_RESOURCE.getString("tooltip.component_node");
        text = node.text.clone();
        createContentStructure();
    }

    /**
     * Reconstruction of the text.
     */
    @Override
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();
        if(null == text)
        {
            text = new SingleLineText();
        }
        text.reconstruction();
        text.setPadding(TOP_AND_BOTTOM_TEXT_PADDING,
                        LEFT_TEXT_PADDING,
                        TOP_AND_BOTTOM_TEXT_PADDING,
                        RIGHT_TEXT_PADDING);
    }

    /**
     * Copy the node.
     */
    @Override
    protected ComponentNode copy() throws CloneNotSupportedException
    {
        return new ComponentNode(this);
    }

    /**
     * Draws the node.
     */
    @Override
    protected void createContentStructure()
    {
        drawNode();
    }

    private void drawNode()
    {
        TextContent signalContent = new TextContent(text);
        signalContent.setMinHeight(MIN_HEIGHT);
        signalContent.setMinWidth(MIN_WIDTH);

        ContentInsideShape contentInsideShape = new ContentInsideCustomShape(signalContent, new ContentInsideCustomShape.ShapeCreator()
        {
            @Override
            public Shape createShape(double contentWidth, double contentHeight)
            {
                GeneralPath path = new GeneralPath();

                //The upper notch
                path.moveTo(ARM_WIDTH /2,0);
                path.lineTo(ARM_WIDTH /2, TOP_NOTCH_HEIGHT);

                //The upper arm
                path.lineTo(0, TOP_NOTCH_HEIGHT);
                path.lineTo(0, TOP_ARM_HEIGHT);
                path.lineTo(ARM_WIDTH, TOP_ARM_HEIGHT);
                path.lineTo(ARM_WIDTH, TOP_NOTCH_HEIGHT);
                path.lineTo(ARM_WIDTH /2, TOP_NOTCH_HEIGHT);

                //The distance between the arms
                path.moveTo(ARM_WIDTH /2, TOP_ARM_HEIGHT);
                path.lineTo(ARM_WIDTH /2, BOTTOM_ARM_HEIGHT);

                //The bottom arm
                path.lineTo(0, BOTTOM_ARM_HEIGHT);
                path.lineTo(0, BOTTOM_NOTCH_HEIGHT);
                path.lineTo(ARM_WIDTH, BOTTOM_NOTCH_HEIGHT);
                path.lineTo(ARM_WIDTH, BOTTOM_ARM_HEIGHT);
                path.lineTo(ARM_WIDTH /2, BOTTOM_ARM_HEIGHT);

                //The bottom notch
                path.moveTo(ARM_WIDTH /2, BOTTOM_NOTCH_HEIGHT);
                path.lineTo(ARM_WIDTH /2, contentHeight);

                //The connection of the node
                path.lineTo(contentWidth, contentHeight);
                path.lineTo(contentWidth, 0);
                path.lineTo(ARM_WIDTH /2, 0);

                return path;
            }
        });
        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());
        setTextColor(super.getTextColor());
    }

    /**
     * Sets the node text color.
     */
    @Override
    public void setTextColor(Color textColor)
    {
        text.setTextColor(textColor);
        super.setTextColor(textColor);
    }

    @Override
    public String getToolTip()
    {
        return tooltip;
    }

    @Override
    public LineText getName()
    {
        return text;
    }

    @Override
    public LineText getAttributes()
    {
        return null;
    }

    @Override
    public LineText getMethods()
    {
        return null;
    }

    /**
     * Gets the connection point.
     */
    @Override
    public Point2D getConnectionPoint(IEdge edge)
    {
        Point2D connectionPoint = super.getConnectionPoint(edge);

        if (Direction.EAST.equals(edge.getDirection(this).getNearestCardinalDirection()))
        {
            double offset = MIN_HEIGHT / 2 - Math.abs(connectionPoint.getY() - getLocation().getY() - MIN_HEIGHT / 2);

            return new Point2D.Double(
                    connectionPoint.getX() + offset,
                    connectionPoint.getY()
            );
        }

        return connectionPoint;
    }

    /**
     * Adds connection edge.
     */
    @Override
    public boolean addConnection(IEdge edge)
    {
        boolean result = false;
        if (edge.getEndNode() != null && this != edge.getEndNode())
        {
            result = true;
        }
        return result;
    }

    public void setText(LineText newValue)
    {
        text.setText(newValue.toEdit());
    }
    public LineText getText()
    {
        return text;
    }

    private SingleLineText text;
    private String tooltip;

    private static final int MIN_WIDTH = 80;
    private static final int MIN_HEIGHT = 40;
    private static final int ARM_WIDTH = 40;
    private static final int TOP_NOTCH_HEIGHT = MIN_HEIGHT*2/7;
    private static final int TOP_ARM_HEIGHT = MIN_HEIGHT*3/7;
    private static final int BOTTOM_ARM_HEIGHT = MIN_HEIGHT*4/7;
    private static final int BOTTOM_NOTCH_HEIGHT = MIN_HEIGHT*5/7;
    private static final int LEFT_TEXT_PADDING = 50;
    private static final int RIGHT_TEXT_PADDING = 10;
    private static final int TOP_AND_BOTTOM_TEXT_PADDING = 1;
}
