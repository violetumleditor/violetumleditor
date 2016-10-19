package com.horstmann.violet.product.diagram.classes.node;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.seanregan.javaimport.IJavaParseable;
import java.awt.*;
import java.util.*;
import java.util.List;

import com.horstmann.violet.framework.graphics.Separator;
import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.content.VerticalLayout;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.classes.ClassDiagramConstant;
import com.horstmann.violet.product.diagram.property.text.decorator.*;
import com.horstmann.violet.product.diagram.common.node.ColorableNode;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.property.text.MultiLineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;
import com.seanregan.javaimport.ClassVisitor;
import java.io.File;

/**
 * A class node in a class diagram.
 */
public class ClassNode extends ColorableNode implements IJavaParseable
{
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
        name.reconstruction(NAME_CONVERTER);
        attributes.reconstruction(PROPERTY_CONVERTER);
        methods.reconstruction(PROPERTY_CONVERTER);
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
        return ClassDiagramConstant.CLASS_DIAGRAM_RESOURCE.getString("tooltip.class_node");
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
	 * Sets the file to import
	 * @param refernce the absolute path of the file to import
	 */
	@Override
	public void setFileReference(String refernce) {
		mFileReference = refernce;
	}

	/**
	 * Gets the file that is currently set to be imported
	 * @return a String containing the absolute path to the
	 * currently imported file
	 */
	@Override
	public String getFileReference() {
		return mFileReference;
	}

	/**
	 * Sets the class name of from the file to import
	 * @param name a String containing the class from the file
	 * to import
	 */
	@Override
	public void setClassName(String name) {
		mFileClassName = name;
	}

	/**
	 * Gets the name of the class to import from 
	 * the imported file
	 * @return a String containing the name of the class
	 * to import
	 */
	@Override
	public String getClassName() {
		return mFileClassName;
	}
	
	/**
	 * Parses the specified class within the specified file and
	 * populates the results within the node
	 */
	@Override
	public void parseAndPopulate() {
		if (mFileReference == null || mFileClassName == null) return;
		
		try {
			//CompilationUnite to parse file
			CompilationUnit cu = JavaParser.parse(new File(mFileReference));
			
			//Get details from file
			final ClassVisitor cVisistor  = new ClassVisitor(cu);
			
			//Get all the clases
			List<String> cs = cVisistor.getClasses();
			for (String c : cs) {
				//Find the class that we want
				if (c.compareToIgnoreCase(mFileClassName) == 0) {
					//Fill the node's data
					setName(new MultiLineText(c));
					setAttributes(new MultiLineText(cVisistor.getAttributesStrings(c)));
					setMethods(new MultiLineText(cVisistor.getMethodsString(c)));

					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String mFileReference = null;
	private String mFileClassName = null;
	
    private SingleLineText name;
    private MultiLineText attributes;
    private MultiLineText methods;

    private transient Separator separator;

    private static final int MIN_NAME_HEIGHT = 45;
    private static final int MIN_WIDTH = 100;
    private static final String STATIC = "<<static>>";
    private static final String ABSTRACT = "«abstract»";
    private static final String[][] SIGNATURE_REPLACE_KEYS = {
            { "public ", "+ " },
            { "package ", "~ " },
            { "protected ", "# " },
            { "private ", "- " },
            { "property ", "/ " }
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
            ABSTRACT
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
