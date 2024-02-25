package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.IResizableNode;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;

public class ResizeNodeBehavior extends AbstractEditorPartBehavior {

    public ResizeNodeBehavior(IEditorPart editorPart, IGraphToolsBar graphToolsBar) {
        this.editorPart = editorPart;
        this.graph = editorPart.getGraph();
        this.selectionHandler = editorPart.getSelectionHandler();
        this.graphToolsBar = graphToolsBar;
    }

    @Override
    public void onMouseClicked(MouseEvent event) {
    	this.isReadyForResizing = false;
    }

    @Override
    public void onMousePressed(MouseEvent event) {
    	this.isReadyForResizing = false;
    	if (Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR).equals(editorPart.getSwingComponent().getCursor())) {
    		this.isReadyForResizing = true;
    	};
    }

    @Override
    public void onMouseMoved(MouseEvent event) {
        zoom = editorPart.getZoomFactor();

        List<INode> sNodes = this.selectionHandler.getSelectedElements().stream().filter(e -> IResizableNode.class.isInstance(e)).map(n -> (INode) n).toList();
        
        if (sNodes.size() == 1) {
            if ((BehaviorUtils.isCursorOnResizePoint(editorPart, event)) || isResizing) {
                editorPart.getSwingComponent().setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                DragSelectedBehavior.lock();
            } else {
                editorPart.getSwingComponent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                DragSelectedBehavior.unlock();
            }
        }
    }

    @Override
    public void onMouseDragged(MouseEvent event) {
        Point point = event.getPoint();
        lastMousePoint = new Point2D.Double(point.getX(), point.getY());

        List<IResizableNode> selectedNodes = this.selectionHandler.getSelectedElements().stream().filter(e -> IResizableNode.class.isInstance(e)).map(n -> (IResizableNode) n).toList();
        
        if (isReadyForResizing) {
            IResizableNode node0 = selectedNodes.get(0);
            Rectangle2D bounds = node0.getBounds();
            isResizing = true;
            editorPart.getSwingComponent().setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
            Dimension snapped = snap(evaluate(event.getPoint(), bounds));
            node0.setPreferredSize(new Rectangle2D.Double(bounds.getX(), bounds.getY(), snapped.getWidth(), snapped.getHeight()));
            editorPart.getSwingComponent().repaint();
        } else {
            isResizing = false;
        }
    }

    @Override
    public void onMouseReleased(MouseEvent event) {
        isResizing = false;
        isReadyForResizing = false;
        DragSelectedBehavior.unlock();
    }

    private Dimension snap(Dimension dimension) {
        double snappingWidth = editorPart.getGrid().getSnappingWidth();
        double snappingHeight = editorPart.getGrid().getSnappingHeight();

        int width = (int) (dimension.getWidth() / snappingWidth);
        width = (int) (width * snappingWidth);

        int height = (int) (dimension.getHeight() / snappingHeight);
        height = (int) (height * snappingHeight);

        return new Dimension(width, height);
    }

    private Dimension evaluate(Point current, Rectangle2D bounds) {
        int width = (int) Math.abs((current.getX() - bounds.getMinX()) / zoom);
        int height = (int) Math.abs((current.getY() - bounds.getMinY()) / zoom);

        return new Dimension(width, height);
    }





    private IGraph graph;

    private Point2D lastMousePoint = null;

    private IEditorPartSelectionHandler selectionHandler;

    private IEditorPart editorPart;

    private IGraphToolsBar graphToolsBar;

    private boolean isReadyForDragging = false;

    private boolean isReadyForResizing = false;

    private boolean isResizing = false;

    private double zoom = 1;

}