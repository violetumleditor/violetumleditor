package com.horstmann.violet.framework.graphics.content;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Adrian Bobrowski on 28.12.2015.
 */
public abstract class ContentInsideShape extends Content{
    protected abstract Shape getShape();

    public void refresh()
    {
        Rectangle2D shapeBounds = getShape().getBounds();
        width = (int)shapeBounds.getWidth();
        height = (int)shapeBounds.getHeight();
    }

    @Override
    public void draw(Graphics2D g2, Point2D point) {
        Rectangle2D shapeBounds = getShape().getBounds();
        Rectangle2D contentBounds = content.getBounds();

        Point2D offset = new Point2D.Double(
            (shapeBounds.getWidth() - contentBounds.getWidth()) / 2,
            (shapeBounds.getHeight() - contentBounds.getHeight()) / 2
        );
        g2.translate(offset.getX(), offset.getY());
        content.draw(g2, point);
        g2.translate(-offset.getX(), -offset.getY());
    }



    protected Content content = new EmptyContent();
}
