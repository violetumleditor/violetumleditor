package com.horstmann.violet.product.diagram.classes.nodes;

import com.horstmann.violet.framework.graphics.Separator;
import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.property.string.LineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.MultiLineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.SingleLineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.*;
import com.horstmann.violet.product.diagram.common.PointNode;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * A class nodes in a class diagram.
 */
public class EnumNode extends ColorableNode
{
	/**
     * Construct a class node with a default size
     */
    public EnumNode()
    {
        super();
        name = new SingleLineText(nameConverter);
        attributes = new MultiLineText();
//        name.setAlignment(LineText.CENTER);
        createContentStructure();
    }

    protected EnumNode(EnumNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        attributes = node.attributes.clone();
        createContentStructure();
    }

    @Override
    public void deserializeSupport()
    {
        super.deserializeSupport();
        name.deserializeSupport(nameConverter);
        attributes.deserializeSupport();
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new EnumNode(this);
    }

    
    @Override
    protected void createContentStructure()
    {
        name.setText(name.toEdit());

        TextContent nameContent = new TextContent(name);
        nameContent.setMinHeight(DEFAULT_NAME_HEIGHT);
        nameContent.setMinWidth(DEFAULT_WIDTH);
        TextContent attributesContent = new TextContent(attributes);

        VerticalLayout verticalGroupContent = new VerticalLayout();
        verticalGroupContent.add(nameContent);
        verticalGroupContent.add(attributesContent);
        separator = new Separator.LineSeparator(getBorderColor());
        verticalGroupContent.setSeparator(separator);

        ContentInsideShape contentInsideShape = new ContentInsideRectangle(verticalGroupContent);

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());

        setTextColor(super.getTextColor());
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
    public void setTextColor(Color textColor)
    {
        name.setTextColor(textColor);
        attributes.setTextColor(textColor);
        super.setTextColor(textColor);
    }

    public boolean addChild(INode n, Point2D p)
    {
        return false;
    }

    /**
     * Sets the name property value.
     * 
     * @param newValue the class name
     */
    public void setName(SingleLineText newValue)
    {
        name.setText(newValue.toEdit());
    }

    /**
     * Gets the name property value.
     * 
     * @return the class name
     */
    public SingleLineText getName()
    {
        return name;
    }

    /**
     * Sets the attributes property value.
     *
     * @param newValue the attributes of this class
     */
    public void setAttributes(MultiLineText newValue)
    {
        attributes.setText(newValue.toEdit());
    }

    /**
     * Gets the attributes property value.
     *
     * @return the attributes of this class
     */
    public MultiLineText getAttributes()
    {
        return attributes;
    }

    private SingleLineText name;
    private MultiLineText attributes;

    private transient Separator separator;

    private final static int DEFAULT_NAME_HEIGHT = 45;
    private final static int DEFAULT_WIDTH = 100;

    private final static LineText.Converter nameConverter = new LineText.Converter(){
        @Override
        public OneLineString toLineString(String text)
        {
            return new PrefixDecorator( new LargeSizeDecorator(new OneLineString(text)), "<center>«enum»</center>");
        }
    };
}
