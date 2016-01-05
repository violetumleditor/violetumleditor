package com.horstmann.violet.framework.graphics.content;

import java.awt.geom.Point2D;

/**
 * Created by Adrian Bobrowski on 21.12.2015.
 */
public class HorizontalGroupContent extends GroupContent
{
    @Override
    protected Point2D getNextOffset(Point2D beforeOffset, Content content) {
        return new Point2D.Double(beforeOffset.getX() + content.getWidth(),beforeOffset.getY());
    }

    @Override
    protected Point2D getStartPointSeparator(Point2D offset) {
        return new Point2D.Double(offset.getX(), 0);
    }

    @Override
    protected Point2D getEndPointSeparator(Point2D offset) {
        return new Point2D.Double(offset.getX(), getHeight());
    }

    @Override
    public final void refreshUp() {
        int height = 0;
        int width = 0;

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
