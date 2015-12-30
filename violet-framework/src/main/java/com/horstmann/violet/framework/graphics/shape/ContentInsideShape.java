package com.horstmann.violet.framework.graphics.shape;

import com.horstmann.violet.framework.graphics.content.Content;
import com.horstmann.violet.framework.graphics.content.EmptyContent;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Adrian Bobrowski on 28.12.2015.
 */
public abstract class ContentInsideShape extends Content {
    protected abstract Shape getShape();

    @Override
    public void refresh()
    {
        Rectangle2D shapeBounds = getShape().getBounds();
        setWidth((int)shapeBounds.getWidth());
        setHeight((int)shapeBounds.getHeight());
    }

    @Override
    public void draw(Graphics2D g2, Point2D point) {
        Point2D offset = getShapeOffset();
        g2.translate(offset.getX(), offset.getY());
        content.draw(g2, point);
        g2.translate(-offset.getX(), -offset.getY());
    }

    private Point2D getShapeOffset()
    {
        Rectangle2D shapeBounds = getShape().getBounds();
        Rectangle2D contentBounds = content.getBounds();

        return new Point2D.Double(
            (shapeBounds.getWidth() - contentBounds.getWidth()) / 2,
            (shapeBounds.getHeight() - contentBounds.getHeight()) / 2
        );
    }

    protected Content content = new EmptyContent();
}
