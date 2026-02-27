package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
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
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.IResizableNode;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;

/**
 * Undo/Redo behavior triggered when a resizable node is resized via corner handles.
 * Captures the full state before and after each resize gesture so that both
 * Ctrl+Z and Ctrl+Y work correctly, including for cropped nodes (which also have
 * a location and crop-insets component to the resize).
 */
public class UndoRedoOnResizeBehavior extends AbstractEditorPartBehavior
{
    // ------------------------------------------------------------------
    // Value object – snapshot of a node's resize-relevant state
    // ------------------------------------------------------------------

    private static final class NodeResizeState
    {
        final Rectangle2D preferredSize;
        final Point2D     location;
        final CropInsets  cropInsets;   // null when node is not ICroppableNode

        NodeResizeState(IResizableNode node)
        {
            Rectangle2D ps = node.getPreferredSize();
            this.preferredSize = ps != null ? new Rectangle2D.Double(ps.getX(), ps.getY(), ps.getWidth(), ps.getHeight()) : null;
            this.location      = (node instanceof INode) ? (Point2D) ((INode) node).getLocation().clone() : null;
            if (node instanceof ICroppableNode)
            {
                CropInsets ci = ((ICroppableNode) node).getCropInsets();
                this.cropInsets = (ci != null) ? ci.clone() : new CropInsets();
            }
            else
            {
                this.cropInsets = null;
            }
        }

        boolean sameAs(NodeResizeState other)
        {
            if (!rectEquals(this.preferredSize, other.preferredSize)) return false;
            if (!pointEquals(this.location, other.location)) return false;
            if (this.cropInsets == null && other.cropInsets == null) return true;
            if (this.cropInsets == null || other.cropInsets == null) return false;
            return this.cropInsets.getTop()    == other.cropInsets.getTop()
                && this.cropInsets.getLeft()   == other.cropInsets.getLeft()
                && this.cropInsets.getBottom() == other.cropInsets.getBottom()
                && this.cropInsets.getRight()  == other.cropInsets.getRight();
        }

        private static boolean rectEquals(Rectangle2D a, Rectangle2D b)
        {
            if (a == b) return true;
            if (a == null || b == null) return false;
            return a.getX() == b.getX() && a.getY() == b.getY()
                && a.getWidth() == b.getWidth() && a.getHeight() == b.getHeight();
        }

        private static boolean pointEquals(Point2D a, Point2D b)
        {
            if (a == b) return true;
            if (a == null || b == null) return false;
            return a.getX() == b.getX() && a.getY() == b.getY();
        }

        void applyTo(IResizableNode node)
        {
            node.setPreferredSize(preferredSize);
            if (location != null && node instanceof INode)
            {
                ((INode) node).setLocation(location);
            }
            if (cropInsets != null && node instanceof ICroppableNode)
            {
                ((ICroppableNode) node).setCropInsets(cropInsets.clone());
            }
        }
    }

    // ------------------------------------------------------------------
    // Undoable edit
    // ------------------------------------------------------------------

    private static final class UndoableResizeEdit extends AbstractUndoableEdit
    {
        private final IResizableNode  node;
        private final NodeResizeState before;
        private final NodeResizeState after;

        UndoableResizeEdit(IResizableNode node, NodeResizeState before, NodeResizeState after)
        {
            this.node   = node;
            this.before = before;
            this.after  = after;
        }

        @Override
        public void undo() throws CannotUndoException
        {
            before.applyTo(node);
            super.undo();
        }

        @Override
        public void redo() throws CannotRedoException
        {
            after.applyTo(node);
            super.redo();
        }
    }

    // ------------------------------------------------------------------
    // Behavior implementation
    // ------------------------------------------------------------------

    private final IEditorPart                 editorPart;
    private final IEditorPartSelectionHandler selectionHandler;
    private final UndoRedoCompoundBehavior    compoundBehavior;

    /** State of each selected resizable node captured on mouse-press. */
    private Map<IResizableNode, NodeResizeState> stateBeforeResize = new LinkedHashMap<>();
    private boolean resizeInProgress = false;

    public UndoRedoOnResizeBehavior(IEditorPart editorPart, UndoRedoCompoundBehavior compoundBehavior)
    {
        this.editorPart       = editorPart;
        this.selectionHandler = editorPart.getSelectionHandler();
        this.compoundBehavior = compoundBehavior;
    }

    @Override
    public void onMousePressed(MouseEvent event)
    {
        this.stateBeforeResize.clear();
        this.resizeInProgress = false;

        if (!BehaviorUtils.isCursorOnResizePoint(editorPart, event)) return;

        List<ISelectable> selected = selectionHandler.getSelectedElements();
        for (ISelectable s : selected)
        {
            if (s instanceof IResizableNode)
            {
                IResizableNode node = (IResizableNode) s;
                stateBeforeResize.put(node, new NodeResizeState(node));
            }
        }
    }

    @Override
    public void onMouseDragged(MouseEvent event)
    {
        if (!stateBeforeResize.isEmpty())
        {
            resizeInProgress = true;
        }
    }

    @Override
    public void onMouseReleased(MouseEvent event)
    {
        if (!resizeInProgress || stateBeforeResize.isEmpty())
        {
            reset();
            return;
        }

        compoundBehavior.startHistoryCapture();
        CompoundEdit capturedEdit = compoundBehavior.getCurrentCapturedEdit();

        for (Map.Entry<IResizableNode, NodeResizeState> entry : stateBeforeResize.entrySet())
        {
            IResizableNode  node   = entry.getKey();
            NodeResizeState before = entry.getValue();
            NodeResizeState after  = new NodeResizeState(node);

            if (!before.sameAs(after))
            {
                capturedEdit.addEdit(new UndoableResizeEdit(node, before, after));
            }
        }

        compoundBehavior.stopHistoryCapture();
        reset();
    }

    private void reset()
    {
        stateBeforeResize.clear();
        resizeInProgress = false;
    }
}
