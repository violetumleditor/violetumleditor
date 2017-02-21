package com.horstmann.violet.product.diagram.propertyeditor.baseeditors;

import com.horstmann.violet.framework.injection.resources.ResourceBundleConstant;
import com.horstmann.violet.product.diagram.property.text.LineText;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.beans.PropertyEditorSupport;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * Base text editor with specified behaviors like redo/undo/traversal.
 **/
abstract class LineTextEditor extends PropertyEditorSupport
{

    /**
     * Number of characters per line.
     */
    static final int COLUMNS;

    /**
     * Keyboard shortcut for forward focus traversal.
     */
    private static final String FORWARD_TRAVERSAL_SHORTCUT;

    /**
     * Keyboard shortcut for backward focus traversal.
     */
    private static final String BACKWARD_TRAVERSAL_SHORTCUT;

    /**
     * Keyboard shortcut for text undo.
     */
    private static final String UNDO_SHORTCUT;

    /**
     * Keyboard shortcut for text redo.
     */
    private static final String REDO_SHORTCUT;

    static
    {
        COLUMNS = Integer.parseInt(ResourceBundleConstant.TEXT_EDITOR_RESOURCE.getString("columns"));
        FORWARD_TRAVERSAL_SHORTCUT = ResourceBundleConstant.TEXT_EDITOR_RESOURCE.getString("shortcut.forwardTraversal");
        BACKWARD_TRAVERSAL_SHORTCUT = ResourceBundleConstant.TEXT_EDITOR_RESOURCE.getString("shortcut.backwardTraversal");
        UNDO_SHORTCUT = ResourceBundleConstant.TEXT_EDITOR_RESOURCE.getString("shortcut.undo");
        REDO_SHORTCUT = ResourceBundleConstant.TEXT_EDITOR_RESOURCE.getString("shortcut.redo");
    }

    /**
     * Text component used to edit text.
     */
    private volatile JComponent textEditorComponent;

    @Override
    public boolean supportsCustomEditor()
    {
        return true;
    }

    @Override
    public Component getCustomEditor()
    {
        setSourceEditor();
        final JPanel panel = new JPanel();
        panel.add(getTextEditorComponent());
        return panel;
    }

    /**
     * Creates scroll panel with editor text component.
     *
     * @param textComponent text editor component
     * @return editor text component with scroll panel
     */
    protected JComponent createScrollPanel(final JTextComponent textComponent)
    {
        return textComponent;
    }

    /**
     * Sets source of text which is edited.
     */
    protected abstract void setSourceEditor();

    /**
     * Gets source of text which is edited.
     */
    protected abstract LineText getSourceEditor();

    /**
     * Creates text component used to edit text.
     *
     * @return text editor component.
     */
    protected abstract JTextComponent createTextComponent();

    private JComponent getTextEditorComponent()
    {
        if (textEditorComponent == null)
        {
            synchronized (this)
            {
                if (textEditorComponent == null)
                {
                    textEditorComponent = initializeTextEditorComponent();
                }
            }
        }
        return textEditorComponent;
    }

    /**
     * Creates editor with behaviors like undo/redo/traversal.
     *
     * @return text editor component.
     */
    private JComponent initializeTextEditorComponent()
    {
        final JTextComponent textComponent = createTextComponent();
        addTraversalBehavior(textComponent);
        addUndoRedoBehavior(textComponent);
        addDocumentListener(textComponent);
        initializeEditorText(textComponent);
        return createScrollPanel(textComponent);
    }

    private void addTraversalBehavior(final JTextComponent textComponent)
    {
        addForwardTraversalBehavior(textComponent);
        addBackwardTraversalBehavior(textComponent);
    }

