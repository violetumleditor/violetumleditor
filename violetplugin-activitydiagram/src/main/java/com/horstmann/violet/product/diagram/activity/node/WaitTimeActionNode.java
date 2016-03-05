package com.horstmann.violet.product.diagram.activity.node;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.activity.ActivityDiagramConstant;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;

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
        return ActivityDiagramConstant.ACTIVITY_DIAGRAM_RESOURCE.getString("wait_time_action_node.tooltip");
    }

    @Override
    public boolean addConnection(IEdge e)
    {
        if (e.getEndNode() != null && this != e.getEndNode())
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
    public void setName(SingleLineText newValue)
    {
        name.setText(newValue.toEdit());
    }

    /**
     * Gets the name property value.
     */
    public SingleLineText getName()
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
