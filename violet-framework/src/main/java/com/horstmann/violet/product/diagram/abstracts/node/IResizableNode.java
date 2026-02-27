package com.horstmann.violet.product.diagram.abstracts.node;

import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Node size is automatic. However, by implementing this interface,
 * the user is able to add extra width and extra height to this diagram element.
 * Resize handles are provided at all four corners and four edge midpoints.
 *
 * @author Alexandre de Pellegrin
 */
public interface IResizableNode
{
    static final int RESIZABLE_POINT_SIZE = 20;

    void setPreferredSize(Rectangle2D size);

    Rectangle2D getPreferredSize();

    Rectangle2D getBounds();

    /**
     * Returns a hit-test rectangle for every resize direction.
     * Each entry maps a {@link ResizeDirection} to a {@link Rectangle2D} that
     * represents the draggable anchor zone for that corner or edge.
     *
     * @return ordered map of resize direction → drag-handle bounding box
     */
    default Map<ResizeDirection, Rectangle2D> getResizableDragPoints()
    {
        Rectangle2D b = getBounds();
        int s = RESIZABLE_POINT_SIZE;
        Map<ResizeDirection, Rectangle2D> points = new LinkedHashMap<>();
        points.put(ResizeDirection.NW, makeHandle(b.getMinX(), b.getMinY(), s));
        points.put(ResizeDirection.NE, makeHandle(b.getMaxX(), b.getMinY(), s));
        points.put(ResizeDirection.SW, makeHandle(b.getMinX(), b.getMaxY(), s));
        points.put(ResizeDirection.SE, makeHandle(b.getMaxX(), b.getMaxY(), s));
        return points;
    }

    /**
     * @deprecated Use {@link #getResizableDragPoints()} for full eight-direction support.
     */
    @Deprecated
    default Rectangle2D getResizableDragPoint()
    {
        return getResizableDragPoints().get(ResizeDirection.SE);
    }

    private static Rectangle2D makeHandle(double cx, double cy, int size)
    {
        return new Rectangle2D.Double(cx - size / 2.0, cy - size / 2.0, size, size);
    }
}