package com.horstmann.violet.product.diagram.classes.node;

import java.awt.*;

import com.horstmann.violet.framework.graphics.Separator;
import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.content.VerticalLayout;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.classes.ClassDiagramConstant;
import com.horstmann.violet.product.diagram.property.text.decorator.*;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.property.text.MultiLineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;

/**
 * A class node in a class diagram.
 */
public class ClassNode extends ColorableNode
{
	/**
     * Construct a class node with a default size
     */
    public ClassNode()
    {
        super();
        name = new SingleLineText(nameConverter);
        name.setAlignment(LineText.CENTER);
        attributes = new MultiLineText(propertyConverter);
        methods = new MultiLineText(propertyConverter);
        createContentStructure();
    }

    protected ClassNode(ClassNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        attributes = node.attributes.clone();
        methods = node.methods.clone();
        createContentStructure();
    }

    @Override
    public void deserializeSupport()
    {
        name.deserializeSupport(nameConverter);
        attributes.deserializeSupport(propertyConverter);
        methods.deserializeSupport(propertyConverter);
        name.setAlignment(LineText.CENTER);

        super.deserializeSupport();
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new ClassNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        TextContent nameContent = new TextContent(name);
        nameContent.setMinHeight(MIN_NAME_HEIGHT);
        nameContent.setMinWidth(MIN_WIDTH);
        TextContent attributesContent = new TextContent(attributes);
        TextContent methodsContent = new TextContent(methods);

        VerticalLayout verticalGroupContent = new VerticalLayout();
        verticalGroupContent.add(nameContent);
        verticalGroupContent.add(attributesContent);
        verticalGroupContent.add(methodsContent);
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
        methods.setTextColor(textColor);
        super.setTextColor(textColor);
    }

    @Override
    public String getToolTip()
    {
        return ClassDiagramConstant.CLASS_DIAGRAM_RESOURCE.getString("class_node.tooltip");
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

    /**
     * Sets the methods property value.
     * 
     * @param newValue the methods of this class
     */
    public void setMethods(MultiLineText newValue)
    {
        methods.setText(newValue.toEdit());
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

    private SingleLineText name;
    private MultiLineText attributes;
    private MultiLineText methods;

    private transient Separator separator;

    private static final int MIN_NAME_HEIGHT = 45;
    private static final int MIN_WIDTH = 100;
    private static final String ABSTRACT = "<<abstract>>";
    private static final String STATIC = "<<static>>";
    private static final String[][] SIGNATURE_REPLACE_KEYS = {
            { "public ", "+ " },
            { "package ", "~ " },
            { "protected ", "# " },
            { "private ", "- " },
            { "property ", "/ " }
    };

    private static final LineText.Converter nameConverter = new LineText.Converter()
    {
        @Override
        public OneLineText toLineString(String text)
        {
            OneLineText lineString = new OneLineText(text);
            if(lineString.contains(ABSTRACT))
            {
                return new PrefixDecorator( new LargeSizeDecorator(new RemoveSentenceDecorator(lineString, ABSTRACT)), "<center>«abstract»</center>");
            }

            return new LargeSizeDecorator(new OneLineText(text));
        }
    };
    private static final LineText.Converter propertyConverter= new LineText.Converter()
    {
        @Override
        public OneLineText toLineString(String text)
        {
            OneLineText lineString = new OneLineText(text);

            if(lineString.contains(STATIC))
            {
                lineString = new UnderlineDecorator(new RemoveSentenceDecorator(lineString, STATIC));
            }
            for(String[] signature : SIGNATURE_REPLACE_KEYS)
            {
                lineString = new ReplaceSentenceDecorator(lineString, signature[0], signature[1]);
            }

            return lineString;
        }
    };
}
