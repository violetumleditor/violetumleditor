package com.horstmann.violet.workspace.editorpart.behavior;

import com.horstmann.violet.framework.util.PropertyUtils;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import java.beans.PropertyChangeEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;


/**
 * Undo/Redo behavior triggered when node and edges are edited
 * 
 * @author Alexandre de Pellegrin
 *
 */
public class UndoRedoOnEditBehavior extends AbstractEditorPartBehavior
{

    /**
     * The global undo/redo behavior which contains all individual undo/redo behaviors
     */
    private UndoRedoCompoundBehavior compoundBehavior;

    /**
     * Default constructor
     * 
     * @param editorPart
     */
    public UndoRedoOnEditBehavior(UndoRedoCompoundBehavior compoundBehavior)
    {
        this.compoundBehavior = compoundBehavior;
    }

    @Override
    public void beforeEditingNode(INode node)
    {
        this.compoundBehavior.startHistoryCapture();
    }

    @Override
    public void whileEditingNode(INode node, PropertyChangeEvent event)
    {
        capturePropertyChanges(event);
    }

    @Override
    public void afterEditingNode(INode node)
    {
        this.compoundBehavior.stopHistoryCapture();
    }

    @Override
    public void beforeEditingEdge(IEdge edge)
    {
        this.compoundBehavior.startHistoryCapture();
    }

    @Override
    public void whileEditingEdge(IEdge edge, final PropertyChangeEvent event)
    {
        capturePropertyChanges(event);
    }

    @Override
    public void afterEditingEdge(IEdge edge)
    {
        this.compoundBehavior.stopHistoryCapture();
    }

    private void capturePropertyChanges(final PropertyChangeEvent event)
    {
        CompoundEdit capturedEdit = this.compoundBehavior.getCurrentCapturedEdit();
        if (capturedEdit == null) return;
        Object newValue = event.getNewValue();
        Object oldValue = event.getOldValue();
        if (oldValue == null && newValue == null) return;
//        boolean isOldValueRecognized = oldValue != null
//                && (String.class.isInstance(oldValue) || MultiLineText.class.isInstance(oldValue));
//        boolean isNewValueRecognized = oldValue != null
//                && (String.class.isInstance(newValue) || MultiLineText.class.isInstance(newValue));
//        if (!isOldValueRecognized && !isNewValueRecognized) return;
        UndoableEdit edit = new AbstractUndoableEdit()
        {
            @Override
            public void undo() throws CannotUndoException
            {
                PropertyChangeEvent invertedEvent = new PropertyChangeEvent(event.getSource(), event.getPropertyName(),
                        event.getNewValue(), event.getOldValue());
                changeNodeOrEdgeProperty(invertedEvent);
            }

            @Override
            public void redo() throws CannotRedoException
            {
                changeNodeOrEdgeProperty(event);
            }

            private void changeNodeOrEdgeProperty(PropertyChangeEvent e)
            {
                PropertyUtils.setProperty(e.getSource(), e.getPropertyName(), e.getNewValue());
            }
        };
        capturedEdit.addEdit(edit);
    }

}
