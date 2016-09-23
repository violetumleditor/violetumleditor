package com.horstmann.violet.framework.graphics.content;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

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
        Rectangle2D minimalBounds = getMinimalBounds();

        setHeight(minimalBounds.getHeight());
        setWidth(minimalBounds.getWidth());
        setContentsWidth(minimalBounds.getWidth());

        super.refreshUp();
    }

    /**
     * @return minimal bounds of this element
     */
    @Override
    public Rectangle2D getMinimalBounds()
    {
        Rectangle2D selfMinimalBounds = super.getMinimalBounds();

        double height = 0;
        double width = 0;

        for (Content content: getContents())
        {
            Rectangle2D contentMinimalBounds = content.getMinimalBounds();

            height += contentMinimalBounds.getHeight();
            if(width < contentMinimalBounds.getWidth())
            {
                width = contentMinimalBounds.getWidth();
            }
        }

        height = Math.max(height, selfMinimalBounds.getHeight());
        width = Math.max(width, selfMinimalBounds.getWidth());

        return new Rectangle2D.Double(getX(),getY(),width,height);
    }

    /**
     * Set the same width for all elements in the layout
     * @param width
     */
    protected final void setContentsWidth(double width)
    {
        for (Content content: getContents())
        {
            content.setWidth(width);
            content.refreshDown();
        }
    }
}
