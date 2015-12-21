package com.horstmann.violet.framework.graphics.content;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Adrian Bobrowski on 21.12.2015.
 */
public class HorizontalGroupContent extends GroupContent {
    @Override
    protected void measureAndSetSize()
    {
        Rectangle2D bounds;
        int tmpHeight;
        width = 0;

        for (Content content: contents) {
            bounds = content.getBounds();
            tmpHeight = (int)bounds.getHeight();
            width += (int)bounds.getWidth();
            if(height<tmpHeight)
            {
                height = tmpHeight;
            }
        }
        setHeight(height);
    }

    @Override
    public void draw(Graphics2D g2, Point2D point) {
        refresh();
        for (Content content: contents) {
            content.draw(g2, point);

            point = new Point2D.Double(point.getX() + content.getBounds().getWidth(),point.getY());
        }
    }
}
