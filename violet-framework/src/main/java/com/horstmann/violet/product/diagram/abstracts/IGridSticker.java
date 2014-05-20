package com.horstmann.violet.product.diagram.abstracts;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public interface IGridSticker
{
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

}
