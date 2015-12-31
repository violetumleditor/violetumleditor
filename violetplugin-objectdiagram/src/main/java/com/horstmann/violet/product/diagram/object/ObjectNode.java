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
import com.horstmann.violet.product.diagram.abstracts.property.string.LineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.LargeSizeDecorator;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.OneLineString;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.UnderlineDecorator;
import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.RectangularNode;
import com.horstmann.violet.product.diagram.abstracts.property.string.SingleLineText;
import com.horstmann.violet.product.diagram.common.edge.BasePropertyEdge;

/**
 * An object node_old in an object diagram.
 */
public class ObjectNode extends RectangularNode
{
    /**
     * Construct an object node_old with a default size
     */
    public ObjectNode()
    {
        name = new SingleLineText(new LineText.Converter(){
            @Override
            public OneLineString toLineString(String text)
            {
                return new LargeSizeDecorator(new UnderlineDecorator(new OneLineString(text)));
            }
        });

        createContentStructure();
    }

    protected void createContentStructure()
    {
        TextContent nameContent = new TextContent(name);
        nameContent.setMinHeight(DEFAULT_HEIGHT);
        nameContent.setMinWidth(DEFAULT_WIDTH);

        fieldsGroup = new VerticalGroupContent();
        nameContent.setMinWidth(DEFAULT_WIDTH);

        VerticalGroupContent verticalGroupContent = new VerticalGroupContent();
        verticalGroupContent.add(nameContent);
        verticalGroupContent.add(fieldsGroup);
        verticalGroupContent.setSeparator(new Separator.LineSeparator(getBorderColor()));

        ContentInsideShape contentInsideShape = new ContentInsideRectangle(verticalGroupContent);

        border = new ContentBorder(contentInsideShape, getBorderColor());
        background = new ContentBackground(border, getBackgroundColor());

        content = background;
    }

    @Override
    public Rectangle2D getBounds()
    {
        Point2D location = getLocationOnGraph();
        Rectangle2D contentBounds = content.getBounds();
        return new Rectangle2D.Double(location.getX(), location.getY(), contentBounds.getWidth(), contentBounds.getHeight());
    }

    public void draw(Graphics2D g2)
    {
        content.draw(g2, getLocationOnGraph());
    }

    /**
     * Returns the rectangle at the top of the object node_old.
     * 
     * @return the top rectangle
     */
    public Rectangle2D getTopRectangle()
    {
        Rectangle2D b = name.getBounds();
        double defaultHeight = DEFAULT_HEIGHT;
        boolean hasChildren = (getChildren().size() > 0);
        if (hasChildren)
        {
            defaultHeight = defaultHeight - YGAP;
        }
        Point2D currentLocation = getLocation();
        double x = currentLocation.getX();
        double y = currentLocation.getY();
        double w = Math.max(b.getWidth(), DEFAULT_WIDTH);
        double h = Math.max(b.getHeight(), DEFAULT_HEIGHT);
        Rectangle2D topBounds = new Rectangle2D.Double(x, y, w, h);
        topBounds = getGraph().getGridSticker().snap(topBounds);
        return topBounds;
    }

    public boolean addConnection(IEdge e)
    {
        if (!e.getClass().isAssignableFrom(BasePropertyEdge.class))
        {
            return false;
        }
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
        return true;
    }

    public Point2D getConnectionPoint(Direction d)
    {
        Rectangle2D topBounds = getTopRectangle();
        double topHeight = topBounds.getHeight();
        if (d.getX() > 0)
        {
            return new Point2D.Double(getBounds().getMaxX(), getBounds().getMinY() + topHeight / 2);
        }
        return new Point2D.Double(getBounds().getX(), getBounds().getMinY() + topHeight / 2);
    }

    /**
     * Sets the name property value.
     * 
     * @param newValue the new object name
     */
    public void setName(SingleLineText n)
    {
        name.setText(n.getText());
    }

    /**
     * Gets the name property value.
     * 
     * @param the object name
     */
    public SingleLineText getName()
    {
        return name;
    }

    @Override
    public boolean addChild(INode n, Point2D p)
    {
        List<INode> fields = getChildren();
        if (!(n instanceof FieldNode)) return false;
        if (fields.contains(n)) return true;

        int i = 0;
        while (i < fields.size() && fields.get(i).getLocation().getY() < p.getY())
            i++;
        addChild(n, i);
        n.setGraph(getGraph());
        n.setParent(this);


        fieldsGroup.add(((FieldNode) n).getContent());


        return true;
    }

    public ObjectNode clone()
    {
        ObjectNode cloned = (ObjectNode) super.clone();
        cloned.name = name.clone();
        cloned.createContentStructure();
        return cloned;
    }


    public int getFieldsTopOffset() {
        return DEFAULT_HEIGHT;
    }
    public VerticalGroupContent getFieldsGroup() {
        return fieldsGroup;
    }

    private Content content = null;
    private ContentBackground background = null;
    private ContentBorder border = null;
    private VerticalGroupContent fieldsGroup = null;

    private SingleLineText name;

    private final static int DEFAULT_WIDTH = 80;
    private final static int DEFAULT_HEIGHT = 30;
    private final static int YGAP = 5;
}
