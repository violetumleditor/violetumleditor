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

import com.horstmann.violet.product.diagram.abstracts.edge.EdgeTransitionPoint;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.edge.ITransitionPoint;

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

	private List<ITransitionPoint> transitionPointsBeforeChanges = new ArrayList<ITransitionPoint>();

	private List<ITransitionPoint> transitionPointsAfterChanges = new ArrayList<ITransitionPoint>();

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
		for (ITransitionPoint aTransitionPoint : edge.getTransitionPoints()) {
			this.transitionPointsBeforeChanges.add(new EdgeTransitionPoint(aTransitionPoint.getX(), aTransitionPoint.getY()));
		}
	}

	@Override
	public void afterChangingTransitionPointsOnEdge(final IEdge edge) {
		this.transitionPointsAfterChanges.clear();
		for (ITransitionPoint aTransitionPoint : edge.getTransitionPoints()) {
			this.transitionPointsAfterChanges.add(new EdgeTransitionPoint(aTransitionPoint.getX(), aTransitionPoint.getY()));
		}
		this.compoundBehavior.startHistoryCapture();
		CompoundEdit capturedEdit = this.compoundBehavior.getCurrentCapturedEdit();
		captureAddedPoints(edge, capturedEdit);
		captureDraggedPoints(edge, capturedEdit);
		this.compoundBehavior.stopHistoryCapture();
	}


	private void captureDraggedPoints(final IEdge edge, CompoundEdit capturedEdit) {
		int beforeSize = this.transitionPointsBeforeChanges.size();
		int afterSize = this.transitionPointsAfterChanges.size();
		boolean isSameQuantity = (beforeSize == afterSize);
		boolean isSameLocation = true;
 
		for (int i = 0; ((i < beforeSize) && (i < afterSize)); i++) {
            Point2D beforeDragPoint = this.transitionPointsBeforeChanges.get(i).toPoint2D();
            Point2D afterDragPoint = this.transitionPointsAfterChanges.get(i).toPoint2D();
            isSameLocation = isSameLocation && beforeDragPoint.equals(afterDragPoint);
		}
		boolean isDragged = isSameQuantity && !isSameLocation;
		if (!isDragged) {
			return;
		}
		final List<ITransitionPoint> transitionPointsBeforeChangesCopy = new ArrayList<ITransitionPoint>(transitionPointsBeforeChanges);
		final List<ITransitionPoint> transitionPointsAfterChangesCopy = new ArrayList<ITransitionPoint>(transitionPointsAfterChanges);
		UndoableEdit edit = new AbstractUndoableEdit() {
			@Override
			public void undo() throws CannotUndoException {
				int beforeCopySize = transitionPointsBeforeChangesCopy.size();
				int afterCopySize = transitionPointsAfterChangesCopy.size();

				for (int i = 0; ((i < beforeCopySize) && (i < afterCopySize)); i++) {
					ITransitionPoint beforeDragPoint = transitionPointsBeforeChangesCopy.get(i);
					ITransitionPoint afterDragPoint = transitionPointsAfterChangesCopy.get(i);
					if (afterDragPoint.getX() != beforeDragPoint.getX() || afterDragPoint.getY() != beforeDragPoint.getY()) {
						ITransitionPoint[] transitionPoints = edge.getTransitionPoints();
						transitionPoints[i].setX(beforeDragPoint.getX());
						transitionPoints[i].setY(beforeDragPoint.getY());
					}
				}
			}

			@Override
			public void redo() throws CannotRedoException {
				int beforeCopySize = transitionPointsBeforeChangesCopy.size();
				int afterCopySize = transitionPointsAfterChangesCopy.size();

				for (int i = 0; ((i < beforeCopySize) && (i < afterCopySize)); i++) {
					ITransitionPoint beforeDragPoint = transitionPointsBeforeChangesCopy.get(i);
					ITransitionPoint afterDragPoint = transitionPointsAfterChangesCopy.get(i);
					if (afterDragPoint.getX() != beforeDragPoint.getX() || afterDragPoint.getY() != beforeDragPoint.getY()) {
						ITransitionPoint[] transitionPoints = edge.getTransitionPoints();
						transitionPoints[i].setX(afterDragPoint.getX());
						transitionPoints[i].setY(afterDragPoint.getY());
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
		final Map<Integer, ITransitionPoint> pointsAndPosition = new HashMap<Integer, ITransitionPoint>();
		for (int i = 0; i < transitionPointsAfterChanges.size(); i++) {
			ITransitionPoint aPoint = transitionPointsAfterChanges.get(i);
			if (!transitionPointsBeforeChanges.contains(aPoint)) {
				pointsAndPosition.put(i, aPoint);
			}
		}
		UndoableEdit edit = new AbstractUndoableEdit() {
			@Override
			public void undo() throws CannotUndoException {
				boolean isOKToRemove = true;
				List<ITransitionPoint> transitionPoints = new ArrayList<ITransitionPoint>(Arrays.asList(edge.getTransitionPoints()));
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
					ITransitionPoint pointToRemove = pointsAndPosition.get(i);
					transitionPoints.remove(pointToRemove);
				}
				ITransitionPoint[] transitionPointsAsArray = transitionPoints.toArray(new ITransitionPoint[transitionPoints.size()]);
				edge.setTransitionPoints(transitionPointsAsArray);
			}

			@Override
			public void redo() throws CannotRedoException {
				List<ITransitionPoint> transitionPoints = new ArrayList<ITransitionPoint>(Arrays.asList(edge.getTransitionPoints()));
				for (Integer i : pointsAndPosition.keySet()) {
					ITransitionPoint pointToAdd = pointsAndPosition.get(i);
					if (transitionPoints.size() >= i) {
						transitionPoints.add(i, pointToAdd);
					} else {
						transitionPoints.add(i, pointToAdd);
					}
				}
				ITransitionPoint[] transitionPointsAsArray = transitionPoints.toArray(new ITransitionPoint[transitionPoints.size()]);
				edge.setTransitionPoints(transitionPointsAsArray);
			}
		};
		capturedEdit.addEdit(edit);
	}

}
