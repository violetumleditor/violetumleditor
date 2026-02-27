package com.horstmann.violet.product.diagram.abstracts.node;

import java.io.Serializable;

/**
 * Stores the four inset amounts (in graph units) that define how much of a
 * node's content is hidden by cropping from each edge.
 * Zero on all sides means no cropping.
 */
public class CropInsets implements Serializable, Cloneable
{
    private static final long serialVersionUID = 1L;

    private double top;
    private double left;
    private double bottom;
    private double right;

    /**
     * Creates zero-inset (no crop) instance.
     */
    public CropInsets()
    {
        this(0, 0, 0, 0);
    }

    /**
     * Creates an instance with the given insets.
     *
     * @param top    pixels cropped from the top edge
     * @param left   pixels cropped from the left edge
     * @param bottom pixels cropped from the bottom edge
     * @param right  pixels cropped from the right edge
     */
    public CropInsets(double top, double left, double bottom, double right)
    {
        this.top    = Math.max(0, top);
        this.left   = Math.max(0, left);
        this.bottom = Math.max(0, bottom);
        this.right  = Math.max(0, right);
    }

    public double getTop()    { return top;    }
    public double getLeft()   { return left;   }
    public double getBottom() { return bottom; }
    public double getRight()  { return right;  }

    public void setTop(double top)       { this.top    = Math.max(0, top);    }
    public void setLeft(double left)     { this.left   = Math.max(0, left);   }
    public void setBottom(double bottom) { this.bottom = Math.max(0, bottom); }
    public void setRight(double right)   { this.right  = Math.max(0, right);  }

    /** Returns {@code true} when no cropping is applied on any side. */
    public boolean isEmpty()
    {
        return top == 0 && left == 0 && bottom == 0 && right == 0;
    }

    @Override
    public CropInsets clone()
    {
        try
        {
            return (CropInsets) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            throw new AssertionError(e);
        }
    }
}
