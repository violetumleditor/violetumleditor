package com.horstmann.violet.product.diagram.state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class ExternalSystemEntryPointNode extends ExternalSystemNode {
	
	
	/* (non-Javadoc)
	 * We draw here circle and fill it with color
	 * @see com.horstmann.violet.product.diagram.state.ExternalSystemNode#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g2) {
		super.draw(g2);
		Color oldColor = g2.getColor();
		double radius = getBounds().getWidth() / 2;
		Ellipse2D circle = new Ellipse2D.Double(getBounds().getX(), getBounds().getY(), 2.0 * radius, 2.0 * radius);
		g2.setColor(getBackgroundColor());
		g2.fill(circle);
		g2.setColor(getBorderColor());
		g2.draw(circle);
		g2.setColor(oldColor);
	}
	
	/* (non-Javadoc)
	 * Clone method for node
	 * @see com.horstmann.violet.product.diagram.abstracts.node.AbstractNode#clone()
	 */
	public ExternalSystemEntryPointNode clone() {
		ExternalSystemEntryPointNode cloned = (ExternalSystemEntryPointNode) super.clone();
		cloned.name = name.clone();
		return cloned;
	}
}
