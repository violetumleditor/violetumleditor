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

import java.awt.Color;
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

import com.horstmann.violet.framework.property.ArrowheadChoiceList;
import com.horstmann.violet.framework.property.LineStyleChoiceList;
import com.horstmann.violet.framework.property.choiceList.ChoiceList;
import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.framework.property.BentStyleChoiceList;
import com.horstmann.violet.framework.property.string.SingleLineText;

/**
 * An edge that is composed of multiple line segments
 */
//public abstract class SegmentedLineEdge extends ShapeEdge
public abstract class SegmentedLineEdge extends ShapeEdge
{
    /**
     * Constructs an edge with no adornments.
     */
    public SegmentedLineEdge()
    {
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
        if (transitionPoints.length > 0) {
//            setBentStyle(BentStyleChoiceList.FREE);
        }
    }

    /**
     * Sets the bentStyle property
     * 
     * @param newValue the bent style
     */
    public void setBentStyle(ChoiceList newValue)
    {
        this.bentStyle = (BentStyleChoiceList)newValue;
    }

    /**
     * Gets the bentStyle property
     * 
     * @return the bent style
     */
    public ChoiceList getBentStyle()
    {
        if (this.bentStyle == null)
        {
            this.bentStyle = new BentStyleChoiceList();
        }
        return bentStyle;
    }

    /**
     * Sets the line style property.
     * 
     * @param newValue the new value
     */
    public void setLineStyle(ChoiceList newValue)
    {
        this.lineStyle = (LineStyleChoiceList)newValue;
    }

    /**
     * Gets the line style property.
     * 
     * @return the line style
     */
    public ChoiceList getLineStyle()
    {
        if (this.lineStyle == null)
        {
            this.lineStyle = new LineStyleChoiceList();
        }
        return this.lineStyle;
    }

    /**
     * Sets the start arrow head property
     * 
     * @param newValue the new value
     */
    public void setStartArrowHead(ChoiceList newValue)
    {
        this.startArrowHead = (ArrowheadChoiceList)newValue;
    }

    /**
     * Gets the start arrow head property
     *
     * @return the end arrow head style
     */
    public ChoiceList getStartArrowHead()
    {
        if (this.startArrowHead == null)
        {
            this.startArrowHead = new ArrowheadChoiceList();
        }
        return this.startArrowHead;
    }

    /**
     * Sets the end arrow head property
     *
     * @param newValue the new value
     */
    public void setEndArrowHead(ChoiceList newValue)
    {
        this.endArrowHead = (ArrowheadChoiceList)newValue;
    }

