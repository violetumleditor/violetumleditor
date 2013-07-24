package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;
import com.horstmann.violet.workspace.editorpart.IGrid;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;

public class DragTransitionPointBehavior extends AbstractEditorPartBehavior
{

    public DragTransitionPointBehavior(IEditorPart editorPart, IGraphToolsBar graphToolsBar)
    {
        this.editorPart = editorPart;
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
        if (!isPrerequisitesOK()) 
        {
        	return;
        }
        if (!isSelectedToolOK()) 
        {
        	return;
        }
        double zoom = editorPart.getZoomFactor();
        final Point2D mousePoint = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        isReadyForDragging = true;
        // As transition points are added on dragging action but with the mouse
        // location saved on onMousePressed, we look for transition points from
        // this first mouse location
        firstMousePoint = mousePoint;
        lastMousePoint = mousePoint;
    }

    @Override
    public void onMouseDragged(MouseEvent event)
    {
        if (!isReadyForDragging)
        {
            return;
        }
        if (this.edgeTransitionPointToDrag == null) 
        {
            // As transition points are added on dragging action, we look for transition points from
            // on dragging event too
            this.edgeTransitionPointToDrag = getEdgeTransitionPointToDrag();
        }
        if (this.edgeTransitionPointToDrag == null) 
        {
            return;
        }
        double zoom = editorPart.getZoomFactor();
        Point2D mousePoint = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        double dx = mousePoint.getX() - lastMousePoint.getX();
        double dy = mousePoint.getY() - lastMousePoint.getY();
        IGrid grid = editorPart.getGrid();
        
        // Drag specific transition point on selected edge
        if (this.edgeTransitionPointToDrag != null) {
            double newTransitionPointLocationX = this.edgeTransitionPointToDrag.getX() + dx;
            double newTransitionPointLocationY = this.edgeTransitionPointToDrag.getY() + dy;
            Point2D newTransitionPoint = new Point2D.Double(newTransitionPointLocationX, newTransitionPointLocationY);
            newTransitionPoint = grid.snap(newTransitionPoint);
            this.edgeTransitionPointToDrag.setLocation(newTransitionPoint.getX(), newTransitionPoint.getY());
            // Save mouse location for next dragging sequence
            lastMousePoint = grid.snap(mousePoint);
            return;
        }
    }

    @Override
    public void onMouseReleased(MouseEvent event)
    {
        firstMousePoint = null;
        lastMousePoint = null;
        isReadyForDragging = false;
        edgeTransitionPointToDrag = null;
        editorPart.getSwingComponent().revalidate();
        editorPart.getSwingComponent().repaint();
    }

    private boolean isPrerequisitesOK() {
    	if (this.selectionHandler.getSelectedEdges().size() == 1) {
    		IEdge selectedEdge = this.selectionHandler.getSelectedEdges().get(0);
    		if (selectedEdge.isTransitionPointsSupported()) {
    			return true;
    		}
        }
        return false;
    }
    
    private boolean isSelectedToolOK() {
    	GraphTool selectedTool = this.graphToolsBar.getSelectedTool();
 		if (GraphTool.SELECTION_TOOL.equals(selectedTool))
        {
            return true;
        }
 		IEdge selectedEdge = this.selectionHandler.getSelectedEdges().get(0);
 		if (selectedTool.getNodeOrEdge().getClass().isInstance(selectedEdge))
 		{
 			return true;
 		}
    	return false;
    }
    
    private Point2D getEdgeTransitionPointToDrag() {
        IEdge selectedEdge = this.selectionHandler.getSelectedEdges().get(0);
        if (!selectedEdge.isTransitionPointsSupported()) {
            return null;
        }
        final double MAX_DIST = 5;
        for (Point2D aTransitionPoint : selectedEdge.getTransitionPoints()) {
            if (aTransitionPoint.distance(this.firstMousePoint) <= MAX_DIST) {
                return aTransitionPoint;
            }
        }
        return null;
    }
    
    private Point2D firstMousePoint = null;
    
    private Point2D lastMousePoint = null;

    private IEditorPartSelectionHandler selectionHandler;

    private IEditorPart editorPart;

    private IGraphToolsBar graphToolsBar;

    private boolean isReadyForDragging = false;
    
    private Point2D edgeTransitionPointToDrag = null;
}
