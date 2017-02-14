package com.horstmann.violet.product.diagram.classes.node;

import com.horstmann.violet.framework.graphics.Separator;
import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.classes.ClassDiagramConstant;
import com.horstmann.violet.product.diagram.common.node.ColorableNode;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.abstracts.node.INamedNode;import com.horstmann.violet.product.diagram.property.text.MultiLineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;
import com.horstmann.violet.product.diagram.property.text.decorator.*;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * A class node in a class diagram.
 */
public class ClassNode extends ColorableNode implements INamedNode
{

    public static boolean classNameChange = false;

    /**
     * Construct a class node with a default size
     */
    public ClassNode()
    {
        super();
        name = new SingleLineText(NAME_CONVERTER);
        name.setAlignment(LineText.CENTER);
        attributes = new MultiLineText(PROPERTY_CONVERTER);
        methods = new MultiLineText(PROPERTY_CONVERTER);
        comment= new MultiLineText(PROPERTY_CONVERTER);
        createContentStructure();
    }

    protected ClassNode(ClassNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        attributes = node.attributes.clone();
        methods = node.methods.clone();
        comment=node.comment.clone();
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
        if(null == methods)
        {
            methods = new MultiLineText();
        }
        if(null == comment)
        {
            comment = new MultiLineText();
        }
        name.reconstruction(NAME_CONVERTER);
        attributes.reconstruction(PROPERTY_CONVERTER);
        methods.reconstruction(PROPERTY_CONVERTER);
        comment.reconstruction(PROPERTY_CONVERTER);
        name.setAlignment(LineText.CENTER);
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
        TextContent commentContent = new TextContent(comment);
        VerticalLayout verticalGroupContent = new VerticalLayout();
        verticalGroupContent.add(nameContent);
        verticalGroupContent.add(attributesContent);
        verticalGroupContent.add(methodsContent);
        verticalGroupContent.add(commentContent);
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
        comment.setTextColor(textColor);
        super.setTextColor(textColor);
    }

    @Override
    public String getToolTip()
    {
        return ClassDiagramConstant.CLASS_DIAGRAM_RESOURCE.getString("tooltip.class_node");
    }

    /**
     * Sets the name property value.
     *
     * @param newValue the class name
     */
    public void setName(LineText newValue)
    {
        if (classNameChange == true)
        {
            toBigLetter(getName());
        }
        else
            {
            name.setText(newValue);
            }
    }

    /**
     * Sets the name from big letter.
     *
     * @param newValue the class name
     */
    public void toBigLetter(LineText newValue)
    {
        String newName = newValue.toString().substring(0, 1).toUpperCase()
                         + getName().toString().substring(1);
        name.setText(newName);
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

    /**
     * Sets the methods property value.
     *
     * @param newValue the methods of this class
     */
    public void setMethods(LineText newValue)
    {
        methods.setText(newValue);
    }

    /**
     * Gets the methods property value.
     *
     * @return the methods of this class
     */
    public LineText getMethods()
    {
        return methods;
    }

    /**
     * Sets the methods property value.
     *
     * @param newValue the methods of this class
     */
    public void setComment(LineText newValue)
    {
        comment.setText(newValue);
    }

    /**
     * Gets the comment property value.
     *
     * @return the attributes of this class
     */
    public LineText getComment()
    {
        return comment;
    }

    private SingleLineText name;
    private MultiLineText attributes;
    private MultiLineText methods;
    private MultiLineText comment;

    private transient Separator separator;

    private static final int MIN_NAME_HEIGHT = 45;
    private static final int MIN_WIDTH = 100;
    private static final String STATIC = "<<static>>";
    private static final String ABSTRACT = "«abstract»";
    private static final String HIDE= "hide ";
    private static final String[][] SIGNATURE_REPLACE_KEYS = {
            { "public ", "+ " },
            { "package ", "~ " },
            { "protected ", "# " },
            { "private ", "- " },
            { "property ", "/ " },
            { "hide ", ""}
    };

    private static final List<String> STEREOTYPES = Arrays.asList(
            "«Utility»",
            "«Type»",
            "«Metaclass»",
            "«ImplementationClass»",
            "«Focus»",
            "«Entity»",
            "«Control»",
            "«Boundary»",
            "«Auxiliary»",
            ABSTRACT,
            HIDE
    );

    private static final LineText.Converter NAME_CONVERTER = new LineText.Converter()
    {
        @Override
        public OneLineText toLineString(String text)
        {
            OneLineText controlText = new OneLineText(text);
            OneLineText lineString = new LargeSizeDecorator(controlText);

            if(controlText.contains(ABSTRACT))
            {
                lineString = new ItalicsDecorator(lineString);
            }

            for(String stereotype : STEREOTYPES)
            {
                if(controlText.contains(stereotype))
                {
                    lineString = new PrefixDecorator(new RemoveSentenceDecorator(
                            lineString, stereotype), String.format("<center>%s</center>", stereotype)
                    );
                }
            }

            return lineString;
        }
    };
    private static final LineText.Converter PROPERTY_CONVERTER = new LineText.Converter()
    {
        @Override
        public OneLineText toLineString(String text)
        {
            OneLineText lineString = new OneLineText(text);

            if(lineString.contains(HIDE))
            {
                lineString = new HideDecorator(lineString);
            }

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
