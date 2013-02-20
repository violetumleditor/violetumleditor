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

package com.horstmann.violet.product.diagram.abstracts.edge;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.property.ArrowHead;
import com.horstmann.violet.product.diagram.abstracts.property.BentStyle;
import com.horstmann.violet.product.diagram.abstracts.property.LineStyle;

/**
 * An edge that is composed of multiple line segments
 */
public abstract class SegmentedLineEdge extends ShapeEdge
{
    /**
     * Constructs an edge with no adornments.
     */
    public SegmentedLineEdge()
    {
        startLabel = "";
        middleLabel = "";
        endLabel = "";
    }
    
    @Override
    public boolean isTransitionPointsSupported()
    {
        return true;
    }
    
    @Override
    public void setTransitionPoints(Point2D[] transitionPoints)
    {
        super.setTransitionPoints(transitionPoints);
        if (transitionPoints.length > 0) {
            setBentStyle(BentStyle.FREE);
        }
    }

    /**
     * Sets the bentStyle property
     * 
     * @param newValue the bent style
     */
    public void setBentStyle(BentStyle newValue)
    {
        this.bentStyle = newValue;
    }

    /**
     * Gets the bentStyle property
     * 
     * @return the bent style
     */
    public BentStyle getBentStyle()
    {
        if (this.bentStyle == null)
        {
            this.bentStyle = BentStyle.AUTO;
        }
        return bentStyle;
    }

    /**
     * Sets the line style property.
     * 
     * @param newValue the new value
     */
    public void setLineStyle(LineStyle newValue)
    {
        this.lineStyle = newValue;
    }

    /**
     * Gets the line style property.
     * 
     * @return the line style
     */
    public LineStyle getLineStyle()
    {
        if (this.lineStyle == null)
        {
            this.lineStyle = LineStyle.SOLID;
        }
        return this.lineStyle;
    }

    /**
     * Sets the start arrow head property
     * 
     * @param newValue the new value
     */
    public void setStartArrowHead(ArrowHead newValue)
    {
        this.startArrowHead = newValue;
    }

    /**
     * Gets the start arrow head property
     * 
     * @return the start arrow head style
     */
    public ArrowHead getStartArrowHead()
    {
        if (this.startArrowHead == null)
        {
            this.startArrowHead = ArrowHead.NONE;
        }
        return this.startArrowHead;
    }

    /**
     * Sets the end arrow head property
     * 
     * @param newValue the new value
     */
    public void setEndArrowHead(ArrowHead newValue)
    {
        this.endArrowHead = newValue;
    }

    /**
     * Gets the end arrow head property
     * 
     * @return the end arrow head style
     */
    public ArrowHead getEndArrowHead()
    {
        if (this.endArrowHead == null)
        {
            this.endArrowHead = ArrowHead.NONE;
        }
        return this.endArrowHead;
    }

    /**
     * Sets the start label property
     * 
     * @param newValue the new value
     */
    public void setStartLabel(String newValue)
    {
        startLabel = newValue;
    }

    /**
     * Gets the start label property
     * 
     * @return the label at the start of the edge
     */
    public String getStartLabel()
    {
        return startLabel;
    }

    /**
     * Sets the middle label property
     * 
     * @param newValue the new value
     */
    public void setMiddleLabel(String newValue)
    {
        middleLabel = newValue;
    }

    /**
     * Gets the middle label property
     * 
     * @return the label at the middle of the edge
     */
    public String getMiddleLabel()
    {
        return middleLabel;
    }

    /**
     * Sets the end label property
     * 
     * @param newValue the new value
     */
    public void setEndLabel(String newValue)
    {
        endLabel = newValue;
    }

    /**
     * Gets the end label property
     * 
     * @return the label at the end of the edge
     */
    public String getEndLabel()
    {
        return endLabel;
    }

    /**
     * Draws the edge.
     * 
     * @param g2 the graphics context
     */
    public void draw(Graphics2D g2)
    {
        ArrayList<Point2D> points = getPoints();

        Stroke oldStroke = g2.getStroke();
        g2.setStroke(getLineStyle().getStroke());
        g2.draw(getSegmentPath());
        g2.setStroke(oldStroke);
        getStartArrowHead().draw(g2, (Point2D) points.get(1), (Point2D) points.get(0));
        getEndArrowHead().draw(g2, (Point2D) points.get(points.size() - 2), (Point2D) points.get(points.size() - 1));

        drawString(g2, (Point2D) points.get(1), (Point2D) points.get(0), getStartArrowHead(), startLabel, false);
        drawString(g2, (Point2D) points.get(points.size() / 2 - 1), (Point2D) points.get(points.size() / 2), null, middleLabel,
                true);
        drawString(g2, (Point2D) points.get(points.size() - 2), (Point2D) points.get(points.size() - 1), getEndArrowHead(),
                endLabel, false);
    }

