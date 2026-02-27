package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.node.CropInsets;
import com.horstmann.violet.product.diagram.abstracts.node.ICroppableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.IResizableNode;
import com.horstmann.violet.product.diagram.abstracts.node.ResizeDirection;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;

public class ResizeNodeBehavior extends AbstractEditorPartBehavior
{
    private static final double MIN_SIZE = 10.0;

    public ResizeNodeBehavior(IEditorPart editorPart, IGraphToolsBar graphToolsBar)
    {
        this.editorPart = editorPart;
        this.selectionHandler = editorPart.getSelectionHandler();
    }

    @Override
    public void onMouseClicked(MouseEvent event)
    {
        this.isReadyForResizing = false;
    }

    @Override
    public void onMousePressed(MouseEvent event)
    {
        this.isReadyForResizing = false;
        this.activeDirection = BehaviorUtils.findResizeDirection(editorPart, event);
        if (this.activeDirection != null)
        {
            this.isReadyForResizing = true;
            List<IResizableNode> selectedNodes = selectedResizableNodes();
            if (!selectedNodes.isEmpty())
            {
                IResizableNode node0 = selectedNodes.get(0);
                this.initialBounds = effectiveBoundsForResize(node0);
                if (node0 instanceof INode)
                {
                    this.initialLocation = ((INode) node0).getLocation();
                }
            }
        }
    }

    @Override
    public void onMouseMoved(MouseEvent event)
    {
        zoom = editorPart.getZoomFactor();
        List<IResizableNode> sNodes = selectedResizableNodes();
        if (sNodes.size() == 1)
        {
            ResizeDirection direction = BehaviorUtils.findResizeDirection(editorPart, event);
            if (direction != null || isResizing)
            {
                int cursorType = direction != null
                        ? direction.getCursorType()
                        : (activeDirection != null ? activeDirection.getCursorType() : Cursor.SE_RESIZE_CURSOR);
                editorPart.getSwingComponent().setCursor(Cursor.getPredefinedCursor(cursorType));
                DragSelectedBehavior.lock();
            }
            else if (!BehaviorUtils.isCursorOnCropPoint(editorPart, event))
            {
                editorPart.getSwingComponent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                DragSelectedBehavior.unlock();
            }
        }
    }

