package com.horstmann.violet.workspace.editorpart.behavior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;

import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.ISelectable;
import com.horstmann.violet.product.diagram.abstracts.Id;
import com.horstmann.violet.product.diagram.abstracts.edge.EdgeTransitionPoint;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.edge.ITransitionPoint;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;

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
	
    private IEditorPartSelectionHandler selectionHandler;
  
	private IEdge selectedEdge;
	
	private IGraph graph;

	private ITransitionPoint[] transitionPointsBeforeChanges;

	private ITransitionPoint[] transitionPointsAfterChanges;

	/**
	 * Default constructor
	 * 
	 * @param editorPart
	 */
	public UndoRedoOnTransitionPointChangeBehavior(IEditorPart editorPart, UndoRedoCompoundBehavior compoundBehavior) {
		this.compoundBehavior = compoundBehavior;
		this.selectionHandler = editorPart.getSelectionHandler();
		this.graph = editorPart.getGraph();
	}

	@Override
	public void beforeChangingTransitionPointsOnEdge(IEdge edge) {
		this.selectedEdge = edge;
		this.transitionPointsBeforeChanges = Arrays.stream(edge.getTransitionPoints()).map(p -> EdgeTransitionPoint.fromPoint2D(p.toPoint2D())).toArray(ITransitionPoint[]::new);
	}

	@Override
	public void afterChangingTransitionPointsOnEdge(final IEdge edge) {
		if (!edge.equals(this.selectedEdge)) {
			return;
		}
		this.transitionPointsAfterChanges = Arrays.stream(edge.getTransitionPoints()).map(p -> EdgeTransitionPoint.fromPoint2D(p.toPoint2D())).toArray(ITransitionPoint[]::new);
		this.compoundBehavior.startHistoryCapture();
		CompoundEdit capturedEdit = this.compoundBehavior.getCurrentCapturedEdit();
		captureChanges(this.graph, this.selectedEdge, this.transitionPointsBeforeChanges, this.transitionPointsAfterChanges, capturedEdit);
		this.compoundBehavior.stopHistoryCapture();
	}


	private void captureChanges(final IGraph graph, final IEdge selectedEdge, final ITransitionPoint[] transitionPointsBeforeChanges, final ITransitionPoint[] transitionPointsAfterChanges, CompoundEdit capturedEdit) {
		Id selectedEdgeId = selectedEdge.getId();
		final ITransitionPoint[] transitionPointsBeforeChangesCopy = Arrays.stream(transitionPointsBeforeChanges).map(p -> EdgeTransitionPoint.fromPoint2D(p.toPoint2D())).toArray(ITransitionPoint[]::new);
		final ITransitionPoint[] transitionPointsAfterChangesCopy = Arrays.stream(transitionPointsAfterChanges).map(p -> EdgeTransitionPoint.fromPoint2D(p.toPoint2D())).toArray(ITransitionPoint[]::new);
		
		UndoableEdit edit = new AbstractUndoableEdit() {
			@Override
			public void undo() throws CannotUndoException {
				IEdge edge = graph.findEdge(selectedEdgeId);
				if (edge == null) {
					return;
				}

				edge.setTransitionPoints(transitionPointsBeforeChangesCopy);
				removeAllTransitionPointsFromSelectionHandler(edge);
			}

			@Override
			public void redo() throws CannotRedoException {
				IEdge edge = graph.findEdge(selectedEdgeId);
				if (edge == null) {
					return;
				}
				
				edge.setTransitionPoints(transitionPointsAfterChangesCopy);
				removeAllTransitionPointsFromSelectionHandler(edge);
			}
		};
		capturedEdit.addEdit(edit);
	}

	
	
	private void removeAllTransitionPointsFromSelectionHandler(IEdge edge) {
		List<ISelectable> selectableListToRemove = new ArrayList<>();
		if (edge == null) {
			return;
		}

		for (ISelectable aSelectable : this.selectionHandler.getSelectedElements()) {
			if (ITransitionPoint.class.isInstance(aSelectable)) {
				selectableListToRemove.add(aSelectable);
			}
		}
		for (ISelectable aSelectableToRemove : selectableListToRemove) {
			this.selectionHandler.removeElementFromSelection(aSelectableToRemove);
		}
	}

}
