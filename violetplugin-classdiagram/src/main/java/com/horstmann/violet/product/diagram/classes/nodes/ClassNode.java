package com.horstmann.violet.product.diagram.classes.nodes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.horstmann.violet.framework.graphics.content.Content;
import com.horstmann.violet.framework.graphics.Separator;
import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.content.VerticalGroupContent;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.property.string.LineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.LargeSizeDecorator;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.OneLineString;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.RemoveSentenceDecorator;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.UnderlineDecorator;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.RectangularNode;
import com.horstmann.violet.product.diagram.abstracts.property.string.MultiLineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.SingleLineText;
import com.horstmann.violet.product.diagram.common.PointNode;

/**
 * A class node_old in a class diagram.
 */
public class ClassNode extends RectangularNode
{
    /**
     * Construct a class node_old with a default size
     */
    public ClassNode()
    {
        name = new SingleLineText(new LineText.Converter(){
            @Override
            public OneLineString toLineString(String text)
            {
                return new LargeSizeDecorator(new OneLineString(text));
            }
        });
        name.setAlignment(LineText.CENTER);

        LineText.Converter converter = new LineText.Converter(){
            @Override
            public OneLineString toLineString(String text)
            {
            OneLineString lineString = new OneLineString(text);

            if(lineString.contains("<<static>>"))
            {
                lineString = new UnderlineDecorator(new RemoveSentenceDecorator(lineString, "<<static>>"));
            }
            return lineString;
            }
        };

        attributes = new MultiLineText(converter);
        methods = new MultiLineText(converter);

        createContentStructure();
    }

    protected void createContentStructure()
    {
        TextContent nameContent = new TextContent(name);
        nameContent.setMinHeight(DEFAULT_NAME_HEIGHT);
        nameContent.setMinWidth(DEFAULT_WIDTH);
        TextContent attributesContent = new TextContent(attributes);
        TextContent methodsContent = new TextContent(methods);

        VerticalGroupContent verticalGroupContent = new VerticalGroupContent();
        verticalGroupContent.add(nameContent);
        verticalGroupContent.add(attributesContent);
        verticalGroupContent.add(methodsContent);
        verticalGroupContent.setSeparator(new Separator.LineSeparator(getBorderColor()));

        ContentInsideShape contentInsideShape = new ContentInsideRectangle(verticalGroupContent);

        border = new ContentBorder(contentInsideShape, getBorderColor());
        background = new ContentBackground(border, getBackgroundColor());

        content = background;
    }

    @Override
    public Rectangle2D getBounds()
    {
        Point2D location = getLocationOnGraph();
        Rectangle2D contentBounds = content.getBounds();
        return new Rectangle2D.Double(location.getX(), location.getY(), contentBounds.getWidth(), contentBounds.getHeight());
    }

    @Override
    public void draw(Graphics2D g2)
    {
        Color oldColor = g2.getColor();
        content.draw(g2, getLocationOnGraph());
        g2.setColor(oldColor);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.Node#addNode(com.horstmann.violet.framework.Node, java.awt.geom.Point2D)
     */
    public boolean addChild(INode n, Point2D p)
    {
        // TODO : where is it added?
        if (n instanceof PointNode)
        {
            return true;
        }
        return false;
    }

    /**
     * Sets the name property value.
     * 
     * @param newValue the class name
     */
    public void setName(SingleLineText newValue)
    {
        name.setText(newValue.getText());
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
        attributes = newValue;
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

    /**
     * Sets the methods property value.
     * 
     * @param newValue the methods of this class
     */
    public void setMethods(MultiLineText newValue)
    {
        methods = newValue;
    }

    /**
     * Gets the methods property value.
     * 
     * @return the methods of this class
     */
    public MultiLineText getMethods()
    {
        return methods;
    }

    public ClassNode clone()
    {
        ClassNode cloned = (ClassNode) super.clone();
        cloned.name = name.clone();
        cloned.methods = methods.clone();
        cloned.attributes = attributes.clone();
        cloned.createContentStructure();
        return cloned;
    }

    private Content content = null;
    private ContentBackground background = null;
    private ContentBorder border = null;

    private SingleLineText name;
    private MultiLineText attributes;
    private MultiLineText methods;

    private static int DEFAULT_NAME_HEIGHT = 45;
    private static int DEFAULT_WIDTH = 100;

}
