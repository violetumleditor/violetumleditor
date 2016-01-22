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

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

import com.horstmann.violet.framework.graphics.Separator;
import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.content.VerticalGroupContent;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.property.string.LineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.LargeSizeDecorator;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.OneLineString;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.UnderlineDecorator;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.property.string.SingleLineText;
import com.horstmann.violet.product.diagram.common.edge.BasePropertyEdge;

public class ObjectNode extends ColorableNode
{
    public ObjectNode()
    {
        super();

        name = new SingleLineText(nameConverter);
        createContentStructure();
    }

    protected ObjectNode(ObjectNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        createContentStructure();
    }

    @Override
    public void deserializeSupport()
    {
        super.deserializeSupport();
        name.setConverter(nameConverter);
        name.deserializeSupport();

        for(INode child : getChildren())
        {
            if (child instanceof FieldNode)
            {
                fieldsGroup.add(((ColorableNode) child).getContent());
            }
        }
    }

    @Override
    protected INode copy() throws CloneNotSupportedException {
        return new ObjectNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        TextContent nameContent = new TextContent(name);
        nameContent.setMinHeight(DEFAULT_HEIGHT);
        nameContent.setMinWidth(DEFAULT_WIDTH);

        separator = new Separator.LineSeparator(getBorderColor());

        fieldsGroup = new VerticalGroupContent();
        nameContent.setMinWidth(DEFAULT_WIDTH);

        VerticalGroupContent verticalGroupContent = new VerticalGroupContent();
        verticalGroupContent.add(nameContent);
        verticalGroupContent.add(fieldsGroup);
        verticalGroupContent.setSeparator(separator);

        ContentInsideShape contentInsideShape = new ContentInsideRectangle(verticalGroupContent);

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());
    }

    @Override
    public void setTextColor(Color textColor)
    {
        name.setTextColor(textColor);
        super.setTextColor(textColor);
    }

    @Override
    public void setBorderColor(Color borderColor)
    {
        if(null != separator)
        {
            separator.setColor(borderColor);
        }
        super.setBorderColor(borderColor);
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


    public void setName(SingleLineText n)
    {
        name.setText(n.toEdit());
    }

    public SingleLineText getName()
    {
        return name;
    }

    @Override
    public void removeChild(INode node)
    {
        fieldsGroup.remove(((FieldNode) node).getContent());
        super.removeChild(node);
    }

    @Override
    public boolean addChild(INode n, Point2D p)
    {
        List<INode> fields = getChildren();
        if (!(n instanceof FieldNode)) return false;
        if (fields.contains(n)) return true;

        int i = 0;
        while (i < fields.size() && fields.get(i).getLocation().getY() < p.getY())
        {
            i++;
        }
        addChild(n, i);
        n.setGraph(getGraph());
        n.setParent(this);

        FieldNode fieldNode = (FieldNode) n;
        fieldNode.setTextColor(getTextColor());
        fieldNode.setBackgroundColor(getBackgroundColor());
        fieldNode.setBorderColor(getBorderColor());
        fieldsGroup.add(fieldNode.getContent());


        return true;
    }

    public int getFieldsTopOffset() {
        return DEFAULT_HEIGHT;
    }
    public VerticalGroupContent getFieldsGroup() {
        return fieldsGroup;
    }

    private SingleLineText name;

    private transient VerticalGroupContent fieldsGroup = null;
    private transient Separator separator = null;

    private final static int DEFAULT_WIDTH = 80;
    private final static int DEFAULT_HEIGHT = 30;
    private final static int YGAP = 5;

    private static LineText.Converter nameConverter = new LineText.Converter(){
        @Override
        public OneLineString toLineString(String text)
        {
            return new LargeSizeDecorator(new UnderlineDecorator(new OneLineString(text)));
        }
    };
}
