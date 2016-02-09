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

package com.horstmann.violet.product.diagram.activity;

import com.horstmann.violet.product.diagram.abstracts.node.RoundRectangularNode;
import com.horstmann.violet.product.diagram.abstracts.property.MultiLineString;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * An activity node in an activity diagram.
 */
public class PageLinkNode extends RoundRectangularNode
{
    /**
     * Construct an action node with a default size
     */
    public PageLinkNode()
    {
        name = new MultiLineString();
    }

    @Override
    public void selfDraw(Graphics2D g2, Rectangle2D bounds) {
        name.draw(g2, getBounds());
    }

    @Override
    public Double getWidth() {
        return DEFAULT_WIDTH;
    }

    @Override
    public Double getHeight() {
        return DEFAULT_HEIGHT;
    }

    @Override
    public Shape getShape()
    {
        return new Ellipse2D.Double(getBounds().getX(), getBounds().getY(), getBounds().getWidth(), getBounds().getHeight());
    }

    /**
     * Sets the name property value.
     * 
     * @param newValue the new action name
     */
    public void setName(MultiLineString newValue)
    {
        name = newValue;
    }

    /**
     * Gets the name property value.
     * 
     * @param the action name
     */
    public MultiLineString getName()
    {
        return name;
    }

    @Override
    public PageLinkNode clone()
    {
        PageLinkNode cloned = (PageLinkNode) super.clone();
        cloned.name = name.clone();
        return cloned;
    }

    private MultiLineString name;

    private static Double DEFAULT_WIDTH = 30.0;
    private static Double DEFAULT_HEIGHT = 30.0;
}
