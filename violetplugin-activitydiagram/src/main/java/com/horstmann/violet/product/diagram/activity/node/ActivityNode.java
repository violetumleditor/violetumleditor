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

import java.awt.Color;

import com.horstmann.violet.framework.graphics.content.ContentBackground;
import com.horstmann.violet.framework.graphics.content.ContentBorder;
import com.horstmann.violet.framework.graphics.content.ContentInsideShape;
import com.horstmann.violet.framework.graphics.content.RelativeLayout;
import com.horstmann.violet.framework.graphics.content.TextContent;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRoundRectangle;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.product.diagram.abstracts.node.IResizableNode;
import com.horstmann.violet.product.diagram.activity.ActivityDiagramConstant;
import com.horstmann.violet.product.diagram.property.text.MultiLineText;

public class ActivityNode extends AbstractNode implements IResizableNode
{
    public ActivityNode()
    {
        super();
        name = new MultiLineText();
        relativeGroupContent = new RelativeLayout();
        createContentStructure();
    }

    protected ActivityNode(ActivityNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        relativeGroupContent = new RelativeLayout();
        createContentStructure();
    }

    @Override
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();

        relativeGroupContent.setMinHeight(Math.max(MIN_HEIGHT, getPreferredSize().getHeight()));
        relativeGroupContent.setMinWidth(Math.max(MIN_WIDTH, getPreferredSize().getWidth()));
        if(null == name)
        {
            name = new MultiLineText();
        }
        name.reconstruction();
        
    }

    @Override
    protected ActivityNode copy() throws CloneNotSupportedException
    {
        return new ActivityNode(this);
    }

    @Override
    protected void createContentStructure()
    {
    	
    	TextContent nameContent = new TextContent(name);
    	nameContent.setMinHeight(MIN_HEIGHT);
    	nameContent.setMinWidth(MIN_WIDTH);
    	
    	relativeGroupContent.clear();
    	relativeGroupContent.add(nameContent);

        ContentInsideShape contentInsideShape = new ContentInsideRoundRectangle(relativeGroupContent, ARC_SIZE);

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
        return ActivityDiagramConstant.ACTIVITY_DIAGRAM_RESOURCE.getString("tooltip.activity_node");
    }

    public void setName(MultiLineText newValue)
    {
        name.setText(newValue.toEdit());
    }

    public MultiLineText getName()
    {
        return name;
    }
    

    private MultiLineText name;
    private RelativeLayout relativeGroupContent;
    

    private static final int ARC_SIZE = 20;
    private static final int MIN_WIDTH = 60;
    private static final int MIN_HEIGHT = 40;
}
