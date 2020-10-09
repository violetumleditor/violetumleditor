package com.horstmann.violet.web.property;

import java.beans.PropertyDescriptor;

import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.MultiLineText;

import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WLabel;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLength.Unit;
import eu.webtoolkit.jwt.WTextArea;
import eu.webtoolkit.jwt.WVBoxLayout;
import eu.webtoolkit.jwt.WWidget;

public class LineTextEditorWidget extends AbstractPropertyEditorWidget<LineText> {

	private WTextArea textAreaComponent;
	private WLabel titleLabel;
	private WContainerWidget editorWidget;
	
	
	public LineTextEditorWidget(Object bean, PropertyDescriptor propertyDescriptor) {
		super(bean, propertyDescriptor);
	}

	private WContainerWidget getEditorWidget() {
		if (this.editorWidget == null) {
			this.editorWidget = new WContainerWidget();
			WVBoxLayout editorLayout = new WVBoxLayout();
			editorLayout.addWidget(getTitleLabel());
			editorLayout.addWidget(getTextAreaComponent());
			editorLayout.setSpacing(20);
			this.editorWidget.setLayout(editorLayout);
			this.editorWidget.setWidth(getTextAreaComponent().getWidth());
			this.editorWidget.setHeight(new WLength(getTitleLabel().getHeight().toPixels() + getTextAreaComponent().getHeight().toPixels(), Unit.Pixel));
		}
		return this.editorWidget;
	}
	
	private WLabel getTitleLabel() {
		if (this.titleLabel == null) {
			this.titleLabel = new WLabel(getPropertyTitle());
		}
		return this.titleLabel;
	}

	private WTextArea getTextAreaComponent() {
		if (this.textAreaComponent == null) {
			this.textAreaComponent = new WTextArea();
			this.textAreaComponent.setWidth(new WLength(300, Unit.Pixel));
			this.textAreaComponent.setHeight(new WLength(100, Unit.Pixel));
			this.textAreaComponent.changed().addListener(this, new Signal.Listener() {
				public void trigger() {
					LineText currentValue = getValue();
					currentValue.setText(getTextAreaComponent().getText());
					setValue(currentValue);
				}
			});
		}
		return this.textAreaComponent;
	}

	@Override
	protected WWidget getCustomEditor() {
		return getEditorWidget();
	}

	@Override
	protected void updateCustomEditor() {
		getTextAreaComponent().setText(super.getValue().toEdit());
	}

}
