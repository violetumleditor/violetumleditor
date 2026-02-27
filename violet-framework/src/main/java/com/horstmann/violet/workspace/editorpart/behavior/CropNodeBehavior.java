package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.node.CropInsets;
import com.horstmann.violet.product.diagram.abstracts.node.ICroppableNode;
import com.horstmann.violet.product.diagram.abstracts.node.IResizableNode;
import com.horstmann.violet.product.diagram.abstracts.node.ResizeDirection;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;

/**
 * Behavior that allows the user to crop an {@link ICroppableNode} by dragging
 * any of its four edge handles (N, S, W, E).
 *
 * <p>Cropping slides the visible window inward or outward from each edge.
 * The node's full content and position are unchanged; only the painted area
 * (and therefore the connection-point placement) is affected.</p>
 */
public class CropNodeBehavior extends AbstractEditorPartBehavior
{
    public CropNodeBehavior(IEditorPart editorPart, IGraphToolsBar graphToolsBar)
    {
        this.editorPart       = editorPart;
        this.selectionHandler = editorPart.getSelectionHandler();
    }

    // ------------------------------------------------------------------
    // Mouse events
    // ------------------------------------------------------------------

    @Override
    public void onMouseClicked(MouseEvent event)
    {
        this.isReadyForCropping = false;
    }

    @Override
    public void onMousePressed(MouseEvent event)
    {
        this.isReadyForCropping = false;
        this.activeDirection    = BehaviorUtils.findCropDirection(editorPart, event);
        if (this.activeDirection != null)
        {
            List<ICroppableNode> nodes = selectedCroppableNodes();
            if (!nodes.isEmpty())
            {
                ICroppableNode node0 = nodes.get(0);
                this.initialFullBounds = node0.getBounds();
                CropInsets ci = node0.getCropInsets();
                this.initialInsets = (ci != null) ? ci.clone() : new CropInsets();
                this.isReadyForCropping = true;
            }
        }
    }

    @Override
    public void onMouseMoved(MouseEvent event)
    {
        List<ICroppableNode> sNodes = selectedCroppableNodes();
        if (sNodes.size() == 1)
        {
            ResizeDirection direction = BehaviorUtils.findCropDirection(editorPart, event);
            if (direction != null || isCropping)
            {
                int cursorType = direction != null
                        ? direction.getCursorType()
                        : (activeDirection != null ? activeDirection.getCursorType() : Cursor.N_RESIZE_CURSOR);
                editorPart.getSwingComponent().setCursor(Cursor.getPredefinedCursor(cursorType));
                DragSelectedBehavior.lock();
            }
            else
            {
                editorPart.getSwingComponent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                DragSelectedBehavior.unlock();
            }
        }
    }

    @Override
    public void onMouseDragged(MouseEvent event)
    {
        if (!isReadyForCropping || activeDirection == null || initialFullBounds == null)
        {
            isCropping = false;
            return;
        }

        List<ICroppableNode> nodes = selectedCroppableNodes();
        if (nodes.isEmpty())
        {
            return;
        }

        double zoom   = editorPart.getZoomFactor();
        double mouseX = event.getX() / zoom;
        double mouseY = event.getY() / zoom;

        double fullMinX = initialFullBounds.getMinX();
        double fullMinY = initialFullBounds.getMinY();
        double fullMaxX = initialFullBounds.getMaxX();
        double fullMaxY = initialFullBounds.getMaxY();
        double fullW    = initialFullBounds.getWidth();
        double fullH    = initialFullBounds.getHeight();

        // Compute the new inset for the dragged direction, keeping others from initialInsets
        double newTop    = initialInsets.getTop();
        double newLeft   = initialInsets.getLeft();
        double newBottom = initialInsets.getBottom();
        double newRight  = initialInsets.getRight();

        switch (activeDirection)
        {
            case N: newTop    = clamp(mouseY - fullMinY,    0, fullH - newBottom  - ICroppableNode.MIN_VISIBLE); break;
            case S: newBottom = clamp(fullMaxY - mouseY,    0, fullH - newTop     - ICroppableNode.MIN_VISIBLE); break;
            case W: newLeft   = clamp(mouseX - fullMinX,    0, fullW - newRight   - ICroppableNode.MIN_VISIBLE); break;
            case E: newRight  = clamp(fullMaxX - mouseX,    0, fullW - newLeft    - ICroppableNode.MIN_VISIBLE); break;
            default: return; // corner directions are handled by ResizeNodeBehavior
        }

        ICroppableNode node0 = nodes.get(0);
        node0.setCropInsets(new CropInsets(newTop, newLeft, newBottom, newRight));

        isCropping = true;
        editorPart.getSwingComponent().setCursor(
                Cursor.getPredefinedCursor(activeDirection.getCursorType()));
        editorPart.getSwingComponent().repaint();
    }

    @Override
    public void onMouseReleased(MouseEvent event)
    {
        isCropping          = false;
        isReadyForCropping  = false;
        activeDirection     = null;
        initialFullBounds   = null;
        initialInsets       = null;
        DragSelectedBehavior.unlock();
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private List<ICroppableNode> selectedCroppableNodes()
    {
        return this.selectionHandler.getSelectedElements().stream()
                .filter(e -> e instanceof ICroppableNode)
                .map(n -> (ICroppableNode) n)
                .toList();
    }

    private static double clamp(double value, double min, double max)
    {
        return Math.max(min, Math.min(max, value));
    }

    // ------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------

    private final IEditorPartSelectionHandler selectionHandler;
    private final IEditorPart                 editorPart;

    private boolean         isReadyForCropping = false;
    private boolean         isCropping         = false;

    /** Edge direction being dragged. */
    private ResizeDirection activeDirection     = null;

    /** Full (un-cropped) bounds of the node captured on drag start. */
    private Rectangle2D     initialFullBounds   = null;

    /** Crop insets captured on drag start. */
    private CropInsets      initialInsets       = null;
}
