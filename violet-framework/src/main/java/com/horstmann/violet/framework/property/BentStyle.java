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

package com.horstmann.violet.framework.property;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

import com.horstmann.violet.framework.util.SerializableEnumeration;

/**
 * A style for a segmented line that indicates the number and sequence of bends.
 */
public class BentStyle extends SerializableEnumeration
{


    /**
     * Default constructor
     */
    private BentStyle()
    {
    }

    /**
     * Gets the four connecting points at which a bent line connects to a rectangle.
     */
    private Point2D[] connectionPoints(Rectangle2D r)
    {
        Point2D[] a = new Point2D[4];
        a[0] = new Point2D.Double(r.getX(), r.getCenterY());
        a[1] = new Point2D.Double(r.getMaxX(), r.getCenterY());
        a[2] = new Point2D.Double(r.getCenterX(), r.getY());
        a[3] = new Point2D.Double(r.getCenterX(), r.getMaxY());
        return a;
    }

    /**
     * Gets the points at which a line joining two rectangles is bent according to a bent style.
     * 
     * @param connectionPoints the points to use (starting point, optional middle points and ending points merged in an array of
     *            points)
     * @return an array list of points at which to bend the segmented line joining the two rectangles
     */
    public ArrayList<Point2D> getPath(Point2D... connectionPoints)
    {
        if (connectionPoints.length < 2)
        {
            throw new RuntimeException("BentStyle need at least two points to process the path of an edge");
        }

        Point2D startingPoint = connectionPoints[0];
        Point2D endingPoint = connectionPoints[connectionPoints.length - 1];

        ArrayList<Point2D> r = null;

        // Try to get current path
        if (this == STRAIGHT) r = getStraightPath(startingPoint, endingPoint);
        else if (this == FREE) r = getFreePath(connectionPoints);
        else if (this == HV) r = getHVPath(startingPoint, endingPoint);
        else if (this == VH) r = getVHPath(startingPoint, endingPoint);
        else if (this == HVH) r = getHVHPath(startingPoint, endingPoint);
        else if (this == VHV) r = getVHVPath(startingPoint, endingPoint);
        if (r != null) return r;

        // Try to inverse path
        if (startingPoint.equals(endingPoint)) r = getSelfPath(startingPoint);
        else if (this == HVH) r = getVHVPath(startingPoint, endingPoint);
        else if (this == VHV) r = getHVHPath(startingPoint, endingPoint);
        else if (this == HV) r = getVHPath(startingPoint, endingPoint);
        else if (this == VH) r = getHVPath(startingPoint, endingPoint);
        else if (this == FREE) r = getFreePath(connectionPoints);
        if (r != null) return r;

        // Return default path
        return getStraightPath(startingPoint, endingPoint);
    }

    /**
     * Gets an Vertical-Horizontal-Vertival path
     * 
     * @param startingRectangle
     * @param endingRectangle
     * @return an array list of points
     */
    private ArrayList<Point2D> getVHVPath(Point2D startingPoint, Point2D endingPoint)
    {

        ArrayList<Point2D> r = new ArrayList<Point2D>();
        double x1 = startingPoint.getX();
        double x2 = endingPoint.getX();
        double y1;
        double y2;
        if (startingPoint.getY() + 2 * MIN_SEGMENT <= endingPoint.getY())
        {
            y1 = startingPoint.getY();
            y2 = endingPoint.getY();
        }
        else if (endingPoint.getY() + 2 * MIN_SEGMENT <= startingPoint.getY())
        {
            y1 = startingPoint.getY();
            y2 = endingPoint.getY();

        }
        else return null;
        if (Math.abs(x1 - x2) <= MIN_SEGMENT)
        {
            r.add(new Point2D.Double(x2, y1));
            r.add(new Point2D.Double(x2, y2));
        }
        else
        {
            r.add(new Point2D.Double(x1, y1));
            r.add(new Point2D.Double(x1, (y1 + y2) / 2));
            r.add(new Point2D.Double(x2, (y1 + y2) / 2));
            r.add(new Point2D.Double(x2, y2));
        }
        return r;
    }

    /**
     * Gets an Horizontal-Vertical-Horizontal path
     * 
     * @param startingRectangle
     * @param endingRectangle
     * @return an array list of points
     */
    private ArrayList<Point2D> getHVHPath(Point2D startingPoint, Point2D endingPoint)
    {
        ArrayList<Point2D> r = new ArrayList<Point2D>();
        double x1;
        double x2;
        double y1 = startingPoint.getY();
        double y2 = endingPoint.getY();
        if (startingPoint.getX() + 2 * MIN_SEGMENT <= endingPoint.getX())
        {
            x1 = startingPoint.getX();
            x2 = endingPoint.getX();
        }
        else if (endingPoint.getX() + 2 * MIN_SEGMENT <= startingPoint.getX())
        {
            x1 = startingPoint.getX();
            x2 = endingPoint.getX();
        }
        else return null;
        if (Math.abs(y1 - y2) <= MIN_SEGMENT)
        {
            r.add(new Point2D.Double(x1, y2));
            r.add(new Point2D.Double(x2, y2));
        }
        else
        {
            r.add(new Point2D.Double(x1, y1));
            r.add(new Point2D.Double((x1 + x2) / 2, y1));
            r.add(new Point2D.Double((x1 + x2) / 2, y2));
            r.add(new Point2D.Double(x2, y2));
        }
        return r;
    }

