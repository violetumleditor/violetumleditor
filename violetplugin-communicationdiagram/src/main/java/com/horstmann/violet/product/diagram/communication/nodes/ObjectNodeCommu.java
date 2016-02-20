package com.horstmann.violet.product.diagram.communication.nodes;

import java.awt.Color;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.framework.property.string.LineText;
import com.horstmann.violet.framework.property.string.SingleLineText;
import com.horstmann.violet.framework.property.string.decorator.LargeSizeDecorator;
import com.horstmann.violet.framework.property.string.decorator.OneLineString;
import com.horstmann.violet.framework.property.string.decorator.UnderlineDecorator;

/**
 * 
 * @author Alexandre de Pellegrin / Cays S. Horstmann
 *
 */
public class ObjectNodeCommu extends ColorableNode
{
	public ObjectNodeCommu()
	{
		super();
		name = new SingleLineText(nameConverter);
		createContentStructure();
	}

	protected ObjectNodeCommu(ObjectNodeCommu node) throws CloneNotSupportedException
	{
		super(node);
		name = node.name.clone();
		createContentStructure();
	}

	@Override
	public void deserializeSupport()
	{
		name.setConverter(nameConverter);
		name.deserializeSupport();

		super.deserializeSupport();
	}

	@Override
	protected INode copy() throws CloneNotSupportedException
	{
		return new ObjectNodeCommu(this);
	}

	@Override
	protected void createContentStructure()
	{
		TextContent nameContent = new TextContent(name);
		nameContent.setMinHeight(DEFAULT_HEIGHT);
		nameContent.setMinWidth(DEFAULT_WIDTH);

		ContentInsideShape contentInsideShape = new ContentInsideRectangle(nameContent);

		setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
		setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
		setContent(getBackground());

		setTextColor(super.getTextColor());
	}

	@Override
	public void setTextColor(Color textColor)
	{
		name.setTextColor(textColor);
		super.setTextColor(textColor);
	}

	/**
	 * Sets the name property value.
	 *
	 * @param newValue the class name
	 */
	public void setName(SingleLineText newValue)
	{
		name = newValue;
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



	private static LineText.Converter nameConverter = new LineText.Converter(){
		@Override
		public OneLineString toLineString(String text)
		{
			return new LargeSizeDecorator(new UnderlineDecorator(new OneLineString(text)));
		}
	};
	
	private SingleLineText name;
    private static int DEFAULT_WIDTH = 100;
    private static int DEFAULT_HEIGHT = 60;
}
