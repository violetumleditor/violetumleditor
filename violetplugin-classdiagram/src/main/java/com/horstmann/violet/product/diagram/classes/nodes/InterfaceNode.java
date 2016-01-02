package com.horstmann.violet.product.diagram.classes.nodes;

import java.awt.geom.Point2D;

import com.horstmann.violet.framework.graphics.Separator;
import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.content.VerticalGroupContent;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.property.string.LineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.LargeSizeDecorator;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.OneLineString;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.PrefixDecorator;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.property.string.MultiLineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.SingleLineText;
import com.horstmann.violet.product.diagram.common.PointNode;

/**
 * An interface node in a class diagram.
 */
public class InterfaceNode extends ColorableNode
{
    public InterfaceNode()
    {
        super();

        name = new SingleLineText(new LineText.Converter(){
            @Override
            public OneLineString toLineString(String text)
            {
                return new PrefixDecorator( new LargeSizeDecorator(new OneLineString(text)), "\u00ABinterface\u00BB<br>");
//                return new ItalicsDecorator( new LargeSizeDecorator(new OneLineString(text)));
            }
        });
        methods = new MultiLineText();
        createContentStructure();
    }

    public InterfaceNode(InterfaceNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        methods = node.methods.clone();
        createContentStructure();
    }

    @Override
    protected INode copy() throws CloneNotSupportedException {
        return new InterfaceNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        TextContent nameContent = new TextContent(name);
        nameContent.setMinHeight(DEFAULT_NAME_HEIGHT);
        nameContent.setMinWidth(DEFAULT_WIDTH);
        TextContent methodsContent = new TextContent(methods);

        VerticalGroupContent verticalGroupContent = new VerticalGroupContent();
        verticalGroupContent.add(nameContent);
        verticalGroupContent.add(methodsContent);
        verticalGroupContent.setSeparator(new Separator.LineSeparator(getBorderColor()));

        ContentInsideShape contentInsideShape = new ContentInsideRectangle(verticalGroupContent);

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());
    }

    @Override
    public boolean addChild(INode n, Point2D p)
    {
        if (n instanceof PointNode)
        {
            return true;
        }
        return false;
    }

    /**
     * Sets the name property value.
     * 
     * @param newValue the interface name
     */
    public void setName(SingleLineText newValue)
    {
        name.setText(newValue.getText());
    }

    /**
     * Gets the name property value.
     * 
     * @return the interface name
     */
    public SingleLineText getName()
    {
        return name;
    }

    /**
     * Sets the methods property value.
     * 
     * @param newValue the methods of this interface
     */
    public void setMethods(MultiLineText newValue)
    {
        methods = newValue;
    }

    /**
     * Gets the methods property value.
     * 
     * @return the methods of this interface
     */
    public MultiLineText getMethods()
    {
        return methods;
    }





    private SingleLineText name;
    private MultiLineText methods;

    private static int DEFAULT_NAME_HEIGHT = 45;
    private static int DEFAULT_WIDTH = 100;
}
