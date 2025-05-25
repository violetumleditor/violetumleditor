package com.horstmann.violet.web.property;

import java.beans.PropertyDescriptor;
import java.util.EnumSet;

import com.horstmann.violet.framework.propertyeditor.customeditor.LineStyleEditor;
import com.horstmann.violet.product.diagram.abstracts.property.LineStyle;

import eu.webtoolkit.jwt.Side;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WComboBox;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WWidget;

public class LineStyleEditorWidget extends AbstractPropertyEditorWidget<LineStyle> {

	private WComboBox comboBoxComponent;

	public LineStyleEditorWidget(Object bean, PropertyDescriptor propertyDescriptor) {
		super(bean, propertyDescriptor);
		initializeWidget();
	}

	@Override
	protected WWidget getCustomEditor() {
		return getComboBoxComponent();
	}

	private void initializeWidget() {
		LineStyle selectedLineStyle = getValue();
		int newIndex = 0;
		for (int i = 0; i < LineStyleEditor.VALUES.length; i++) {
			if (LineStyleEditor.VALUES[i].equals(selectedLineStyle)) {
				newIndex = i;
				break;
			}
		}
		getComboBoxComponent().setCurrentIndex(newIndex);
	}


	private WComboBox getComboBoxComponent() {
		if (this.comboBoxComponent == null) {
			this.comboBoxComponent = new WComboBox();
			this.comboBoxComponent.setMargin(new WLength(10), EnumSet.of(Side.Right));
			for (int i = 0; i < LineStyleEditor.NAMES.length; i++) {
				this.comboBoxComponent.addItem(LineStyleEditor.NAMES[i]);
			}
			this.comboBoxComponent.changed().addListener(this, new Signal.Listener() {
				public void trigger() {
					int row = getComboBoxComponent().getCurrentIndex();
					LineStyle selectedLineStyle = (LineStyle) LineStyleEditor.VALUES[row];
					setValue(selectedLineStyle);
				}
			});
		}
		return this.comboBoxComponent;
	}

}