    /**
     * Gets a Vertical-Horizontal path
     * 
     * @param startingRectangle
     * @param endingRectangle
     * @return an array list of points
     */
    private ArrayList<Point2D> getVHPath(Point2D startingPoint, Point2D endingPoint)
    {
        ArrayList<Point2D> r = new ArrayList<Point2D>();
        double x1 = startingPoint.getX();
        double x2;
        double y1;
        double y2 = endingPoint.getY();
        if (x1 + MIN_SEGMENT <= endingPoint.getX()) x2 = endingPoint.getX();
        else if (x1 - MIN_SEGMENT >= endingPoint.getX()) x2 = endingPoint.getX();
        else return null;
        if (y2 + MIN_SEGMENT <= startingPoint.getY()) y1 = startingPoint.getY();
        else if (y2 - MIN_SEGMENT >= startingPoint.getY()) y1 = startingPoint.getY();
        else return null;
        r.add(new Point2D.Double(x1, y1));
        r.add(new Point2D.Double(x1, y2));
        r.add(new Point2D.Double(x2, y2));
        return r;
    }

    /**
     * Gets an Horizontal-Vertical path
     * 
     * @param startingRectangle
     * @param endingRectangle
     * @return an array list of points
     */
    private ArrayList<Point2D> getHVPath(Point2D startingPoint, Point2D endingPoint)
    {
        ArrayList<Point2D> r = new ArrayList<Point2D>();
        double x1;
        double x2 = endingPoint.getX();
        double y1 = startingPoint.getY();
        double y2;
        if (x2 + MIN_SEGMENT <= startingPoint.getX()) x1 = startingPoint.getX();
        else if (x2 - MIN_SEGMENT >= startingPoint.getX()) x1 = startingPoint.getX();
        else return null;
        if (y1 + MIN_SEGMENT <= endingPoint.getY()) y2 = endingPoint.getY();
        else if (y1 - MIN_SEGMENT >= endingPoint.getY()) y2 = endingPoint.getY();
        else return null;
        r.add(new Point2D.Double(x1, y1));
        r.add(new Point2D.Double(x2, y1));
        r.add(new Point2D.Double(x2, y2));
        return r;
    }

    /**
     * Gets a straight path
     * 
     * @param startingRectangle
     * @param endingRectangle
     * @return an array list of points
     */
    private ArrayList<Point2D> getStraightPath(Point2D startingPoint, Point2D endingPoint)
    {
        ArrayList<Point2D> r = new ArrayList<Point2D>();
        r.add(startingPoint);
        r.add(endingPoint);
        return r;
    }

    /**
     * Gets a free path
     * 
     * @param connectionPoints
     * @return
     */
    private ArrayList<Point2D> getFreePath(Point2D... connectionPoints)
    {
        ArrayList<Point2D> r = new ArrayList<Point2D>();
        r.addAll(Arrays.asList(connectionPoints));
        return r;
    }

    /**
     * Gets the points at which a line joining two rectangles is bent according to a bent style.
     * 
     * @param s the starting and ending rectangle
     */
    private ArrayList<Point2D> getSelfPath(Point2D p)
    {
        ArrayList<Point2D> r = new ArrayList<Point2D>();
        double x1 = p.getX() + SELF_WIDTH * 3 / 4;
        double y1 = p.getY();
        double y2 = p.getY() - SELF_HEIGHT;
        double x2 = p.getX() + 2 * SELF_WIDTH;
        double y3 = p.getY() + SELF_HEIGHT / 4;
        double x3 = p.getX() + SELF_WIDTH;
        r.add(new Point2D.Double(x1, y1));
        r.add(new Point2D.Double(x1, y2));
        r.add(new Point2D.Double(x2, y2));
        r.add(new Point2D.Double(x2, y3));
        r.add(new Point2D.Double(x3, y3));
        return r;
    }

    /** minimum segment size */
    private static final int MIN_SEGMENT = 10;
    /** width on self path */
    private static final int SELF_WIDTH = 30;
    /** height on self path */
    private static final int SELF_HEIGHT = 25;

    /** straight bent style */
    public static final BentStyle STRAIGHT = new BentStyle();
    /** free bent style */
    public static final BentStyle FREE = new BentStyle();
    /** Horizontal-Vertical bent style */
    public static final BentStyle HV = new BentStyle();
    /** Vertical-Horizontal bent style */
    public static final BentStyle VH = new BentStyle();
    /** Horizontal-Vertical-Horizontal bent style */
    public static final BentStyle HVH = new BentStyle();
    /** Vertical-Horizontal-Vertical bent style */
    public static final BentStyle VHV = new BentStyle();
    /** Automatic bent style */
    public static final BentStyle AUTO = new BentStyle();
}
