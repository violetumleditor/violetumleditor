package com.horstmann.violet.product.diagram.communication.node;

import java.awt.Color;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.communication.CommunicationDiagramConstant;
import com.horstmann.violet.product.diagram.property.text.decorator.OneLineText;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;
import com.horstmann.violet.product.diagram.property.text.decorator.LargeSizeDecorator;
import com.horstmann.violet.product.diagram.property.text.decorator.UnderlineDecorator;

/**
 * 
 * @author Alexandre de Pellegrin / Cays S. Horstmann
 *
 */
public class ObjectCommuNode extends ColorableNode
{
	public ObjectCommuNode()
	{
		super();
		name = new SingleLineText(nameConverter);
		createContentStructure();
	}

	protected ObjectCommuNode(ObjectCommuNode node) throws CloneNotSupportedException
	{
		super(node);
		name = node.name.clone();
		createContentStructure();
	}

	@Override
	protected void beforeReconstruction()
	{
		super.beforeReconstruction();
		name.reconstruction(nameConverter);
	}

	@Override
	protected INode copy() throws CloneNotSupportedException
	{
		return new ObjectCommuNode(this);
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

	@Override
	public String getToolTip()
	{
		return CommunicationDiagramConstant.COMMUNICATION_DIAGRAM_RESOURCE.getString("tooltip.object_commu_node");
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



	private static LineText.Converter nameConverter = new LineText.Converter(){
		@Override
		public OneLineText toLineString(String text)
		{
			return new LargeSizeDecorator(new UnderlineDecorator(new OneLineText(text)));
		}
	};
	
	private SingleLineText name;
    private static int DEFAULT_WIDTH = 100;
    private static int DEFAULT_HEIGHT = 60;
}
