package com.horstmann.violet.framework.graphics.content;

import com.horstmann.violet.framework.graphics.content.Content;
import com.horstmann.violet.framework.graphics.content.GroupContent;

import java.awt.geom.Point2D;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 21.12.2015
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
