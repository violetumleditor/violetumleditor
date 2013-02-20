package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.Id;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;
import com.horstmann.violet.workspace.editorpart.IGrid;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;

public class AddNodeBehavior extends AbstractEditorPartBehavior
{

    public AddNodeBehavior(IEditorPart editorPart, IGraphToolsBar graphToolsBar)
    {
        this.editorPart = editorPart;
        this.graph = editorPart.getGraph();
        this.selectionHandler = editorPart.getSelectionHandler();
        this.behaviorManager = editorPart.getBehaviorManager();
        this.graphToolsBar = graphToolsBar;
    }

    @Override
    public void onMouseClicked(MouseEvent event)
    {
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
        IGrid grid = editorPart.getGrid();
        Point2D newNodeLocation = grid.snap(mousePoint);
        INode prototype = (INode) selectedTool.getNodeOrEdge();
        INode newNode = (INode) prototype.clone();
        newNode.setId(new Id());
        
        boolean added = addNodeAtPoint(newNode, newNodeLocation);
        if (added)
        {
            selectionHandler.setSelectedElement(newNode);
        }
    }

    /**
     * Adds a new at a precise location
     * 
     * @param newNode to be added
     * @param location
     * @return true if the node has been added
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

    private IEditorPart editorPart;

    private IGraph graph;

    private IEditorPartSelectionHandler selectionHandler;

    private IEditorPartBehaviorManager behaviorManager;

    private IGraphToolsBar graphToolsBar;
}
