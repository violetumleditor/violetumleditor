package com.horstmann.violet.framework.graphics.content;

import java.awt.geom.Point2D;

/**
 * This class groups together all the component vertically
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 21.12.2015
 */
public class VerticalLayout extends Layout
{
    /**
     * @see Layout#getNextOffset(Point2D, Content)
     */
    @Override
    protected Point2D getNextOffset(Point2D beforeOffset, Content content)
    {
        return new Point2D.Double(beforeOffset.getX(),beforeOffset.getY() + content.getHeight());
    }

    /**
     * @see Layout#getStartPointSeparator(Point2D)
     */
    @Override
    protected Point2D getStartPointSeparator(Point2D offset)
    {
        return new Point2D.Double(0, offset.getY());
    }

    /**
     * @see Layout#getEndPointSeparator(Point2D)
     */
    @Override
    protected Point2D getEndPointSeparator(Point2D offset)
    {
        return new Point2D.Double(getWidth(), offset.getY());
    }

    /**
     * @see Content#refreshUp()
     */
    @Override
    public final void refreshUp()
    {
        int height = 0;
        int width = 0;

        for (Content content: getContents()) {
            height += content.getHeight();
            if(width < content.getWidth())
            {
                width = content.getWidth();
            }
        }

        setHeight(height);
        setWidth(width);
        setContentsWidth(width);

        super.refreshUp();
    }
}