    /**
     * Draws a string.
     * 
     * @param g2 the graphics context
     * @param p an endpoint of the segment along which to draw the string
     * @param q the other endpoint of the segment along which to draw the string
     * @param s the string to draw
     * @param center true if the string should be centered along the segment
     */
    private void drawString(Graphics2D g2, Point2D p, Point2D q, ArrowHead arrow, String s, boolean center)
    {
        if (s == null || s.length() == 0) return;
        label.setText("<html>" + s + "</html>");
        label.setFont(g2.getFont());
        Dimension d = label.getPreferredSize();
        label.setBounds(0, 0, d.width, d.height);

        Rectangle2D b = getStringBounds(p, q, arrow, s, center);

        g2.translate(b.getX(), b.getY());
        label.paint(g2);
        g2.translate(-b.getX(), -b.getY());
    }

    /**
     * Computes the attachment point for drawing a string.
     * 
     * @param p an endpoint of the segment along which to draw the string
     * @param q the other endpoint of the segment along which to draw the string
     * @param b the bounds of the string to draw
     * @param center true if the string should be centered along the segment
     * @return the point at which to draw the string
     */
    private static Point2D getAttachmentPoint(Point2D p, Point2D q, ArrowHead arrow, Dimension d, boolean center)
    {
        final int GAP = 3;
        double xoff = GAP;
        double yoff = -GAP - d.getHeight();
        Point2D attach = q;
        if (center)
        {
            if (p.getX() > q.getX())
            {
                return getAttachmentPoint(q, p, arrow, d, center);
            }
            attach = new Point2D.Double((p.getX() + q.getX()) / 2, (p.getY() + q.getY()) / 2);
            if (p.getY() < q.getY()) yoff = -GAP - d.getHeight();
            else if (p.getY() == q.getY()) xoff = -d.getWidth() / 2;
            else yoff = GAP;
        }
        else
        {
            if (p.getX() < q.getX())
            {
                xoff = -GAP - d.getWidth();
            }
            if (p.getY() > q.getY())
            {
                yoff = GAP;
            }
            if (arrow != null)
            {
                Rectangle2D arrowBounds = arrow.getPath(p, q).getBounds2D();
                if (p.getX() < q.getX())
                {
                    xoff -= arrowBounds.getWidth();
                }
                else
                {
                    xoff += arrowBounds.getWidth();
                }
            }
        }
        return new Point2D.Double(attach.getX() + xoff, attach.getY() + yoff);
    }

    /**
     * Computes the extent of a string that is drawn along a line segment.
     * 
     * @param g2 the graphics context
     * @param p an endpoint of the segment along which to draw the string
     * @param q the other endpoint of the segment along which to draw the string
     * @param s the string to draw
     * @param center true if the string should be centered along the segment
     * @return the rectangle enclosing the string
     */
    private static Rectangle2D getStringBounds(Point2D p, Point2D q, ArrowHead arrow, String s, boolean center)
    {
        BufferedImage dummy = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        // need a dummy image to get a Graphics to
        // measure the size
        Graphics2D g2 = (Graphics2D) dummy.getGraphics();
        if (s == null || s.equals("")) return new Rectangle2D.Double(q.getX(), q.getY(), 0, 0);
        label.setText("<html>" + s + "</html>");
        label.setFont(g2.getFont());
        Dimension d = label.getPreferredSize();
        Point2D a = getAttachmentPoint(p, q, arrow, d, center);
        return new Rectangle2D.Double(a.getX(), a.getY(), d.getWidth(), d.getHeight());
    }

    @Override
    public Rectangle2D getBounds()
    {
        ArrayList<Point2D> points = getPoints();
        Rectangle2D r = super.getBounds();
        r.add(getStringBounds((Point2D) points.get(1), (Point2D) points.get(0), getStartArrowHead(), startLabel, false));
        r.add(getStringBounds((Point2D) points.get(points.size() / 2 - 1), (Point2D) points.get(points.size() / 2), null,
                middleLabel, true));
        r.add(getStringBounds((Point2D) points.get(points.size() - 2), (Point2D) points.get(points.size() - 1), getEndArrowHead(),
                endLabel, false));
        return r;
    }

    @Override
    public Shape getShape()
    {
        GeneralPath path = getSegmentPath();
        ArrayList<Point2D> points = getPoints();
        path.append(getStartArrowHead().getPath((Point2D) points.get(1), (Point2D) points.get(0)), false);
        path.append(getEndArrowHead().getPath((Point2D) points.get(points.size() - 2), (Point2D) points.get(points.size() - 1)),
                false);
        return path;
    }

