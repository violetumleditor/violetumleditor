package com.horstmann.violet.product.diagram.state.node;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;
import com.horstmann.violet.product.diagram.state.StateDiagramConstant;

public class ExternalSystemExitPointNode extends ColorableNode
{
	protected static class ExternalSystemEntryPointShape implements ContentInsideCustomShape.ShapeCreator
	{
		public ExternalSystemEntryPointShape(TextContent nameContent)
		{
			super();
			this.nameContent = nameContent;
		}

		@Override
		public Shape createShape(double contentWidth, double contentHeight)
		{
			GeneralPath path = new GeneralPath();
			path.append(new Ellipse2D.Double((nameContent.getWidth() - DEFAULT_DIAMETER)/2,0,DEFAULT_DIAMETER,DEFAULT_DIAMETER), false);

			double p = (0.5+(DEFAULT_DIAMETER/2) - ((DEFAULT_DIAMETER/2)/(Math.sqrt(2))));
			double center = (nameContent.getWidth() - DEFAULT_DIAMETER)/2;

			path.moveTo(center+p,p);
			path.lineTo(center+DEFAULT_DIAMETER-p,DEFAULT_DIAMETER-p);
			path.moveTo(center+DEFAULT_DIAMETER-p,p);
			path.lineTo(center+p,DEFAULT_DIAMETER-p);

			return path;
		}

		private TextContent nameContent;
	}
	/**
	 * Construct an actor node_old with a default size and name
	 */
	public ExternalSystemExitPointNode()
	{
		super();
		name = new SingleLineText();
		name.setAlignment(LineText.CENTER);
		name.setPadding(5,5,5,5);
		createContentStructure();
	}

	protected ExternalSystemExitPointNode(ExternalSystemExitPointNode node) throws CloneNotSupportedException
	{
		super(node);
		name = node.name.clone();
		createContentStructure();
	}

	@Override
	public void deserializeSupport()
	{
		name.deserializeSupport();
		name.setAlignment(LineText.CENTER);
		name.setPadding(5,5,5,5);

		super.deserializeSupport();
	}

	@Override
	protected void createContentStructure()
	{
		EmptyContent emptyContent = new EmptyContent();
		TextContent nameContent = new TextContent(name);
		nameContent.setMinWidth(DEFAULT_DIAMETER);

		emptyContent.setMinWidth(DEFAULT_DIAMETER);
		emptyContent.setMinHeight(DEFAULT_DIAMETER);

		ContentInsideCustomShape contentInsideCustomShape = new ContentInsideCustomShape(emptyContent, new ExternalSystemEntryPointShape(nameContent));;
		setBorder(new ContentBorder(contentInsideCustomShape, getBorderColor()));
		setBackground(new ContentBackground(getBorder(), getBackgroundColor()));

		VerticalLayout verticalGroupContent = new VerticalLayout();
		verticalGroupContent.add(getBackground());
		verticalGroupContent.add(nameContent);

		setContent(new ContentInsideRectangle(verticalGroupContent));

		setTextColor(super.getTextColor());
	}

	@Override
	protected INode copy() throws CloneNotSupportedException
	{
		return new ExternalSystemExitPointNode(this);
	}

	@Override
	public String getToolTip()
	{
		return StateDiagramConstant.STATE_DIAGRAM_RESOURCE.getString("external_system_exit_point_node.tooltip");
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
	 * text label near node
	 */
	protected SingleLineText name;
	/** default node diameter */
	private static int DEFAULT_DIAMETER = 14;
}
