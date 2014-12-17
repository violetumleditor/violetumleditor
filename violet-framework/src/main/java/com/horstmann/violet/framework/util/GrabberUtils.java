package com.horstmann.violet.framework.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Utility class to draw "grabber", filled squares
 * 
 * @author HP-4720s
 *
 */
public class GrabberUtils
{

    /**
     * Draws a single "grabber", colored in purple
     * 
     * @param g2 the graphics context
     * @param x the x coordinate of the center of the grabber
     * @param y the y coordinate of the center of the grabber
     */
    public static void drawPurpleGrabber(Graphics2D g2, double x, double y)
    {
        Color oldColor = g2.getColor();
        g2.setColor(GrabberUtils.PURPLE);
        g2.fill(new Rectangle2D.Double(x - GrabberUtils.GRABBER_WIDTH / 2, y - GrabberUtils.GRABBER_WIDTH / 2, GrabberUtils.GRABBER_WIDTH, GrabberUtils.GRABBER_WIDTH));
        g2.setColor(oldColor);
    }

    /**
     * Draws a single "grabber", colored in gray
     * 
     * @param g2 the graphics context
     * @param x the x coordinate of the center of the grabber
     * @param y the y coordinate of the center of the grabber
     */
    public static void drawGrayGrabber(Graphics2D g2, double x, double y)
    {
        Color oldColor = g2.getColor();
        g2.setColor(GrabberUtils.GRAY);
        g2.fill(new Rectangle2D.Double(x - GrabberUtils.GRABBER_WIDTH / 2, y - GrabberUtils.GRABBER_WIDTH / 2, GrabberUtils.GRABBER_WIDTH, GrabberUtils.GRABBER_WIDTH));
        g2.setColor(oldColor);
    }
    
    
    private static final Color PURPLE = new Color(0.7f, 0.4f, 0.7f);
    private static final Color GRAY = Color.GRAY.brighter();
    public static final int GRABBER_WIDTH = 5;
    
    
}