    private GeneralPath getSegmentPath()
    {
        ArrayList<Point2D> points = getPoints();

        GeneralPath path = new GeneralPath();
        Point2D p = (Point2D) points.get(points.size() - 1);
        path.moveTo((float) p.getX(), (float) p.getY());
        for (int i = points.size() - 2; i >= 0; i--)
        {
            p = (Point2D) points.get(i);
            path.lineTo((float) p.getX(), (float) p.getY());
        }
        return path;
    }

    /**
     * Gets the corner points of this segmented line edge
     * 
     * @return an array list of Point2D objects, containing the corner points
     */
    public ArrayList<Point2D> getPoints()
    {
        Line2D connectionPoints = getConnectionPoints();
        Point2D startingPoint = connectionPoints.getP1();
        Point2D endingPoint = connectionPoints.getP2();

        // Automatic based path
        if (!BentStyle.AUTO.equals(getBentStyle()))
        {
            List<Point2D> bentStylePoints = new ArrayList<Point2D>();
            bentStylePoints.add(startingPoint);
            for (Point2D aTransitionPoint : getTransitionPoints()) {
                bentStylePoints.add(aTransitionPoint);
            }
            bentStylePoints.add(endingPoint);

            Point2D[] bentStylePointsAsArray = bentStylePoints.toArray(new Point2D[bentStylePoints.size()]);
            return getBentStyle().getPath(bentStylePointsAsArray);
        }

        // User choice based path
        Direction startingCardinalDirection = getDirection(getStart()).getNearestCardinalDirection();
        Direction endingCardinalDirection = getDirection(getEnd()).getNearestCardinalDirection();
        if ((Direction.NORTH.equals(startingCardinalDirection) || Direction.SOUTH.equals(startingCardinalDirection))
                && (Direction.NORTH.equals(endingCardinalDirection) || Direction.SOUTH.equals(endingCardinalDirection)))
        {
            return BentStyle.VHV.getPath(startingPoint, endingPoint);
        }
        if ((Direction.NORTH.equals(startingCardinalDirection) || Direction.SOUTH.equals(startingCardinalDirection))
                && (Direction.EAST.equals(endingCardinalDirection) || Direction.WEST.equals(endingCardinalDirection)))
        {
            return BentStyle.VH.getPath(startingPoint, endingPoint);
        }
        if ((Direction.EAST.equals(startingCardinalDirection) || Direction.WEST.equals(startingCardinalDirection))
                && (Direction.NORTH.equals(endingCardinalDirection) || Direction.SOUTH.equals(endingCardinalDirection)))
        {
            return BentStyle.HV.getPath(startingPoint, endingPoint);
        }
        if ((Direction.EAST.equals(startingCardinalDirection) || Direction.WEST.equals(startingCardinalDirection))
                && (Direction.EAST.equals(endingCardinalDirection) || Direction.WEST.equals(endingCardinalDirection)))
        {
            return BentStyle.HVH.getPath(startingPoint, endingPoint);
        }
        return BentStyle.STRAIGHT.getPath(startingPoint, endingPoint);
    }

    @Override
    public Direction getDirection(INode node)
    {
        Direction straightDirection = super.getDirection(node);
        double x = straightDirection.getX();
        double y = straightDirection.getY();
        if (node.equals(getStart()))
        {
            if (BentStyle.HV.equals(getBentStyle()) || BentStyle.HVH.equals(getBentStyle()))
            {
                return (x >= 0) ? Direction.EAST : Direction.WEST;
            }
            if (BentStyle.VH.equals(getBentStyle()) || BentStyle.VHV.equals(getBentStyle()))
            {
                return (y >= 0) ? Direction.SOUTH : Direction.NORTH;
            }
        }
        if (node.equals(getEnd()))
        {
            if (BentStyle.HV.equals(getBentStyle()) || BentStyle.VHV.equals(getBentStyle()))
            {
                return (y >= 0) ? Direction.SOUTH : Direction.NORTH;
            }
            if (BentStyle.VH.equals(getBentStyle()) || BentStyle.HVH.equals(getBentStyle()))
            {
                return (x >= 0) ? Direction.EAST : Direction.WEST;
            }
        }
        return straightDirection;
    }

    private transient LineStyle lineStyle;
    private transient ArrowHead startArrowHead;
    private transient ArrowHead endArrowHead;
    private BentStyle bentStyle;
    private String startLabel;
    private String middleLabel;
    private String endLabel;

    private static JLabel label = new JLabel();
}
