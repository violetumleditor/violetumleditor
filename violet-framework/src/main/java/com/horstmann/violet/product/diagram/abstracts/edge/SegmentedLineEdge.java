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
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import com.horstmann.violet.product.diagram.abstracts.edge.arrowhead.Arrowhead;
import com.horstmann.violet.framework.property.BentStyleChoiceList;
import com.horstmann.violet.framework.property.text.SingleLineText;

/**
 * An edge that is composed of multiple line segments
 */
//public abstract class SegmentedLineEdge extends ShapeEdge
public abstract class SegmentedLineEdge extends ArrowheadEdge
{
    /**
     * Constructs an edge with no adornments.
     */
    public SegmentedLineEdge()
    {
        super();
        startLabel = new SingleLineText();
        middleLabel = new SingleLineText();
        endLabel = new SingleLineText();
    }

    public void deserializeSupport()
    {
        super.deserializeSupport();
        startLabel.deserializeSupport();
        middleLabel.deserializeSupport();
        endLabel.deserializeSupport();
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
        if (transitionPoints.length > 0)
        {
            setBentStyle(BentStyleChoiceList.FREE);
        }
    }

    /**
     * Sets the start label property
     * 
     * @param newValue the new value
     */
    public void setStartLabel(SingleLineText newValue)
    {
        startLabel.setText(newValue.toEdit());
    }
    public void setStartLabel(String newValue)
    {
        startLabel.setText(newValue);
    }

    /**
     * Gets the start label property
     * 
     * @return the label at the start of the edge
     */
    public SingleLineText getStartLabel()
    {
        return startLabel;
    }

    /**
     * Sets the middle label property
     * 
     * @param newValue the new value
     */
    public void setMiddleLabel(SingleLineText newValue)
    {
        middleLabel.setText(newValue.toEdit());
    }
    public void setMiddleLabel(String newValue)
    {
        middleLabel.setText(newValue);
    }

    /**
     * Gets the middle label property
     * 
     * @return the label at the middle of the edge
     */
    public SingleLineText getMiddleLabel()
    {
        return middleLabel;
    }

    /**
     * Sets the end label property
     * 
     * @param newValue the new value
     */
    public void setEndLabel(SingleLineText newValue)
    {
        endLabel.setText(newValue.toEdit());
    }
    public void setEndLabel(String newValue)
    {
        endLabel.setText(newValue);
    }

    /**
     * Gets the end label property
     *
     * @return the label at the end of the edge
     */
    public SingleLineText getEndLabel()
    {
        return endLabel;
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
    private void drawString(Graphics2D g2, Point2D p, Point2D q, Arrowhead arrow, String s, boolean center)
    {
        if (s == null || s.length() == 0) return;
        label.setText(s);
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
    private static Point2D getAttachmentPoint(Point2D p, Point2D q, Arrowhead arrow, Dimension d, boolean center)
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
                Rectangle2D arrowBounds = arrow.getPath().getBounds2D();
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
    private static Rectangle2D getStringBounds(Point2D p, Point2D q, Arrowhead arrow, String s, boolean center)
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

//    @Override
//    public Rectangle2D getBounds()
//    {
//        ArrayList<Point2D> points = new ArrayList<Point2D>();
//        points.addAll(Arrays.asList(getTransitionPoints()));
//
//        Rectangle2D r = super.getBounds();
//        r.add(getStringBounds((Point2D) points.get(1), (Point2D) points.get(0), getStartArrowhead(), startLabel.toDisplay(), false));
//        r.add(getStringBounds((Point2D) points.get(points.size() / 2 - 1), (Point2D) points.get(points.size() / 2), null,
//                middleLabel.toDisplay(), true));
//        r.add(getStringBounds((Point2D) points.get(points.size() - 2), (Point2D) points.get(points.size() - 1), getEndArrowhead(),
//                endLabel.toDisplay(), false));
//        return r;
//    }

//    @Override
//    public Shape getShape()
//    {
//        GeneralPath path = getSegmentPath();
//        path.append(getStartArrowhead().getPath(), false);
//        path.append(getEndArrowhead().getPath(), false);
//        return path;
//    }



    protected SegmentedLineEdge(SegmentedLineEdge segmentedLineEdge)
    {
        super(segmentedLineEdge);
        this.startLabel = segmentedLineEdge.startLabel.clone();
        this.middleLabel = segmentedLineEdge.middleLabel.clone();
        this.endLabel = segmentedLineEdge.endLabel.clone();
    }

    private SingleLineText startLabel;
    private SingleLineText middleLabel;
    private SingleLineText endLabel;

    private static JLabel label = new JLabel();
}
