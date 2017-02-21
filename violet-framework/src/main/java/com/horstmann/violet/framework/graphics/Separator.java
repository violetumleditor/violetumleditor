package com.horstmann.violet.framework.graphics;

import com.horstmann.violet.framework.graphics.content.Layout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 * This class defines how the separation of elements in the layout
 *
 * @see Layout#setSeparator(Separator)
 * @see Layout#getSeparator()
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 29.12.2015
 */
public abstract class Separator
{
    /**
     * draws a separator between the start point and end point
     * @param graphics
     * @param startPoint
     * @param endPoint
     */
    public abstract void draw(Graphics2D graphics, Point2D startPoint, Point2D endPoint);

    public static class EmptySeparator extends Separator
    {
        /**
         * @see Separator#draw(Graphics2D, Point2D, Point2D)
         */
        @Override
        public void draw(Graphics2D graphics, Point2D startPoint, Point2D endPoint)
        {}
    }

    public static class LineSeparator extends Separator
    {
        public LineSeparator()
        {
            this(Color.BLACK);
        }
        public LineSeparator(Color color)
        {
            this.color = color;
        }

        /**
         * @see Separator#draw(Graphics2D, Point2D, Point2D)
         */
        @Override
        public void draw(Graphics2D graphics, Point2D startPoint, Point2D endPoint)
        {
            if(null != color)
            {
                Color oldColor = graphics.getColor();
                graphics.setColor(color);
                graphics.drawLine((int) startPoint.getX(), (int) startPoint.getY(), (int) endPoint.getX(), (int) endPoint.getY());
                graphics.setColor(oldColor);
            }
        }

        /**
         * @see Separator#setColor(Color)
         */
        public void setColor(Color color) {
            this.color = color;
        }

        private Color color;
    }

    /**
     * sets the color which will be drawn separator
     * @param color
     */
    public void setColor(Color color) {}

    public static final Separator EMPTY = new EmptySeparator();
    public static final Separator LINE = new LineSeparator();
}
