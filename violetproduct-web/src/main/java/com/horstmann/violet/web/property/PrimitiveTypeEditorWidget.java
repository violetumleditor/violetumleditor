package com.horstmann.violet.web.property;

import java.beans.PropertyDescriptor;
import java.util.function.Function;

import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WLabel;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLength.Unit;
import eu.webtoolkit.jwt.WText;
import eu.webtoolkit.jwt.WVBoxLayout;
import eu.webtoolkit.jwt.WWidget;


public class PrimitiveTypeEditorWidget<T> extends AbstractPropertyEditorWidget<T> {

	private WText textComponent;
	private WLabel titleLabel;
	private WContainerWidget editorWidget;
	private Function<String, T> convertStringToPropertyValue;
	private Function<T, String> convertPropertyValueToString;
	
	
	
	
	public PrimitiveTypeEditorWidget(Object bean, PropertyDescriptor propertyDescriptor, Function<String, T> convertStringToPropertyValue, Function<T, String> convertPropertyValueToString) {
		super(bean, propertyDescriptor);
		this.convertStringToPropertyValue = convertStringToPropertyValue;
		this.convertPropertyValueToString = convertPropertyValueToString;
	}

	private WContainerWidget getEditorWidget() {
		if (this.editorWidget == null) {
			this.editorWidget = new WContainerWidget();
			WVBoxLayout editorLayout = new WVBoxLayout();
			editorLayout.addWidget(getTitleLabel());
			editorLayout.addWidget(getTextComponent());
			this.editorWidget.setLayout(editorLayout);
			this.editorWidget.setWidth(getTextComponent().getWidth());
			this.editorWidget.setHeight(new WLength(getTitleLabel().getHeight().toPixels() + getTextComponent().getHeight().toPixels(), Unit.Pixel));
		}
		return this.editorWidget;
	}
	
	private WLabel getTitleLabel() {
		if (this.titleLabel == null) {
			this.titleLabel = new WLabel(getPropertyTitle());
		}
		return this.titleLabel;
	}

	private WText getTextComponent() {
		if (this.textComponent == null) {
			this.textComponent = new WText();
			this.textComponent.setWidth(new WLength(300, Unit.Pixel));
			this.textComponent.setHeight(new WLength(100, Unit.Pixel));
			this.textComponent.setText(convertPropertyValueToString.apply(getValue()));
			this.textComponent.keyPressed().addListener(this, new Signal.Listener() {
				public void trigger() {
					setValue(convertStringToPropertyValue.apply(getTextComponent().getText().getValue()));
				}
			});
		}
		return this.textComponent;
	}

	@Override
	protected WWidget getCustomEditor() {
		return getEditorWidget();
	}



}
