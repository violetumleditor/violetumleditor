package com.horstmann.violet.framework.graphics.content;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * This class groups together all the component relative
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 21.12.2015
 */
public class RelativeLayout extends Layout
{
    static protected class RelativeContent extends Content
    {
        public RelativeContent(Content content, Point2D position) {
            content.addParent(this);
            this.content = content;
            this.position = position;
            refreshUp();
        }

        /**
         * @see Content#draw(Graphics2D)
         */
        @Override
        public void draw(Graphics2D graphics) {
            content.draw(graphics, position);
        }

        /**
         * @see Content#refreshUp()
         */
        @Override
        public void refreshUp()
        {
            setHeight(content.getHeight());
            setWidth(content.getWidth());
            super.refreshUp();
        }

        /**
         * @return the same as bounds
         * @see Content#getBounds()
         */
        public Rectangle2D getRect()
        {
            return new Rectangle2D.Double(position.getX(), position.getY(), getWidth(), getHeight());
        }

        /**
         * @return positions relative to the inside layout
         */
        public Point2D getPosition()
        {
            return position;
        }

        /**
         * set positions relative to the inside layout
         * @param position
         */
        public void setPosition(Point2D position)
        {
            if(null == position)
            {
                this.position.setLocation(0,0);
            }
            else
            {
                this.position.setLocation(Math.max(0, position.getX()),Math.max(0, position.getY()));
            }
            refreshUp();
        }

        private Point2D position;
        private Content content;
    }

    /**
     * @see Layout#getNextOffset(Point2D, Content)
     */
    @Override
    protected Point2D getNextOffset(Point2D beforeOffset, Content content)
    {
        return new Point2D.Double(0,0);
    }

    /**
     * @see Layout#getStartPointSeparator(Point2D)
     */
    @Override
    protected Point2D getStartPointSeparator(Point2D offset)
    {
        return new Point2D.Double(0,0);
    }

    /**
     * @see Layout#getEndPointSeparator(Point2D)
     */
    @Override
    protected Point2D getEndPointSeparator(Point2D offset)
    {
        return new Point2D.Double(0,0);
    }

    /**
     * @see Layout#add(Content)
     */
    @Override
    public void add(Content content)
    {
        add(content, new Point2D.Double(0,0));
    }

    /**
     * add a content to layout and sets him positions
     * @param content
     * @param position
     */
    public void add(Content content, Point2D position)
    {
        if(null == content)
        {
            throw new NullPointerException("Content can't be null");
        }
        super.add(new RelativeContent(content, position));
    }

    /**
     * @see Layout#remove(Content)
     */
    @Override
    public void remove(Content content)
    {
        if(null == content)
        {
            throw new NullPointerException("Content can't be null");
        }
        RelativeContent relativeContent = null;
        for (Content c: getContents())
        {
            relativeContent = (RelativeContent)c;

            if(relativeContent.content.equals(content))
            {
                super.remove(relativeContent);
                break;
            }
        }
    }

    /**
     * @see Content#refreshUp()
     */
    @Override
    public final void refreshUp() {
        Rectangle2D rect;
        double maxX = 0;
        double maxY = 0;

        for (Content content: getContents()) {
            rect = ((RelativeContent)content).getRect();

            maxX = Math.max(maxX, rect.getMaxX());
            maxY = Math.max(maxY, rect.getMaxY());
        }

        setHeight(maxY);
        setWidth(maxX);

        super.refreshUp();
    }

    public final boolean setPosition(Content content, Point2D position)
    {
        if(null == content)
        {
            return false;
        }

        RelativeContent relativeContent = null;
        for (Content c: getContents())
        {
            relativeContent = (RelativeContent)c;

            if(relativeContent.content.equals(content))
            {
                relativeContent.setPosition(position);
                return true;
            }
        }
        return false;
    }
}
