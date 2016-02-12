package com.horstmann.violet.product.diagram.classes.nodes;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.property.string.LineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.SingleLineText;
import com.horstmann.violet.product.diagram.common.PointNode;
import com.horstmann.violet.workspace.sidebar.colortools.ColorToolsBarPanel;

/**
 * @author Jakub Homlala
 * This class represents Lollipop node (Ball interface notification).
 */
public class LollipopNode extends ColorableNode {

    protected static class Lollipop implements ContentInsideCustomShape.ShapeCreator {
        public Lollipop(TextContent nameContent) {
            super();
            this.nameContent = nameContent;
        }

        @Override
        public Shape createShape(int contentWidth, int contentHeight)
        {
            final int radius = DEFAULT_DIAMETER + 2 * DEFAULT_GAP;

            double x = (this.nameContent.getBounds().getWidth() / 2);

            if(this.nameContent.getBounds().getWidth() > 0)
            {
                x -= (radius / 2);
            }

            return new Ellipse2D.Double(x, 0, radius, radius);
        }

        private TextContent nameContent;
    }

	/**
	 * Constructor, setting default values for properties, setting colors of
	 * node.
	 */
	public LollipopNode()
	{
		super();
		name = new SingleLineText();
        name.setAlignment(LineText.CENTER);

		setBackgroundColor(ColorToolsBarPanel.PASTEL_GREY.getBackgroundColor());
		setBorderColor(ColorToolsBarPanel.PASTEL_GREY.getBorderColor());
		setTextColor(ColorToolsBarPanel.PASTEL_GREY.getTextColor());
	}

	protected LollipopNode(LollipopNode node) throws CloneNotSupportedException
	{
		super(node);
		name = node.name.clone();
		createContentStructure();
	}

	@Override
	public void deserializeSupport()
	{
		super.deserializeSupport();
		name.deserializeSupport();
	}

	@Override
	protected INode copy() throws CloneNotSupportedException
	{
		return new LollipopNode(this);
	}

	/*
	 * (non-Javadoc) This method is used for drawing circle and text below.
	 *
	 * @see
	 * com.horstmann.violet.product.diagram.abstracts.node.RectangularNode#draw(
	 * java.awt.Graphics2D)
	 */
	@Override
	public void createContentStructure() {

		TextContent nameContent = new TextContent(this.name);
        EmptyContent empty = new EmptyContent();

        ContentInsideShape contentInsideShape = new ContentInsideCustomShape(empty, new Lollipop(nameContent));

		setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
		setBackground(new ContentBackground(getBorder(), getBackgroundColor()));

		setTextColor(super.getTextColor());

        VerticalLayout verticalGroupContent = new VerticalLayout();
        verticalGroupContent.add(getBackground());
        verticalGroupContent.add(nameContent);

        setContent(verticalGroupContent);
	}

	/**
	 * Sets the name property value.
	 * 
	 * @param newValue
	 *            the new state name
	 */
	public void setName(SingleLineText newValue) {
		name = newValue;
	}

	/**
	 * Gets the name property value.
	 * 
	 * @return state name
	 */
	public SingleLineText getName() {
		return name;
	}


	
	/*
	 * (non-Javadoc) This method is used for setting right starting point for wire
	 * 
	 * @see
	 * com.horstmann.violet.product.diagram.abstracts.node.AbstractNode#addChild
	 * (com.horstmann.violet.product.diagram.abstracts.node.INode,
	 * java.awt.geom.Point2D)
	 */
	@Override
	public boolean addChild(INode n, Point2D p) {
		if (n instanceof PointNode) {
			return true;
		}
		return false;
	}

    private SingleLineText name;

    /**
     * Diameter of circle used for creating bounds
     */
    private static int DEFAULT_DIAMETER = 10;

    /**
     * Gap of circle used for creating bounds
     */
    private static int DEFAULT_GAP = 6;
}
