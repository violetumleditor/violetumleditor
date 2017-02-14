package com.horstmann.violet.product.diagram.propertyeditor.baseeditors;

import com.horstmann.violet.framework.injection.resources.ResourceBundleConstant;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Set;

import static org.junit.Assert.*;

public class SingleLineTextEditorTest
{

    private static SingleLineTextEditor singleLineTextEditor;
    private static JPanel panel;
    private static JTextField textField;
    private static final String EDITOR_START_TEXT = "TEST";

    @BeforeClass
    public static void beforeClass()
    {
        singleLineTextEditor = new SingleLineTextEditor();
        final SingleLineText singleLineText = new SingleLineText();
        singleLineText.setText(EDITOR_START_TEXT);
        singleLineTextEditor.setValue(singleLineText);
        panel = (JPanel) singleLineTextEditor.getCustomEditor();
        textField = (JTextField) panel.getComponents()[0];

    }

    @Before
    public void before()
    {
        textField.setText(EDITOR_START_TEXT);
    }

    @Test
    public void shouldReturnSourceText()
    {
        //given

        //when
        singleLineTextEditor.setSourceEditor();
        final String sourceText = singleLineTextEditor.getSourceEditor().toEdit();

        //then
        assertEquals(sourceText, EDITOR_START_TEXT);
    }

    @Test
    public void shouldCreateEditorWithSpecifiedColumnNumber()
    {
        //given
        final int columns = Integer.parseInt(ResourceBundleConstant.TEXT_EDITOR_RESOURCE.getString("columns"));

        //when
        final Component[] components = panel.getComponents();

        //then
        assertNotNull(panel);
        assertEquals(components.length, 1);
        assertEquals(components[0].getClass(), JTextField.class);
        assertEquals(((JTextField) components[0]).getColumns(), columns);
    }

    @Test
    public void shouldSourceTextAndEditorTextBeTheSame()
    {
        //given

        //when
        final String sourceText = singleLineTextEditor.getSourceEditor().toEdit();
        final String editorText = textField.getText();

        //then
        assertEquals(sourceText, editorText);
    }

    @Test
    public void shouldSupportsCustomEditor()
    {
        //given

        //when
        final boolean supportsCustomEditor = singleLineTextEditor.supportsCustomEditor();

        //then
        assertTrue(supportsCustomEditor);
    }

    @Test
    public void shouldCreateEditorWithUndoKeys()
    {
        //given
        final String undoShortcut = ResourceBundleConstant.TEXT_EDITOR_RESOURCE.getString("shortcut.undo");

        //when
        final KeyStroke[] keyStrokes = textField.getRegisteredKeyStrokes();
        final KeyStroke undoKey = KeyStroke.getKeyStroke(undoShortcut);
        boolean keyExist = false;
        for (final KeyStroke key : keyStrokes)
        {
            if (undoKey.equals(key))
            {
                keyExist = true;
            }
        }

        //then
        assertTrue(keyExist);
    }

    @Test
    public void shouldCreateEditorWithRedoKeys()
    {
        //given
        final String redoShortcut = ResourceBundleConstant.TEXT_EDITOR_RESOURCE.getString("shortcut.redo");

        //when
        final KeyStroke[] keyStrokes = textField.getRegisteredKeyStrokes();
        final KeyStroke redoKey = KeyStroke.getKeyStroke(redoShortcut);
        boolean keyExist = false;
        for (final KeyStroke key : keyStrokes)
        {
            if (redoKey.equals(key))
            {
                keyExist = true;
            }
        }

        //then
        assertTrue(keyExist);
    }

    @Test
    public void shouldCreateEditorWithForwardTraversalKeys()
    {
        //given
        final String forwardTraversalShortcut = ResourceBundleConstant.TEXT_EDITOR_RESOURCE.getString("shortcut.forwardTraversal");

        //when
        final Set<AWTKeyStroke> keyStrokes = textField.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
        final AWTKeyStroke forwardTraversalKey = AWTKeyStroke.getAWTKeyStroke(forwardTraversalShortcut);
        boolean keyExist = false;
        for (final AWTKeyStroke key : keyStrokes)
        {
            if (forwardTraversalKey.equals(key))
            {
                keyExist = true;
            }
        }

        //then
        assertTrue(keyExist);
    }

    @Test
    public void shouldCreateEditorWithBackwardTraversalKeys()
    {
        //given
        final String backwardTraversalShortcut = ResourceBundleConstant.TEXT_EDITOR_RESOURCE
                .getString("shortcut.backwardTraversal");

        //when
        final Set<AWTKeyStroke> keyStrokes = textField.getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
        final AWTKeyStroke backwardTraversalKey = AWTKeyStroke.getAWTKeyStroke(backwardTraversalShortcut);
        boolean keyExist = false;
        for (final AWTKeyStroke key : keyStrokes)
        {
            if (backwardTraversalKey.equals(key))
            {
                keyExist = true;
            }
        }

        //then
        assertTrue(keyExist);
    }

    @Test
    public void shouldChangeOfEditorTextMakeChangeInSourceText() throws BadLocationException
    {
        //given
        final String insertedText = "abc";

        //when
        final String textBeforeInsert = textField.getText();
        textField.getDocument().insertString(textBeforeInsert.length(), insertedText, new SimpleAttributeSet());

        //then
        assertEquals(textField.getText(), EDITOR_START_TEXT + insertedText);
        assertEquals(singleLineTextEditor.getSourceEditor().toEdit(), EDITOR_START_TEXT + insertedText);
    }

    @Test
    public void shouldUndoText() throws BadLocationException
    {
        //given
        final String insertedText = "abc";
        final String undoShortcut = ResourceBundleConstant.TEXT_EDITOR_RESOURCE.getString("shortcut.undo");
        final Action undoAction = textField.getActionMap().get("Undo");

        //when
        final String textBeforeInsert = textField.getText();
        textField.getDocument().insertString(textBeforeInsert.length(), insertedText, new SimpleAttributeSet());
        final String textAfterInsert = textField.getText();
        undoAction.actionPerformed(new ActionEvent(textField, ActionEvent.ACTION_PERFORMED, undoShortcut));

        //then
        assertEquals(textAfterInsert, textBeforeInsert + insertedText);
        assertEquals(textField.getText(), textBeforeInsert);
    }

    @Test
    public void shouldRedoText() throws BadLocationException
    {
        //given
        final String insertedText = "abc";
        final String undoShortcut = ResourceBundleConstant.TEXT_EDITOR_RESOURCE.getString("shortcut.undo");
        final String redoShortcut = ResourceBundleConstant.TEXT_EDITOR_RESOURCE.getString("shortcut.redo");
        final Action undoAction = textField.getActionMap().get("Undo");
        final Action redoAction = textField.getActionMap().get("Redo");

        //when
        final String textBeforeInsert = textField.getText();
        textField.getDocument().insertString(textBeforeInsert.length(), insertedText, new SimpleAttributeSet());
        final String textAfterInsert = textField.getText();
        undoAction.actionPerformed(new ActionEvent(textField, ActionEvent.ACTION_PERFORMED, undoShortcut));
        redoAction.actionPerformed(new ActionEvent(textField, ActionEvent.ACTION_PERFORMED, redoShortcut));

        //then
        assertEquals(textAfterInsert, textBeforeInsert + insertedText);
        assertEquals(textField.getText(), textAfterInsert);
    }

}
