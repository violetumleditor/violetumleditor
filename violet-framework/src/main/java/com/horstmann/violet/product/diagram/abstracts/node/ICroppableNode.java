package com.horstmann.violet.product.diagram.abstracts.node;

import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A node that supports cropping: the visible display window can be narrowed
 * from any of the four edges (top, left, bottom, right) independently.
 *
 * <p>Cropping does <em>not</em> change the node's allocated position or its
 * full content size; it only masks the painted area.  Connection points are
 * calculated against the <em>visible</em> (cropped) bounds so that edges
 * always attach to the currently visible edge of the node.</p>
 */
public interface ICroppableNode
{
    /** Hit-zone half-size (in graph units) for each crop drag handle. */
    static final int CROP_POINT_SIZE = 20;

    /** Minimum visible dimension (width or height) after cropping. */
    static final double MIN_VISIBLE  = 10.0;

    // ------------------------------------------------------------------
    // Contract
    // ------------------------------------------------------------------

    /** Returns the current crop insets (never {@code null}). */
    CropInsets getCropInsets();

    /** Replaces the current crop insets. Pass {@code null} to reset. */
    void setCropInsets(CropInsets insets);

    /**
     * Returns the full (uncropped) bounds of the node.
     * Used as the basis for computing the visible bounds.
     */
    Rectangle2D getBounds();

    // ------------------------------------------------------------------
    // Default helpers
    // ------------------------------------------------------------------

    /**
     * Returns the visible (cropped) bounds: the subset of the full bounds
     * that remains after subtracting all four crop insets.
     */
    default Rectangle2D getVisibleBounds()
    {
        Rectangle2D full = getBounds();
        CropInsets ci = getCropInsets();
        if (ci == null || ci.isEmpty())
        {
            return full;
        }
        double x = full.getX()      + ci.getLeft();
        double y = full.getY()      + ci.getTop();
        double w = full.getWidth()  - ci.getLeft() - ci.getRight();
        double h = full.getHeight() - ci.getTop()  - ci.getBottom();
        w = Math.max(w, MIN_VISIBLE);
        h = Math.max(h, MIN_VISIBLE);
        return new Rectangle2D.Double(x, y, w, h);
    }

    /**
     * Returns hit-test rectangles for the four edge crop handles (N, S, W, E),
     * centred on the edges of the <em>visible</em> bounds.
     *
     * @return ordered map of {@link ResizeDirection} → handle bounding box
     */
    default Map<ResizeDirection, Rectangle2D> getCropDragPoints()
    {
        Rectangle2D v = getVisibleBounds();
        int s = CROP_POINT_SIZE;
        Map<ResizeDirection, Rectangle2D> points = new LinkedHashMap<>();
        points.put(ResizeDirection.N, makeHandle(v.getCenterX(), v.getMinY(), s));
        points.put(ResizeDirection.S, makeHandle(v.getCenterX(), v.getMaxY(), s));
        points.put(ResizeDirection.W, makeHandle(v.getMinX(),    v.getCenterY(), s));
        points.put(ResizeDirection.E, makeHandle(v.getMaxX(),    v.getCenterY(), s));
        return points;
    }

    private static Rectangle2D makeHandle(double cx, double cy, int size)
    {
        return new Rectangle2D.Double(cx - size / 2.0, cy - size / 2.0, size, size);
    }
}
