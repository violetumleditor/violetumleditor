package com.horstmann.violet.framework.graphics;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Adrian Bobrowski on 29.12.2015.
 */
public abstract class Separator
{
    public abstract void draw(Graphics2D g2, Point2D startPoint, Point2D endPoint);

    public static class EmptySeparator extends Separator
    {
        @Override
        public void draw(Graphics2D g2, Point2D startPoint, Point2D endPoint)
        {}
    };

    public static class LineSeparator extends Separator
    {
        public LineSeparator()
        {
            color = Color.BLACK;
        }
        public LineSeparator(Color color)
        {
            this.color = color;
        }

        @Override
        public void draw(Graphics2D g2, Point2D startPoint, Point2D endPoint)
        {
            Color oldColor = g2.getColor();
            g2.setColor(color);
            g2.drawLine((int)startPoint.getX(), (int)startPoint.getY(), (int)endPoint.getX(), (int)endPoint.getY());
            g2.setColor(oldColor);
        }

        private Color color;
    }

    public static final Separator EMPTY = new EmptySeparator();
    public static final Separator LINE = new LineSeparator();
}
