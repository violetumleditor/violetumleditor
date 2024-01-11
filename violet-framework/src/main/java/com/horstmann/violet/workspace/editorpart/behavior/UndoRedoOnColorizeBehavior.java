package com.horstmann.violet.workspace.editorpart.behavior;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;

import com.horstmann.violet.product.diagram.abstracts.node.IColorableNode;
import com.horstmann.violet.workspace.sidebar.colortools.ColorChoice;

/**
 * Undo/Redo behavior triggered when node and edges are colorized
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class UndoRedoOnColorizeBehavior extends AbstractEditorPartBehavior
{

    /**
     * The global undo/redo behavior which contains all individual undo/redo behaviors
     */
    private UndoRedoCompoundBehavior compoundBehavior;

    private ColorChoice oldColorChoice;
    private ColorChoice newColorChoice;

    /**
     * Default constructor
     * 
     * @param compoundBehavior
     */
    public UndoRedoOnColorizeBehavior(UndoRedoCompoundBehavior compoundBehavior)
    {
        this.compoundBehavior = compoundBehavior;
    }

    @Override
    public void beforeChangingColorOnElement(IColorableNode element)
    {
        reset();
        this.oldColorChoice = new ColorChoice(element.getBackgroundColor(), element.getBorderColor(), element.getTextColor());
    }

    @Override
    public void afterChangingColorOnElement(final IColorableNode element)
    {
        this.newColorChoice = new ColorChoice(element.getBackgroundColor(), element.getBorderColor(), element.getTextColor());
        this.compoundBehavior.startHistoryCapture();
        CompoundEdit currentCapturedEdit = this.compoundBehavior.getCurrentCapturedEdit();
        currentCapturedEdit.addEdit(new UndoableColorEdit(element, this.oldColorChoice, this.newColorChoice));
        this.compoundBehavior.stopHistoryCapture();
        reset();
    }

    private void reset()
    {
        this.oldColorChoice = null;
        this.newColorChoice = null;
    }

    private class UndoableColorEdit extends AbstractUndoableEdit
    {

        private IColorableNode element;
        private ColorChoice oldColorChoice;
        private ColorChoice newColorChoice;

        public UndoableColorEdit(IColorableNode element, ColorChoice oldColorChoice, ColorChoice newColorChoice)
        {
            this.element = element;
            this.oldColorChoice = oldColorChoice;
            this.newColorChoice = newColorChoice;
        }

        @Override
        public void undo() throws CannotUndoException
        {
            this.element.setBackgroundColor(this.oldColorChoice.getBackgroundColor());
            this.element.setBorderColor(this.oldColorChoice.getBorderColor());
            this.element.setTextColor(this.oldColorChoice.getTextColor());
        }

        @Override
        public void redo() throws CannotRedoException
        {
            this.element.setBackgroundColor(this.newColorChoice.getBackgroundColor());
            this.element.setBorderColor(this.newColorChoice.getBorderColor());
            this.element.setTextColor(this.newColorChoice.getTextColor());
        }

    }

}
