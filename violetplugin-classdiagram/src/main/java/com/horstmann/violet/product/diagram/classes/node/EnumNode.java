package com.horstmann.violet.product.diagram.classes.node;

import com.horstmann.violet.framework.graphics.Separator;
import com.horstmann.violet.framework.graphics.content.ContentBackground;
import com.horstmann.violet.framework.graphics.content.ContentBorder;
import com.horstmann.violet.framework.graphics.content.ContentInsideShape;
import com.horstmann.violet.framework.graphics.content.TextContent;
import com.horstmann.violet.framework.graphics.content.VerticalLayout;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.framework.dialog.IRevertableProperties;
import com.horstmann.violet.framework.util.MementoCaretaker;
import com.horstmann.violet.framework.util.ThreeStringMemento;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.classes.ClassDiagramConstant;
import com.horstmann.violet.product.diagram.common.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INamedNode;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.MultiLineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;
import com.horstmann.violet.product.diagram.property.text.decorator.LargeSizeDecorator;
import com.horstmann.violet.product.diagram.property.text.decorator.OneLineText;
import com.horstmann.violet.product.diagram.property.text.decorator.PrefixDecorator;
import com.horstmann.violet.product.diagram.property.text.decorator.ReplaceSentenceDecorator;
import java.awt.Color;

/**
 * A class node in a class diagram.
 */

public class EnumNode extends ColorableNode implements INamedNode, IRevertableProperties
{
    private SingleLineText name;
    private MultiLineText attributes;
    private MultiLineText methods;
    private transient Separator separator;
    private static final int MIN_NAME_HEIGHT = 45;
    private static final int MIN_WIDTH = 100;

    /**
     * Construct a class node with a default size
     */
    public EnumNode()
    {
        super();
        name = new SingleLineText(NAME_CONVERTER);
        attributes = new MultiLineText(PROPERTY_CONVERTER);
        methods = new MultiLineText(PROPERTY_CONVERTER);
        createContentStructure();
    }

    /**
     * Clone enumeration diagram
     * @param node
     * @throws CloneNotSupportedException
     */
    protected EnumNode(final EnumNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        attributes = node.attributes.clone();
        methods = node.methods.clone();
        createContentStructure();
    }

    @Override
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();
        name = new SingleLineText();
        attributes = new MultiLineText();
        methods = new MultiLineText();
        name.reconstruction(NAME_CONVERTER);
        attributes.reconstruction(PROPERTY_CONVERTER);
        methods.reconstruction(PROPERTY_CONVERTER);
        name.setAlignment(LineText.CENTER);
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
        TextContent nameContent = getTextContent();
        TextContent attributesContent = new TextContent(attributes);
        TextContent methodsContent = new TextContent(methods);
        VerticalLayout verticalGroupContent = getVerticalLayout(nameContent, attributesContent, methodsContent);
        ContentInsideShape contentInsideShape = new ContentInsideRectangle(verticalGroupContent);
        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());
        setTextColor(super.getTextColor());
    }

    private VerticalLayout getVerticalLayout(final TextContent nameContent, final TextContent attributesContent,
                                             final TextContent methodsContent) {
        VerticalLayout verticalGroupContent = new VerticalLayout();
        verticalGroupContent.add(nameContent);
        verticalGroupContent.add(attributesContent);
        verticalGroupContent.add(methodsContent);
        separator = new Separator.LineSeparator(getBorderColor());
        verticalGroupContent.setSeparator(separator);
        return verticalGroupContent;
    }

    private TextContent getTextContent() {
        TextContent nameContent = new TextContent(name);
        nameContent.setMinHeight(MIN_NAME_HEIGHT);
        nameContent.setMinWidth(MIN_WIDTH);
        return nameContent;
    }

    @Override
    public void setBorderColor(final Color borderColor)
    {
        if(borderColor != null) {
            separator.setColor(borderColor);
            super.setBorderColor(borderColor);
        }
    }

    @Override
    public void setTextColor(final Color textColor)
    {
        if(textColor != null) {
            name.setTextColor(textColor);
            attributes.setTextColor(textColor);
            super.setTextColor(textColor);
        }
    }

    @Override
    public String getToolTip()
    {
        return ClassDiagramConstant.CLASS_DIAGRAM_RESOURCE.getString("tooltip.enum_node");
    }

    public void setName(final LineText newValue)
    {
        if (newValue != null ) {
            name.setText(newValue);
        }
    }

    public LineText getName()
    {
        return name;
    }

    public void setAttributes(final LineText newValue)
    {
        if(newValue != null) {
            attributes.setText(newValue);
        }
    }

    public LineText getAttributes()
    {
        return attributes;
    }


    public void setMethods(final LineText newValue)
    {
        if( newValue != null) {
            methods.setText(newValue);
        }
    }

    private final MementoCaretaker<ThreeStringMemento> caretaker = new MementoCaretaker<ThreeStringMemento>();

    @Override
    public void beforeUpdate()
    {
        caretaker.save(new ThreeStringMemento(name.toString(), attributes.toString()));
    }

    @Override
    public void revertUpdate()
    {
        ThreeStringMemento memento = caretaker.load();

        name.setText(memento.getFirstValue());
        attributes.setText(memento.getSecondValue());
    }


    public LineText getMethods()
    {
        return methods;
    }


    private static final LineText.Converter NAME_CONVERTER = text -> new PrefixDecorator( new LargeSizeDecorator(new OneLineText(text)), "<center>«enumeration»</center>");
    private static final String[][] SIGNATURE_REPLACE_KEYS = {
        { "public ", "+ " },
        { "package ", "~ " },
        { "protected ", "# " },
        { "private ", "- " },
        { "property ", "/ " }
    };

    private static final LineText.Converter PROPERTY_CONVERTER = new LineText.Converter()
    {
        @Override
        public OneLineText toLineString(String text)
        {
            OneLineText lineString = new OneLineText(text);
            String[] publicSignature = SIGNATURE_REPLACE_KEYS[0];
            String[] packageSignature = SIGNATURE_REPLACE_KEYS[0];
            for(int i = 0; i < SIGNATURE_REPLACE_KEYS.length; i++)
            {
                lineString = new ReplaceSentenceDecorator(lineString, publicSignature[0], packageSignature[0]);
            }

            return lineString;
        }
    };
}