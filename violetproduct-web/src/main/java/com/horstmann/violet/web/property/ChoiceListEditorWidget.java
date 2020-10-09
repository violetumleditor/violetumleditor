package com.horstmann.violet.web.property;

import java.beans.PropertyDescriptor;

import com.horstmann.violet.product.diagram.property.choiceList.ChoiceList;

import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WComboBox;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WLabel;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLength.Unit;
import eu.webtoolkit.jwt.WVBoxLayout;
import eu.webtoolkit.jwt.WWidget;

public class ChoiceListEditorWidget extends AbstractPropertyEditorWidget<ChoiceList<Object, Object>> {

	private WComboBox comboBoxComponent;
	private WLabel titleLabel;
	private WContainerWidget editorWidget;
	
	public ChoiceListEditorWidget(Object bean, PropertyDescriptor propertyDescriptor) {
		super(bean, propertyDescriptor);
	}


	private WContainerWidget getEditorWidget() {
		if (this.editorWidget == null) {
			this.editorWidget = new WContainerWidget();
			WVBoxLayout editorLayout = new WVBoxLayout();
			editorLayout.addWidget(getTitleLabel());
			editorLayout.addWidget(getComboBoxComponent());
			this.editorWidget.setLayout(editorLayout);
			this.editorWidget.setWidth(getComboBoxComponent().getWidth());
			this.editorWidget.setHeight(new WLength(getTitleLabel().getHeight().toPixels() + getComboBoxComponent().getHeight().toPixels(), Unit.Pixel));
		}
		return this.editorWidget;
	}
	
	private WLabel getTitleLabel() {
		if (this.titleLabel == null) {
			this.titleLabel = new WLabel(getPropertyTitle());
		}
		return this.titleLabel;
	}
	
	private WComboBox getComboBoxComponent() {
		if (this.comboBoxComponent == null) {
			this.comboBoxComponent = new WComboBox();
			this.comboBoxComponent.changed().addListener(this, new Signal.Listener() {
				public void trigger() {
					String selectedKey = getComboBoxComponent().getValueText();
					Object selectedValue = null;
					Object[] keys = getValue().getKeys();
					Object[] values = getValue().getValues();
					for (int i = 0; i < keys.length; i++) {
						String key = keys[i].toString();
						if (key.equals(selectedKey)) {
							selectedValue = values[i];
						}
					}
					if (selectedValue != null) {
						getValue().setSelectedValue(selectedValue);
					}
					ChoiceListEditorWidget.this.setValue(getValue());
				}
			});
		}
		return this.comboBoxComponent;
	}
	
	
	@Override
	protected WWidget getCustomEditor() {
		return getEditorWidget();
	}

	@Override
	protected void updateCustomEditor() {
		getComboBoxComponent().clear();
		Object selectedValue = super.getValue().getSelectedValue();
		String selectedKey = null;
		Object[] keys = super.getValue().getKeys();
		Object[] values = super.getValue().getValues();
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i].toString();
			getComboBoxComponent().addItem(key);
			if (values[i].equals(selectedValue)) {
				selectedKey = key;
			}
		}
		if (selectedKey != null) {
			getComboBoxComponent().setValueText(selectedKey);
		}
	}

}
