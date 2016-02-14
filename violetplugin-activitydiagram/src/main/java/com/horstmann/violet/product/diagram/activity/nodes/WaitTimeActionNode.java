package com.horstmann.violet.product.diagram.activity.nodes;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.property.string.SingleLineText;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * An receive event node in an activity diagram.
 */
public class WaitTimeActionNode  extends ColorableNode
{
    protected static class WaitTimeShape implements ContentInsideCustomShape.ShapeCreator
    {
        public WaitTimeShape(TextContent nameContent)
        {
            super();
            this.nameContent = nameContent;
        }

        @Override
        public Shape createShape(double contentWidth, double contentHeight) {
            GeneralPath path = new GeneralPath();

            double startX = (nameContent.getWidth() - DEFAULT_WIDTH) /2;

            path.moveTo(startX, 0);
            path.lineTo(startX + DEFAULT_WIDTH, 0);
            path.lineTo(startX, DEFAULT_HEIGHT);
            path.lineTo(startX + DEFAULT_WIDTH, DEFAULT_HEIGHT);
            path.closePath();
            return path;
        }

        private TextContent nameContent;
    }

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
        super.deserializeSupport();
        waitTimeAction.deserializeSupport();
        waitTimeAction.setPadding(5,5,5,5);
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new WaitTimeActionNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        EmptyContent emptyContent = new EmptyContent();
        TextContent nameContent = new TextContent(waitTimeAction);

        emptyContent.setMinWidth(DEFAULT_WIDTH);
        nameContent.setMinWidth(DEFAULT_WIDTH);

        ContentInsideShape stickPersonContent = new ContentInsideCustomShape(emptyContent, new WaitTimeShape(nameContent)){
            @Override
            protected Point2D getShapeOffset()
            {
                Rectangle2D shapeBounds = getShape().getBounds();
                return new Point2D.Double(
                        (shapeBounds.getWidth() - getContent().getWidth()) / 2,
                        (shapeBounds.getHeight())
                );
            }
        };

        setBorder(new ContentBorder(stickPersonContent, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));

        VerticalLayout verticalGroupContent = new VerticalLayout();
        verticalGroupContent.add(getBackground());
        verticalGroupContent.add(nameContent);

        setContent(new ContentInsideRectangle(verticalGroupContent));

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


    private static final int DEFAULT_WIDTH = 40;
    private static final int DEFAULT_HEIGHT = 40;
    private static final int EDGE_WIDTH = 20;

    private SingleLineText waitTimeAction;
}
