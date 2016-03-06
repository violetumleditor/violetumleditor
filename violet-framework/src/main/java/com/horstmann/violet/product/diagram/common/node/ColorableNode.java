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

package com.horstmann.violet.product.diagram.common.node;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Point2D;

import com.horstmann.violet.framework.graphics.content.ContentBackground;
import com.horstmann.violet.framework.graphics.content.ContentBorder;
import com.horstmann.violet.framework.graphics.content.EmptyContent;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRoundRectangle;
import com.horstmann.violet.product.diagram.abstracts.IColorable;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.sidebar.colortools.ColorToolsBarPanel;

/**
 * A node_old that has a rectangular shape.
 */
public abstract class ColorableNode extends AbstractNode implements IColorable
{
    public ColorableNode()
    {
        super();
    }

    protected ColorableNode(ColorableNode node) throws CloneNotSupportedException
    {
        super(node);
    }

    @Override
//  protected abstract void createContentStructure();
    protected void createContentStructure()
    {
        setBorder(new ContentBorder(new ContentInsideRoundRectangle(new EmptyContent()), getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());
    }

    @Override
    public boolean contains(Point2D p)
    {
        if(null != getBackground())
        {
            return getBackground().contains(p);
        }
        return getContent().contains(p);
    }
    
    @Override
    public boolean addConnection(IEdge e) {
    	// Self call (loop)
    	INode endingNode = e.getEndNode();
    	if (endingNode == null) {
    		e.setEndNode(e.getStartNode());
    		e.setEndLocation(e.getStartLocation());
    	}
    	// Back to default behavior
    	return super.addConnection(e);
    }

    @Override
    public Shape getShape()
    {
        return getBounds();
    }

    protected final void setBackground(ContentBackground background)
    {
        this.background = background;
    }
    protected final ContentBackground getBackground()
    {
        if(null == background)
        {
            getContent();
        }
        return background;
    }

    protected final void setBorder(ContentBorder border)
    {
        this.border = border;
    }
    protected final ContentBorder getBorder()
    {
        if(null == border)
        {
            getContent();
        }
        return border;
    }



    @Override
    public void setBackgroundColor(Color bgColor)
    {
        backgroundColor = bgColor;
        if(null != background)
        {
            background.setBackgroundColor(bgColor);
        }
    }

    @Override
    public final Color getBackgroundColor()
    {
        if(null == backgroundColor)
        {
            return ColorToolsBarPanel.DEFAULT_COLOR.getBackgroundColor();
        }
        return backgroundColor;
    }

    @Override
    public void setBorderColor(Color borderColor)
    {
        this.borderColor = borderColor;
        if(null != border)
        {
            border.setBorderColor(borderColor);
        }
    }

    @Override
    public final Color getBorderColor()
    {
        if(null == borderColor)
        {
            return ColorToolsBarPanel.DEFAULT_COLOR.getBorderColor();
        }
        return borderColor;
    }

    @Override
    public void setTextColor(Color textColor)
    {
        this.textColor = textColor;
    }

    @Override
    public final Color getTextColor()
    {
        if(null == textColor)
        {
            return ColorToolsBarPanel.DEFAULT_COLOR.getTextColor();
        }
        return textColor;
    }

    private transient ContentBackground background = null;
    private transient ContentBorder border = null;

    private Color backgroundColor;
    private Color borderColor;
    private Color textColor;
}
