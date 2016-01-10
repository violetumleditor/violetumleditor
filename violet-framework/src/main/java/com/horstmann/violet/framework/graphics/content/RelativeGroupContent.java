package com.horstmann.violet.framework.graphics.content;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 21.12.2015
 */
public class RelativeGroupContent extends GroupContent
{
    static protected class RelativeContent extends Content
    {
        public RelativeContent(Content content, Point2D position) {
            content.addParent(this);
            this.content = content;
            this.position = position;
            refreshUp();
        }

        @Override
        public void draw(Graphics2D g2) {
            content.draw(g2, position);
        }

        @Override
        public void refreshUp()
        {
            setHeight(content.getHeight());
            setWidth(content.getWidth());
            super.refreshUp();
        }

        public Rectangle2D getRect()
        {
            return new Rectangle2D.Double(position.getX(), position.getY(), getWidth(), getHeight());
        }

        public Point2D getPosition()
        {
            return position;
        }
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

    @Override
    protected Point2D getNextOffset(Point2D beforeOffset, Content content) {
        return new Point2D.Double(0,0);
    }

    @Override
    protected Point2D getStartPointSeparator(Point2D offset) {
        return new Point2D.Double(0,0);
    }

    @Override
    protected Point2D getEndPointSeparator(Point2D offset) {
        return new Point2D.Double(0,0);
    }

    @Override
    public void add(Content content)
    {
        add(content, new Point2D.Double(0,0));
    }
    public void add(Content content, Point2D position)
    {
        if(null == content)
        {
            throw new NullPointerException("Content can't be null");
        }
        super.add(new RelativeContent(content, position));
    }

    @Override
    public final void refreshUp() {
        Rectangle2D rect;
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = 0;
        int maxY = 0;

        for (Content content: getContents()) {
            rect = ((RelativeContent)content).getRect();

            minX = Math.min(minX, (int)rect.getMinX());
            minY = Math.min(minY, (int)rect.getMinY());
            maxX = Math.max(maxX, (int)rect.getMaxX());
            maxY = Math.max(maxY, (int)rect.getMaxY());
        }

        setHeight(maxY-minY);
        setWidth(maxX-minX);

        super.refreshUp();
    }

    public final boolean setPosition(Content c, Point2D position)
    {
        if(null == c)
        {
            return false;
        }

        RelativeContent relativeContent = null;
        for (Content content: getContents())
        {
            relativeContent = (RelativeContent)content;

            if(relativeContent.content.equals(c))
            {
                relativeContent.setPosition(position);
                return true;
            }
        }
        return false;
    }
}
