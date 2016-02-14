package com.horstmann.violet.product.diagram.sequence.nodes;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.IResizableNode;
import com.horstmann.violet.product.diagram.abstracts.property.IntegrationFrameType;
import com.horstmann.violet.product.diagram.abstracts.property.string.LineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.MultiLineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.SingleLineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.LargeSizeDecorator;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.OneLineString;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * A integration frame node in a UML diagram.
 */
public class IntegrationFrameNode extends ColorableNode implements IResizableNode {
    public IntegrationFrameNode() {
        type = IntegrationFrameType.ALT;
        name = new SingleLineText(nameConverter);
        name.setText(type.getName());
        frameContent = new MultiLineText();
        createContentStructure();
    }

    protected IntegrationFrameNode(IntegrationFrameNode node) throws CloneNotSupportedException {
        super(node);
        name = new SingleLineText(nameConverter);
        type = node.type;
        name.setText(type.getName());
        frameContent = new MultiLineText();
        frameContent.setText(node.frameContent.toEdit());
        wantedSize = (Rectangle2D) node.wantedSize.clone();
        createContentStructure();
    }

    @Override
    public void deserializeSupport() {
        super.deserializeSupport();
        name.deserializeSupport(nameConverter);
        frameContent.deserializeSupport();
    }


    @Override
    protected INode copy() throws CloneNotSupportedException {
        return new IntegrationFrameNode(this);
    }

    @Override
    protected void createContentStructure() {
        RelativeLayout relativeGroupContent = new RelativeLayout();
        relativeGroupContent.setMinHeight(wantedSize.getHeight());
        relativeGroupContent.setMinWidth(wantedSize.getWidth());

        TextContent nameContent = new TextContent(name);
        nameContent.setMinHeight(DEFAULT_TYPE_HEIGHT);
        nameContent.setMinWidth(DEFAULT_TYPE_WIDTH);

        ContentInsideShape nameInsideShape = new ContentInsideCustomShape(nameContent, new ContentInsideCustomShape.ShapeCreator() {
            @Override
            public Shape createShape(double contentWidth, double contentHeight) {
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
        relativeGroupContent.add(nameBackground);

        TextContent contextContent = new TextContent(frameContent);
        contextContent.setMinHeight(wantedSize.getHeight());
        contextContent.setMinWidth(DEFAULT_TYPE_WIDTH);
        ContentInsideShape contentRectangle = new ContentInsideRectangle(contextContent);
        relativeGroupContent.add(contentRectangle);

        ContentInsideShape contentInsideShape = new ContentInsideRectangle(relativeGroupContent);

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setContent(getBorder());
    }

    @Override
    public void setTextColor(Color textColor) {
        frameContent.setTextColor(textColor);
        super.setTextColor(textColor);
    }


    @Override
    public boolean addChild(INode n, Point2D p) {
        return false;
    }


    @Override
    public void setWantedSize(Rectangle2D size) {
        this.wantedSize = size;
        createContentStructure();
    }

    @Override
    public Rectangle2D getResizablePoint() {
        Rectangle2D nodeBounds = getBounds();
        int pointSize = 4;

        double x = nodeBounds.getMaxX() - pointSize;
        double y = nodeBounds.getMaxY() - pointSize;

        return new Rectangle2D.Double(x, y, pointSize, pointSize);
    }

    /**
     * Sets the contents property value.
     *
     * @param newValue the contents of this class
     */
    public void setFrameContent(MultiLineText newValue) {
        frameContent.setText(newValue.toEdit());
    }

    /**
     * Gets the contents property value.
     *
     * @return the contents of this class
     */
    public MultiLineText getFrameContent() {
        return frameContent;
    }

    /**
     * Gets the type property value.
     *
     * @return the type of this frame
     */
    public IntegrationFrameType getType() {
        return type;
    }

    /**
     * Sets the type property value.
     *
     * @param type the type of this frame
     */
    public void setType(IntegrationFrameType type) {
        this.type = type;
        name.setText(type.getName());
    }

    private IntegrationFrameType type;
    private SingleLineText name;
    private MultiLineText frameContent;

    private Rectangle2D wantedSize = new Rectangle2D.Double(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);

    private static final int DEFAULT_TYPE_WIDTH = 60;
    private static final int DEFAULT_TYPE_HEIGHT = 20;
    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_HEIGHT = 150;

    private static final LineText.Converter nameConverter = new LineText.Converter() {
        @Override
        public OneLineString toLineString(String text) {
            return new LargeSizeDecorator(new OneLineString(text));
        }
    };

}