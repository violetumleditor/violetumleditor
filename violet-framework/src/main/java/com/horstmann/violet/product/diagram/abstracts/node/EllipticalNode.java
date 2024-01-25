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

package com.horstmann.violet.product.diagram.abstracts.node;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.RectangularNode;

/**
 * An elliptical (or circular) node.
 */
public abstract class EllipticalNode extends RectangularNode
{

    @Override
    public Point2D getConnectionPoint(IEdge e)
    {
        Direction d = e.getDirection(this).turn(180);
        Rectangle2D bounds = getBounds();
        double a = bounds.getWidth() / 2;
        double b = bounds.getHeight() / 2;
        double x = d.getX();
        double y = d.getY();
        double cx = bounds.getCenterX();
        double cy = bounds.getCenterY();

        if (a != 0 && b != 0 && !(x == 0 && y == 0))
        {
            double t = Math.sqrt((x * x) / (a * a) + (y * y) / (b * b));
            return new Point2D.Double(cx + x / t, cy + y / t);
        }
        else
        {
            return new Point2D.Double(cx, cy);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.AbstractNode#getShape()
     */
    public Shape getShape()
    {
        return new Ellipse2D.Double(getBounds().getX(), getBounds().getY(), getBounds().getWidth() - 1, getBounds().getHeight() - 1);
    }

}
