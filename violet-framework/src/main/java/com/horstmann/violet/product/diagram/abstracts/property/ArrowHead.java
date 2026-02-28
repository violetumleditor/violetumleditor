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

package com.horstmann.violet.product.diagram.abstracts.property;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import com.horstmann.violet.framework.util.SerializableEnumeration;

/**
 * This class defines arrow heads of various shapes.
 */
public class ArrowHead extends SerializableEnumeration
{

    /**
     * Draws the arrowhead with default size.
     * 
     * @param g2 the graphics context
     * @param p a point on the axis of the arrow head
     * @param q the end point of the arrow head
     */
    public void draw(Graphics2D g2, Point2D p, Point2D q)
    {
        draw(g2, p, q, 1.0f);
    }

    /**
     * Draws the arrowhead scaled by the given factor.
     * 
     * @param g2 the graphics context
     * @param p a point on the axis of the arrow head
     * @param q the end point of the arrow head
     * @param scale the scale factor (1.0 = default size, larger = bigger)
     */
    public void draw(Graphics2D g2, Point2D p, Point2D q, float scale)
    {
        GeneralPath path = getPath(p, q, scale);
        Color oldColor = g2.getColor();
        Stroke oldStroke = g2.getStroke();
        if (scale > 1.0f)
        {
            g2.setStroke(new BasicStroke(scale));
        }
        if (this != V && this != HALF_V && this != NONE)
        {
           if (this == BLACK_DIAMOND || this == BLACK_TRIANGLE)
              g2.setColor(Color.BLACK);
           else
              g2.setColor(Color.WHITE);
           g2.fill(path);
        }        
        
        g2.setColor(oldColor);
        g2.draw(path);
        g2.setStroke(oldStroke);
    }

    /**
     * Gets the path of the arrowhead with default size.
     * 
     * @param p a point on the axis of the arrow head
     * @param q the end point of the arrow head
     * @return the path
     */
    public GeneralPath getPath(Point2D p, Point2D q)
    {
        return getPath(p, q, 1.0f);
    }

    /**
     * Gets the path of the arrowhead scaled by the given factor.
     * 
     * @param p a point on the axis of the arrow head
     * @param q the end point of the arrow head
     * @param scale the scale factor (1.0 = default size, larger = bigger)
     * @return the path
     */
    public GeneralPath getPath(Point2D p, Point2D q, float scale)
    {
        GeneralPath path = new GeneralPath();
        if (this == NONE) return path;
        final double ARROW_ANGLE = Math.PI / 6;
        final double ARROW_LENGTH = 5 * (1 + scale);

        double dx = q.getX() - p.getX();
        double dy = q.getY() - p.getY();
        double angle = Math.atan2(dy, dx);
        double x1 = q.getX() - ARROW_LENGTH * Math.cos(angle + ARROW_ANGLE);
        double y1 = q.getY() - ARROW_LENGTH * Math.sin(angle + ARROW_ANGLE);
        double x2 = q.getX() - ARROW_LENGTH * Math.cos(angle - ARROW_ANGLE);
        double y2 = q.getY() - ARROW_LENGTH * Math.sin(angle - ARROW_ANGLE);

        if (this == V)
        {
            path.moveTo((float) x1, (float) y1);
            path.lineTo((float) q.getX(), (float) q.getY());
            path.lineTo((float) x2, (float) y2);
            path.lineTo((float) q.getX(), (float) q.getY());
            path.lineTo((float) x1, (float) y1);
            path.closePath();
        }
        else if (this == TRIANGLE || this == BLACK_TRIANGLE)
        {
            path.moveTo((float) q.getX(), (float) q.getY());
            path.lineTo((float) x1, (float) y1);
            path.lineTo((float) x2, (float) y2);
            path.closePath();
        }
        else if (this == DIAMOND || this == BLACK_DIAMOND)
        {
            path.moveTo((float) q.getX(), (float) q.getY());
            path.lineTo((float) x1, (float) y1);
            double x3 = x2 - ARROW_LENGTH * Math.cos(angle + ARROW_ANGLE);
            double y3 = y2 - ARROW_LENGTH * Math.sin(angle + ARROW_ANGLE);
            path.lineTo((float) x3, (float) y3);
            path.lineTo((float) x2, (float) y2);
            path.closePath();
        }
        return path;
    }

    /**
     * Returns the length of the arrow head along its axis for a given scale.
     * This is how far the base of the arrow is from the tip.
     * Used to shorten the edge line so it doesn't show behind the arrow.
     *
     * @param scale the scale factor
     * @return the arrow base length in pixels, 0 for NONE
     */
    public double getArrowBaseLength(float scale)
    {
        if (this == NONE) return 0;
        double arrowLength = 5 * (1 + scale);
        if (this == DIAMOND || this == BLACK_DIAMOND)
        {
            // Diamond is twice the arrow length along the axis
            return 2 * arrowLength * Math.cos(Math.PI / 6);
        }
        return arrowLength * Math.cos(Math.PI / 6);
    }

    /** Array head type : this head has no shape */
    public static final ArrowHead NONE = new ArrowHead();

    /** Array head type : this head is a triangle */
    public static final ArrowHead TRIANGLE = new ArrowHead();

    /** Array head type : this head is a black filled triangle */
    public static final ArrowHead BLACK_TRIANGLE = new ArrowHead();

    /** Array head type : this head is a V */
    public static final ArrowHead V = new ArrowHead();

    /** Array head type : this head is a half V */
    public static final ArrowHead HALF_V = new ArrowHead();

    /** Array head type : this head is a diamond */
    public static final ArrowHead DIAMOND = new ArrowHead();

    /** Array head type : this head is black filled diamond */
    public static final ArrowHead BLACK_DIAMOND = new ArrowHead();

    /** Internal Java UID */
    private static final long serialVersionUID = -3824887997763775890L;

}
