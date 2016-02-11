package com.horstmann.violet.product.diagram.sequence.nodes;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
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
 * A package node in a UML diagram.
 */
public class IntegrationFrameNode extends ColorableNode
{
    public IntegrationFrameNode()
    {
        type = IntegrationFrameType.ALT;
        name = new SingleLineText(nameConverter);
        name.setText(type.getName());
        context = new MultiLineText();
        createContentStructure();
    }

    protected IntegrationFrameNode(IntegrationFrameNode node) throws CloneNotSupportedException
    {
        super(node);
        type = node.type;
        name = node.name.clone();
        context = node.context.clone();
        createContentStructure();
    }

    @Override
    public void deserializeSupport()
    {
        super.deserializeSupport();
        name.deserializeSupport(nameConverter);
        context.deserializeSupport();
    }


    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new IntegrationFrameNode(this);
    }

    @Override
    protected void createContentStructure() {
        RelativeLayout relativeGroupContent = new RelativeLayout();
        relativeGroupContent.setMinHeight(DEFAULT_HEIGHT);
        relativeGroupContent.setMinWidth(DEFAULT_WIDTH);

        TextContent nameContent = new TextContent(name);
        nameContent.setMinHeight(DEFAULT_TYPE_HEIGHT);
        nameContent.setMinWidth(DEFAULT_TYPE_WIDTH);

        ContentInsideShape nameInsideShape = new ContentInsideCustomShape(nameContent, new ContentInsideCustomShape.ShapeCreator() {
            @Override
            public Shape createShape(int contentWidth, int contentHeight) {
                GeneralPath path = new GeneralPath();
                path.moveTo(0, 0);
                path.lineTo(contentWidth, 0);
                path.lineTo(contentWidth, contentHeight/2);
                path.lineTo(contentWidth - contentHeight/2, contentHeight);
                path.lineTo(0, contentHeight);
                path.closePath();
                return path;
            }
        });

        ContentBackground nameBackground = new ContentBackground(new ContentBorder(nameInsideShape, getBorderColor()), new Color(255, 255, 153, 255));
        relativeGroupContent.add(nameBackground);

        ContentInsideShape contentInsideShape = new ContentInsideRectangle(relativeGroupContent);

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setContent(getBorder());
    }

    @Override
    public void setTextColor(Color textColor) {
//        name.setTextColor(textColor);
        context.setTextColor(textColor);
        super.setTextColor(textColor);
    }


    @Override
    public boolean addChild(INode n, Point2D p) {
        return false;
    }


    /**
     * Sets the name property value.
     *
     * @param newValue the class name
     */
//    public void setName(SingleLineText newValue) {
//        name = newValue;
//    }

    /**
     * Gets the name property value.
     *
     * @return the class name
     */
//    public SingleLineText getName() {
//        return name;
//    }

    /**
     * Sets the contents property value.
     *
     * @param newValue the contents of this class
     */
    public void setContext(MultiLineText newValue)
    {
        context.setText(newValue.toEdit());
    }

    /**
     * Gets the contents property value.
     *
     * @return the contents of this class
     */
    public MultiLineText getContext()
    {
        return context;
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
    public void setType(IntegrationFrameType type)
    {
        this.type = type;
        name.setText(type.getName());
    }

    private IntegrationFrameType type;
    private SingleLineText name;
    private MultiLineText context;

    private final static int DEFAULT_TYPE_WIDTH = 60;
    private final static int DEFAULT_TYPE_HEIGHT = 20;
    private final static int DEFAULT_WIDTH = 200;
    private final static int DEFAULT_HEIGHT = 150;

    private final static LineText.Converter nameConverter = new LineText.Converter(){
        @Override
        public OneLineString toLineString(String text)
        {
            return new LargeSizeDecorator(new OneLineString(text));
        }
    };
}