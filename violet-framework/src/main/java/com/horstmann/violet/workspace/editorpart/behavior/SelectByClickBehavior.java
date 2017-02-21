package com.horstmann.violet.workspace.editorpart.behavior;

import com.horstmann.violet.framework.util.GrabberUtils;
import com.horstmann.violet.framework.util.KeyModifierUtil;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class SelectByClickBehavior extends AbstractEditorPartBehavior
{

    public SelectByClickBehavior(IEditorPart editorPart, IGraphToolsBar graphToolsBar)
    {
        this.editorPart = editorPart;
        this.graph = editorPart.getGraph();
        this.selectionHandler = editorPart.getSelectionHandler();
        this.behaviorManager = editorPart.getBehaviorManager();
        this.graphToolsBar = graphToolsBar;
    }

    @Override
    public void onMousePressed(MouseEvent event)
    {
        resetEventAttributes();
        
        if (event.getClickCount() > 1) {
            return;
        }
        if (event.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        if (!GraphTool.SELECTION_TOOL.equals(this.graphToolsBar.getSelectedTool())) {
            return;
        }
        double zoom = editorPart.getZoomFactor();
        Point2D mousePoint = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        boolean isCtrl = KeyModifierUtil.isCtrl(event);
        boolean isOnNodeOrEdge = isMouseOnNodeOrEdge(mousePoint);
        if (!isOnNodeOrEdge && !isCtrl)
        {
            resetSelectedElements();
            return;
        }
        if (isOnNodeOrEdge && !isCtrl)
        {
            processSelection(mousePoint, true);
            return;
        }
        if (isOnNodeOrEdge && isCtrl)
        {
            processSelection(mousePoint, false);
            return;
        }
    }

    
    @Override
    public void onMouseDragged(MouseEvent event)
    {
        this.isDragGesture = true;
    }
    
    @Override
    public void onMouseReleased(MouseEvent event)
    {
        if (event.getClickCount() > 1) {
            return;
        }
        if (event.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        if (this.isDragGesture) {
            return;
        }
        if (KeyModifierUtil.isCtrl(event)) {
            processSelectionInConflictWithDraggingEvents(false);
        } else {
            processSelectionInConflictWithDraggingEvents(true);
        }
        this.editorPart.getSwingComponent().invalidate();
    }

    private void resetEventAttributes()
    {
        this.isDragGesture = false;
        this.unprocessedNode = null;
        this.unprocessedEdge = null;
    }

    
    private boolean isMouseOnNodeOrEdge(Point2D mouseLocation)
    {
        INode node = this.graph.findNode(mouseLocation);
        IEdge edge = this.graph.findEdge(mouseLocation);
        if (node == null && edge == null)
        {
            return false;
        }
        return true;
    }

    private void resetSelectedElements()
    {
        this.selectionHandler.clearSelection();
    }
    
    
    /**
     * Here, we add or remove the selected node_old or edge to the global selection. Under the wood, we can't remove anything.
     * This can only made on 'mouse released' to avoid conflict to any dragging event.
     * 
     * @param mouseLocation
     * @param isResetSelectionFirst
     */
    private void processSelection(Point2D mouseLocation, boolean isResetSelectionFirst)
    {
        INode node = this.graph.findNode(mouseLocation);
        IEdge edge = this.graph.findEdge(mouseLocation);
        if (edge != null)
        {
        	if (this.selectionHandler.isElementAlreadySelected(edge))
        	{
        		// This edge will be removed only on mouse button released
        		// to avoid conflicts with dragging events
        		this.unprocessedEdge = edge;
        	}
        	else
        	{
        		if (isResetSelectionFirst) {
        			resetSelectedElements();
        		}
        		this.selectionHandler.addSelectedElement(edge);
        		if (this.selectionHandler.getSelectedEdges().size() == 1) {
        			this.behaviorManager.fireOnEdgeSelected(edge);
        		}
        	}
        	return;
        }
        if (node != null)
        {
            if (this.selectionHandler.isElementAlreadySelected(node))
            {
                // This node_old will be removed only on mouse button released
                // to avoid conflicts with dragging events
                this.unprocessedNode = node;
            }
            else
            {
                if (isResetSelectionFirst) {
                    resetSelectedElements();
                }
                this.selectionHandler.addSelectedElement(node);
                if (this.selectionHandler.getSelectedNodes().size() == 1) {
                	this.behaviorManager.fireOnNodeSelected(node);
                }
            }
            return;
        }
    }
    
    /**
     * We process node or edges on 'mouse released' event because it's not possible to remove node or edges
     * from selection on 'mouse pressed' because it can be part of a dragging intention from the user. So,
     * unprocessed elements are removed from selection only on 'mouse released' if the user is pressing CTRL.
     * They are set as unique selection if the user isn't pressing CTRL.
     * 
     * @param isResetSelectionFirst
     */
    private void processSelectionInConflictWithDraggingEvents(boolean isResetSelectionFirst) {
        if (this.unprocessedNode == null && this.unprocessedEdge == null) {
            return;
        }
        if (isResetSelectionFirst) {
            resetSelectedElements();
            if (this.unprocessedNode != null) {
                this.selectionHandler.addSelectedElement(this.unprocessedNode);
            }
            if (this.unprocessedEdge != null) {
                this.selectionHandler.addSelectedElement(this.unprocessedEdge);
            }
        }
        if (!isResetSelectionFirst) {
            if (this.unprocessedNode != null) {
                this.selectionHandler.removeElementFromSelection(this.unprocessedNode);
            }
            if (this.unprocessedEdge != null) {
                this.selectionHandler.removeElementFromSelection(this.unprocessedEdge);
            }
        }
    }
    
    @Override
    public void onPaint(Graphics2D g2)
    {
        List<INode> nodes = selectionHandler.getSelectedNodes();
        for (INode n : nodes)
        {
            if (graph.getAllNodes().contains(n))
            {
                Point2D nodeLocationOnGraph = n.getLocationOnGraph();
                Rectangle2D nodeBounds = n.getBounds();
                GrabberUtils.drawPurpleGrabber(g2, nodeLocationOnGraph.getX(), nodeLocationOnGraph.getY());
                GrabberUtils.drawPurpleGrabber(g2, nodeLocationOnGraph.getX(), nodeLocationOnGraph.getY() + nodeBounds.getHeight());
                GrabberUtils.drawPurpleGrabber(g2, nodeLocationOnGraph.getX() + nodeBounds.getWidth(), nodeLocationOnGraph.getY());
                GrabberUtils.drawPurpleGrabber(g2, nodeLocationOnGraph.getX() + nodeBounds.getWidth(), nodeLocationOnGraph.getY() + nodeBounds.getHeight());
            }
        }
        List<IEdge> edges = selectionHandler.getSelectedEdges();
        for (IEdge e : edges)
        {
            if (graph.getAllEdges().contains(e))
            {
                Line2D line = e.getConnectionPoints();
                GrabberUtils.drawPurpleGrabber(g2, line.getX1(), line.getY1());
                GrabberUtils.drawPurpleGrabber(g2, line.getX2(), line.getY2());
                for (Point2D aTransitionPoint : e.getTransitionPoints()) {
                    GrabberUtils.drawGrayGrabber(g2, aTransitionPoint.getX(), aTransitionPoint.getY());
                }
            }
        }
    }

    private IEditorPart editorPart;

    private IGraph graph;

    private IEditorPartSelectionHandler selectionHandler;
    
    private IEditorPartBehaviorManager behaviorManager;
    
    private IGraphToolsBar graphToolsBar;
    
    private boolean isDragGesture = false;
    
    private INode unprocessedNode = null; 

    private IEdge unprocessedEdge = null; 

    
}