    @Override
    public void onMouseDragged(MouseEvent event)
    {
        if (!isReadyForResizing || activeDirection == null || initialBounds == null)
        {
            isResizing = false;
            return;
        }

        List<IResizableNode> selectedNodes = selectedResizableNodes();
        if (selectedNodes.isEmpty())
        {
            return;
        }

        IResizableNode node0 = selectedNodes.get(0);
        zoom = editorPart.getZoomFactor();

        // Convert mouse from screen space to graph space
        double mouseX = event.getX() / zoom;
        double mouseY = event.getY() / zoom;

        double initMinX = initialBounds.getMinX();
        double initMinY = initialBounds.getMinY();
        double initMaxX = initialBounds.getMaxX();
        double initMaxY = initialBounds.getMaxY();
        double initW    = initialBounds.getWidth();
        double initH    = initialBounds.getHeight();

        double newW;
        double newH;
        double desiredMinX = initMinX;
        double desiredMinY = initMinY;

        switch (activeDirection)
        {
            case SE: newW = mouseX - initMinX; newH = mouseY - initMinY;                                              break;
            case NW: newW = initMaxX - mouseX; newH = initMaxY - mouseY; desiredMinX = mouseX; desiredMinY = mouseY;  break;
            case NE: newW = mouseX - initMinX; newH = initMaxY - mouseY;                       desiredMinY = mouseY;  break;
            case SW: newW = initMaxX - mouseX; newH = mouseY  - initMinY; desiredMinX = mouseX;                       break;
            case N:  newW = initW;             newH = initMaxY - mouseY;                       desiredMinY = mouseY;  break;
            case S:  newW = initW;             newH = mouseY  - initMinY;                                             break;
            case E:  newW = mouseX - initMinX; newH = initH;                                                          break;
            case W:  newW = initMaxX - mouseX; newH = initH;              desiredMinX = mouseX;                       break;
            default: return;
        }

        // Clamp to minimum – re-anchor fixed edge when clamped
        if (newW < MIN_SIZE)
        {
            newW = MIN_SIZE;
            switch (activeDirection)
            {
                case NW: case SW: case W: desiredMinX = initMaxX - newW; break;
                default: break;
            }
        }
        if (newH < MIN_SIZE)
        {
            newH = MIN_SIZE;
            switch (activeDirection)
            {
                case NW: case NE: case N: desiredMinY = initMaxY - newH; break;
                default: break;
            }
        }

        // For cropped nodes newW/newH are desired *visible* dimensions (because
        // effectiveBoundsForResize used visible bounds as the baseline). Convert
        // to *full* node size by adding back the crop margins on each axis.
        if (node0 instanceof ICroppableNode)
        {
            CropInsets ci = ((ICroppableNode) node0).getCropInsets();
            if (ci != null && !ci.isEmpty())
            {
                newW += ci.getLeft() + ci.getRight();
                newH += ci.getTop()  + ci.getBottom();
            }
        }

        Dimension snapped = snap(new Dimension((int) newW, (int) newH));

        // Reposition the node when pulling its top-left corner/edge
        if (node0 instanceof INode && initialLocation != null)
        {
            double dx = desiredMinX - initMinX;
            double dy = desiredMinY - initMinY;
            if (dx != 0.0 || dy != 0.0)
            {
                ((INode) node0).setLocation(new Point2D.Double(
                        initialLocation.getX() + dx,
                        initialLocation.getY() + dy));
            }
        }

        node0.setPreferredSize(new Rectangle2D.Double(0, 0, snapped.getWidth(), snapped.getHeight()));
        isResizing = true;
        editorPart.getSwingComponent().setCursor(
                Cursor.getPredefinedCursor(activeDirection.getCursorType()));
        editorPart.getSwingComponent().repaint();
    }

    @Override
    public void onMouseReleased(MouseEvent event)
    {
        isResizing = false;
        isReadyForResizing = false;
        activeDirection = null;
        initialBounds = null;
        initialLocation = null;
        DragSelectedBehavior.unlock();
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private List<IResizableNode> selectedResizableNodes()
    {
        return this.selectionHandler.getSelectedElements().stream()
                .filter(e -> e instanceof IResizableNode)
                .map(n -> (IResizableNode) n)
                .toList();
    }

    /**
     * Returns the bounds to use as the resize baseline.
     * For croppable nodes the visible (cropped) bounds are used so that the
     * resize drag anchors match where the corner handles are actually drawn.
     */
    private static Rectangle2D effectiveBoundsForResize(IResizableNode node)
    {
        if (node instanceof ICroppableNode)
        {
            return ((ICroppableNode) node).getVisibleBounds();
        }
        return node.getBounds();
    }

    private Dimension snap(Dimension dimension)
    {
        double snappingWidth  = editorPart.getGrid().getSnappingWidth();
        double snappingHeight = editorPart.getGrid().getSnappingHeight();

        int width  = (int) (Math.floor(dimension.getWidth()  / snappingWidth)  * snappingWidth);
        int height = (int) (Math.floor(dimension.getHeight() / snappingHeight) * snappingHeight);

        width  = (int) Math.max(width,  MIN_SIZE);
        height = (int) Math.max(height, MIN_SIZE);

        return new Dimension(width, height);
    }

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private final IEditorPartSelectionHandler selectionHandler;
    private final IEditorPart                 editorPart;

    private boolean         isReadyForResizing = false;
    private boolean         isResizing         = false;
    private double          zoom               = 1;

    /** Which of the eight handles is currently being dragged. */
    private ResizeDirection activeDirection    = null;

    /** Node bounds captured at the start of the drag gesture. */
    private Rectangle2D     initialBounds      = null;

    /** Node location captured at the start of the drag gesture. */
    private Point2D         initialLocation    = null;
}