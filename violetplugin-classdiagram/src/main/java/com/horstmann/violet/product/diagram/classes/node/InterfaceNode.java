package com.horstmann.violet.product.diagram.classes.node;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.seanregan.javaimport.IJavaParseable;
import java.awt.geom.Point2D;

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
import com.horstmann.violet.product.diagram.common.node.PointNode;
import com.seanregan.javaimport.ClassVisitor;
import java.awt.Color;
import java.io.File;
import java.util.List;

/**
 * An interface node in a class diagram.
 */
public class InterfaceNode extends ColorableNode implements IJavaParseable
{
    /**
     * Construct an interface node with a default size and the text <<interface>>.
     */
    public InterfaceNode()
    {
        super();

        name = new SingleLineText(nameConverter);
        methods = new MultiLineText(methodsConverter);
        createContentStructure();
    }

    protected InterfaceNode(InterfaceNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
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
        if(null == methods)
        {
            methods = new MultiLineText();
        }
        name.reconstruction(nameConverter);
        methods.reconstruction(methodsConverter);
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new InterfaceNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        name.setText(name.toEdit());

        TextContent nameContent = new TextContent(name);
        nameContent.setMinHeight(MIN_NAME_HEIGHT);
        nameContent.setMinWidth(MIN_WIDTH);
        TextContent methodsContent = new TextContent(methods);

        VerticalLayout verticalGroupContent = new VerticalLayout();
        verticalGroupContent.add(nameContent);
        verticalGroupContent.add(methodsContent);
        separator = new Separator.LineSeparator(getBorderColor());
        verticalGroupContent.setSeparator(separator);

        ContentInsideShape contentInsideShape = new ContentInsideRectangle(verticalGroupContent);

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());
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
        methods.setTextColor(textColor);
    }

    @Override
    public String getToolTip()
    {
        return ClassDiagramConstant.CLASS_DIAGRAM_RESOURCE.getString("tooltip.interface_node");
    }

    @Override
    public boolean addChild(INode node, Point2D point)
    {
        if (node instanceof PointNode)
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
    public void setName(LineText newValue)
    {
        name.setText(newValue);
    }

    /**
     * Gets the name property value.
     * 
     * @return the interface name
     */
    public LineText getName()
    {
        return name;
    }

    /**
     * Sets the methods property value.
     * 
     * @param newValue the methods of this interface
     */
    public void setMethods(LineText newValue)
    {
        methods.setText(newValue);
    }

    /**
     * Gets the methods property value.
     * 
     * @return the methods of this interface
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
    private MultiLineText methods;

    private transient Separator separator = null;

    private static final int MIN_NAME_HEIGHT = 45;
    private static final int MIN_WIDTH = 100;
    private static final String STATIC = "<<static>>";

    private static LineText.Converter nameConverter = new LineText.Converter()
    {
        @Override
        public OneLineText toLineString(String text)
        {
            return new PrefixDecorator( new LargeSizeDecorator(new OneLineText(text)), "<center>«interface»</center>");
        }
    };
    private static final LineText.Converter methodsConverter = new LineText.Converter()
    {
        @Override
        public OneLineText toLineString(String text)
        {
            OneLineText lineString = new OneLineText(text);

            if(lineString.contains(STATIC))
            {
                lineString = new UnderlineDecorator(new RemoveSentenceDecorator(lineString, STATIC));
            }
            return lineString;
        }
    };
}
