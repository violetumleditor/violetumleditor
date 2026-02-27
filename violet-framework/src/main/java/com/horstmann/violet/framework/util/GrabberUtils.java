package com.horstmann.violet.framework.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.horstmann.violet.product.diagram.abstracts.node.ResizeDirection;

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
     * Draws a directional resize indicator at the given point.
     * Corners use an L-shaped bracket; edge midpoints use a straight dash.
     * The bracket arms always point <em>inwards</em> toward the node interior.
     *
     * @param g2        the graphics context
     * @param p         the anchor location of the handle
     * @param direction the resize direction this handle represents
     */
    public static void drawPurpleResizeGrabber(Graphics2D g2, Point2D p, ResizeDirection direction)
    {
        Color oldColor = g2.getColor();
        Stroke oldStroke = g2.getStroke();
        g2.setColor(PURPLE);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
        int arm = GRABBER_WIDTH * 2;
        int cx = (int) p.getX();
        int cy = (int) p.getY();
        switch (direction)
        {
            case SE: // L pointing left + up
                g2.drawLine(cx - arm, cy,       cx,       cy      );
                g2.drawLine(cx,       cy - arm, cx,       cy      );
                break;
            case NW: // L pointing right + down
                g2.drawLine(cx,       cy,       cx + arm, cy      );
                g2.drawLine(cx,       cy,       cx,       cy + arm);
                break;
            case NE: // L pointing left + down
                g2.drawLine(cx - arm, cy,       cx,       cy      );
                g2.drawLine(cx,       cy,       cx,       cy + arm);
                break;
            case SW: // L pointing right + up
                g2.drawLine(cx,       cy,       cx + arm, cy      );
                g2.drawLine(cx,       cy - arm, cx,       cy      );
                break;
            case N:  // horizontal dash
            case S:
                g2.drawLine(cx - arm, cy,       cx + arm, cy      );
                break;
            case W:  // vertical dash
            case E:
                g2.drawLine(cx,       cy - arm, cx,       cy + arm);
                break;
        }
        g2.setColor(oldColor);
        g2.setStroke(oldStroke);
    }

    /**
     * Draws a resize corner indicator as a small purple L-shaped bracket (SE direction).
     *
     * @param g2 the graphics context
     * @param p  the corner anchor location
     */
    public static void drawPurpleResizeGrabber(Graphics2D g2, Point2D p)
    {
        drawPurpleResizeGrabber(g2, p, ResizeDirection.SE);
    }

    private static final Color PURPLE = new Color(0.7f, 0.4f, 0.7f);
    private static final Color GRAY = Color.GRAY.brighter();
    public static final int GRABBER_WIDTH = 5;
    
    
}
