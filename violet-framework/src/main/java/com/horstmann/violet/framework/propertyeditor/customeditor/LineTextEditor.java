/**
 * 
 */
package com.horstmann.violet.framework.propertyeditor.customeditor;

import com.horstmann.violet.product.diagram.abstracts.property.string.LineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.SingleLineText;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.beans.PropertyEditorSupport;
import java.util.HashSet;
import java.util.Set;

public abstract class LineTextEditor extends PropertyEditorSupport
{
    protected abstract void setSourceEditor();
    protected abstract LineText getSourceEditor();
    protected abstract JTextComponent createTextComponent();

    public boolean supportsCustomEditor()
    {
        return true;
    }

    public Component getCustomEditor()
    {
        setSourceEditor();
        final JPanel panel = new JPanel();
        panel.add(getTextEditorComponent());
        return panel;
    }

    private JComponent getTextEditorComponent()
    {
        if (this.textEditorComponent == null)
        {
            final JTextComponent textComponent = createTextComponent();

            textComponent.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, tab);
            textComponent.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, shiftTab);

            textComponent.setText(getSourceEditor().toEdit());
            textComponent.getDocument().addDocumentListener(new DocumentListener()
            {
                public void insertUpdate(DocumentEvent e)
                {
                    getSourceEditor().setText(textComponent.getText());
                    firePropertyChange();
                }

                public void removeUpdate(DocumentEvent e)
                {
                    getSourceEditor().setText(textComponent.getText());
                    firePropertyChange();
                }

                public void changedUpdate(DocumentEvent e)
                {
                }
            });
            this.textEditorComponent = textComponent;
        }
        return this.textEditorComponent;
    }

    protected JComponent textEditorComponent;

    protected static final int COLUMNS = 30;

    protected static Set<KeyStroke> tab = new HashSet<KeyStroke>(1);
    protected static Set<KeyStroke> shiftTab = new HashSet<KeyStroke>(1);
    static
    {
        tab.add(KeyStroke.getKeyStroke("TAB"));
        shiftTab.add(KeyStroke.getKeyStroke("shift TAB"));
    }
}