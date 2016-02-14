package com.horstmann.violet.framework.graphics.content;

import java.awt.geom.Point2D;

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
        double height = 0;
        double width = 0;

        for (Content content: getContents()) {
            width += content.getWidth();
            if(height < content.getHeight())
            {
                height = content.getHeight();
            }
        }

        setHeight(height);
        setWidth(width);
        setContentsHeight(height);

        super.refreshUp();
    }
}
