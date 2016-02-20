/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.framework.propertyeditor;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.horstmann.violet.framework.injection.resources.ResourceBundleConstant;
import com.horstmann.violet.framework.property.*;
import com.horstmann.violet.framework.propertyeditor.baseeditors.ChoiceListEditor;
import com.horstmann.violet.framework.propertyeditor.baseeditors.MultiLineTextEditor;
import com.horstmann.violet.framework.propertyeditor.customeditor.*;

import com.horstmann.violet.framework.propertyeditor.baseeditors.SingleLineTextEditor;
import com.horstmann.violet.framework.util.SerializableEnumeration;
import com.horstmann.violet.framework.property.choiceList.ChoiceList;
import com.horstmann.violet.framework.property.string.MultiLineText;
import com.horstmann.violet.framework.property.string.SingleLineText;

import com.horstmann.violet.product.diagram.common.DiagramLink;

/**
 * A component filled with editors for all editable properties of an object.
 */
public class CustomPropertyEditor implements ICustomPropertyEditor
{
    /**
     * Constructs a property sheet that shows the editable properties of a given object.
     * 
     * @param bean the object whose properties are being edited
     */
    public CustomPropertyEditor(Object bean)
    {
        panel = new JPanel();
        try
        {
            Introspector.flushFromCaches(bean.getClass());
            BeanInfo info = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] descriptors = (PropertyDescriptor[]) info.getPropertyDescriptors().clone();
            Arrays.sort(descriptors, new Comparator<PropertyDescriptor>()
            {
                public int compare(PropertyDescriptor d1, PropertyDescriptor d2)
                {
                    Integer p1 = (Integer) d1.getValue("priority");
                    Integer p2 = (Integer) d2.getValue("priority");
                    if (p1 == null && p2 == null) return 0;
                    if (p1 == null) return 1;
                    if (p2 == null) return -1;
                    return p1.intValue() - p2.intValue();
                }
            });

            panel.setLayout(new CustomPropertyEditorLayout());

            ResourceBundle rs = ResourceBundle.getBundle(ResourceBundleConstant.NODE_AND_EDGE_STRINGS, Locale.getDefault());
            for (int i = 0; i < descriptors.length; i++)
            {
                PropertyEditor editor = getEditor(bean, descriptors[i]);
                if (editor != null)
                {
                    // Try to extract title from resource bundle
                    String title = descriptors[i].getName();
                    try
                    {
                        String translatedTitle = rs.getString(title.toLowerCase());
                        if (translatedTitle != null) title = translatedTitle;
                    }
                    catch (MissingResourceException e)
                    {
                        // Nothing to do
                    }

                    // Upper case the first character
                    title = title.substring(0, Math.min(1, title.length())).toUpperCase()
                            + title.substring(Math.min(1, title.length()), title.length());

                    JLabel label = new JLabel(title);
                    label.setFont(label.getFont().deriveFont(Font.PLAIN));
                    panel.add(label);
                    panel.add(getEditorComponent(editor));
                    this.isEditable = true;
                }
            }
        }
        catch (IntrospectionException exception)
        {
            exception.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.display.clipboard.IPropertyEditor#getAWTComponent()
     */
    public JComponent getAWTComponent()
    {
        return panel;
    }

    /**
     * Gets the property editor for a given property, and wires it so that it updates the given object.
     * 
     * @param bean the object whose properties are being edited
     * @param descriptor the descriptor of the property to be edited
     * @return a property editor that edits the property with the given descriptor and updates the given object
     */
    private PropertyEditor getEditor(final Object bean, final PropertyDescriptor descriptor)
    {
        try
        {
            Method getter = descriptor.getReadMethod();
            if (getter == null) return null;
            final Method setter = descriptor.getWriteMethod();
            if (setter == null) return null;
            Class<?> type = descriptor.getPropertyType();
            final PropertyEditor editor;
            Class<?> editorClass = descriptor.getPropertyEditorClass();
            if (editorClass == null && editors.containsKey(type)) editorClass = (Class<?>) editors.get(type);
            if (editorClass != null) editor = (PropertyEditor) editorClass.newInstance();
            else editor = PropertyEditorManager.findEditor(type);
            if (editor == null) return null;

            Object value = getter.invoke(bean);
            editor.setValue(value);

            if (!isKnownImmutable(type))
            {
                try
                {
                    value = value.getClass().getMethod("clone").invoke(value);
                }
                catch (Throwable t)
                {
                    // we tried
                }
            }
            final Object oldValue = value;
            editor.addPropertyChangeListener(new PropertyChangeListener()
            {
                public void propertyChange(PropertyChangeEvent event)
                {
                try
                {
                    Object newValue = editor.getValue();
                    setter.invoke(bean, newValue);
                    firePropertyStateChanged(new PropertyChangeEvent(bean, descriptor.getName(), oldValue, newValue));
                }
                catch (IllegalAccessException exception)
                {
                    exception.printStackTrace();
                }
                catch (InvocationTargetException exception)
                {
                    exception.printStackTrace();
                }
                }
            });
            return editor;
        }
        catch (InstantiationException exception)
        {
            exception.printStackTrace();
            return null;
        }
        catch (IllegalAccessException exception)
        {
            exception.printStackTrace();
            return null;
        }
        catch (InvocationTargetException exception)
        {
            exception.printStackTrace();
            return null;
        }
    }

    private static boolean isKnownImmutable(Class<?> type)
    {
        if (type.isPrimitive()) return true;
        if (knownImmutables.contains(type)) return true;
        if (SerializableEnumeration.class.isAssignableFrom(type)) return true;
        return false;
    }

    /**
     * Wraps a property editor into a component.
     * 
     * @param editor the editor to wrap
     * @return a button (if there is a custom editor), combo box (if the editor has tags), or text field (otherwise)
     */
    private Component getEditorComponent(final PropertyEditor editor)
    {
        String[] tags = editor.getTags();
        String text = editor.getAsText();
        if (editor.supportsCustomEditor())
        {
            return editor.getCustomEditor();
            /*
             * // Make a button that pops up the custom editor final JButton button = new JButton(); // if the editor is paintable,
             * have it paint an icon if (editor.isPaintable()) { button.setIcon(new Icon() { public int getIconWidth() { return
             * WIDTH - 8; } public int getIconHeight() { return HEIGHT - 8; }
             * 
             * public void paintIcon(Component c, Graphics g, int x, int y) { g.translate(x, y); Rectangle r = new Rectangle(0, 0,
             * getIconWidth(), getIconHeight()); Color oldColor = g.getBackgroundColor(); g.setBackgroundColor(Color.BLACK); editor.paintValue(g, r);
             * g.setBackgroundColor(oldColor); g.translate(-x, -y); } }); } else button.setText(buttonText(text)); // pop up custom editor
             * when button is clicked button.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent event)
             * { final Component customEditor = editor.getCustomEditor();
             * 
             * JOptionPane.showMessageDialog(parent, customEditor); // This should really be showInternalMessageDialog, // but then
             * you get awful focus behavior with JDK 5.0 // (i.e. the property sheet retains focus). In // particular, the color
             * dialog never works.
             * 
             * if (editor.isPaintable()) button.repaint(); else button.setText(buttonText(editor.getAsText())); } }); return button;
             */
        }
        else if (tags != null)
        {
            // TODO: 14.02.2016 tlanslation for tags
            // make a combo box that shows all tags
            final JComboBox comboBox = new JComboBox(tags);
            comboBox.setSelectedItem(text);
            comboBox.addItemListener(new ItemListener()
            {
                public void itemStateChanged(ItemEvent event)
                {
                    if (event.getStateChange() == ItemEvent.SELECTED) editor.setAsText((String) comboBox.getSelectedItem());
                }
            });
            return comboBox;
        }
        else
        {
            final JTextField textField = new JTextField(text, 10);
            textField.getDocument().addDocumentListener(new DocumentListener()
            {
                public void insertUpdate(DocumentEvent e)
                {
                    try
                    {
                        editor.setAsText(textField.getText());
                    }
                    catch (IllegalArgumentException exception)
                    {
                    }
                }

                public void removeUpdate(DocumentEvent e)
                {
                    try
                    {
                        editor.setAsText(textField.getText());
                    }
                    catch (IllegalArgumentException exception)
                    {
                    }
                }

                public void changedUpdate(DocumentEvent e)
                {
                }
            });
            return textField;
        }
    }

    /*
     * Formats text for the button that pops up a custom editor.
     * 
     * @param text the property value as text @return the text to put on the button private static String buttonText(String text) {
     * if (text == null || text.equals("")) return " "; if (text.length() > MAX_TEXT_LENGTH) return text.substring(0,
     * MAX_TEXT_LENGTH) + "..."; return text; }
     */

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.horstmann.violet.framework.display.clipboard.IPropertyEditor#addPropertyChangeListener(java.beans.PropertyChangeListener)
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        synchronized (listeners)
        {
            listeners.add(listener);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.horstmann.violet.framework.display.clipboard.IPropertyEditor#removePropertyChangeListener(java.beans.PropertyChangeListener
     * )
     */
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        synchronized (listeners)
        {
            listeners.remove(listener);
        }
    }

