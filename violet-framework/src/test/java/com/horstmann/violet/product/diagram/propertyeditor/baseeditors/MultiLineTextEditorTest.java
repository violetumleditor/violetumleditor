package com.horstmann.violet.product.diagram.propertyeditor.baseeditors;

import com.horstmann.violet.framework.injection.resources.ResourceBundleConstant;
import com.horstmann.violet.product.diagram.property.text.MultiLineText;
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

public class MultiLineTextEditorTest
{
    private static MultiLineTextEditor multiLineTextEditor;
    private static JPanel panel;
    private static JViewport viewport;
    private static JTextArea textArea;
    private static final String EDITOR_START_TEXT = "TEST";

    @BeforeClass
    public static void beforeClass()
    {
        multiLineTextEditor = new MultiLineTextEditor();
        final MultiLineText multiLineText = new MultiLineText();
        multiLineText.setText(EDITOR_START_TEXT);
        multiLineTextEditor.setValue(multiLineText);
        panel = (JPanel) multiLineTextEditor.getCustomEditor();
        final JScrollPane scrollPane = (JScrollPane) panel.getComponents()[0];
        viewport = scrollPane.getViewport();
        textArea = (JTextArea) viewport.getComponents()[0];
    }

    @Before
    public void before()
    {
        textArea.setText(EDITOR_START_TEXT);
    }

    @Test
    public void shouldReturnSourceText()
    {
        //given

        //when
        multiLineTextEditor.setSourceEditor();
        final String sourceText = multiLineTextEditor.getSourceEditor().toEdit();

        //then
        assertEquals(sourceText, EDITOR_START_TEXT);
    }

    @Test
    public void shouldCreateEditorWithSpecifiedColumnsNumber()
    {
        //given
        final int columns = Integer.parseInt(ResourceBundleConstant.TEXT_EDITOR_RESOURCE.getString("columns"));

        //when
        final Component[] components = viewport.getComponents();

        //then
        assertNotNull(panel);
        assertEquals(components.length, 1);
        assertEquals(components[0].getClass(), JTextArea.class);
        assertEquals(((JTextArea) components[0]).getColumns(), columns);
    }

    @Test
    public void shouldCreateEditorWithSpecifiedRowsNumber()
    {
        //given
        final int rows = Integer.parseInt(ResourceBundleConstant.TEXT_EDITOR_RESOURCE.getString("rows"));

        //when
        final Component[] components = viewport.getComponents();

        //then
        assertNotNull(panel);
        assertEquals(components.length, 1);
        assertEquals(components[0].getClass(), JTextArea.class);
        assertEquals(((JTextArea) components[0]).getRows(), rows);
    }

    @Test
    public void shouldCreateEditorWithJScrollPane()
    {
        //given

        //when
        final Component component = panel.getComponents()[0];

        //then
        assertEquals(component.getClass(), JScrollPane.class);
    }

    @Test
    public void shouldSourceTextAndEditorTextBeTheSame()
    {
        //given

        //when
        final String sourceText = multiLineTextEditor.getSourceEditor().toEdit();
        final String editorText = textArea.getText();

        //then
        assertEquals(sourceText, editorText);
    }

    @Test
    public void shouldSupportsCustomEditor()
    {
        //given

        //when
        final boolean supportsCustomEditor = multiLineTextEditor.supportsCustomEditor();

        //then
        assertTrue(supportsCustomEditor);
    }

    @Test
    public void shouldCreateEditorWithUndoKeys()
    {
        //given
        final String undoShortcut = ResourceBundleConstant.TEXT_EDITOR_RESOURCE.getString("shortcut.undo");

        //when
        final KeyStroke[] keyStrokes = textArea.getRegisteredKeyStrokes();
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
        final KeyStroke[] keyStrokes = textArea.getRegisteredKeyStrokes();
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
        final Set<AWTKeyStroke> keyStrokes = textArea.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
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
        final Set<AWTKeyStroke> keyStrokes = textArea.getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
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
        final String editorText = textArea.getText();
        textArea.getDocument().insertString(editorText.length(), insertedText, new SimpleAttributeSet());

        //then
        assertEquals(textArea.getText(), EDITOR_START_TEXT + insertedText);
        assertEquals(multiLineTextEditor.getSourceEditor().toEdit(), EDITOR_START_TEXT + insertedText);
    }

    @Test
    public void shouldUndoText() throws BadLocationException
    {
        //given
        final String insertedText = "abc";
        final String undoShortcut = ResourceBundleConstant.TEXT_EDITOR_RESOURCE.getString("shortcut.undo");

        //when
        final Action undoAction = textArea.getActionMap().get("Undo");
        final String textBeforeInsert = textArea.getText();
        textArea.getDocument().insertString(textBeforeInsert.length(), insertedText, new SimpleAttributeSet());
        final String textAfterInsert = textArea.getText();
        undoAction.actionPerformed(new ActionEvent(textArea, ActionEvent.ACTION_PERFORMED, undoShortcut));

        //then
        assertEquals(textAfterInsert, textBeforeInsert + insertedText);
        assertEquals(textArea.getText(), textBeforeInsert);
    }

    @Test
    public void shouldRedoText() throws BadLocationException
    {
        //given
        final String insertedText = "abc";
        final String undoShortcut = ResourceBundleConstant.TEXT_EDITOR_RESOURCE.getString("shortcut.undo");
        final String redoShortcut = ResourceBundleConstant.TEXT_EDITOR_RESOURCE.getString("shortcut.redo");

        //when
        final Action undoAction = textArea.getActionMap().get("Undo");
        final Action redoAction = textArea.getActionMap().get("Redo");
        final String textBeforeInsert = textArea.getText();
        textArea.getDocument().insertString(textBeforeInsert.length(), insertedText, new SimpleAttributeSet());
        final String textAfterInsert = textArea.getText();
        undoAction.actionPerformed(new ActionEvent(textArea, ActionEvent.ACTION_PERFORMED, undoShortcut));
        redoAction.actionPerformed(new ActionEvent(textArea, ActionEvent.ACTION_PERFORMED, redoShortcut));

        //then
        assertEquals(textAfterInsert, textBeforeInsert + insertedText);
        assertEquals(textArea.getText(), textAfterInsert);
    }

}
