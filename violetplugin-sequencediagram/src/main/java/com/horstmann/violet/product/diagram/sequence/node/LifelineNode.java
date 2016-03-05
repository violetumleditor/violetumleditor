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

package com.horstmann.violet.product.diagram.sequence.node;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.property.ArrowheadChoiceList;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.decorator.*;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;
import com.horstmann.violet.product.diagram.sequence.SequenceDiagramConstant;
import com.horstmann.violet.product.diagram.sequence.edge.CallEdge;

/**
 * An object node_old in a scenario diagram.
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 */
public class LifelineNode extends ColorableNode
{
    /**
     * Construct an object node_old with a default size
     */
    public LifelineNode()
    {
        super();

        name = new SingleLineText(nameConverter);
        name.setPadding(5, 10, 5, 0);
        type = new SingleLineText(typeConverter);
        type.setPadding(5, 0, 5, 10);

        createContentStructure();
    }

    protected LifelineNode(LifelineNode node) throws CloneNotSupportedException
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
        if(null==name)
        {
            name = new SingleLineText();
        }
        if(null==type)
        {
            type = new SingleLineText();
        }

        name.reconstruction(nameConverter);
        type.reconstruction(typeConverter);

        name.setPadding(5, 10, 5, 0);
        type.setPadding(5, 0, 5, 10);
    }

    @Override
    protected void afterReconstruction()
    {
        for(INode child : getChildren())
        {
            if (child instanceof ActivationBarNode)
            {
                child.reconstruction();
                activationsGroup.add(((ActivationBarNode) child).getContent());
                onChildChangeLocation(child);
            }
        }
        super.afterReconstruction();
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new LifelineNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        TextContent nameContent = new TextContent(name);
        TextContent typeContent = new TextContent(type);
        nameContent.setMinHeight(TOP_HEIGHT);
        typeContent.setMinHeight(TOP_HEIGHT);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(nameContent);
        horizontalLayout.add(typeContent);

        CenterContent centerContent = new CenterContent(horizontalLayout);
        centerContent.setMinWidth(MIN_WIDTH);

        ContentInsideShape contentInsideShape = new ContentInsideRectangle(centerContent);

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));

        activationsGroup = new RelativeLayout();
        activationsGroup.setMinWidth(ActivationBarNode.WIDTH);

        EmptyContent padding = new EmptyContent();
        padding.setMinHeight(ACTIVATIONS_PADDING);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(getBackground());
        verticalLayout.add(padding);
        verticalLayout.add(activationsGroup);
        verticalLayout.add(padding);
        verticalLayout.setMinHeight(MIN_HEIGHT);

        setContent(verticalLayout);

        setTextColor(getTextColor());
        setName(getName());
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
        return SequenceDiagramConstant.SEQUENCE_DIAGRAM_RESOURCE.getString("lifeline_node.tooltip");
    }

    @Override
    public Rectangle2D getBounds()
    {
        Rectangle2D bounds = super.getBounds();
        return new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), getMaxYOverAllLifeLineNodes()-bounds.getY());
    }

    @Override
    public void removeChild(INode node)
    {
        activationsGroup.remove(((ActivationBarNode) node).getContent());
        super.removeChild(node);
    }

    @Override
    public boolean addChild(INode node, Point2D point)
    {
        List<INode> activations = getChildren();
        if (!(node instanceof ActivationBarNode))
        {
            return false;
        }
        if (activations.contains(node))
        {
            return true;
        }
        addChild(node, activations.size());

        ActivationBarNode activationBarNode = (ActivationBarNode) node;
        activationBarNode.setTextColor(getTextColor());
        activationBarNode.setBackgroundColor(getBackgroundColor());
        activationBarNode.setBorderColor(getBorderColor());

        activationsGroup.add(activationBarNode.getContent());

        activationBarNode.setLocation(point);
        activationBarNode.setGraph(getGraph());
        activationBarNode.setParent(this);

        return true;
    }

    /**
     * Ensure that child node_old respects the minimum gap with package borders
     *
     * @param child
     */
    protected void onChildChangeLocation(INode child)
    {
        activationsGroup.setPosition(((AbstractNode) child).getContent(), getChildRelativeLocation(child));
    }

    protected Point2D getChildRelativeLocation(INode node)
    {
        double relativeCenteredX = getRelativeCenteredPositionX();

        Point2D nodeLocation = node.getLocation();

        if(TOP_HEIGHT + ACTIVATIONS_PADDING > nodeLocation.getY() || relativeCenteredX != nodeLocation.getX())
        {
            nodeLocation.setLocation(relativeCenteredX, Math.max(nodeLocation.getY(), TOP_HEIGHT + ACTIVATIONS_PADDING));
            node.setLocation(nodeLocation);
        }

        return new Point2D.Double(nodeLocation.getX(), nodeLocation.getY() - TOP_HEIGHT - ACTIVATIONS_PADDING);
    }

    @Override
    public Point2D getLocation()
    {
        double y = 0;
        for (IEdge edge : getGraph().getAllEdges())
        {
            if (edge instanceof CallEdge)
            {
                if (this == edge.getEndNode())
                {
                    y = edge.getStartNode().getLocationOnGraph().getY() - TOP_HEIGHT /2 + ActivationBarNode.CALL_Y_GAP / 2;
                    break;
                }
            }
        }
        return new Point2D.Double(super.getLocation().getX(), y);
    }

    public void draw(Graphics2D g2)
    {
        Rectangle2D bounds = getBounds();
        Point2D startPoint = new Point2D.Double(bounds.getCenterX(), bounds.getMinY());
        Point2D endPoint  = new Point2D.Double(bounds.getCenterX(), getMaxYOverAllLifeLineNodes());

        Color oldColor = g2.getColor();
        Stroke oldStroke = g2.getStroke();
        g2.setColor(getBorderColor());
        if(!name.toEdit().isEmpty())
        {
            g2.setStroke(new BasicStroke(
                    1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0.0f, new float[]{5.0f, 5.0f}, 0.0f
            ));
        }
        g2.draw(new Line2D.Double(startPoint, endPoint));
        g2.setStroke(oldStroke);
        if(endOfLife)
        {
            ArrowheadChoiceList.X.draw(g2, startPoint, endPoint);
        }
        g2.setColor(oldColor);

        super.draw(g2);
    }

    public boolean contains(Point2D p)
    {
        double maxYOverAllLifeLineNodes = getMaxYOverAllLifeLineNodes();
        Rectangle2D bounds = getBounds();
        if((maxYOverAllLifeLineNodes >= p.getY() &&
                ActivationBarNode.WIDTH /2 >= p.getX() - bounds.getCenterX() &&
                ActivationBarNode.WIDTH /2 >= bounds.getCenterX() - p.getX()) ||
                (bounds.getX() <= p.getX() &&
                        p.getX() <= bounds.getX() + bounds.getWidth()))
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean addConnection(IEdge e)
    {
        return false;
    }

    @Override
    public Point2D getConnectionPoint(IEdge e)
    {
        Point2D locationOnGraph = getLocationOnGraph();
        double x = locationOnGraph.getX();
        if (0 >= e.getDirection(this).getX())
        {
            x += getContent().getWidth();
        }
        return new Point2D.Double(x, locationOnGraph.getY() + TOP_HEIGHT / 2);
    }

    private double getMaxY()
    {
        return getContent().getHeight() + getLocationOnGraph().getY();
    }

    private double getMaxYOverAllLifeLineNodes()
    {
        double maxY = getMaxY();

        for (INode node : getGraph().getAllNodes())
        {
            if (node instanceof LifelineNode)
            {
                maxY = Math.max(maxY, ((LifelineNode) node).getMaxY() + ACTIVATIONS_PADDING);
            }
        }
        return maxY;
    }

    private void centeredActivationsGroup()
    {
        double relativeCenteredX = getRelativeCenteredPositionX();
        for(INode child : getChildren())
        {
            child.setLocation(new Point.Double(
                    relativeCenteredX,
                    child.getLocation().getY()
            ));
        }
    }
    private double getRelativeCenteredPositionX()
    {
        return (getContent().getWidth()- ActivationBarNode.WIDTH)/2;
    }

    /**
     * Sets the name property value.
     *
     * @param newValue the name of this object
     */
    public void setName(SingleLineText newValue)
    {
        name.setText(newValue.toEdit());
        setType(getType());
//        centeredActivationsGroup();
    }

    /**
     * Gets the name property value.
     *
     * @return the name of this object
     */
    public SingleLineText getName()
    {
        return name;
    }

    /**
     * Sets the type property value.
     *
     * @param newValue the type of this object
     */
    public void setType(SingleLineText newValue)
    {
        type.setText(newValue.toEdit());
        centeredActivationsGroup();
    }

    /**
     * Gets the type property value.
     *
     * @return the name of this object
     */
    public SingleLineText getType()
    {
        return type;
    }

    /**
     * Sets the  end of life property value.
     *
     * @param newValue the end of life of this object
     */
    public void setEndOfLife(boolean newValue)
    {
        endOfLife = newValue;
    }

    /**
     * Gets the end of life property value.
     *
     * @return the end of life of this object
     */
    public boolean isEndOfLife()
    {
        return endOfLife;
    }

    private SingleLineText name;
    private SingleLineText type;
    private boolean endOfLife;

    private transient RelativeLayout activationsGroup = null;

    public static final int TOP_HEIGHT = 60;
    private static final int MIN_WIDTH = 100;
    private static final int MIN_HEIGHT = 100;
    private static final int ACTIVATIONS_PADDING = 15;

    private static final LineText.Converter nameConverter = new LineText.Converter(){
        @Override
        public OneLineText toLineString(String text)
        {
            return new LargeSizeDecorator(new OneLineText(text));
        }
    };

    private static final LineText.Converter typeConverter = new LineText.Converter(){
        @Override
        public OneLineText toLineString(String text)
        {
            return new LargeSizeDecorator(new RemoveSentenceDecorator(new PrefixDecorator(new OneLineText(text), ":"), " "));
        }
    };
}
