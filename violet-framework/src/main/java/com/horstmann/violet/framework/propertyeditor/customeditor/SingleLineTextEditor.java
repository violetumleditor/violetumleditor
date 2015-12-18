/**
 * 
 */
package com.horstmann.violet.framework.propertyeditor.customeditor;

import com.horstmann.violet.product.diagram.abstracts.property.string.SingleLineText;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyEditorSupport;
import java.util.HashSet;
import java.util.Set;

public class SingleLineTextEditor extends PropertyEditorSupport
{
    public boolean supportsCustomEditor()
    {
        return true;
    }

    public Component getCustomEditor()
    {
        this.source = (SingleLineText) getValue();
        final JPanel panel = new JPanel();
        panel.add(getTextEditorComponent());
        return panel;
    }

    private JComponent getTextEditorComponent()
    {
        if (this.textEditorComponent == null)
        {
            final JTextField textField = new JTextField(COLUMNS);

            textField.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, tab);
            textField.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, shiftTab);

            textField.setText(source.getText());
            textField.getDocument().addDocumentListener(new DocumentListener()
            {
                public void insertUpdate(DocumentEvent e)
                {
                    source.setText(textField.getText());
                    firePropertyChange();
                }

                public void removeUpdate(DocumentEvent e)
                {
                    source.setText(textField.getText());
                    firePropertyChange();
                }

                public void changedUpdate(DocumentEvent e)
                {
                }
            });
            this.textEditorComponent = textField;
        }
        return this.textEditorComponent;
    }

    private SingleLineText source;
    private JComponent textEditorComponent;

    private static final int COLUMNS = 30;

    private static Set<KeyStroke> tab = new HashSet<KeyStroke>(1);
    private static Set<KeyStroke> shiftTab = new HashSet<KeyStroke>(1);
    static
    {
        tab.add(KeyStroke.getKeyStroke("TAB"));
        shiftTab.add(KeyStroke.getKeyStroke("shift TAB"));
    }
}