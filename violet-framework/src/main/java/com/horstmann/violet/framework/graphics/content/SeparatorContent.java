package com.horstmann.violet.framework.graphics.content;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Adrian Bobrowski on 21.12.2015.
 */
public class SeparatorContent extends Content{
    public SeparatorContent()
    {
        this(1);
    }

    public SeparatorContent(int thickness)
    {
        setMinHeight(thickness);
        setMinWidth(thickness);
    }

    @Override
    public void draw(Graphics2D g2, Point2D point) {
        g2.fill(new Rectangle2D.Double(point.getX(), point.getY(), width, height));
    }
}
