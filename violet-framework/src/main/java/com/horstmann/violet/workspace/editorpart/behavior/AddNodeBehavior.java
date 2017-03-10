package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;
import com.horstmann.violet.framework.util.KeyModifierUtil;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.IGridSticker;
import com.horstmann.violet.product.diagram.abstracts.Id;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBarMouseListener;

public class AddNodeBehavior extends AbstractEditorPartBehavior implements IGraphToolsBarMouseListener
{
	private static final int OUTSIDE_SCREEN_POSITION = -1000;

    public AddNodeBehavior(IEditorPart editorPart, IGraphToolsBar graphToolsBar)
    {
    	this.editorPart = editorPart;
        this.graph = editorPart.getGraph();
        this.selectionHandler = editorPart.getSelectionHandler();
        this.behaviorManager = editorPart.getBehaviorManager();
        this.graphToolsBar = graphToolsBar;
        
        this.dragging = false;
        this.draggedNode = null;
        graphToolsBar.addMouseListener(this);
    }
    
    @Override
    public void onMouseClicked(MouseEvent event)
    {
    	this.draggedNode = null;
    	
        if (event.getClickCount() > 1)
        {
            return;
        }
        if (event.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        if (GraphTool.SELECTION_TOOL.equals(this.graphToolsBar.getSelectedTool()))
        {
            return;
        }
        GraphTool selectedTool = this.selectionHandler.getSelectedTool();
        if (!INode.class.isInstance(selectedTool.getNodeOrEdge()))
        {
            return;
        }
        double zoom = editorPart.getZoomFactor();
        final Point2D mousePoint = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        IGridSticker gridSticker = graph.getGridSticker();
        Point2D newNodeLocation = gridSticker.snap(mousePoint);
        INode prototype = (INode) selectedTool.getNodeOrEdge();
        INode newNode = (INode) prototype.clone();
        newNode.setId(new Id());
        
        boolean added = addNodeAtPoint(newNode, newNodeLocation);
        if (added)
        {
            selectionHandler.setSelectedElement(newNode);
            
//            if (!KeyModifierUtil.isCtrl(event)) {
//	            selectionHandler.setSelectedTool(GraphTool.SELECTION_TOOL);
//	            graphToolsBar.setSelectedTool(GraphTool.SELECTION_TOOL);
//	            graphToolsBar.getAWTComponent().invalidate();
//            }
            
            editorPart.getSwingComponent().invalidate();
        }
    }

    /**
     * Adds a new at a precise location
     * 
     * @param newNode to be added
     * @param location
     * @return true if the node_old has been added
     */
    public boolean addNodeAtPoint(INode newNode, Point2D location)
    {
        boolean isAdded = false;
        this.behaviorManager.fireBeforeAddingNodeAtPoint(newNode, location);
        try
        {
            if (graph.addNode(newNode, location))
            {
                newNode.incrementRevision();
                isAdded = true;
            }
        }
        finally
        {
            this.behaviorManager.fireAfterAddingNodeAtPoint(newNode, location);
        }
        return isAdded;
    }

	@Override
	public void onMouseToolClicked(GraphTool selectedTool)
	{
		Object obj = selectedTool.getNodeOrEdge();
		if (obj instanceof INode)
		{
			this.dragging = true;
	        INode prototype = (INode) selectedTool.getNodeOrEdge();
	        this.draggedNode = (INode) prototype.clone();
	        this.draggedNode.setId(new Id());
	        
	        
			double zoom = editorPart.getZoomFactor();
	        final Point2D initialLocation = new Point2D.Double(OUTSIDE_SCREEN_POSITION / zoom, OUTSIDE_SCREEN_POSITION / zoom);
	        IGridSticker gridSticker = graph.getGridSticker();
	        Point2D newNodeLocation = gridSticker.snap(initialLocation);
			
	        boolean added = addNodeAtPoint(this.draggedNode, newNodeLocation);
	        if (added)
	        {
	            selectionHandler.setSelectedElement(this.draggedNode);
	            editorPart.getSwingComponent().invalidate();
	        }
		}
	}

	@Override
	public void onMouseToolDragged(MouseEvent event)
	{
		if (this.dragging) {
			MouseEvent outEvent = SwingUtilities.convertMouseEvent((Component)event.getSource(), event, editorPart.getSwingComponent());
			
			moveDraggedNode(outEvent);
		}
	}

	@Override
	public void onMouseToolReleased(MouseEvent event)
	{
		this.dragging = false;
		if (this.draggedNode != null) {
			MouseEvent outEvent = SwingUtilities.convertMouseEvent((Component)event.getSource(), event, editorPart.getSwingComponent());
            
			if (outEvent.getX() < 0 || outEvent.getY() < 0) {
				this.graph.removeNode(this.draggedNode);
				editorPart.getSwingComponent().invalidate();
				editorPart.getSwingComponent().repaint();
			} else {
				moveDraggedNode(outEvent);
			}
			
	        this.draggedNode = null;
		}
	}
	
	private void moveDraggedNode(MouseEvent convertedEvent) {
		double zoom = editorPart.getZoomFactor();
        final Point2D mousePoint = new Point2D.Double(convertedEvent.getX() / zoom, convertedEvent.getY() / zoom);
        IGridSticker gridSticker = graph.getGridSticker();
        Point2D newNodeLocation = gridSticker.snap(mousePoint);
		
        draggedNode.setLocation(newNodeLocation);
        
        selectionHandler.setSelectedElement(this.draggedNode);
        editorPart.getSwingComponent().invalidate();
        editorPart.getSwingComponent().repaint();
	}
	
    private IEditorPart editorPart;

    private IGraph graph;

    private IEditorPartSelectionHandler selectionHandler;

    private IEditorPartBehaviorManager behaviorManager;

    private IGraphToolsBar graphToolsBar;

    private boolean dragging;
    
    private INode draggedNode;
}
