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

package com.horstmann.violet.product.diagram.object.node;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.List;

import com.horstmann.violet.framework.graphics.Separator;
import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.content.VerticalLayout;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.common.node.ColorableNode;
import com.horstmann.violet.product.diagram.object.ObjectDiagramConstant;
import com.horstmann.violet.product.diagram.object.edge.AssociationEdge;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.decorator.*;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;

public class ObjectNode extends ColorableNode
{
    public ObjectNode()
    {
        super();
        name = new SingleLineText(nameConverter);
        type = new SingleLineText(typeConverter);
        name.setPadding(5, 10, 5, 2);
        type.setPadding(5, 2, 5, 10);

        createContentStructure();
    }

    protected ObjectNode(ObjectNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        type = node.type.clone();
        createContentStructure();
    }

    @Override
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();
        if(null == name)
        {
            name = new SingleLineText();
        }
        if(null == type)
        {
            type = new SingleLineText();
        }
        name.reconstruction(nameConverter);
        type.reconstruction(typeConverter);

        name.setPadding(5, 10, 5, 2);
        type.setPadding(5, 2, 5, 10);
    }

    @Override
    protected void afterReconstruction()
    {
        List<INode> children = getChildren();
        Collections.reverse(children);

        for(INode child : children)
        {
            if (child instanceof FieldNode)
            {
                fieldsGroup.add(((ColorableNode) child).getContent());
            }
        }
        super.afterReconstruction();
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new ObjectNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        TextContent nameContent = new TextContent(name);
        nameContent.setMinHeight(DEFAULT_HEIGHT);
        TextContent typeContent = new TextContent(type);
        typeContent.setMinHeight(DEFAULT_HEIGHT);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(nameContent);
        horizontalLayout.add(typeContent);
        horizontalLayout.setMinWidth(DEFAULT_WIDTH);

        separator = new Separator.LineSeparator(getBorderColor());
        fieldsGroup = new VerticalLayout();

        VerticalLayout verticalGroupContent = new VerticalLayout();
        verticalGroupContent.add(horizontalLayout);
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
        type.setTextColor(textColor);
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

    @Override
    public String getToolTip()
    {
        return ObjectDiagramConstant.OBJECT_DIAGRAM_RESOURCE.getString("tooltip.object_node");
    }

    public boolean addConnection(IEdge e)
    {
        if (!e.getClass().isAssignableFrom(AssociationEdge.class))
        {
            return false;
        }
        INode startingNode = e.getStartNode();
        INode endingNode = e.getEndNode();
        if (startingNode.getClass().isAssignableFrom(FieldNode.class))
        {
            startingNode = startingNode.getParent();
        }
        if (endingNode.getClass().isAssignableFrom(FieldNode.class))
        {
            endingNode = endingNode.getParent();
        }
        e.setStartNode(startingNode);
        e.setEndNode(endingNode);
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

    public void setType(SingleLineText n)
    {
        type.setText(n.toEdit());
    }

    public SingleLineText getType()
    {
        return type;
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
    public VerticalLayout getFieldsGroup() {
        return fieldsGroup;
    }

    private SingleLineText name;
    private SingleLineText type;

    private transient VerticalLayout fieldsGroup = null;
    private transient Separator separator = null;

    private static final int DEFAULT_WIDTH = 80;
    private static final int DEFAULT_HEIGHT = 40;
    private static final int YGAP = 5;

    private static final LineText.Converter nameConverter = new LineText.Converter(){
        @Override
        public OneLineText toLineString(String text)
        {
            return new LargeSizeDecorator(new UnderlineDecorator(new OneLineText(text)));
        }
    };

    private static final LineText.Converter typeConverter = new LineText.Converter(){
        @Override
        public OneLineText toLineString(String text)
        {
            return new LargeSizeDecorator(new UnderlineDecorator(new RemoveSentenceDecorator(new PrefixDecorator(new OneLineText(text), ":"), " ")));
        }
    };
}
