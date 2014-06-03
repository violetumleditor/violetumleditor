package com.horstmann.violet.property;

import java.beans.PropertyDescriptor;

import com.horstmann.violet.product.diagram.abstracts.property.MultiLineString;

import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WTextArea;
import eu.webtoolkit.jwt.WWidget;

public class MultilineStringEditorWidget extends AbstractPropertyEditorWidget<MultiLineString> {

	private WTextArea textAreaComponent;

	
	public MultilineStringEditorWidget(Object bean, PropertyDescriptor propertyDescriptor) {
		super(bean, propertyDescriptor);
	}


	private WTextArea getTextAreaComponent() {
		if (this.textAreaComponent == null) {
			this.textAreaComponent = new WTextArea();
			this.textAreaComponent.changed().addListener(this, new Signal.Listener() {
				public void trigger() {
					MultiLineString currentValue = getValue();
					currentValue.setText(getTextAreaComponent().getText());
					setValue(currentValue);
				}
			});
		}
		return this.textAreaComponent;
	}

	@Override
	protected WWidget getCustomEditor() {
		return getTextAreaComponent();
	}

	@Override
	protected void updateCustomEditor() {
		getTextAreaComponent().setText(super.getValue().getText());
	}

}
