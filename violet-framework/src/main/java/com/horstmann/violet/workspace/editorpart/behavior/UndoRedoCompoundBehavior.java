package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;

import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.IColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.editorpart.IEditorPart;

/**
 * This behavior for undo/redo actions is composed of sub-behaviors
 * 
 * @author Alexandre de Pellegrin
 *
 */
public class UndoRedoCompoundBehavior extends AbstractEditorPartBehavior
{

    /**
     * The concerned workspace
     */
    private IEditorPart editorPart;

    /**
     * Current composed undoable edit
     */
    private CompoundEdit currentCapturedEdit;

    /**
     * Undo/redo manager
     */
    private UndoManager undoManager = new UndoManager();
    
    /**
     * List of individual undo/redo behaviors
     */
    private List<IEditorPartBehavior> behaviors = new ArrayList<IEditorPartBehavior>();


    /**
     * Default constructor
     * @param editorPart
     */
    public UndoRedoCompoundBehavior(IEditorPart editorPart)
    {
        this.editorPart = editorPart;
        behaviors.add(new UndoRedoOnAddBehavior(editorPart, this));
        behaviors.add(new UndoRedoOnDragBehavior(editorPart, this));
        behaviors.add(new UndoRedoOnEditBehavior(this));
        behaviors.add(new UndoRedoOnRemoveBehavior(editorPart, this));
        behaviors.add(new UndoRedoOnTransitionPointChangeBehavior(this));
        behaviors.add(new UndoRedoOnColorizeBehavior(this));
    }

    
    @Override
    public void onMousePressed(MouseEvent event)
    {
        for (IEditorPartBehavior aBehavior : this.behaviors) {
            aBehavior.onMousePressed(event);
        }
    }



    @Override
    public void onMouseDragged(MouseEvent event)
    {
        for (IEditorPartBehavior aBehavior : this.behaviors) {
            aBehavior.onMouseDragged(event);
        }
    }



    @Override
    public void onMouseReleased(MouseEvent event)
    {
        for (IEditorPartBehavior aBehavior : this.behaviors) {
            aBehavior.onMouseReleased(event);
        }
    }

    
    @Override
    public void beforeRemovingSelectedElements()
    {
        for (IEditorPartBehavior aBehavior : this.behaviors) {
            aBehavior.beforeRemovingSelectedElements();
        }
    }
    
    @Override
    public void afterRemovingSelectedElements() {
        for (IEditorPartBehavior aBehavior : this.behaviors) {
            aBehavior.afterRemovingSelectedElements();
        }
    }
    
    
    @Override
    public void beforeAddingNodeAtPoint(INode node, Point2D location)
    {
        for (IEditorPartBehavior aBehavior : this.behaviors) {
            aBehavior.beforeAddingNodeAtPoint(node, location);
        }
    }

    @Override
    public void afterAddingNodeAtPoint(final INode node, final Point2D location)
    {
        for (IEditorPartBehavior aBehavior : this.behaviors) {
            aBehavior.afterAddingNodeAtPoint(node, location);
        }
    }

    @Override
    public void beforeAddingEdgeAtPoints(IEdge edge, Point2D startPoint, Point2D endPoint)
    {
        for (IEditorPartBehavior aBehavior : this.behaviors) {
            aBehavior.beforeAddingEdgeAtPoints(edge, startPoint, endPoint);
        }
    }
    
    @Override
    public void afterAddingEdgeAtPoints(final IEdge edge, final Point2D startPoint, final Point2D endPoint)
    {
        for (IEditorPartBehavior aBehavior : this.behaviors) {
            aBehavior.afterAddingEdgeAtPoints(edge, startPoint, endPoint);
        }
    }
    
    @Override
    public void beforeChangingTransitionPointsOnEdge(IEdge edge)
    {
        for (IEditorPartBehavior aBehavior : this.behaviors) {
            aBehavior.beforeChangingTransitionPointsOnEdge(edge);
        }
    }
    
    @Override
    public void afterChangingTransitionPointsOnEdge(IEdge edge)
    {
        for (IEditorPartBehavior aBehavior : this.behaviors) {
            aBehavior.afterChangingTransitionPointsOnEdge(edge);
        }
    }
    
    @Override
    public void beforeChangingColorOnElement(IColorableNode element)
    {
        for (IEditorPartBehavior aBehavior : this.behaviors) {
            aBehavior.beforeChangingColorOnElement(element);
        }
    }
    
    @Override
    public void afterChangingColorOnElement(IColorableNode element)
    {
        for (IEditorPartBehavior aBehavior : this.behaviors) {
            aBehavior.afterChangingColorOnElement(element);
        }
    }
    

    @Override
    public void beforeEditingNode(INode node)
    {
        for (IEditorPartBehavior aBehavior : this.behaviors) {
            aBehavior.beforeEditingNode(node);
        }
    }

    @Override
    public void whileEditingNode(INode node, PropertyChangeEvent event)
    {
        for (IEditorPartBehavior aBehavior : this.behaviors) {
            aBehavior.whileEditingNode(node, event);
        }
    }

    @Override
    public void afterEditingNode(INode node)
    {
        for (IEditorPartBehavior aBehavior : this.behaviors) {
            aBehavior.afterEditingNode(node);
        }
    }

    @Override
    public void beforeEditingEdge(IEdge edge)
    {
        for (IEditorPartBehavior aBehavior : this.behaviors) {
            aBehavior.beforeEditingEdge(edge);
        }
    }

    @Override
    public void whileEditingEdge(IEdge edge, final PropertyChangeEvent event)
    {
        for (IEditorPartBehavior aBehavior : this.behaviors) {
            aBehavior.whileEditingEdge(edge, event);
        }
    }

    @Override
    public void afterEditingEdge(IEdge edge)
    {
        for (IEditorPartBehavior aBehavior : this.behaviors) {
            aBehavior.afterEditingEdge(edge);
        }
    }

    
    /**
     * Restores previous graph action from the history cursor location
     */
    public void undo()
    {
        if (undoManager.canUndo())
        {
            undoManager.undo();
            editorPart.getSwingComponent().invalidate();
            editorPart.getSwingComponent().repaint();
        }
    }

    /**
     * Restores next graph action from the history cursor location
     */
    public void redo()
    {
        if (undoManager.canRedo())
        {
            undoManager.redo();
            editorPart.getSwingComponent().invalidate();
            editorPart.getSwingComponent().repaint();
        }
    }

    /**
     * Starts capturing actions on graph
     */
    protected void startHistoryCapture()
    {
        if (this.currentCapturedEdit == null)
        {
        	this.currentCapturedEdit = new CompoundEdit();
        }
    }


    /**
     * @return current composed undoable edit
     */
    protected CompoundEdit getCurrentCapturedEdit()
    {
        return this.currentCapturedEdit;
    }

    /**
     * Stops capturing actions on graph and adds an entry to history
     */
    protected void stopHistoryCapture()
    {
        if (this.currentCapturedEdit == null) return;
        this.currentCapturedEdit.end();
        this.undoManager.addEdit(this.currentCapturedEdit);
        this.currentCapturedEdit = null;
    }
    
    
}
