package com.horstmann.violet.product.diagram.classes.node;

import com.horstmann.violet.framework.graphics.Separator;
import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.classes.ClassDiagramConstant;
import com.horstmann.violet.product.diagram.common.node.ColorableNode;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.MultiLineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;
import com.horstmann.violet.product.diagram.property.text.decorator.LargeSizeDecorator;
import com.horstmann.violet.product.diagram.property.text.decorator.OneLineText;
import com.horstmann.violet.product.diagram.property.text.decorator.PrefixDecorator;

import java.awt.*;

/**
 * A class node in a class diagram.
 */
public class EnumNode extends ColorableNode
{
	/**
     * Construct a class node with a default size
     */
    public EnumNode()
    {
        super();
        name = new SingleLineText(NAME_CONVERTER);
        attributes = new MultiLineText();
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
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();

        if(null == name)
        {
            name = new SingleLineText();
        }
        if(null == attributes)
        {
            attributes = new MultiLineText();
        }
        name.reconstruction(NAME_CONVERTER);
        attributes.reconstruction();
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
        nameContent.setMinHeight(MIN_NAME_HEIGHT);
        nameContent.setMinWidth(MIN_WIDTH);
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

    @Override
    public String getToolTip()
    {
        return ClassDiagramConstant.CLASS_DIAGRAM_RESOURCE.getString("tooltip.enum_node");
    }

    /**
     * Sets the name property value.
     * 
     * @param newValue the class name
     */
    public void setName(LineText newValue)
    {
        name.setText(newValue);
    }

    /**
     * Gets the name property value.
     * 
     * @return the class name
     */
    public LineText getName()
    {
        return name;
    }

    /**
     * Sets the attributes property value.
     *
     * @param newValue the attributes of this class
     */
    public void setAttributes(LineText newValue)
    {
        attributes.setText(newValue);
    }

    /**
     * Gets the attributes property value.
     *
     * @return the attributes of this class
     */
    public LineText getAttributes()
    {
        return attributes;
    }

    @Override
    public LineText getMethods() {
        return null;
    }

    private SingleLineText name;
    private MultiLineText attributes;

    private transient Separator separator;

    private static final int MIN_NAME_HEIGHT = 45;
    private static final int MIN_WIDTH = 100;

    private static final LineText.Converter NAME_CONVERTER = text -> new PrefixDecorator( new LargeSizeDecorator(new OneLineText(text)), "<center>«enumeration»</center>");
}
