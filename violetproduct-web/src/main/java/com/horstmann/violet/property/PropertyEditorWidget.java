package com.horstmann.violet.property;

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
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.horstmann.violet.framework.injection.resources.ResourceBundleConstant;
import com.horstmann.violet.framework.propertyeditor.CustomPropertyEditorLayout;
import com.horstmann.violet.framework.util.SerializableEnumeration;

import eu.webtoolkit.jwt.WCompositeWidget;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WVBoxLayout;
import eu.webtoolkit.jwt.WVectorImage;

public class PropertyEditorWidget extends WCompositeWidget {
	
	private Object bean;

	public PropertyEditorWidget(Object bean) {
		super();
		this.bean = bean;
	}
	
	private void populateWidget() {
		
		WContainerWidget container = new WContainerWidget();
	    container.resize(new WLength(150), new WLength(450));
	    WVBoxLayout vbox = new WVBoxLayout();
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
             * getIconWidth(), getIconHeight()); Color oldColor = g.getColor(); g.setColor(Color.BLACK); editor.paintValue(g, r);
             * g.setColor(oldColor); g.translate(-x, -y); } }); } else button.setText(buttonText(text)); // pop up custom editor
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

	
	

}
