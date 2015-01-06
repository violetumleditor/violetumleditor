package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class UndoRedoOnTransitionPointChangeBehavior extends AbstractEditorPartBehavior {

	/**
	 * The global undo/redo behavior which contains all individual undo/redo
	 * behaviors
	 */
	private UndoRedoCompoundBehavior compoundBehavior;

	private List<Point2D> transitionPointsBeforeChanges = new ArrayList<Point2D>();

	private List<Point2D> transitionPointsAfterChanges = new ArrayList<Point2D>();

	/**
	 * Default constructor
	 * 
	 * @param editorPart
	 */
	public UndoRedoOnTransitionPointChangeBehavior(UndoRedoCompoundBehavior compoundBehavior) {
		this.compoundBehavior = compoundBehavior;
	}

	@Override
	public void beforeChangingTransitionPointsOnEdge(IEdge edge) {
		this.transitionPointsBeforeChanges.clear();
		for (Point2D aTransitionPoint : edge.getTransitionPoints()) {
			this.transitionPointsBeforeChanges.add(new Point2D.Double(aTransitionPoint.getX(), aTransitionPoint.getY()));
		}
	}

	@Override
	public void afterChangingTransitionPointsOnEdge(final IEdge edge) {
		this.transitionPointsAfterChanges.clear();
		for (Point2D aTransitionPoint : edge.getTransitionPoints()) {
			this.transitionPointsAfterChanges.add(new Point2D.Double(aTransitionPoint.getX(), aTransitionPoint.getY()));
		}
		this.compoundBehavior.startHistoryCapture();
		CompoundEdit capturedEdit = this.compoundBehavior.getCurrentCapturedEdit();
		captureAddedPoints(edge, capturedEdit);
		captureDraggedPoints(edge, capturedEdit);
		this.compoundBehavior.stopHistoryCapture();
	}

	private void captureDraggedPoints(final IEdge edge, CompoundEdit capturedEdit) {
		boolean isSameQuantity = (this.transitionPointsBeforeChanges.size() == this.transitionPointsAfterChanges.size());
		boolean isSameLocation = true;
		for (int i = 0; i < this.transitionPointsBeforeChanges.size(); i++) {
            Point2D beforeDragPoint = this.transitionPointsBeforeChanges.get(i);
            Point2D afterDragPoint = this.transitionPointsAfterChanges.get(i);
            isSameLocation = isSameLocation && beforeDragPoint.equals(afterDragPoint);
		}
		boolean isDragged = isSameQuantity && !isSameLocation;
		if (!isDragged) {
			return;
		}
		final List<Point2D> transitionPointsBeforeChangesCopy = new ArrayList<Point2D>(transitionPointsBeforeChanges);
		final List<Point2D> transitionPointsAfterChangesCopy = new ArrayList<Point2D>(transitionPointsAfterChanges);
		UndoableEdit edit = new AbstractUndoableEdit() {
			@Override
			public void undo() throws CannotUndoException {
				for (int i = 0; i < transitionPointsAfterChangesCopy.size(); i++) {
					Point2D beforeDragPoint = transitionPointsBeforeChangesCopy.get(i);
					Point2D afterDragPoint = transitionPointsAfterChangesCopy.get(i);
					if (afterDragPoint.getX() != beforeDragPoint.getX() || afterDragPoint.getY() != beforeDragPoint.getY()) {
						Point2D[] transitionPoints = edge.getTransitionPoints();
						transitionPoints[i].setLocation(beforeDragPoint.getX(), beforeDragPoint.getY());
					}
				}
			}

			@Override
			public void redo() throws CannotRedoException {
				for (int i = 0; i < transitionPointsAfterChangesCopy.size(); i++) {
					Point2D beforeDragPoint = transitionPointsBeforeChangesCopy.get(i);
					Point2D afterDragPoint = transitionPointsAfterChangesCopy.get(i);
					if (afterDragPoint.getX() != beforeDragPoint.getX() || afterDragPoint.getY() != beforeDragPoint.getY()) {
						Point2D[] transitionPoints = edge.getTransitionPoints();
						transitionPoints[i].setLocation(afterDragPoint.getX(), afterDragPoint.getY());
					}
				}
			}
		};
		capturedEdit.addEdit(edit);
	}

	private void captureAddedPoints(final IEdge edge, CompoundEdit capturedEdit) {
		boolean isAdded = (this.transitionPointsBeforeChanges.size() != this.transitionPointsAfterChanges.size());
		if (!isAdded) {
			return;
		}
		final Map<Integer, Point2D> pointsAndPosition = new HashMap<Integer, Point2D>();
		for (int i = 0; i < transitionPointsAfterChanges.size(); i++) {
			Point2D aPoint = transitionPointsAfterChanges.get(i);
			if (!transitionPointsBeforeChanges.contains(aPoint)) {
				pointsAndPosition.put(i, aPoint);
			}
		}
		UndoableEdit edit = new AbstractUndoableEdit() {
			@Override
			public void undo() throws CannotUndoException {
				boolean isOKToRemove = true;
				List<Point2D> transitionPoints = new ArrayList<Point2D>(Arrays.asList(edge.getTransitionPoints()));
				for (Integer i : pointsAndPosition.keySet()) {
					isOKToRemove = isOKToRemove && (transitionPoints.size() >= i);
					if (!isOKToRemove) {
						break;
					}
					isOKToRemove = isOKToRemove && (pointsAndPosition.get(i).equals(transitionPoints.get(i)));
				}
				if (!isOKToRemove) {
					return;
				}
				for (Integer i : pointsAndPosition.keySet()) {
					Point2D pointToRemove = pointsAndPosition.get(i);
					transitionPoints.remove(pointToRemove);
				}
				Point2D[] transitionPointsAsArray = transitionPoints.toArray(new Point2D.Double[transitionPoints.size()]);
				edge.setTransitionPoints(transitionPointsAsArray);
			}

			@Override
			public void redo() throws CannotRedoException {
				List<Point2D> transitionPoints = new ArrayList<Point2D>(Arrays.asList(edge.getTransitionPoints()));
				for (Integer i : pointsAndPosition.keySet()) {
					Point2D pointToAdd = pointsAndPosition.get(i);
					if (transitionPoints.size() >= i) {
						transitionPoints.add(i, pointToAdd);
					} else {
						transitionPoints.add(i, pointToAdd);
					}
				}
				Point2D[] transitionPointsAsArray = transitionPoints.toArray(new Point2D.Double[transitionPoints.size()]);
				edge.setTransitionPoints(transitionPointsAsArray);
			}
		};
		capturedEdit.addEdit(edit);
	}

}
