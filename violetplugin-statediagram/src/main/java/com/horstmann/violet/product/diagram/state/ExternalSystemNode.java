/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.product.diagram.state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.horstmann.violet.product.diagram.abstracts.node.EllipticalNode;
import com.horstmann.violet.product.diagram.abstracts.property.MultiLineString;
import com.horstmann.violet.workspace.sidebar.colortools.ColorToolsBarPanel;

/**
 * An superclass for external system nodes
 */
public class ExternalSystemNode extends EllipticalNode
{
 
	/**
	 * text label near node
	 */
	protected MultiLineString name;
    /** default node diameter */
    private static int DEFAULT_DIAMETER = 14;

    /**
     * default node gap
     */
    private static int DEFAULT_GAP = 3;
    
    
    public ExternalSystemNode()
    {
        super();
        name = new MultiLineString();
        setBackgroundColor(ColorToolsBarPanel.PASTEL_GREY.getBackgroundColor());
        setBorderColor(ColorToolsBarPanel.PASTEL_GREY.getBorderColor());
        setTextColor(ColorToolsBarPanel.PASTEL_GREY.getTextColor());
    }

    /* (non-Javadoc)
     * Get External System Node bounds
     * @see com.horstmann.violet.product.diagram.abstracts.node.INode#getBounds()
     */
    @Override
    public Rectangle2D getBounds()
    {
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


	
	/* (non-Javadoc)
	 * This draw override is only used for drawing text
	 * @see com.horstmann.violet.product.diagram.abstracts.node.RectangularNode#draw(java.awt.Graphics2D)
	 */
	public void draw(Graphics2D g2)
	{
		double radius = getBounds().getHeight()/2;
		Rectangle2D textRectangle2D = getBounds();
		textRectangle2D.setRect(textRectangle2D.getX() + radius, textRectangle2D.getY() + 2 * radius,
				textRectangle2D.getHeight() + radius, textRectangle2D.getWidth() + radius);

		name.draw(g2, textRectangle2D);
		
	}

}
