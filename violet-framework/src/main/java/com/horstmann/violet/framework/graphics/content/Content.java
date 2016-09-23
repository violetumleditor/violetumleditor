package com.horstmann.violet.framework.graphics.content;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * This class defines the dimensions and manner of drawing element
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 21.12.2015
 */
public abstract class Content
{
    /**
     * Checks whether a point contained in the element
     * @param point
     * @return true if the point contains in item otherwise false
     */
    public boolean contains(Point2D point)
    {
        return getBounds().contains(point);
    }

    /**
     * Defines how to draw element
     * @param graphics
     */
    public abstract void draw(Graphics2D graphics);

    /**
     * Draws element shifted by offset
     * @param graphics
     * @param offset
     */
    public final void draw(Graphics2D graphics, Point2D offset)
    {
        graphics.translate(offset.getX(), offset.getY());
        draw(graphics);
        graphics.translate(-offset.getX(), -offset.getY());
    }

    /**
     * @return bounds of this element
     */
    public final Rectangle2D getBounds()
    {
        return new Rectangle2D.Double(getX(),getY(),getWidth(),getHeight());
    }

    /**
     * @return minimal bounds of this element
     */
    public Rectangle2D getMinimalBounds()
    {
        return new Rectangle2D.Double(getX(),getY(),minWidth,minHeight);
    }

    /**
     * @return positions on the x-axis of this element
     */
    public final double getX()
    {
        return 0;
    }

    /**
     * @return positions on the y-axis of this element
     */
    public final double getY()
    {
        return 0;
    }

    /**
     * @return width of this element
     */
    public final double getWidth()
    {
        return Math.max(width, minWidth);
    }

    /**
     * @return height of this element
     */
    public final double getHeight()
    {
        return Math.max(height, minHeight);
    }

    /**
     * sets the min width of the element
     * @param minWidth
     */
    public void setMinWidth(double minWidth)
    {
        if(0 > minWidth)
        {
            throw new IllegalArgumentException("min width can only be a positive number");
        }
        this.minWidth = minWidth;
        refreshUp();
    }

    /**
     * sets the min height of the element
     * @param minHeight
     */
    public void setMinHeight(double minHeight){
        if(0 > minHeight)
        {
            throw new IllegalArgumentException("min height can only be a positive number");
        }
        this.minHeight = minHeight;
        refreshUp();
    }

    /**
     * Recalculates all dimensions
     */
    public final void refresh()
    {
        refreshUp();
        refreshDown();
    }

    /**
     * Recalculates dimensions of objects listening and self
     */
    protected void refreshUp()
    {
        for (Content parent: parents )
        {
            parent.refreshUp();
        }
    }

    /**
     * Recalculates dimensions of objects subordinate and self
     */
    protected void refreshDown()
    {}

    /**
     * sets the width of the element
     * @param width
     */
    protected void setWidth(double width)
    {
        if(0 > width)
        {
            throw new IllegalArgumentException("width can only be a positive number");
        }
        this.width = width;
    }

    /**
     * sets the height of the element
     * @param height
     */
    protected void setHeight(double height)
    {
        if(0 > height)
        {
            throw new IllegalArgumentException("height can only be a positive number");
        }
        this.height = height;
    }

    /**
     * adds "Content" to the collection of objects listening
     * @param parent the listener
     */
    protected final void addParent(Content parent)
    {
        if(null == parent)
        {
            throw new NullPointerException("parent can't be null");
        }
        parents.add(parent);
    }

    /**
     * removes "Content" from the collection of objects listening
     * @param parent the listener
     */
    protected final void removeParent(Content parent)
    {
        parents.remove(parent);
        refresh();
    }

    private ArrayList<Content> parents = new ArrayList<Content>();

    private double minWidth = 0;
    private double minHeight = 0;
    private double width = 0;
    private double height = 0;
}
