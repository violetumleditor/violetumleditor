package com.horstmann.violet.web.property;

import java.beans.PropertyDescriptor;
import java.util.EnumSet;

import com.horstmann.violet.framework.property.ArrowheadChoiceList;
//import com.horstmann.violet.framework.propertyeditor.customeditor.ArrowHeadEditor;

import eu.webtoolkit.jwt.Side;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WComboBox;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WWidget;

public class ArrowHeadEditorWidget extends AbstractPropertyEditorWidget<ArrowheadChoiceList> {

	private WComboBox comboBoxComponent;

	public ArrowHeadEditorWidget(Object bean, PropertyDescriptor propertyDescriptor) {
		super(bean, propertyDescriptor);
	}

	@Override
	protected WWidget getCustomEditor() {
		return getComboBoxComponent();
	}

	@Override
	protected void updateCustomEditor() {
		ArrowheadChoiceList selectedArrowHead = getValue();
		int newIndex = 0;
//		for (int i = 0; i < ArrowHeadEditor.VALUES.length; i++) {
//			if (ArrowHeadEditor.VALUES[i].equals(selectedArrowHead)) {
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
//			for (int i = 0; i < ArrowHeadEditor.NAMES.length; i++) {
//				this.comboBoxComponent.addItem(ArrowHeadEditor.NAMES[i]);
//			}
//			this.comboBoxComponent.changed().addListener(this, new Signal.Listener() {
//				public void trigger() {
//					int row = getComboBoxComponent().getCurrentIndex();
//					ArrowheadChoiceList selectedArrowHead = (ArrowheadChoiceList) ArrowHeadEditor.VALUES[row];
//					setValue(selectedArrowHead);
//				}
//			});
		}
		return this.comboBoxComponent;
	}

}