    /**
     * Notifies all listeners of a state change.
     * 
     * @param event the event to propagate
     */
    private void firePropertyStateChanged(PropertyChangeEvent event)
    {
        synchronized (listeners)
        {
            for (PropertyChangeListener listener : listeners)
                listener.propertyChange(event);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.display.clipboard.IPropertyEditor#isEditable()
     */
    public boolean isEditable()
    {
        return this.isEditable;
    }

    private ArrayList<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();

    /**
     * Flag indicating that the bean has a minimum of one editable property
     */
    private boolean isEditable = false;

    private JPanel panel;

    private static Map<Class<?>, Class<? extends PropertyEditor>> editors;

    static
    {
        editors = new HashMap<Class<?>, Class<? extends PropertyEditor>>();
//        editors.put(ArrowheadChoiceList.class, ArrowHeadEditor.class);
        editors.put(BentStyle.class, BentStyleEditor.class);
        editors.put(ChoiceList.class, ChoiceListEditor.class);
        editors.put(java.awt.Color.class, ColorEditor.class);
        editors.put(DiagramLink.class, AbstractDiagramLinkEditor.class);
//        editors.put(LineStyleChoiceList.class, LineStyleEditor.class);
        editors.put(StretchStyle.class, StretchStyleEditor.class);
        editors.put(MultiLineText.class, MultiLineTextEditor.class);
        editors.put(SingleLineText.class, SingleLineTextEditor.class);
        editors.put(String.class, SingleLineTextEditor.class);
        editors.put(ImageIcon.class, ImageIconEditor.class);
        editors.put(IntegrationFrameType.class, IntegrationFrameTypeEditor.class);
    }

    private static Set<Class<?>> knownImmutables = new HashSet<Class<?>>();

    static
    {
        knownImmutables.add(String.class);
        knownImmutables.add(Integer.class);
        knownImmutables.add(Boolean.class);
        knownImmutables.add(Double.class);
    }
}
