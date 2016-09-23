package com.horstmann.violet.product.diagram.activity.node;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.common.node.ColorableNode;
import com.horstmann.violet.product.diagram.activity.ActivityDiagramConstant;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * An receive event node in an activity diagram.
 */
public class WaitTimeActionNode extends ColorableNode
{
    /**
     * Construct an receive event node with a default size
     */
    public WaitTimeActionNode()
    {
        super();
        name = new SingleLineText();
        name.setPadding(5,5,5,5);
        createContentStructure();
    }

    protected WaitTimeActionNode(WaitTimeActionNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
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
        name.reconstruction();
        name.setPadding(5,5,5,5);
    }

    @Override
    protected WaitTimeActionNode copy() throws CloneNotSupportedException
    {
        return new WaitTimeActionNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        TextContent nameContent = new TextContent(name);

        ContentInsideCustomShape shape = new ContentInsideCustomShape(null, WAIT_SHAPE);
        shape.setMinWidth(SHAPE_WIDTH);
        shape.setMinHeight(SHAPE_HEIGHT);

        setBorder(new ContentBorder(shape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));

        CenterContent centeredShape = new CenterContent(getBackground());

        VerticalLayout verticalGroupContent = new VerticalLayout();
        verticalGroupContent.add(centeredShape);
        verticalGroupContent.add(nameContent);

        setContent(verticalGroupContent);

        setTextColor(super.getTextColor());
    }

    @Override
    public String getToolTip()
    {
        return ActivityDiagramConstant.ACTIVITY_DIAGRAM_RESOURCE.getString("tooltip.wait_time_action_node");
    }

    @Override
    public Point2D getConnectionPoint(IEdge edge)
    {
        Direction direction = edge.getDirection(this).getNearestCardinalDirection();
        Rectangle2D selfBounds = getBounds();
        Point2D point = super.getConnectionPoint(edge);

        if(!name.toEdit().isEmpty() && Direction.NORTH.equals(direction))
        {
            return new Point2D.Double(
                    point.getX(),
                    selfBounds.getMaxY()
            );
        }

        if(Direction.NORTH.equals(direction) || Direction.SOUTH.equals(direction))
        {
            return new Point2D.Double((SHAPE_WIDTH * (point.getX()-selfBounds.getX()))/selfBounds.getWidth() +
                    (selfBounds.getCenterX() - SHAPE_WIDTH/2),point.getY());
        }

        return new Point2D.Double(selfBounds.getCenterX() +
                ((SHAPE_WIDTH * (point.getX() - selfBounds.getCenterX())/ selfBounds.getWidth())),
                selfBounds.getY() + SHAPE_HEIGHT * (point.getY() - selfBounds.getY()) / selfBounds.getHeight()
        );
    }

    @Override
    public boolean addConnection(IEdge edge)
    {
        if (edge.getEndNode() != null && this != edge.getEndNode())
        {
            return true;
        }
        return false;
    }

    /**
     * Sets the name property value.
     *
     * @param newValue the new name description
     */
    public void setName(LineText newValue)
    {
        name.setText(newValue.toEdit());
    }

    /**
     * Gets the name property value.
     */
    public LineText getName()
    {
        return name;
    }

    private SingleLineText name;

    private static final int SHAPE_WIDTH = 40;
    private static final int SHAPE_HEIGHT = 40;

    private static final ContentInsideCustomShape.ShapeCreator WAIT_SHAPE = new ContentInsideCustomShape.ShapeCreator()
    {
        @Override
        public Shape createShape(double contentWidth, double contentHeight) {
            GeneralPath path = new GeneralPath();

            path.moveTo(0, 0);
            path.lineTo(SHAPE_WIDTH, 0);
            path.lineTo(0, SHAPE_HEIGHT);
            path.lineTo(SHAPE_WIDTH, SHAPE_HEIGHT);
            path.closePath();
            return path;
        }
    };
}
