package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import com.horstmann.violet.product.diagram.abstracts.node.ICroppableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.IResizableNode;
import com.horstmann.violet.product.diagram.abstracts.node.ResizeDirection;
import com.horstmann.violet.workspace.editorpart.IEditorPart;

public class BehaviorUtils
{
    /**
     * Returns the {@link ResizeDirection} of the resize handle the mouse is
     * currently hovering over, or {@code null} if not over any resize handle.
     */
    public static ResizeDirection findResizeDirection(IEditorPart editorPart, MouseEvent event)
    {
        INode node = findScaledNode(editorPart, event);
        if (node == null || !(node instanceof IResizableNode)) return null;
        IResizableNode resizableNode = (IResizableNode) node;
        Point scaledLocation = scaledPoint(editorPart, event);
        for (Map.Entry<ResizeDirection, Rectangle2D> entry : resizableNode.getResizableDragPoints().entrySet())
        {
            if (entry.getValue().contains(scaledLocation)) return entry.getKey();
        }
        return null;
    }

    /**
     * Returns the {@link ResizeDirection} of the crop handle the mouse is
     * currently hovering over, or {@code null} if not over any crop handle.
     * Never returns a corner direction (NW/NE/SW/SE); only N/S/W/E.
     */
    public static ResizeDirection findCropDirection(IEditorPart editorPart, MouseEvent event)
    {
        INode node = findScaledNode(editorPart, event);
        if (node == null || !(node instanceof ICroppableNode)) return null;
        ICroppableNode croppableNode = (ICroppableNode) node;
        Point scaledLocation = scaledPoint(editorPart, event);
        for (Map.Entry<ResizeDirection, Rectangle2D> entry : croppableNode.getCropDragPoints().entrySet())
        {
            if (entry.getValue().contains(scaledLocation)) return entry.getKey();
        }
        return null;
    }

    /** Returns {@code true} if the mouse is over any resize handle. */
    public static boolean isCursorOnResizePoint(IEditorPart editorPart, MouseEvent event)
    {
        return findResizeDirection(editorPart, event) != null;
    }

    /** Returns {@code true} if the mouse is over any crop handle. */
    public static boolean isCursorOnCropPoint(IEditorPart editorPart, MouseEvent event)
    {
        return findCropDirection(editorPart, event) != null;
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private static Point scaledPoint(IEditorPart editorPart, MouseEvent event)
    {
        double zoom = editorPart.getZoomFactor();
        Point p = new Point();
        p.setLocation(event.getX() / zoom, event.getY() / zoom);
        return p;
    }

    private static INode findScaledNode(IEditorPart editorPart, MouseEvent event)
    {
        return editorPart.getGraph().findNode(scaledPoint(editorPart, event));
    }
}
