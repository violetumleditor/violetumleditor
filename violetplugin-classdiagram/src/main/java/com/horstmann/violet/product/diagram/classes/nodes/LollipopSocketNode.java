package com.horstmann.violet.product.diagram.classes.nodes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.RectangularNode;
import com.horstmann.violet.product.diagram.abstracts.property.ChoiceList;
import com.horstmann.violet.product.diagram.abstracts.property.MultiLineString;
import com.horstmann.violet.product.diagram.common.PointNode;
import com.horstmann.violet.workspace.sidebar.colortools.ColorToolsBarPanel;

/**
 * @author Jakub Homlala This class represents Lollipop socket (Socket interface
 *         notification).
 */
public class LollipopSocketNode extends RectangularNode {

	/**
	 * Text label near socket
	 */
	private MultiLineString name;

	/**
	 * Type of socket (left,right,top,bottom)
	 */
	private ChoiceList type;

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
	public LollipopSocketNode() {
		super();
		name = new MultiLineString();
		String[] typesArray = { "top", "bottom", "left", "right" };
		type = new ChoiceList(typesArray);

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
	 * (non-Javadoc) This method is used for drawing half circle. Drawing socket
	 * is done with Arc2D. Our node can be drawn in 4 types
	 * (left,right,top,bottom). For example if property left is selected from
	 * choice list, Arc will be drawn with 270 degrees. Because socket can be
	 * added for example in Package node, we need to correct node
	 * location(translate).
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
		g2.setColor(getBorderColor());
		double radius = getBounds().getWidth() / 2;
		Arc2D.Double arc = new Arc2D.Double(getBounds().getX(), getBounds().getY(), 2 * radius, 2 * radius, getDegrees(), 180,
				Arc2D.OPEN);
		g2.draw(arc);
		g2.setColor(oldColor);
		Rectangle2D textRectangle2D = getBounds();
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
	@Override
	public LollipopSocketNode clone() {
		LollipopSocketNode cloned = (LollipopSocketNode) super.clone();
		cloned.name = name.clone();
		cloned.type = type.clone();
		return cloned;
	}

	/*
	 * (non-Javadoc) This method is used for setting right starting point for
	 * wire
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

	/**
	 * This method returns degree based on socket type selected in properties
	 * 
	 * @return degree of socket node
	 */
	private int getDegrees() {
		int degrees = 0;
		if (type != null) {
			String selected = type.getSelectedItem();
			if (selected != null) {
				if (selected.equals("top"))
					degrees = 180;
				else if (selected.equals("bottom"))
					degrees = 0;
				else if (selected.equals("right"))
					degrees = 90;
				else if (selected.equals("left"))
					degrees = 270;
			}
		}

		return degrees;
	}

	/**
	 * This getter is used for violet framework in order to make type property
	 * visible in property editor. Get choice type list used in node.
	 * 
	 * @return type choice list
	 */
	public ChoiceList getType() {
		return type;
	}

	/**
	 * This setter is used for violet framework in order to make type property
	 * visible in property editor. Set node type choice list.
	 * 
	 * @param type
	 *            -
	 */
	public void setType(ChoiceList type) {
		this.type = type;
	}

}