    private void addForwardTraversalBehavior(final JTextComponent textComponent)
    {
        final KeyStroke forwardTraversal = KeyStroke.getKeyStroke(FORWARD_TRAVERSAL_SHORTCUT);
        final Set<KeyStroke> forwardTraversalKeyStrokes = new HashSet<KeyStroke>();
        forwardTraversalKeyStrokes.add(forwardTraversal);
        textComponent.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardTraversalKeyStrokes);
    }

    private void addBackwardTraversalBehavior(final JTextComponent textComponent)
    {
        final KeyStroke backwardTraversal = KeyStroke.getKeyStroke(BACKWARD_TRAVERSAL_SHORTCUT);
        final Set<KeyStroke> backwardTraversalKeyStrokes = new HashSet<KeyStroke>();
        backwardTraversalKeyStrokes.add(backwardTraversal);
        textComponent.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardTraversalKeyStrokes);
    }

    /**
     * Adds behavior to undo/redo text when specified keys are pressed.
     *
     * @param textComponent text editor component.
     */
    private void addUndoRedoBehavior(final JTextComponent textComponent)
    {
        final UndoableTextBehaviorManager undoableTextBehaviorManager = new UndoableTextBehaviorManager();
        undoableTextBehaviorManager.addUndoRedoBehavior(textComponent);
    }

    /**
     * Adds listener for insert/remove characters in editor.
     *
     * @param textComponent text editor component.
     */
    private void addDocumentListener(final JTextComponent textComponent)
    {
        final EditorTextListener editorTextListener = new EditorTextListener(textComponent);
        textComponent.getDocument().addDocumentListener(editorTextListener);
    }

    /**
     * Initialize editor text with source text.
     *
     * @param textComponent text editor component.
     */
    private void initializeEditorText(final JTextComponent textComponent)
    {
        final String textToEdit = getSourceEditor().toEdit();
        textComponent.setText(textToEdit);
    }

    /**
     * Defines action for insert/remove text in editor.
     */
    private class EditorTextListener implements DocumentListener
    {

        private JTextComponent textComponent;

        EditorTextListener(final JTextComponent textComponent)
        {
            this.textComponent = textComponent;
        }

        /**
         * Updates source text when insert happen.
         *
         * @param e the document event
         */
        @Override
        public void insertUpdate(final DocumentEvent e)
        {
            updateSourceText(textComponent);
            firePropertyChange();
        }

        /**
         * Updates source text when remove happen.
         *
         * @param e the document event
         */
        @Override
        public void removeUpdate(final DocumentEvent e)
        {
            updateSourceText(textComponent);
            firePropertyChange();
        }

        @Override
        public void changedUpdate(final DocumentEvent e)
        {
            throw new UnsupportedOperationException();
        }

        private void updateSourceText(final JTextComponent textComponent)
        {
            final String actualText = textComponent.getText();
            getSourceEditor().setText(actualText);
        }

    }

    /**
     * Adds undo / redo behavior for text component.
     */
    private class UndoableTextBehaviorManager
    {

        /**
         * Contains and mange with operation history.
         */
        private UndoManager undoManager;

        /**
         * Action performed when text undo.
         */
        private UndoAction undoAction;

        /**
         * Action performed when text redo.
         */
        private RedoAction redoAction;

        UndoableTextBehaviorManager()
        {
            this.undoManager = new UndoManager();
        }

        private void addUndoRedoBehavior(final JTextComponent textComponent)
        {
            addUndoableEditListener(textComponent);
            addUndoBehavior(textComponent);
            addRedoBehavior(textComponent);
        }

        private void addUndoableEditListener(final JTextComponent textComponent)
        {
            final UndoHandler undoHandler = new UndoHandler();
            final Document document = textComponent.getDocument();
            document.addUndoableEditListener(undoHandler);
        }

        private void addUndoBehavior(final JTextComponent textComponent)
        {
            final String undoActionKey = "Undo";
            undoAction = new UndoAction();
            final KeyStroke undoKeyStroke = KeyStroke.getKeyStroke(UNDO_SHORTCUT);
            textComponent.getInputMap().put(undoKeyStroke, undoActionKey);
            textComponent.getActionMap().put(undoActionKey, undoAction);
        }

        private void addRedoBehavior(final JTextComponent textComponent)
        {
            final String redoActionKey = "Redo";
            redoAction = new RedoAction();
            final KeyStroke redoKeyStroke = KeyStroke.getKeyStroke(REDO_SHORTCUT);
            textComponent.getInputMap().put(redoKeyStroke, redoActionKey);
            textComponent.getActionMap().put(redoActionKey, redoAction);
        }

        /**
         * Updates status of undo/redo action
         */
        private void updateActions()
        {
            undoAction.update();
            redoAction.update();
        }

        /**
         * Handles undoable operations.
         */
        private class UndoHandler implements UndoableEditListener
        {

            /**
             * @see UndoableEditListener#undoableEditHappened(UndoableEditEvent)
             */
            @Override
            public void undoableEditHappened(final UndoableEditEvent undoableEditEvent)
            {
                final UndoableEdit undoableEdit = undoableEditEvent.getEdit();
                undoManager.addEdit(undoableEdit);
                updateActions();
            }
        }

        /**
         * Action performed when text undo.
         */
        private class UndoAction extends AbstractAction
        {
            UndoAction()
            {
                setEnabled(false);
            }

            /**
             * Handles undo action.
             *
             * @param e undo event
             */
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                undoManager.undo();
                updateActions();
            }

            /**
             * Updates undo action enabled status.
             */
            void update()
            {
                final boolean canUndo = undoManager.canUndo();
                setEnabled(canUndo);
            }
        }

        /**
         * Action performed when text redo.
         */
        private class RedoAction extends AbstractAction
        {
            RedoAction()
            {
                setEnabled(false);
            }

            /**
             * Handles redo action.
             *
             * @param e redo event
             */
            @Override
            public void actionPerformed(final ActionEvent e)
            {
                undoManager.redo();
                updateActions();
            }

            /**
             * Updates redo action enabled status.
             */
            void update()
            {
                final boolean canRedo = undoManager.canRedo();
                setEnabled(canRedo);
            }
        }
    }
}