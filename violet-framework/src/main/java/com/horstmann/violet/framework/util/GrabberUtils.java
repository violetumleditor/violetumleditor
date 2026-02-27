package com.horstmann.violet.framework.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;
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
     * Draws a single "grabber", colored in purple
     * 
     * @param g2 the graphics context
     * @param p the grabber point location
     */
    public static void drawPurpleGrabber(Graphics2D g2, Point2D p) {
    	drawPurpleGrabber(g2, p.getX(), p.getY());
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
    
    /**
     * Draws a single "grabber", colored in gray
     * 
     * @param g2 the graphics context
     * @param p the grabber point location
     */
    public static void drawGrayGrabber(Graphics2D g2, Point2D p) {
    	drawGrayGrabber(g2, p.getX(), p.getY());
    }
    
    
    
    /**
     * Draws a resize corner indicator as a small purple L-shaped bracket.
     * Suitable for the resize anchor of resizable nodes.
     *
     * @param g2 the graphics context
     * @param x the x coordinate of the corner (bottom-right anchor)
     * @param y the y coordinate of the corner (bottom-right anchor)
     */
    public static void drawPurpleResizeGrabber(Graphics2D g2, double x, double y)
    {
        Color oldColor = g2.getColor();
        Stroke oldStroke = g2.getStroke();
        g2.setColor(PURPLE);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
        int arm = GRABBER_WIDTH * 2;
        // Horizontal arm going left from the corner
        g2.drawLine((int) (x - arm), (int) y, (int) x, (int) y);
        // Vertical arm going up from the corner
        g2.drawLine((int) x, (int) (y - arm), (int) x, (int) y);
        g2.setColor(oldColor);
        g2.setStroke(oldStroke);
    }

    /**
     * Draws a resize corner indicator as a small purple L-shaped bracket.
     *
     * @param g2 the graphics context
     * @param p  the corner (bottom-right anchor) location
     */
    public static void drawPurpleResizeGrabber(Graphics2D g2, Point2D p)
    {
        drawPurpleResizeGrabber(g2, p.getX(), p.getY());
    }

    private static final Color PURPLE = new Color(0.7f, 0.4f, 0.7f);
    private static final Color GRAY = Color.GRAY.brighter();
    public static final int GRABBER_WIDTH = 5;
    
    
}
