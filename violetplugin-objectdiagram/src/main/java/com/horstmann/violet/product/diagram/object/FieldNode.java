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

package com.horstmann.violet.product.diagram.object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import com.horstmann.violet.framework.graphics.Separator;
import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.RectangularNode;
import com.horstmann.violet.product.diagram.abstracts.property.string.LineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.MultiLineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.SingleLineText;
import com.horstmann.violet.product.diagram.common.edge.BasePropertyEdge;

/**
 * A field node_old in an object diagram.
 */
public class FieldNode extends RectangularNode
{
    protected static Separator equalSeparator = new Separator()
    {
        @Override
        public void draw(Graphics2D g2, Point2D startPoint, Point2D endPoint)
        {
            g2.drawLine((int)startPoint.getX()-3, (int)startPoint.getY() + DEFAULT_HEIGHT/2 -1, (int)startPoint.getX()+3, (int)startPoint.getY() + DEFAULT_HEIGHT/2 -1);
            g2.drawLine((int)startPoint.getX()-3, (int)startPoint.getY() + DEFAULT_HEIGHT/2 +2, (int)startPoint.getX()+3, (int)startPoint.getY() + DEFAULT_HEIGHT/2 +2);
        }
    };

    /**
     * Default constructor
     */
    public FieldNode()
    {
//        setZ(1);

        name = new SingleLineText();
        name.setAlignment(LineText.RIGHT);
        name.setPadding(0, 10, 0, 20);
        value = new SingleLineText();
        name.setAlignment(LineText.LEFT);
        name.setPadding(0, 20, 0, 10);
//        equalSeparator.setText(" = ");

        createContentStructure();
    }

    protected void createContentStructure()
    {
        TextContent nameContent = new TextContent(name);
        nameContent.setMinHeight(DEFAULT_HEIGHT);
        nameContent.setMinWidth(DEFAULT_WIDTH/2);
        TextContent valueContent = new TextContent(value);
        valueContent.setMinHeight(DEFAULT_HEIGHT);
        valueContent.setMinWidth(DEFAULT_WIDTH/2);

        horizontalGroupContent = new HorizontalGroupContent();
        horizontalGroupContent.add(nameContent);
        horizontalGroupContent.add(valueContent);
        horizontalGroupContent.setSeparator(equalSeparator);

        ContentInsideShape contentInsideShape = new ContentInsideRectangle(horizontalGroupContent);

        content = contentInsideShape;
//        border = new ContentBorder(contentInsideShape, getBorderColor());
        background = new ContentBackground(contentInsideShape, getBackgroundColor());
    }

    public Content getContent()
    {
        return content;
    }

    @Override
    public Point2D getLocation()
    {
        INode parent = getParent();
        if (parent == null )
        {
            return new Point2D.Double(0, 0);
        }
        if (!(parent instanceof ObjectNode))
        {
            throw new IllegalStateException("Field node can be only ObjectNode child");
        }
        Point2D location = ((ObjectNode)parent).getFieldsGroup().getLocation(content);

        return new Point2D.Double(location.getX(), location.getY()+((ObjectNode)parent).getFieldsTopOffset());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.INode#draw(java.awt.Graphics2D)
     */
    public void draw(Graphics2D g2)
    {
        background.draw(g2, getLocationOnGraph());
    }

    @Override
    public boolean addConnection(IEdge e)
    {
        INode endingINode = e.getEnd();
        if (e.getClass().isAssignableFrom(ObjectReferenceEdge.class) && endingINode.getClass().isAssignableFrom(ObjectNode.class))
        {
            value.setText("");
            return true;
        }
        // Hack to allow drawing relationship edge over fields
        if (e.getClass().isAssignableFrom(BasePropertyEdge.class))
        {
            INode startingNode = e.getStart();
            INode endingNode = e.getEnd();
            if (startingNode.getClass().isAssignableFrom(FieldNode.class))
            {
                startingNode = startingNode.getParent();
            }
            if (endingNode.getClass().isAssignableFrom(FieldNode.class))
            {
                endingNode = endingNode.getParent();
            }
            e.setStart(startingNode);
            e.setEnd(endingNode);
            return getParent().addConnection(e);
        }
        return false;
    }

    /**
     * Hack to be able to add fields on object when we do a single click on another field READ THIS : due to this hack, when you
     * dble click to edit this field, the first click triggers this methods (which is a correct framework behavior). The workaround
     * for end users is to use right click instead of dble click to edit fields. It is so simple to find it so we accept to deal
     * with this bug.
     */
    @Override
    public boolean addChild(INode n, Point2D p)
    {
        if (!n.getClass().isAssignableFrom(FieldNode.class))
        {
            return false;
        }
        INode parent = getParent();
        List<INode> parentChildren = parent.getChildren();
        int currentPosition = parentChildren.indexOf(this);
        parent.addChild(n, currentPosition + 1);
        return true;
    }

    @Override
    public Point2D getConnectionPoint(IEdge edge)
    {
        Point2D location = getLocation();
        int x = (int)name.getBounds().getWidth();
        if(0==x)
        {
            x = DEFAULT_WIDTH/2;
        }
        return new Point2D.Double(x + 20, location.getY() + DEFAULT_HEIGHT/2);
    }

    @Override
    public Rectangle2D getBounds()
    {
        Point2D location = getLocationOnGraph();
        Rectangle2D contentBounds = content.getBounds();
        return new Rectangle2D.Double(location.getX(), location.getY(), contentBounds.getWidth(), contentBounds.getHeight());
    }

    /**
     * Sets the name property value.
     * 
     * @param newValue the field name
     */
    public void setName(SingleLineText newValue)
    {
        name.setText(newValue.getText());
    }

    /**
     * Gets the name property value.
     * 
     * @return the field name
     */
    public SingleLineText getName()
    {
        return name;
    }

    /**
     * Sets the value property value.
     * 
     * @param newValue the field value
     */
    public void setValue(SingleLineText newValue)
    {
        value.setText(newValue.getText());
    }

    /**
     * Gets the value property value.
     * 
     * @return the field value
     */
    public SingleLineText getValue()
    {
        return value;
    }

//    /**
//     * Gets the x-offset of the axis (the location of the = sign) from the left corner of the bounding rectangle.
//     *
//     * @return the x-offset of the axis
//     */
//    public double getAxisX()
//    {
//        Rectangle2D nameBounds = getNameBounds();
//        double leftWidth = nameBounds.getWidth();
//        double middleWidth = 10;
//        return leftWidth + middleWidth / 2;
//    }

    @Override
    public FieldNode clone()
    {
        FieldNode cloned = (FieldNode) super.clone();
        cloned.name = name.clone();
        cloned.value = value.clone();
        cloned.createContentStructure();
        return cloned;
    }

    private Content content = null;
    private ContentBorder border = null;
    private ContentBackground background = null;
    private HorizontalGroupContent horizontalGroupContent = null;

    private SingleLineText name;
    private SingleLineText value;
//    private SingleLineText equalSeparator;

    private final static int DEFAULT_WIDTH = 80;
    private final static int DEFAULT_HEIGHT = 20;
    private final static int XGAP = 5;
    private final static int YGAP = 5;

    private transient double verticalLocation = 0;
    private transient double horizontalLocation = 0;

}
