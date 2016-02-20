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

package com.horstmann.violet.product.diagram.communication.nodes;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.framework.property.string.LineText;
import com.horstmann.violet.framework.property.string.SingleLineText;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


import com.horstmann.violet.framework.graphics.content.VerticalLayout;

/**
 * An actor node_old in a use case diagram.
 */
public class ActorNode extends ColorableNode
{
    protected static class ActorShape implements ContentInsideCustomShape.ShapeCreator
    {
        public ActorShape(TextContent nameContent)
        {
            super();
            this.nameContent = nameContent;
        }

        @Override
        public Shape createShape(double contentWidth, double contentHeight) {
            GeneralPath path = new GeneralPath();
            double neckX = nameContent.getWidth() / 2;
            double neckY = HEAD_SIZE + GAP_ABOVE;
            // head
            path.moveTo(neckX, neckY);
            path.quadTo(neckX + HEAD_SIZE / 2, neckY, neckX + HEAD_SIZE / 2, neckY - HEAD_SIZE / 2);
            path.quadTo(neckX + HEAD_SIZE / 2, neckY - HEAD_SIZE, neckX, neckY - HEAD_SIZE);
            path.quadTo(neckX - HEAD_SIZE / 2, neckY - HEAD_SIZE, neckX - HEAD_SIZE / 2, neckY - HEAD_SIZE / 2);
            path.quadTo(neckX - HEAD_SIZE / 2, neckY, neckX, neckY);
            // body
            double hipX = neckX;
            double hipY = neckY + BODY_SIZE;
            path.lineTo(hipX, hipY);
            // arms
            path.moveTo(neckX - ARMS_SIZE / 2, neckY + BODY_SIZE / 3);
            path.lineTo(neckX + ARMS_SIZE / 2, neckY + BODY_SIZE / 3);
            // legs
            double dx = (LEG_SIZE / Math.sqrt(2));
            double feetX1 = hipX - dx + 5;
            double feetX2 = hipX + dx - 5;
            double feetY = hipY + dx + 1;
            path.moveTo(hipX, hipY);
            path.lineTo(feetX1, feetY);
            path.moveTo(hipX, hipY);
            path.lineTo(feetX2, feetY);
            return path;
        }

        private TextContent nameContent;
    }

    /**
     * Construct an actor node_old with a default size and name
     */
    public ActorNode()
    {
        super();
        name = new SingleLineText();
        name.setPadding(10,5,5,5);
        name.setAlignment(LineText.CENTER);
//        name.setText("Actor");
        createContentStructure();
    }

    protected ActorNode(ActorNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        createContentStructure();
    }

    @Override
    public void deserializeSupport()
    {
        name.deserializeSupport();

        super.deserializeSupport();
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new ActorNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        EmptyContent emptyContent = new EmptyContent();
        TextContent nameContent = new TextContent(name);

        emptyContent.setMinWidth(DEFAULT_WIDTH);
        nameContent.setMinWidth(DEFAULT_WIDTH);

        ContentInsideShape stickPersonContent = new ContentInsideCustomShape(emptyContent, new ActorShape(nameContent)){
            @Override
            protected Point2D getShapeOffset()
            {
                Rectangle2D shapeBounds = getShape().getBounds();
                return new Point2D.Double(
                        (shapeBounds.getWidth() - getContent().getWidth()) / 2,
                        (shapeBounds.getHeight())
                );
            }
        };

        setBorder(new ContentBorder(stickPersonContent, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), null));

        VerticalLayout verticalGroupContent = new VerticalLayout();
        verticalGroupContent.add(getBackground());
        verticalGroupContent.add(nameContent);

        setContent(new ContentInsideRectangle(verticalGroupContent));

        setTextColor(super.getTextColor());
    }

    @Override
    public void setTextColor(Color textColor)
    {
        name.setTextColor(textColor);
        super.setTextColor(textColor);
    }


    /**
     * Sets the name property value.
     *
     * @param newValue the new actor name
     */
    public void setName(SingleLineText newValue)
    {
        name.setText(newValue.toEdit());
//        getContent().refresh();
    }

    /**
     * Gets the name property value.
     */
    public SingleLineText getName()
    {
        return name;
    }


    /** Actor name */
    private SingleLineText name;
    /** Bounding rectangle width */
    private static int DEFAULT_WIDTH = 48;
    /** Bounding rectangle height */
    private static int DEFAULT_HEIGHT = 64;

    /** Stick man : neck size */
    private static int GAP_ABOVE = 4;
    /** Stick man : head size */
    private static int HEAD_SIZE = DEFAULT_WIDTH * 4 / 12;
    /** Stick man : body size */
    private static int BODY_SIZE = DEFAULT_WIDTH * 5 / 12;
    /** Stick man : leg size - Note : Height = HEAD_SIZE + BODY_SIZE + LEG_SIZE/sqrt(2) */
    private static int LEG_SIZE = DEFAULT_WIDTH * 5 / 12;
    /** Stick man : arm size */
    private static int ARMS_SIZE = DEFAULT_WIDTH * 6 / 12;
}
