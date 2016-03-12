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

package com.horstmann.violet.product.diagram.common.node;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.horstmann.violet.framework.graphics.content.EmptyContent;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRectangle;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;

/**
 * An inivisible node_old that is used in the toolbar to draw an edge, and in notes to serve as an end point of the node_old connector.
 */
public class PointNode extends AbstractNode
{
    public boolean contains(Point2D p)
    {
        final double THRESHOLD = 5;
        return getLocation().distance(p) < THRESHOLD;
    }

    public Rectangle2D getBounds()
    {
        return new Rectangle2D.Double(getLocation().getX(), getLocation().getY(), 0, 0);
    }

    @Override
    public Point2D getLocation()
    {
        if (tempLocation != null) return tempLocation;
        else return super.getLocation();
    }
    
    public void setBounds(Rectangle2D bounds)
    {
        if (tempLocation != null) tempLocation.setLocation(bounds.getX(), bounds.getY());
    }
    
    public Point2D getConnectionPoint(IEdge edge)
    {
        return getLocation();
    }
    
    @Override
    public void translate(double dx, double dy)
    {
        super.translate(dx, dy);
        tempLocation = null; 
    }
    
    private Point2D.Double tempLocation = new Point2D.Double(); 
    // Legacy grief--some versions of the XML encoder wrote calls to setBounds
    // We use the location set by setBounds until the first call to translate.

    @Override
    protected void createContentStructure()
    {
        setContent(new ContentInsideRectangle(new EmptyContent()));
    }

    @Override
    public void draw(Graphics2D g2)
    {
        // Invisible node_old
    }

    @Override
    public String getToolTip()
    {
        return "";
    }
}
