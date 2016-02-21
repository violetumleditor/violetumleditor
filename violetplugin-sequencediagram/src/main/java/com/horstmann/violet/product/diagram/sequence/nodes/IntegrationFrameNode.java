package com.horstmann.violet.product.diagram.sequence.nodes;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.IResizableNode;
import com.horstmann.violet.framework.property.IntegrationFrameType;
import com.horstmann.violet.framework.property.text.LineText;
import com.horstmann.violet.framework.property.text.MultiLineText;
import com.horstmann.violet.framework.property.text.SingleLineText;
import com.horstmann.violet.framework.property.text.decorator.LargeSizeDecorator;
import com.horstmann.violet.framework.property.text.decorator.OneLineText;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * A integration frame node in a UML diagram.
 */
public class IntegrationFrameNode extends ColorableNode implements IResizableNode
{
    public IntegrationFrameNode()
    {
        type = IntegrationFrameType.ALT;
        name = new SingleLineText(nameConverter);
        name.setText(type.getName());
        name.setPadding(0,8,0,18);
        frameContent = new MultiLineText();
        createContentStructure();
    }

    protected IntegrationFrameNode(IntegrationFrameNode node) throws CloneNotSupportedException
    {
        super(node);
        type = node.type;
        name = node.name.clone();
        frameContent = node.frameContent.clone();
        createContentStructure();
    }

    @Override
    public void deserializeSupport()
    {
        frameContent.deserializeSupport();
        name.deserializeSupport(nameConverter);
        name.setPadding(0,8,0,18);

        super.deserializeSupport();
    }


    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new IntegrationFrameNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        wantedSizeContent = new EmptyContent();

        RelativeLayout relativeGroupContent = new RelativeLayout();
        relativeGroupContent.setMinHeight(DEFAULT_HEIGHT);
        relativeGroupContent.setMinWidth(DEFAULT_WIDTH);

        TextContent nameContent = new TextContent(name);
        nameContent.setMinHeight(DEFAULT_TYPE_HEIGHT);
        nameContent.setMinWidth(DEFAULT_TYPE_WIDTH);

        ContentInsideShape nameInsideShape = new ContentInsideCustomShape(nameContent, new ContentInsideCustomShape.ShapeCreator()
        {
            @Override
            public Shape createShape(double contentWidth, double contentHeight)
            {
                GeneralPath path = new GeneralPath();
                path.moveTo(0, 0);
                path.lineTo(contentWidth, 0);
                path.lineTo(contentWidth, contentHeight / 2);
                path.lineTo(contentWidth - contentHeight / 2, contentHeight);
                path.lineTo(0, contentHeight);
                path.closePath();
                return path;
            }
        });

        ContentBackground nameBackground = new ContentBackground(new ContentBorder(nameInsideShape, getBorderColor()), new Color(255, 255, 153, 255));

        EmptyContent nameMarginRight = new EmptyContent();
        nameMarginRight.setMinWidth(NAME_MARGIN_RIGHT);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(nameBackground);
        horizontalLayout.add(nameMarginRight);

        relativeGroupContent.add(wantedSizeContent);
        relativeGroupContent.add(horizontalLayout);
        relativeGroupContent.add(new ContentInsideRectangle(new TextContent(frameContent)), new Point2D.Double(0,DEFAULT_TYPE_HEIGHT));

        ContentInsideShape contentInsideShape = new ContentInsideRectangle(relativeGroupContent);

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setContent(getBorder());
    }

    @Override
    public void setTextColor(Color textColor)
    {
        frameContent.setTextColor(textColor);
        super.setTextColor(textColor);
    }

    @Override
    public void setBorderColor(Color borderColor)
    {
    }

    @Override
    public boolean addChild(INode n, Point2D p)
    {
        return false;
    }


    @Override
    public void setWantedSize(Rectangle2D size)
    {
        wantedSizeContent.setMinWidth(size.getWidth());
        wantedSizeContent.setMinHeight(size.getHeight());
    }

    @Override
    public Rectangle2D getResizablePoint()
    {
        Rectangle2D nodeBounds = getBounds();

        double x = nodeBounds.getMaxX() - RESIZABLE_POINT_SIZE;
        double y = nodeBounds.getMaxY() - RESIZABLE_POINT_SIZE;

        return new Rectangle2D.Double(x, y, RESIZABLE_POINT_SIZE, RESIZABLE_POINT_SIZE);
    }

    /**
     * Sets the contents property value.
     *
     * @param newValue the contents of this class
     */
    public void setFrameContent(MultiLineText newValue)
    {
        frameContent.setText(newValue.toEdit());
    }

    /**
     * Gets the contents property value.
     *
     * @return the contents of this class
     */
    public MultiLineText getFrameContent()
    {
        return frameContent;
    }

    /**
     * Gets the type property value.
     *
     * @return the type of this frame
     */
    public IntegrationFrameType getType()
    {
        return type;
    }

    /**
     * Sets the type property value.
     *
     * @param type the type of this frame
     */
    public void setType(IntegrationFrameType type)
    {
        this.type = type;
        name.setText(type.getName());
    }

    private IntegrationFrameType type;
    private SingleLineText name;
    private MultiLineText frameContent;

    private EmptyContent wantedSizeContent;

    private static final int DEFAULT_TYPE_WIDTH = 60;
    private static final int DEFAULT_TYPE_HEIGHT = 20;
    private static final int DEFAULT_WIDTH = 80;
    private static final int DEFAULT_HEIGHT = 50;
    private static final int NAME_MARGIN_RIGHT = 10;
    private static final int RESIZABLE_POINT_SIZE = 5;

    private static final LineText.Converter nameConverter = new LineText.Converter()
    {
        @Override
        public OneLineText toLineString(String text)
        {
            return new LargeSizeDecorator(new OneLineText(text));
        }
    };

}