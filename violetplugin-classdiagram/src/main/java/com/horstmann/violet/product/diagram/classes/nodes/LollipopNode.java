package com.horstmann.violet.product.diagram.classes.nodes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.horstmann.violet.product.diagram.abstracts.node.EllipticalNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.RectangularNode;
import com.horstmann.violet.product.diagram.abstracts.property.MultiLineString;
import com.horstmann.violet.product.diagram.common.PointNode;
import com.horstmann.violet.workspace.sidebar.colortools.ColorToolsBarPanel;

/**
 * @author Jakub Homlala
 * This class represents Lollipop node (Ball interface notification).
 */
public class LollipopNode extends EllipticalNode {
	/**
	 * Text label near lollipop node
	 */
	private MultiLineString name;
	/**
	 * Diameter of circle used for creating bounds
	 */
	private static int DEFAULT_DIAMETER = 14;

	/**
	 * Gap of circle used for creating bounds
	 */
	private static int DEFAULT_GAP = 3;

	/**
	 * Constructor, setting default values for properties, setting colors of
	 * node.
	 */
	public LollipopNode() {
		super();
		name = new MultiLineString();

		setBackgroundColor(ColorToolsBarPanel.PASTEL_GREY.getBackgroundColor());
		setBorderColor(ColorToolsBarPanel.PASTEL_GREY.getBorderColor());
		setTextColor(ColorToolsBarPanel.PASTEL_GREY.getTextColor());
	}

	/*
	 * (non-Javadoc) Get bounds of socket node. We calculate this based on
	 * diameter and gap.
	 * 
	 * @see
	 * com.horstmann.violet.product.diagram.abstracts.node.INode#getBounds()
	 */
	@Override
	public Rectangle2D getBounds() {
		Point2D currentLocation = getLocation();
		double x = currentLocation.getX();
		double y = currentLocation.getY();
		double w = DEFAULT_DIAMETER + 2 * DEFAULT_GAP;
		double h = DEFAULT_DIAMETER + 2 * DEFAULT_GAP;
		Rectangle2D currentBounds = new Rectangle2D.Double(x, y, w, h);
		Rectangle2D snappedBounds = getGraph().getGridSticker().snap(currentBounds);
		return snappedBounds;
	}

	/**
	 * Sets the name property value.
	 * 
	 * @param newValue
	 *            the new state name
	 */
	public void setName(MultiLineString newValue) {
		name = newValue;
	}

	/**
	 * Gets the name property value.
	 * 
	 * @param the
	 *            state name
	 */
	public MultiLineString getName() {
		return name;
	}

	/*
	 * (non-Javadoc) This method is used for drawing circle. We use Ellipse2D
	 * for creating simple circle. Because socket can be added for example in
	 * Package node, we need to correct node location(translate).
	 * 
	 * @see
	 * com.horstmann.violet.product.diagram.abstracts.node.RectangularNode#draw(
	 * java.awt.Graphics2D)
	 */
	@Override
	public void draw(Graphics2D g2) {

		Color oldColor = g2.getColor();
		Point2D nodeLocationOnGraph = getLocationOnGraph();
		Point2D nodeLocation = getLocation();
		Point2D g2Location = new Point2D.Double(nodeLocationOnGraph.getX() - nodeLocation.getX(),
				nodeLocationOnGraph.getY() - nodeLocation.getY());
		g2.translate(g2Location.getX(), g2Location.getY());
		super.draw(g2);
		Rectangle2D currentBounds = getBounds();
		double radius = getBounds().getWidth() / 2;

		Ellipse2D circle = new Ellipse2D.Double(currentBounds.getX(), currentBounds.getY(), 2.0 * radius, 2.0 * radius);

		g2.setColor(getBackgroundColor());
		g2.fill(circle);
		g2.setColor(getBorderColor());

		g2.draw(circle);

		g2.setColor(oldColor);
		Rectangle2D textRectangle2D = currentBounds;
		textRectangle2D.setRect(textRectangle2D.getX() + radius, textRectangle2D.getY() + 2 * radius,
				textRectangle2D.getHeight() + radius, textRectangle2D.getWidth() + radius);

		name.draw(g2, textRectangle2D);

		g2.translate(-g2Location.getX(), -g2Location.getY());
	}
	/*
	 * (non-Javadoc) This method is used for making clone elements not related
	 * with each other. Every property field should be cloned with specific
	 * clone method.
	 * 
	 * @see
	 * com.horstmann.violet.product.diagram.abstracts.node.AbstractNode#clone()
	 */
	public LollipopNode clone() {
		LollipopNode cloned = (LollipopNode) super.clone();
		cloned.name = name.clone();
		return cloned;
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
}
