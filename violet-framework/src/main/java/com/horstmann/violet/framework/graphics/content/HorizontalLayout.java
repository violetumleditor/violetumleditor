package com.horstmann.violet.framework.graphics.content;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * This class groups together all the component horizontally
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 21.12.2015
 */
public class HorizontalLayout extends Layout
{
    /**
     * @see Layout#getNextOffset(Point2D, Content)
     */
    @Override
    protected Point2D getNextOffset(Point2D beforeOffset, Content content)
    {
        return new Point2D.Double(beforeOffset.getX() + content.getWidth(),beforeOffset.getY());
    }

    /**
     * @see Layout#getStartPointSeparator(Point2D)
     */
    @Override
    protected Point2D getStartPointSeparator(Point2D offset)
    {
        return new Point2D.Double(offset.getX(), 0);
    }

    /**
     * @see Layout#getEndPointSeparator(Point2D)
     */
    @Override
    protected Point2D getEndPointSeparator(Point2D offset)
    {
        return new Point2D.Double(offset.getX(), getHeight());
    }

    /**
     * @see Content#refreshUp()
     */
    @Override
    public final void refreshUp()
    {
        Rectangle2D minimalBounds = getMinimalBounds();

        setHeight(minimalBounds.getHeight());
        setWidth(minimalBounds.getWidth());
        setContentsHeight(minimalBounds.getWidth());

        super.refreshUp();
    }

    /**
     * @return minimal bounds of this element
     */
    @Override
    public Rectangle2D getMinimalBounds()
    {
        Rectangle2D selfMinimalBounds = super.getMinimalBounds();

        double width = 0;
        double height = 0;

        for (Content content: getContents())
        {
            Rectangle2D contentMinimalBounds = content.getMinimalBounds();

            width += contentMinimalBounds.getWidth();
            if(height < contentMinimalBounds.getHeight())
            {
                height = contentMinimalBounds.getHeight();
            }
        }

        height = Math.max(height, selfMinimalBounds.getHeight());
        width = Math.max(width, selfMinimalBounds.getWidth());

        return new Rectangle2D.Double(getX(),getY(),width,height);
    }

    /**
     * Set the same height for all elements in the layout
     * @param height
     */
    protected final void setContentsHeight(double height)
    {
        for (Content content: getContents())
        {
            content.setHeight(height);
            content.refreshDown();
        }
    }
}
