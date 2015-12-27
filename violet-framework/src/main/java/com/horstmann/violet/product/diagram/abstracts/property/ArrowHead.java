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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.horstmann.violet.framework.util.SerializableEnumeration;

/**
 * This class defines arrow heads of various shapes.
 */
public class ArrowHead extends SerializableEnumeration
{
    static private class FilledArrowHead extends ArrowHead
    {
        public FilledArrowHead(Color color)
        {
            this.color = color;
        }

        @Override
        protected void fillPath(Graphics2D g2, GeneralPath path) {
            g2.setColor(color);
            g2.fill(path);
        }

        private Color color;
    }

    /**
     * Draws the arrowhead.
     * 
     * @param g2 the graphics context
     * @param p a point on the axis of the arrow head
     * @param q the end point of the arrow head
     */
    public void draw(Graphics2D g2, Point2D p, Point2D q)
    {
        Color oldColor = g2.getColor();

        GeneralPath path = getPath();
        rotatePath(path, calculateAngle(q, p));

        g2.translate(q.getX(), q.getY());
        fillPath(g2, path);
        g2.setColor(Color.BLACK);
        g2.draw(path);
        g2.translate(-q.getX(), -q.getY());
        g2.setColor(oldColor);
    }

    public GeneralPath getPath()
    {
        return new GeneralPath();
    }

    protected GeneralPath getPathTypeV()
    {
        GeneralPath path = new GeneralPath();
        double x = ARROW_LENGTH * Math.cos(ARROW_ANGLE);
        double y = ARROW_LENGTH * Math.sin(ARROW_ANGLE);

        path.moveTo((float) x, (float) y);
        path.lineTo((float) 0, (float) 0);
        path.lineTo((float) x, (float) -y);

        return path;
    }
    protected GeneralPath getPathTypeTriangle()
    {
        GeneralPath path = new GeneralPath();
        double x = ARROW_LENGTH * Math.cos(ARROW_ANGLE);
        double y = ARROW_LENGTH * Math.sin(ARROW_ANGLE);

        path.moveTo((float) 0, (float) 0);
        path.lineTo((float) x, (float) y);
        path.lineTo((float) x, (float) -y);
        path.lineTo((float) 0, (float) 0);

        return path;
    }
    protected GeneralPath getPathTypeDiamond()
    {
        GeneralPath path = new GeneralPath();
        double x = ARROW_LENGTH * Math.cos(ARROW_ANGLE);
        double y = ARROW_LENGTH * Math.sin(ARROW_ANGLE);

        path.moveTo((float) 0, (float) 0);
        path.lineTo((float) x, (float) y);
        path.lineTo((float) 2*x, (float) 0);
        path.lineTo((float) x, (float) -y);
        path.lineTo((float) 0, (float) 0);

        return path;
    }
    protected GeneralPath getPathTypeX()
    {
        final double CROSS_ANGLE = Math.PI / 4;

        GeneralPath path = new GeneralPath();
        double x = 0.75 * ARROW_LENGTH * Math.cos(CROSS_ANGLE);
        double y = 0.75 * ARROW_LENGTH * Math.sin(CROSS_ANGLE);

//        path.moveTo((float) 2*x, (float) y);
//        path.lineTo((float) 0, (float) -y);
//        path.moveTo((float) 2*x, (float) -y);
//        path.lineTo((float) 0, (float) y);

        path.moveTo((float) x, (float) y);
        path.lineTo((float) -x, (float) -y);
        path.moveTo((float) x, (float) -y);
        path.lineTo((float) -x, (float) y);

        return path;
    }

    private double calculateAngle(Point2D p, Point2D q)
    {
        return Math.atan2(q.getY() - p.getY(), q.getX() - p.getX());
    }

    private void rotatePath(GeneralPath basePath, double angle)
    {
        AffineTransform af = new AffineTransform();
        af.rotate(angle);
        basePath.transform(af);
    }

    protected void fillPath(Graphics2D g2, GeneralPath basePath){};

    protected static final double ARROW_ANGLE = Math.PI / 6;
    protected static final double ARROW_LENGTH = 10;

    /** Array head type : this head has no shape */
    public static final ArrowHead NONE = new ArrowHead();

    /** Array head type : this head is a triangle */
    public static final ArrowHead TRIANGLE = new FilledArrowHead(Color.WHITE){
        @Override
        public GeneralPath getPath()
        {
            return getPathTypeTriangle();
        }
    };

    /** Array head type : this head is a black filled triangle */
    public static final ArrowHead BLACK_TRIANGLE = new FilledArrowHead(Color.BLACK){
        @Override
        public GeneralPath getPath()
        {
            return getPathTypeTriangle();
        }
    };

    /** Array head type : this head is a V */
    public static final ArrowHead V = new ArrowHead(){
        @Override
        public GeneralPath getPath()
        {
            return getPathTypeV();
        }
    };

    /** Array head type : this head is a half V */
//    public static final ArrowHead HALF_V = new ArrowHead();

    /** Array head type : this head is a diamond */
    public static final ArrowHead DIAMOND = new FilledArrowHead(Color.WHITE){
        @Override
        public GeneralPath getPath()
        {
            return getPathTypeDiamond();
        }
    };

    /** Array head type : this head is black filled diamond */
    public static final ArrowHead BLACK_DIAMOND = new FilledArrowHead(Color.BLACK){
        @Override
        public GeneralPath getPath()
        {
            return getPathTypeDiamond();
        }
    };

    /** Array head type : this head is a X */
    public static final ArrowHead X = new ArrowHead(){
        @Override
        public GeneralPath getPath()
        {
            return getPathTypeX();
        }
    };

    /** Internal Java UID */
    private static final long serialVersionUID = -3824887997763775890L;

}
