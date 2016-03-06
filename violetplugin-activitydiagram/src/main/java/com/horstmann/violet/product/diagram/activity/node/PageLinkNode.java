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

import com.horstmann.violet.framework.graphics.content.ContentBackground;
import com.horstmann.violet.framework.graphics.content.ContentBorder;
import com.horstmann.violet.framework.graphics.content.ContentInsideShape;
import com.horstmann.violet.framework.graphics.content.TextContent;
import com.horstmann.violet.framework.graphics.shape.ContentInsideEllipse;
import com.horstmann.violet.product.diagram.common.node.ColorableNode;
import com.horstmann.violet.product.diagram.activity.ActivityDiagramConstant;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;

import java.awt.*;

/**
 * An activity node in an activity diagram.
 */
public class PageLinkNode extends ColorableNode
{
    /**
     * Construct an action node with a default size
     */
    public PageLinkNode()
    {
        super();
        name = new SingleLineText();
        createContentStructure();
    }

    protected PageLinkNode(PageLinkNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        createContentStructure();
    }

    @Override
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();

        name.reconstruction();
    }

    @Override
    protected PageLinkNode copy() throws CloneNotSupportedException
    {
        return new PageLinkNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        TextContent nameContent = new TextContent(name);
        nameContent.setMinHeight(MIN_HEIGHT);
        nameContent.setMinWidth(MIN_WIDTH);

        ContentInsideShape contentInsideShape = new ContentInsideEllipse(nameContent, MIN_WIDTH, MIN_HEIGHT);

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());

        setTextColor(super.getTextColor());
    }

    @Override
    public void setTextColor(Color textColor)
    {
        name.setTextColor(textColor);
        super.setTextColor(textColor);
    }

    @Override
    public String getToolTip()
    {
        return ActivityDiagramConstant.ACTIVITY_DIAGRAM_RESOURCE.getString("page_connector_node.tooltip");
    }

    /**
     * Sets the name property value.
     * 
     * @param newValue the new action name
     */
    public void setName(SingleLineText newValue)
    {
        name.setText(newValue.toEdit());
    }

    /**
     * Gets the name property value.
     */
    public SingleLineText getName()
    {
        return name;
    }

    private SingleLineText name;

    private static final int MIN_WIDTH = 30;
    private static final int MIN_HEIGHT = 20;
}
