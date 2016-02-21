package com.horstmann.violet.product.diagram.activity.nodes;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.framework.property.text.SingleLineText;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * An receive event node in an activity diagram.
 */
public class WaitTimeActionNode  extends ColorableNode
{
    /**
     * Construct an receive event node with a default size
     */
    public WaitTimeActionNode()
    {
        super();

        waitTimeAction = new SingleLineText();
        waitTimeAction.setPadding(5,5,5,5);
        createContentStructure();
    }

    protected WaitTimeActionNode(WaitTimeActionNode node) throws CloneNotSupportedException
    {
        super(node);
        waitTimeAction = node.waitTimeAction.clone();
        createContentStructure();
    }

    @Override
    public void deserializeSupport()
    {
        waitTimeAction.deserializeSupport();
        waitTimeAction.setPadding(5,5,5,5);

        super.deserializeSupport();
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new WaitTimeActionNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        TextContent nameContent = new TextContent(waitTimeAction);

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
    public boolean addConnection(IEdge e)
    {
        if (e.getEnd() != null && this != e.getEnd())
        {
            return true;
        }
        return false;
    }

    /**
     * Sets the waitTimeAction property value.
     *
     * @param newValue the new waitTimeAction description
     */
    public void setWaitTimeAction(SingleLineText newValue)
    {
        waitTimeAction.setText(newValue.toEdit());
    }

    /**
     * Gets the waitTimeAction property value.
     */
    public SingleLineText getWaitTimeAction()
    {
        return waitTimeAction;
    }

    private SingleLineText waitTimeAction;

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
