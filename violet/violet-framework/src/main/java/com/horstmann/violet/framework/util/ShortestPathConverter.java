/*
 * Projet     : 
 * Package    : com.horstmann.violet.framework.util
 * Auteur     : a.depellegrin
 * Cr le    : 26 mai 08
 */
package com.horstmann.violet.framework.util;

import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class ShortestPathConverter
{

    public static GeneralPath getShortestPath(GeneralPath path)
    {

        List<Point2D> points = extractPointsFromGeneralPath(path);
        List<Point2D> sortedPoints = new ArrayList<Point2D>();

        while (points.size() > 0)
        {
            Point2D currentPoint = null;
            if (sortedPoints.size() > 0)
            {
                currentPoint = sortedPoints.get(sortedPoints.size() - 1);
            }
            else
            {
                currentPoint = points.get(0);
            }
            points.remove(currentPoint);
            Point2D closestPoint = getClosestPoint(currentPoint, points);
            if (closestPoint != null)
            {
                sortedPoints.add(closestPoint);
                currentPoint = closestPoint;
            }
        }

        GeneralPath newPath = buildPath(sortedPoints);
        return newPath;

    }

    private static List<Point2D> extractPointsFromGeneralPath(GeneralPath path)
    {
        List<Point2D> points = new ArrayList<Point2D>();
        double seg[] = new double[6];
        for (PathIterator i = path.getPathIterator(null); !i.isDone(); i.next())
        {
            int segType = i.currentSegment(seg);
            switch (segType)
            {
            case PathIterator.SEG_MOVETO:
                points.add(new Point2D.Double(seg[0], seg[1]));
                break;
            case PathIterator.SEG_LINETO:
                points.add(new Point2D.Double(seg[0], seg[1]));
                break;
            }
        }
        return points;
    }

    private static Point2D getClosestPoint(Point2D refPoint, List<Point2D> pointList)
    {
        Point2D closestPoint = null;
        double shortestDistance = -1;
        for (Point2D aPoint : pointList)
        {
            double distance = refPoint.distance(aPoint);
            if (shortestDistance < 0 || distance < shortestDistance)
            {
                closestPoint = aPoint;
                shortestDistance = distance;
            }
        }
        return closestPoint;
    }

    private static GeneralPath buildPath(List<Point2D> points)
    {
        GeneralPath newPath = null;
        if (points == null)
        {
            return new GeneralPath();
        }
        if (points.isEmpty())
        {
            return new GeneralPath();
        }
        for (Point2D aPoint : points)
        {
            if (newPath == null)
            {
                newPath = new GeneralPath();
                newPath.moveTo((float) aPoint.getX(), (float) aPoint.getY());
            }
            else
            {
                newPath.lineTo((float) aPoint.getX(), (float) aPoint.getY());
            }
        }
        return newPath;
    }

}
