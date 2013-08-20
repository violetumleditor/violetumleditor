package com.horstmann.violet.workspace.editorpart;

import java.awt.Graphics2D;

import com.horstmann.violet.product.diagram.abstracts.IGridSticker;


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
     * 
     * @return the corrector able to stick points on this grid
     */
    public abstract IGridSticker getGridSticker();

    /**
     * @return width of a grid portion (depending on the grid global size)
     */
    public abstract double getSnappingWidth();

    /**
     * @return height of a grid portion (depending on the grid global size)
     */
    public abstract double getSnappingHeight();

}