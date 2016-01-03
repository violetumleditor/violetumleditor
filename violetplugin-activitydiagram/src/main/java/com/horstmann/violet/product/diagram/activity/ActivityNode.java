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

package com.horstmann.violet.product.diagram.activity;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import com.horstmann.violet.framework.graphics.Separator;
import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRoundRectangle;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.property.string.LineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.SingleLineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.LargeSizeDecorator;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.OneLineString;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.UnderlineDecorator;

/**
 * An activity node_old in an activity diagram.
 */
public class ActivityNode extends ColorableNode
{
    /**
     * Construct an action node_old with a default size
     */
    public ActivityNode()
    {
        super();

        name = new SingleLineText();
        createContentStructure();
    }

    public ActivityNode(ActivityNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        createContentStructure();
    }

    @Override
    protected INode copy() throws CloneNotSupportedException {
        return new ActivityNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        TextContent nameContent = new TextContent(name);
        nameContent.setMinHeight(DEFAULT_HEIGHT);
        nameContent.setMinWidth(DEFAULT_WIDTH);

        ContentInsideShape contentInsideShape = new ContentInsideRoundRectangle(nameContent, ARC_SIZE);

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());

        setTextColor(super.getTextColor());
    }

    @Override
    public void setTextColor(Color textColor)
    {
        name.setTextColor(textColor);
    }

    @Override
    public Color getTextColor()
    {
        return name.getTextColor();
    }



    /**
     * Sets the name property value.
     * 
     * @param newValue the new action name
     */
    public void setName(SingleLineText newValue)
    {
        name = newValue;
    }

    /**
     * Gets the name property value.
     * 
     * @param the action name
     */
    public SingleLineText getName()
    {
        return name;
    }


    private SingleLineText name;

    private static int ARC_SIZE = 20;
    private static int DEFAULT_WIDTH = 60;
    private static int DEFAULT_HEIGHT = 40;
}
