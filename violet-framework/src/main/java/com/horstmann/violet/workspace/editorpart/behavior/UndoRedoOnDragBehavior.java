package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;

import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;

/**
 * Undo/Redo behavior triggered when node and edges are dragged
 * 
 * @author Alexandre de Pellegrin
 *
 */
public class UndoRedoOnDragBehavior extends AbstractEditorPartBehavior
{

    /**
     * The concerned workspace
     */
    private IEditorPart editorPart;

    /**
     * The global undo/redo behavior which contains all individual undo/redo behaviors
     */
    private UndoRedoCompoundBehavior compoundBehavior;

    /**
     * To retreive selected elements
     */
    private IEditorPartSelectionHandler selectionHandler;

    /**
     * Keeps node_old locations before dragging event
     */
    private Map<INode, Point2D> nodesLocationsBeforeDrag = new HashMap<INode, Point2D>();

    /**
     * Used on node_old's drag'n drop
     */
    private boolean isDragInProgress = false;

    /**
     * Default constructor
     * @param editorPart
     * @param compoundBehavior
     */
    public UndoRedoOnDragBehavior(IEditorPart editorPart, UndoRedoCompoundBehavior compoundBehavior)
    {
        this.editorPart = editorPart;
        this.selectionHandler = editorPart.getSelectionHandler();
        this.compoundBehavior = compoundBehavior;
    }

    @Override
    public void onMousePressed(MouseEvent event)
    {
        double zoom = editorPart.getZoomFactor();
        final Point2D mousePoint = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        this.isDragInProgress = false;
        if (isMouseOnNode(mousePoint))
        {
            saveNodesLocationsBeforeDrag();
        }
    }

    @Override
    public void onMouseDragged(MouseEvent event)
    {
        this.isDragInProgress = true;
    }

    @Override
    public void onMouseReleased(MouseEvent event)
    {
        if (!this.isDragInProgress)
        {
            return;
        }
        List<INode> selectedNodes = this.selectionHandler.getSelectedElements().stream().filter(e -> INode.class.isInstance(e)).map(n -> (INode) n).toList();
        List<UndoableEdit> editList = new ArrayList<UndoableEdit>();
        for (final INode aSelectedNode : selectedNodes)
        {
            if (!this.nodesLocationsBeforeDrag.containsKey(aSelectedNode))
            {
                continue;
            }
            Point2D lastNodeLocation = this.nodesLocationsBeforeDrag.get(aSelectedNode);
            Point2D currentNodeLocation = aSelectedNode.getLocation();
            if (currentNodeLocation.equals(lastNodeLocation))
            {
                continue;
            }
            final double dx = currentNodeLocation.getX() - lastNodeLocation.getX();
            final double dy = currentNodeLocation.getY() - lastNodeLocation.getY();
            UndoableEdit edit = new AbstractUndoableEdit()
            {
                @Override
                public void undo() throws CannotUndoException
                {
                    aSelectedNode.translate(-dx, -dy);
                    super.undo();
                }

                @Override
                public void redo() throws CannotRedoException
                {
                    super.redo();
                    aSelectedNode.translate(dx, dy);
                }
            };
            editList.add(edit);
        }
        if (editList.size() > 0)
        {
            this.compoundBehavior.startHistoryCapture();
            CompoundEdit capturedEdit = this.compoundBehavior.getCurrentCapturedEdit();
            for (UndoableEdit edit : editList)
            {
                capturedEdit.addEdit(edit);
            }
            this.compoundBehavior.stopHistoryCapture();
        }
        this.nodesLocationsBeforeDrag.clear();
        this.isDragInProgress = false;
    }

    /**
     * Saves node locations
     */
    private void saveNodesLocationsBeforeDrag()
    {
        this.nodesLocationsBeforeDrag.clear();
        Collection<INode> selectedNodes = this.editorPart.getGraph().getAllNodes();
        for (INode aSelectedNode : selectedNodes)
        {
            Point2D location = aSelectedNode.getLocation();
            this.nodesLocationsBeforeDrag.put(aSelectedNode, location);
        }
    }

    private boolean isMouseOnNode(Point2D mouseLocation)
    {
        IGraph graph = this.editorPart.getGraph();
        INode node = graph.findNode(mouseLocation);
        if (node == null)
        {
            return false;
        }
        return true;
    }

}
