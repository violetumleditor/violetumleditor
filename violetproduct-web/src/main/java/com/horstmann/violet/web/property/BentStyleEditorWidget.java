package com.horstmann.violet.web.property;

import java.beans.PropertyDescriptor;
import java.util.EnumSet;

import com.horstmann.violet.framework.property.BentStyleChoiceList;
//import com.horstmann.violet.framework.propertyeditor.customeditor.BentStyleEditor;

import eu.webtoolkit.jwt.Side;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WComboBox;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WWidget;

public class BentStyleEditorWidget extends AbstractPropertyEditorWidget<BentStyleChoiceList> {

	private WComboBox comboBoxComponent;

	public BentStyleEditorWidget(Object bean, PropertyDescriptor propertyDescriptor) {
		super(bean, propertyDescriptor);
	}

	@Override
	protected WWidget getCustomEditor() {
		return getComboBoxComponent();
	}

	@Override
	protected void updateCustomEditor() {
		BentStyleChoiceList selectedBentStyle = getValue();
		int newIndex = 0;
//		for (int i = 0; i < BentStyleEditor.VALUES.length; i++) {
//			if (BentStyleEditor.VALUES[i].equals(selectedBentStyle)) {
//				newIndex = i;
//				break;
//			}
//		}
		getComboBoxComponent().setCurrentIndex(newIndex);
	}


	private WComboBox getComboBoxComponent() {
		if (this.comboBoxComponent == null) {
			this.comboBoxComponent = new WComboBox();
			this.comboBoxComponent.setMargin(new WLength(10), EnumSet.of(Side.Right));
//			for (int i = 0; i < BentStyleEditor.NAMES.length; i++) {
//				this.comboBoxComponent.addItem(BentStyleEditor.NAMES[i]);
//			}
//			this.comboBoxComponent.changed().addListener(this, new Signal.Listener() {
//				public void trigger() {
//					int row = getComboBoxComponent().getCurrentIndex();
//					BentStyleChoiceList selectedBentStyle = (BentStyleChoiceList) BentStyleEditor.VALUES[row];
//					setValue(selectedBentStyle);
//				}
//			});
		}
		return this.comboBoxComponent;
	}

}
