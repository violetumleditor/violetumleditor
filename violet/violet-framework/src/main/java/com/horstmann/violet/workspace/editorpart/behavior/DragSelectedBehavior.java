package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;
import com.horstmann.violet.workspace.editorpart.IGrid;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;

public class DragSelectedBehavior extends AbstractEditorPartBehavior
{

    public DragSelectedBehavior(IEditorPart editorPart, IGraphToolsBar graphToolsBar)
    {
        this.editorPart = editorPart;
        this.graph = editorPart.getGraph();
        this.selectionHandler = editorPart.getSelectionHandler();
        this.graphToolsBar = graphToolsBar;
    }

    @Override
    public void onMousePressed(MouseEvent event)
    {
        if (event.getClickCount() > 1)
        {
            return;
        }
        if (event.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        if (!GraphTool.SELECTION_TOOL.equals(this.graphToolsBar.getSelectedTool()))
        {
            return;
        }
        double zoom = editorPart.getZoomFactor();
        final Point2D mousePoint = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        if (isMouseOnNode(mousePoint))
        {
            isReadyForDragging = true;
            lastMousePoint = mousePoint;
        }
    }

    private boolean isMouseOnNode(Point2D mouseLocation)
    {
        INode node = this.graph.findNode(mouseLocation);
        if (node == null)
        {
            return false;
        }
        return true;
    }

    @Override
    public void onMouseDragged(MouseEvent event)
    {
        if (!isReadyForDragging)
        {
            return;
        }
        double zoom = editorPart.getZoomFactor();
        Point2D mousePoint = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        // TODO : behaviorManager.fireOnElementsDragged(selectionHandler.getSelectedNodes(), selectionHandler.getSelectedEdges());
        INode lastNode = selectionHandler.getLastSelectedNode();
        if (lastNode == null) {
            return;
        }
        Rectangle2D bounds = lastNode.getBounds();
        double dx = mousePoint.getX() - lastMousePoint.getX();
        double dy = mousePoint.getY() - lastMousePoint.getY();

        // we don't want to drag nodes into negative coordinates
        // particularly with multiple selection, we might never be
        // able to get them back.
        List<INode> selectedNodes = selectionHandler.getSelectedNodes();
        for (INode n : selectedNodes)
            bounds.add(n.getBounds());
        dx = Math.max(dx, -bounds.getX());
        dy = Math.max(dy, -bounds.getY());

        boolean isAtLeastOneNodeMoved = false;
        IGrid grid = editorPart.getGrid();
        for (INode n : selectedNodes)
        {
            if (selectedNodes.contains(n.getParent())) continue; // parents are responsible for translating their children
            Point2D currentNodeLocation = n.getLocation();
            Point2D futureNodeLocation = new Point2D.Double(currentNodeLocation.getX() + dx, currentNodeLocation.getY() + dy);
            Point2D fixedFutureNodeLocation = grid.snap(futureNodeLocation);
            if (!currentNodeLocation.equals(fixedFutureNodeLocation)) {
                n.setLocation(fixedFutureNodeLocation);
                isAtLeastOneNodeMoved = true;
            }
        }
        if (isAtLeastOneNodeMoved) {
            lastMousePoint = grid.snap(mousePoint);
        }
    }

    @Override
    public void onMouseReleased(MouseEvent event)
    {
        lastMousePoint = null;
        isReadyForDragging = false;
        editorPart.getSwingComponent().revalidate();
        editorPart.getSwingComponent().repaint();
    }

    private IGraph graph;

    private Point2D lastMousePoint = null;

    private IEditorPartSelectionHandler selectionHandler;

    private IEditorPart editorPart;

    private IGraphToolsBar graphToolsBar;

    private boolean isReadyForDragging = false;
}
