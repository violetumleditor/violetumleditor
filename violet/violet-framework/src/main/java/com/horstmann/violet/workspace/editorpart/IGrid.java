package com.horstmann.violet.workspace.editorpart;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public interface IGrid
{

    /**
     * Changes grid visibility
     * 
     * @param isVisible
     */
    public abstract void setVisible(boolean isVisible);

    /**
     * @return true id the grid is visible
     */
    public abstract boolean isVisible();

    /**
     * Change grid size
     * 
     * @param steps the number of steps by which to change the zoom. A positive value zooms in, a negative value zooms out.
     */
    public abstract void changeGridSize(int steps);

    /**
     * Draws this grid inside a rectangle.
     * 
     * @param g2 the graphics context
     * @param bounds the bounding rectangle
     */
    public abstract void paint(Graphics2D g2);

    /**
     * Snaps a point to the nearest grid point
     * 
     * @param p the point to snap. After the call, the coordinates of p are changed so that p falls on the grid.
     * @return the point after modification
     */
    public abstract Point2D snap(Point2D p);

    /**
     * Snaps a rectangle to the nearest grid points
     * 
     * @param r the rectangle to snap. After the call, the coordinates of r are changed so that all of its corners falls on the
     *            grid.
     * @return r (for convenience)
     */
    public abstract Rectangle2D snap(Rectangle2D r);

    /**
     * @return width of a grid portion (depending on the grid global size)
     */
    public abstract double getSnappingWidth();

    /**
     * @return height of a grid portion (depending on the grid global size)
     */
    public abstract double getSnappingHeight();

}