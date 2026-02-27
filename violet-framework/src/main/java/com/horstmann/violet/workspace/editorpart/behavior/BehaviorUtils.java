package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.IResizableNode;
import com.horstmann.violet.product.diagram.abstracts.node.ResizeDirection;
import com.horstmann.violet.workspace.editorpart.IEditorPart;

public class BehaviorUtils
{
    /**
     * Returns the {@link ResizeDirection} corresponding to the resize handle the
     * mouse cursor is currently hovering over, or {@code null} if the cursor is
     * not over any resize handle of the selected resizable node.
     */
    public static ResizeDirection findResizeDirection(IEditorPart editorPart, MouseEvent event)
    {
        IGraph graph = editorPart.getGraph();
        double zoom = editorPart.getZoomFactor();
        Point currentLocation = event.getPoint();
        double x = currentLocation.getX() / zoom;
        double y = currentLocation.getY() / zoom;
        currentLocation.setLocation(x, y);

        // Mouse must be over a node
        INode node = graph.findNode(currentLocation);
        if (node == null) {
            return null;
        }

        // Node must be resizable
        if (!(node instanceof IResizableNode)) {
            return null;
        }

        IResizableNode resizableNode = (IResizableNode) node;
        Map<ResizeDirection, Rectangle2D> handles = resizableNode.getResizableDragPoints();
        for (Map.Entry<ResizeDirection, Rectangle2D> entry : handles.entrySet()) {
            if (entry.getValue().contains(currentLocation)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Returns {@code true} if the mouse cursor is over any resize handle of the
     * selected resizable node.
     */
    public static boolean isCursorOnResizePoint(IEditorPart editorPart, MouseEvent event)
    {
        return findResizeDirection(editorPart, event) != null;
    }
}