    /**
     * Gets the end arrow head property
     *
     * @return the end arrow head style
     */
    public ChoiceList getEndArrowHead()
    {
        if (this.endArrowHead == null)
        {
            this.endArrowHead = new ArrowheadChoiceList();
        }
        return this.endArrowHead;
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
     * Draws the edge.
     * 
     * @param g2 the graphics context
     */
    public void draw(Graphics2D g2)
    {
    	Color oldColor = g2.getColor();
    	g2.setColor(Color.BLACK);
    	ArrayList<Point2D> points = getPoints();
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(((LineStyleChoiceList)getLineStyle()).getSelectedValue());
        g2.draw(getSegmentPath());
        g2.setStroke(oldStroke);
        ((ArrowheadChoiceList)getStartArrowHead()).getSelectedValue().draw(g2, (Point2D) points.get(1), (Point2D) points.get(0));
        ((ArrowheadChoiceList)getEndArrowHead()).getSelectedValue().draw(g2, (Point2D) points.get(points.size() - 2), (Point2D) points.get(points.size() - 1));

        drawString(g2, (Point2D) points.get(1), (Point2D) points.get(0), ((ArrowheadChoiceList)getStartArrowHead()), startLabel.toDisplay(), false);
        drawString(g2, (Point2D) points.get(points.size() / 2 - 1), (Point2D) points.get(points.size() / 2), null, middleLabel.toDisplay(),
                true);
        drawString(g2, (Point2D) points.get(points.size() - 2), (Point2D) points.get(points.size() - 1), ((ArrowheadChoiceList)getEndArrowHead()),
                endLabel.toDisplay(), false);
        g2.setColor(oldColor);
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
    private void drawString(Graphics2D g2, Point2D p, Point2D q, ArrowheadChoiceList arrow, String s, boolean center)
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
    private static Point2D getAttachmentPoint(Point2D p, Point2D q, ArrowheadChoiceList arrow, Dimension d, boolean center)
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
                Rectangle2D arrowBounds = arrow.getSelectedValue().getPath().getBounds2D();
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
    private static Rectangle2D getStringBounds(Point2D p, Point2D q, ArrowheadChoiceList arrow, String s, boolean center)
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
        r.add(getStringBounds((Point2D) points.get(1), (Point2D) points.get(0), ((ArrowheadChoiceList)getStartArrowHead()), startLabel.toDisplay(), false));
        r.add(getStringBounds((Point2D) points.get(points.size() / 2 - 1), (Point2D) points.get(points.size() / 2), null,
                middleLabel.toDisplay(), true));
        r.add(getStringBounds((Point2D) points.get(points.size() - 2), (Point2D) points.get(points.size() - 1), ((ArrowheadChoiceList)getEndArrowHead()),
                endLabel.toDisplay(), false));
        return r;
    }

    @Override
    public Shape getShape()
    {
        GeneralPath path = getSegmentPath();
        ArrayList<Point2D> points = getPoints();
        path.append(((ArrowheadChoiceList)getStartArrowHead()).getSelectedValue().getPath(), false);
        path.append(((ArrowheadChoiceList)getEndArrowHead()).getSelectedValue().getPath(),
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
        
        // Path for self loop
        if (getStart().equals(getEnd())) {
        	int gapX = 20;
        	int gapY = 20;
        	Point2D p1 = new Point2D.Double(getStart().getBounds().getMaxX(), startingPoint.getY());
        	Point2D p5 = new Point2D.Double(endingPoint.getX(), getEnd().getBounds().getMaxY());
        	Point2D p2 = new Point2D.Double(p1.getX() + gapX, p1.getY());
        	Point2D p4 = new Point2D.Double(p5.getX(), p5.getY() + gapY);
        	Point2D p3 = new Point2D.Double(p2.getX(), p4.getY());
        	ArrayList<Point2D> path = new ArrayList<Point2D>();
        	path.add(p1);
        	path.add(p2);
        	path.add(p3);
        	path.add(p4);
        	path.add(p5);
        	return path;
        }
        

        // Automatic based path
        if (!BentStyleChoiceList.AUTO.equals(getBentStyle()))
        {
            List<Point2D> bentStylePoints = new ArrayList<Point2D>();
            bentStylePoints.add(startingPoint);
            for (Point2D aTransitionPoint : getTransitionPoints()) {
                bentStylePoints.add(aTransitionPoint);
            }
            bentStylePoints.add(endingPoint);

            Point2D[] bentStylePointsAsArray = bentStylePoints.toArray(new Point2D[bentStylePoints.size()]);
            return ((BentStyleChoiceList)getBentStyle()).getSelectedValue().getPath(bentStylePointsAsArray);
        }

        // User choice based path
        Direction startingCardinalDirection = getDirection(getStart()).getNearestCardinalDirection();
        Direction endingCardinalDirection = getDirection(getEnd()).getNearestCardinalDirection();
        if ((Direction.NORTH.equals(startingCardinalDirection) || Direction.SOUTH.equals(startingCardinalDirection))
                && (Direction.NORTH.equals(endingCardinalDirection) || Direction.SOUTH.equals(endingCardinalDirection)))
        {
            return BentStyleChoiceList.VHV.getPath(startingPoint, endingPoint);
        }
        if ((Direction.NORTH.equals(startingCardinalDirection) || Direction.SOUTH.equals(startingCardinalDirection))
                && (Direction.EAST.equals(endingCardinalDirection) || Direction.WEST.equals(endingCardinalDirection)))
        {
            return BentStyleChoiceList.VH.getPath(startingPoint, endingPoint);
        }
        if ((Direction.EAST.equals(startingCardinalDirection) || Direction.WEST.equals(startingCardinalDirection))
                && (Direction.NORTH.equals(endingCardinalDirection) || Direction.SOUTH.equals(endingCardinalDirection)))
        {
            return BentStyleChoiceList.HV.getPath(startingPoint, endingPoint);
        }
        if ((Direction.EAST.equals(startingCardinalDirection) || Direction.WEST.equals(startingCardinalDirection))
                && (Direction.EAST.equals(endingCardinalDirection) || Direction.WEST.equals(endingCardinalDirection)))
        {
            return BentStyleChoiceList.HVH.getPath(startingPoint, endingPoint);
        }
        return BentStyleChoiceList.STRAIGHT.getPath(startingPoint, endingPoint);
    }

    @Override
    public Direction getDirection(INode node)
    {
        Direction straightDirection = super.getDirection(node);
        double x = straightDirection.getX();
        double y = straightDirection.getY();
        if (!getStart().equals(getEnd()) && node.equals(getStart()))
        {
            if (BentStyleChoiceList.HV.equals(getBentStyle()) || BentStyleChoiceList.HVH.equals(getBentStyle()))
            {
                return (x >= 0) ? Direction.EAST : Direction.WEST;
            }
            if (BentStyleChoiceList.VH.equals(getBentStyle()) || BentStyleChoiceList.VHV.equals(getBentStyle()))
            {
                return (y >= 0) ? Direction.SOUTH : Direction.NORTH;
            }
        }
        if (!getStart().equals(getEnd()) && node.equals(getEnd()))
        {
            if (BentStyleChoiceList.HV.equals(getBentStyle()) || BentStyleChoiceList.VHV.equals(getBentStyle()))
            {
                return (y >= 0) ? Direction.SOUTH : Direction.NORTH;
            }
            if (BentStyleChoiceList.VH.equals(getBentStyle()) || BentStyleChoiceList.HVH.equals(getBentStyle()))
            {
                return (x >= 0) ? Direction.EAST : Direction.WEST;
            }
        }
        return straightDirection;
    }

    protected SegmentedLineEdge(SegmentedLineEdge segmentedLineEdge)
    {
        this.lineStyle = segmentedLineEdge.lineStyle;
        this.startArrowHead = segmentedLineEdge.startArrowHead.clone();
        this.endArrowHead = segmentedLineEdge.endArrowHead.clone();
        this.bentStyle = segmentedLineEdge.bentStyle;
        this.startLabel = segmentedLineEdge.startLabel.clone();
        this.middleLabel = segmentedLineEdge.middleLabel.clone();
        this.endLabel = segmentedLineEdge.endLabel.clone();
    }

    private LineStyleChoiceList lineStyle;
    private ArrowheadChoiceList startArrowHead;
    private ArrowheadChoiceList endArrowHead;
    private BentStyleChoiceList bentStyle;
    private SingleLineText startLabel;
    private SingleLineText middleLabel;
    private SingleLineText endLabel;

    private static JLabel label = new JLabel();
}
