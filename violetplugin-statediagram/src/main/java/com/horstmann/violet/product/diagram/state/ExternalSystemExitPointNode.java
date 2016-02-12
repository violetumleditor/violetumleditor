package com.horstmann.violet.product.diagram.state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import com.horstmann.violet.product.diagram.abstracts.property.MultiLineString;

public class ExternalSystemExitPointNode extends ExternalSystemNode {

	/* (non-Javadoc)
	 * We draw circle here and X inside
	 * @see com.horstmann.violet.product.diagram.state.ExternalSystemNode#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g2) {
		super.draw(g2);
//
		Color oldColor = g2.getColor();
		double radius = getBounds().getWidth() / 2;
		double lineLength = radius * 0.7;
		Ellipse2D circle = new Ellipse2D.Double(getBounds().getX(), getBounds().getY(), 2.0 * radius, 2.0 * radius);

		g2.setColor(getBackgroundColor());
		g2.fill(circle);
		g2.setColor(getBorderColor());

		double centerX = circle.getCenterX();
		double centerY = circle.getCenterY();
		Line2D lineLeft = new Line2D.Double(centerX - lineLength, centerY - lineLength, centerX + lineLength,
				centerY + lineLength);
		g2.draw(lineLeft);
		Line2D lineRight = new Line2D.Double(centerX + lineLength, centerY - lineLength, centerX - lineLength,
				centerY + lineLength);
		g2.draw(lineRight);
		g2.draw(circle);
		g2.setColor(oldColor);
	}

	/* (non-Javadoc)
	 * Method used for cloning this node
	 * @see com.horstmann.violet.product.diagram.abstracts.node.AbstractNode#clone()
	 */
	public ExternalSystemExitPointNode clone() {
		ExternalSystemExitPointNode cloned = (ExternalSystemExitPointNode) super.clone();
		cloned.name = name.clone();
		return cloned;
	}
}
