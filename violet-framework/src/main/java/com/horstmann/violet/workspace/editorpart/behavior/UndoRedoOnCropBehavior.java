package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;

import com.horstmann.violet.product.diagram.abstracts.ISelectable;
import com.horstmann.violet.product.diagram.abstracts.node.CropInsets;
import com.horstmann.violet.product.diagram.abstracts.node.ICroppableNode;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;

/**
 * Undo/Redo behavior triggered when a croppable node is cropped via edge handles.
 * Snapshots the {@link CropInsets} before and after each crop gesture so that
 * both Ctrl+Z and Ctrl+Y work correctly.
 */
public class UndoRedoOnCropBehavior extends AbstractEditorPartBehavior
{
    // ------------------------------------------------------------------
    // Undoable edit
    // ------------------------------------------------------------------

    private static final class UndoableCropEdit extends AbstractUndoableEdit
    {
        private final ICroppableNode node;
        private final CropInsets     before;
        private final CropInsets     after;

        UndoableCropEdit(ICroppableNode node, CropInsets before, CropInsets after)
        {
            this.node   = node;
            this.before = before;
            this.after  = after;
        }

        @Override
        public void undo() throws CannotUndoException
        {
            node.setCropInsets(before.clone());
            super.undo();
        }

        @Override
        public void redo() throws CannotRedoException
        {
            node.setCropInsets(after.clone());
            super.redo();
        }
    }

    // ------------------------------------------------------------------
    // Behavior implementation
    // ------------------------------------------------------------------

    private final IEditorPart                 editorPart;
    private final IEditorPartSelectionHandler selectionHandler;
    private final UndoRedoCompoundBehavior    compoundBehavior;

    /** Crop insets of each selected croppable node captured on mouse-press. */
    private final Map<ICroppableNode, CropInsets> insetsBeforeCrop = new LinkedHashMap<>();
    private boolean cropInProgress = false;

    public UndoRedoOnCropBehavior(IEditorPart editorPart, UndoRedoCompoundBehavior compoundBehavior)
    {
        this.editorPart       = editorPart;
        this.selectionHandler = editorPart.getSelectionHandler();
        this.compoundBehavior = compoundBehavior;
    }

    @Override
    public void onMousePressed(MouseEvent event)
    {
        insetsBeforeCrop.clear();
        cropInProgress = false;

        if (!BehaviorUtils.isCursorOnCropPoint(editorPart, event)) return;

        List<ISelectable> selected = selectionHandler.getSelectedElements();
        for (ISelectable s : selected)
        {
            if (s instanceof ICroppableNode)
            {
                ICroppableNode node = (ICroppableNode) s;
                CropInsets ci = node.getCropInsets();
                insetsBeforeCrop.put(node, ci != null ? ci.clone() : new CropInsets());
            }
        }
    }

    @Override
    public void onMouseDragged(MouseEvent event)
    {
        if (!insetsBeforeCrop.isEmpty())
        {
            cropInProgress = true;
        }
    }

    @Override
    public void onMouseReleased(MouseEvent event)
    {
        if (!cropInProgress || insetsBeforeCrop.isEmpty())
        {
            reset();
            return;
        }

        compoundBehavior.startHistoryCapture();
        CompoundEdit capturedEdit = compoundBehavior.getCurrentCapturedEdit();

        for (Map.Entry<ICroppableNode, CropInsets> entry : insetsBeforeCrop.entrySet())
        {
            ICroppableNode node   = entry.getKey();
            CropInsets     before = entry.getValue();
            CropInsets     after  = node.getCropInsets();
            if (after == null) after = new CropInsets();

            if (!sameInsets(before, after))
            {
                capturedEdit.addEdit(new UndoableCropEdit(node, before, after.clone()));
            }
        }

        compoundBehavior.stopHistoryCapture();
        reset();
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private static boolean sameInsets(CropInsets a, CropInsets b)
    {
        return a.getTop()    == b.getTop()
            && a.getLeft()   == b.getLeft()
            && a.getBottom() == b.getBottom()
            && a.getRight()  == b.getRight();
    }

    private void reset()
    {
        insetsBeforeCrop.clear();
        cropInProgress = false;
    }
}
