package com.horstmann.violet.product.diagram.classes.node;

import com.horstmann.violet.framework.graphics.Separator;
import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.framework.dialog.IRevertableProperties;
import com.horstmann.violet.framework.util.MementoCaretaker;
import com.horstmann.violet.framework.util.ThreeStringMemento;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.ISwitchableNode;
import com.horstmann.violet.product.diagram.abstracts.node.IVisibleNode;
import com.horstmann.violet.product.diagram.classes.ClassDiagramConstant;
import com.horstmann.violet.product.diagram.common.node.ColorableNode;
import com.horstmann.violet.product.diagram.common.node.ColorableNodeWithMethodsInfo;
import com.horstmann.violet.product.diagram.common.node.PointNode;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.abstracts.node.INamedNode;
import com.horstmann.violet.product.diagram.property.text.MultiLineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;
import com.horstmann.violet.product.diagram.property.text.decorator.*;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * An interface node in a class diagram.
 */
public class InterfaceNode extends ColorableNodeWithMethodsInfo implements INamedNode, IRevertableProperties, ISwitchableNode, IVisibleNode
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
    
    /**
     * Construct an interface node from class node
     * @param the class node
     * @throws CloneNotSupportedException 
     */
    public InterfaceNode(ClassNode node) throws CloneNotSupportedException
    {
        super(node);
        name = (SingleLineText) node.getName();
        name.reconstruction(nameConverter);
        methods = (MultiLineText) node.getMethods();
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

        VerticalLayout verticalGroupContent = new VerticalLayout();
        verticalGroupContent.add(nameContent);
		if (VISIBLE_METHODS_AND_ATRIBUTES == true) {
			TextContent methodsContent = new TextContent(methods);
			verticalGroupContent.add(methodsContent);
		} else {
			if(!methods.getText().isEmpty()){
				MultiLineText hiddenText = new MultiLineText();
				hiddenText.setText("\u2022\u2022\u2022");
				TextContent hiddenContent = new TextContent(hiddenText);
				verticalGroupContent.add(hiddenContent);
			}
		}
        separator = new Separator.LineSeparator(getBorderColor());
        verticalGroupContent.setSeparator(separator);

        ContentInsideShape contentInsideShape = new ContentInsideRectangle(verticalGroupContent);

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());
    }
    
    /**
     * Converts interface node to class node
     */
	@Override
	public INode switchNode() {
		try {
			return new ClassNode(this);
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	/**
	 * Edit visible boolean parameter to opposite value. And refers structure.
	 */
	@Override
	public void switchVisible() {
		VISIBLE_METHODS_AND_ATRIBUTES = !VISIBLE_METHODS_AND_ATRIBUTES;
		createContentStructure();
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

    private final MementoCaretaker<ThreeStringMemento> caretaker = new MementoCaretaker<ThreeStringMemento>();

    @Override
    public void beforeUpdate()
    {
        caretaker.save(new ThreeStringMemento(name.toString(), methods.toString()));
    }

    @Override
    public void revertUpdate()
    {
        ThreeStringMemento memento = caretaker.load();

        name.setText(memento.getFirstValue());
        methods.setText(memento.getSecondValue());
    }

    private transient Separator separator = null;

    private static final int MIN_NAME_HEIGHT = 45;
    private static final int MIN_WIDTH = 100;
    private boolean VISIBLE_METHODS_AND_ATRIBUTES = true;
    private static final String STATIC = "\u00ABstatic\u00BB";
    private static final String HIDE = "hide ";

    private static LineText.Converter nameConverter = new LineText.Converter()
    {
        @Override
        public OneLineText toLineString(String text)
        {
            return new PrefixDecorator( new LargeSizeDecorator(new OneLineText(text)), "<center>\u00ABinterface\u00BB</center>");
        }
    };
    private static final LineText.Converter methodsConverter = new LineText.Converter()
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
            return lineString;
        }
    };
}
