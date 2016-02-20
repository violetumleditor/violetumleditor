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

package com.horstmann.violet.product.diagram.object.edges;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.edge.ShapeEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.framework.property.ArrowheadChoiceList;

/**
 * An S- or C-shaped edge with an arrowhead.
 */
public class ObjectReferenceEdge extends ShapeEdge
{
    public void draw(Graphics2D g2)
    {
        g2.draw(getShape());
        Line2D line = getConnectionPoints();
        double x1;
        double x2 = line.getX2();
        double y = line.getY2();
        if (isSShaped()) x1 = x2 - END_SIZE;
        else x1 = x2 + END_SIZE;
        ArrowheadChoiceList.TRIANGLE_BLACK.draw(g2, new Point2D.Double(x1, y), new Point2D.Double(x2, y));
    }

    public Shape getShape()
    {
        Line2D line = getConnectionPoints();

        double y1 = line.getY1();
        double y2 = line.getY2();
        double yMid = (line.getY1() + line.getY2()) / 2;

        GeneralPath p = new GeneralPath();
        if (isSShaped())
        {
            double x1 = getStart().getBounds().getMaxX() + START_SIZE;
            double x2 = line.getX2() - END_SIZE;
            double xMid = (x1 + x2) / 2;

            p.moveTo((float) line.getX1(), (float) y1);
            p.lineTo((float) x1, (float) y1);
            p.quadTo((float) ((x1 + xMid) / 2), (float) y1, (float) xMid, (float) yMid);
            p.quadTo((float) ((x2 + xMid) / 2), (float) y2, (float) x2, (float) y2);
            p.lineTo((float) line.getX2(), (float) y2);
        }
        else
        // reverse C shaped
        {
            double x1 = Math.max(getStart().getBounds().getMaxX(), line.getX2()+END_SIZE) + START_SIZE;
            double x2 = x1 + END_SIZE;
            p.moveTo((float) line.getX1(), (float) y1);
            p.lineTo((float) x1, (float) y1);
            p.quadTo((float) x2, (float) y1, (float) x2, (float) yMid);
            p.quadTo((float) x2, (float) y2, (float) x1, (float) y2);
            p.lineTo((float) line.getX2(), (float) y2);
        }
        return p;
    }

    
    @Override
    public Direction getDirection(INode node) {
        // Case 1 : start node_old
        if (node.equals(getStart())) {
            return Direction.WEST;
        }
        // Case 2 : end node_old
        if (isSShaped()) {
            return Direction.EAST;
        }
        return Direction.WEST;
    }

    /**
     * Tests whether the node_old should be S- or C-shaped.
     * 
     * @return true if the node_old should be S-shaped
     */
    private boolean isSShaped()
    {
        return (2 * END_SIZE) <= (getEnd().getBounds().getMinX() - getStart().getBounds().getMaxX());
    }

    private static final int END_SIZE = 12;
    private static final int START_SIZE = 2;
}
