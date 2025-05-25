package com.horstmann.violet.web.property;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.property.BentStyle;
import com.horstmann.violet.product.diagram.abstracts.property.MultiLineString;
import com.horstmann.violet.web.workspace.editorpart.EditorPartWidget;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;

import eu.webtoolkit.jwt.WCompositeWidget;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WVBoxLayout;

public class CompositeEditorWidget extends WCompositeWidget {

	private Object bean;
	private EditorPartWidget editorPartWidget;
	private IEditorPartBehaviorManager behaviorManager;
	private boolean isSomethingToEdit = false;

	public CompositeEditorWidget(Object bean, IEditorPart editorPart, EditorPartWidget editorPartWidget) {
		super();
		this.bean = bean;
		this.editorPartWidget = editorPartWidget;
		this.behaviorManager = editorPart.getBehaviorManager();
		populateWidget();
	}

	public boolean isEditable() {
		return this.isSomethingToEdit;
	}

	private void populateWidget() {

		WContainerWidget container = new WContainerWidget();
		WVBoxLayout vbox = new WVBoxLayout();
		try {
			Introspector.flushFromCaches(bean.getClass());
			BeanInfo info = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] descriptors = (PropertyDescriptor[]) info.getPropertyDescriptors().clone();
			Arrays.sort(descriptors, new Comparator<PropertyDescriptor>() {
				public int compare(PropertyDescriptor d1, PropertyDescriptor d2) {
					Integer p1 = (Integer) d1.getValue("priority");
					Integer p2 = (Integer) d2.getValue("priority");
					if (p1 == null && p2 == null)
						return 0;
					if (p1 == null)
						return 1;
					if (p2 == null)
						return -1;
					return p1.intValue() - p2.intValue();
				}
			});

			for (int i = 0; i < descriptors.length; i++) {
				AbstractPropertyEditorWidget<?> editor = getEditorWidget(bean, descriptors[i]);
				if (editor != null) {
					vbox.addWidget(editor);
					this.isSomethingToEdit = true;
				}
			}
		} catch (IntrospectionException exception) {
			exception.printStackTrace();
		}
		container.setLayout(vbox);
		setImplementation(container);
	}

	private AbstractPropertyEditorWidget<?> getEditorWidget(final Object bean, final PropertyDescriptor descriptor) {
		try {
			Method getter = descriptor.getReadMethod();
			if (getter == null)
				return null;
			final Method setter = descriptor.getWriteMethod();
			if (setter == null)
				return null;
			Class<?> type = descriptor.getPropertyType();
			final AbstractPropertyEditorWidget<?> editorWidget = getEditorWidget(type, bean, descriptor);
			if (editorWidget == null)
				return null;
			Object value = getter.invoke(bean);
			@SuppressWarnings("unchecked")
			AbstractPropertyEditorWidget<Object> uncheckedEditor = (AbstractPropertyEditorWidget<Object>) editorWidget;
			uncheckedEditor.setValue(value);
			editorWidget.addPropertyChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					try {
						Object newValue = editorWidget.getValue();
						setter.invoke(bean, newValue);
						if (bean instanceof INode) {
							behaviorManager.fireWhileEditingNode((INode) bean, evt);
						}
						if (bean instanceof IEdge) {
							behaviorManager.fireWhileEditingEdge((IEdge) bean, evt);
						}
						editorPartWidget.update();
					} catch (IllegalAccessException exception) {
						exception.printStackTrace();
					} catch (InvocationTargetException exception) {
						exception.printStackTrace();
					}
				}
			});
			return editorWidget;
		} catch (IllegalAccessException exception) {
			exception.printStackTrace();
			return null;
		} catch (InvocationTargetException exception) {
			exception.printStackTrace();
			return null;
		}
	}

	private AbstractPropertyEditorWidget<?> getEditorWidget(Class<?> type, Object bean, PropertyDescriptor descriptor) {
		AbstractPropertyEditorWidget<?> editorWidget = null;
		if (String.class.equals(type)) {
			editorWidget = new PrimitiveTypeEditorWidget<String>(bean, descriptor, (String str) -> str, (String value) -> value);
		}
		if (Integer.class.equals(type)) {
			editorWidget = new PrimitiveTypeEditorWidget<Integer>(bean, descriptor, (String str) -> Integer.parseInt(str), (Integer value) -> value.toString());
		}
		if (MultiLineString.class.equals(type)) {
			editorWidget = new MultilineStringEditorWidget(bean, descriptor);
		}
		if (BentStyle.class.equals(type)) {
			editorWidget = new BentStyleEditorWidget(bean, descriptor);
		}
		return editorWidget;
	}

	
}
