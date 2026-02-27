package com.horstmann.violet.product.diagram.abstracts.node;

import java.awt.Cursor;

/**
 * Represents the eight possible resize directions for a resizable node.
 * Each direction carries the corresponding AWT cursor type.
 */
public enum ResizeDirection
{
    NW(Cursor.NW_RESIZE_CURSOR),
    N(Cursor.N_RESIZE_CURSOR),
    NE(Cursor.NE_RESIZE_CURSOR),
    W(Cursor.W_RESIZE_CURSOR),
    E(Cursor.E_RESIZE_CURSOR),
    SW(Cursor.SW_RESIZE_CURSOR),
    S(Cursor.S_RESIZE_CURSOR),
    SE(Cursor.SE_RESIZE_CURSOR);

    private final int cursorType;

    ResizeDirection(int cursorType)
    {
        this.cursorType = cursorType;
    }

    /**
     * Returns the AWT predefined cursor type for this resize direction.
     */
    public int getCursorType()
    {
        return cursorType;
    }
}
