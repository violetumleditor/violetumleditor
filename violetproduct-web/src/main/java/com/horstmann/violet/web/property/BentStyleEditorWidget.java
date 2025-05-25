package com.horstmann.violet.web.property;

import java.beans.PropertyDescriptor;
import java.util.EnumSet;

import com.horstmann.violet.framework.propertyeditor.customeditor.BentStyleEditor;
import com.horstmann.violet.product.diagram.abstracts.property.BentStyle;

import eu.webtoolkit.jwt.Side;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WComboBox;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WWidget;

public class BentStyleEditorWidget extends AbstractPropertyEditorWidget<BentStyle> {

	private WComboBox comboBoxComponent;

	public BentStyleEditorWidget(Object bean, PropertyDescriptor propertyDescriptor) {
		super(bean, propertyDescriptor);
		initializeWidget();
	}

	@Override
	protected WWidget getCustomEditor() {
		return getComboBoxComponent();
	}

	private void initializeWidget() {
		BentStyle selectedBentStyle = getValue();
		int newIndex = 0;
		for (int i = 0; i < BentStyleEditor.VALUES.length; i++) {
			if (BentStyleEditor.VALUES[i].equals(selectedBentStyle)) {
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
			for (int i = 0; i < BentStyleEditor.NAMES.length; i++) {
				this.comboBoxComponent.addItem(BentStyleEditor.NAMES[i]);
			}
			this.comboBoxComponent.changed().addListener(this, new Signal.Listener() {
				public void trigger() {
					int row = getComboBoxComponent().getCurrentIndex();
					BentStyle selectedBentStyle = (BentStyle) BentStyleEditor.VALUES[row];
					setValue(selectedBentStyle);
				}
			});
		}
		return this.comboBoxComponent;
	}

}
