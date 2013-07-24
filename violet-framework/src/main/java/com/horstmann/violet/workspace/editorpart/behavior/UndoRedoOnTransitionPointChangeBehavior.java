package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;

import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;

/**
 * Undo/Redo behavior triggered when transition points change on free path edge
 * 
 * @author Alexandre de Pellegrin
 *
 */
public class UndoRedoOnTransitionPointChangeBehavior extends AbstractEditorPartBehavior
{

    /**
     * The global undo/redo behavior which contains all individual undo/redo behaviors
     */
    private UndoRedoCompoundBehavior compoundBehavior;
    
    private List<Point2D> transitionPointsBeforeChanges = new ArrayList<Point2D>();

    private List<Point2D> transitionPointsAfterChanges = new ArrayList<Point2D>();

    /**
     * Default constructor
     * 
     * @param editorPart
     */
    public UndoRedoOnTransitionPointChangeBehavior(UndoRedoCompoundBehavior compoundBehavior)
    {
        this.compoundBehavior = compoundBehavior;
    }

    @Override
    public void beforeChangingTransitionPointsOnEdge(IEdge edge)
    {
        this.transitionPointsBeforeChanges.clear();
        for (Point2D aTransitionPoint : edge.getTransitionPoints()) {
            this.transitionPointsBeforeChanges.add(new Point2D.Double(aTransitionPoint.getX(), aTransitionPoint.getY()));
        }
    }
    
    @Override
    public void afterChangingTransitionPointsOnEdge(final IEdge edge)
    {
        this.transitionPointsAfterChanges.clear();
        for (Point2D aTransitionPoint : edge.getTransitionPoints()) {
            this.transitionPointsAfterChanges.add(new Point2D.Double(aTransitionPoint.getX(), aTransitionPoint.getY()));
        }
        boolean isDragged = (this.transitionPointsBeforeChanges.size() == this.transitionPointsAfterChanges.size());
        boolean isAdded = (this.transitionPointsBeforeChanges.size() != this.transitionPointsAfterChanges.size());
        this.compoundBehavior.startHistoryCapture();
        CompoundEdit capturedEdit = this.compoundBehavior.getCurrentCapturedEdit();
        if (isDragged) {
            UndoableEdit edit = new AbstractUndoableEdit() {
                @Override
                public void undo() throws CannotUndoException
                {
                    for (int i = 0; i < transitionPointsAfterChanges.size(); i++) {
                        Point2D beforeDragPoint = transitionPointsBeforeChanges.get(i);
                        Point2D afterDragPoint = transitionPointsAfterChanges.get(i);
                        if (afterDragPoint.getX() != beforeDragPoint.getX() || afterDragPoint.getY() != beforeDragPoint.getY()) {
                            Point2D[] transitionPoints = edge.getTransitionPoints();
                            transitionPoints[i].setLocation(beforeDragPoint.getX(), beforeDragPoint.getY());
                        }
                    }
                }
                @Override
                public void redo() throws CannotRedoException
                {
                    for (int i = 0; i < transitionPointsAfterChanges.size(); i++) {
                        Point2D beforeDragPoint = transitionPointsBeforeChanges.get(i);
                        Point2D afterDragPoint = transitionPointsAfterChanges.get(i);
                        if (afterDragPoint.getX() != beforeDragPoint.getX() || afterDragPoint.getY() != beforeDragPoint.getY()) {
                            Point2D[] transitionPoints = edge.getTransitionPoints();
                            transitionPoints[i].setLocation(afterDragPoint.getX(), afterDragPoint.getY());
                        }
                    }
                }
            };
            capturedEdit.addEdit(edit);
        }
//        if (isAdded) {
//            UndoableEdit edit = new AbstractUndoableEdit() {
//                @Override
//                public void undo() throws CannotUndoException
//                {
//                }
//                @Override
//                public void redo() throws CannotRedoException
//                {
//                }
//            };
//            capturedEdit.addEdit(edit);
//        }
        this.compoundBehavior.stopHistoryCapture();
    }
    

}
