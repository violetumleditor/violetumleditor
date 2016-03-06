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
import java.awt.geom.Rectangle2D;
import java.util.List;

import com.horstmann.violet.framework.graphics.Separator;
import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.content.HorizontalLayout;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.common.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.object.ObjectDiagramConstant;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;
import com.horstmann.violet.product.diagram.common.edge.BasePropertyEdge;
import com.horstmann.violet.product.diagram.object.edge.ObjectReferenceEdge;

/**
 * A field node_old in an object diagram.
 */
public class FieldNode extends ColorableNode
{
    /**
     * Default constructor
     */
    public FieldNode()
    {
        super();

        name = new SingleLineText();
        name.setAlignment(LineText.RIGHT);
        name.setPadding(0, 10, 0, 15);
        value = new SingleLineText();
        value.setAlignment(LineText.LEFT);
        value.setPadding(0, 15, 0, 10);
        createContentStructure();
    }

    protected FieldNode(FieldNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        value = node.value.clone();
        createContentStructure();
    }

    @Override
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();

        name.reconstruction();
        value.reconstruction();
        name.setPadding(0, 10, 0, 15);
        value.setPadding(0, 15, 0, 10);
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new FieldNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        TextContent nameContent = new TextContent(name);
        nameContent.setMinHeight(DEFAULT_HEIGHT);
        nameContent.setMinWidth(DEFAULT_WIDTH/2);
        TextContent valueContent = new TextContent(value);
        valueContent.setMinHeight(DEFAULT_HEIGHT);
        valueContent.setMinWidth(DEFAULT_WIDTH/2);

        HorizontalLayout horizontalGroupContent = new HorizontalLayout();
        horizontalGroupContent.add(nameContent);
        horizontalGroupContent.add(valueContent);
        horizontalGroupContent.setSeparator(EQUAL_SEPARATOR);

        ContentInsideShape contentInsideShape = new ContentInsideRectangle(horizontalGroupContent);

        setBorder(new ContentBorder(contentInsideShape, null));
        setBackground(new ContentBackground(contentInsideShape, getBackgroundColor()));

        RelativeLayout relativeLayout =new RelativeLayout();
        relativeLayout.add(getBackground(), new Point2D.Double(1,0));

        setContent(new ContentInsideRectangle(relativeLayout));
    }

    @Override
    public Rectangle2D getBounds()
    {
        Point2D location = getLocationOnGraph();
        Rectangle2D contentBounds = getContent().getBounds();
        return new Rectangle2D.Double(location.getX(), location.getY(), contentBounds.getWidth(), contentBounds.getHeight());
    }

    @Override
    public void setTextColor(Color textColor)
    {
        name.setTextColor(textColor);
        value.setTextColor(textColor);
        super.setTextColor(textColor);
    }

    @Override
    public String getToolTip()
    {
        return ObjectDiagramConstant.OBJECT_DIAGRAM_RESOURCE.getString("field_node.tooltip");
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
        Point2D location = ((ObjectNode)parent).getFieldsGroup().getLocation(getContent());

        return new Point2D.Double(location.getX(), location.getY()+((ObjectNode)parent).getFieldsTopOffset());
    }

    @Override
    public boolean addConnection(IEdge e)
    {
        INode endingINode = e.getEndNode();
        if (e.getClass().isAssignableFrom(ObjectReferenceEdge.class) && endingINode.getClass().isAssignableFrom(ObjectNode.class))
        {
            value.setText( "" );
            return true;
        }
        // Hack to allow drawing relationship edge over fields
        if (e.getClass().isAssignableFrom(BasePropertyEdge.class))
        {
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

    /**
     * Sets the name property value.
     * 
     * @param newValue the field name
     */
    public void setName(SingleLineText newValue)
    {
        name.setText(newValue.toEdit());
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
        if(0==getConnectedEdges().size())
        {
            value.setText(newValue.toEdit());
        }
    }

    /**
     * Gets the value property value.
     * 
     * @return the field value
     */
    public SingleLineText getValue()
    {
        if(0<getConnectedEdges().size())
        {
            return new SingleLineText();
        }
        return value;
    }

    private SingleLineText name;
    private SingleLineText value;

    private static final int DEFAULT_WIDTH = 80;
    private static final int DEFAULT_HEIGHT = 20;
    private static final int XGAP = 5;
    private static final int YGAP = 5;

    private static final Separator EQUAL_SEPARATOR = new Separator()
    {
        @Override
        public void draw(Graphics2D graphics, Point2D startPoint, Point2D endPoint)
        {
            graphics.drawLine((int)startPoint.getX()-3, (int)startPoint.getY() + DEFAULT_HEIGHT/2 -1, (int)startPoint.getX()+3, (int)startPoint.getY() + DEFAULT_HEIGHT/2 -1);
            graphics.drawLine((int)startPoint.getX()-3, (int)startPoint.getY() + DEFAULT_HEIGHT/2 +2, (int)startPoint.getX()+3, (int)startPoint.getY() + DEFAULT_HEIGHT/2 +2);
        }
    };
}
