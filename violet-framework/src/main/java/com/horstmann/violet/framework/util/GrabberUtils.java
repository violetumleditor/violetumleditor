package com.horstmann.violet.framework.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;


public class GrabberUtils
{

    /**
     * Draws a single "grabber", a filled square
     * 
     * @param g2 the graphics context
     * @param x the x coordinate of the center of the grabber
     * @param y the y coordinate of the center of the grabber
     */
    public static void drawGrabber(Graphics2D g2, double x, double y)
    {
        final int SIZE = 5;
        Color oldColor = g2.getColor();
        g2.setColor(GrabberUtils.PURPLE);
        g2.fill(new Rectangle2D.Double(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE));
        g2.setColor(oldColor);
    }

    private static final Color PURPLE = new Color(0.7f, 0.4f, 0.7f);

}
