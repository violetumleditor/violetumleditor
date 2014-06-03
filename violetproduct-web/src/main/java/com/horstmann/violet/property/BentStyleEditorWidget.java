package com.horstmann.violet.property;

import java.beans.PropertyDescriptor;
import java.util.EnumSet;

import com.horstmann.violet.product.diagram.abstracts.property.BentStyle;

import eu.webtoolkit.jwt.Side;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WComboBox;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WStringListModel;
import eu.webtoolkit.jwt.WWidget;

public class BentStyleEditorWidget extends AbstractPropertyEditorWidget<BentStyle> {

	private WComboBox comboBoxComponent;
	private WStringListModel model;

	public BentStyleEditorWidget(Object bean, PropertyDescriptor propertyDescriptor) {
		super(bean, propertyDescriptor);
	}

	@Override
	protected WWidget getCustomEditor() {
		return getComboBoxComponent();
	}

	@Override
	protected void updateCustomEditor() {
		BentStyle currentBentStyle = getValue();
		for (int i = 0; i < getModel().getRowCount(); i++) {
			BentStyle aBentStyle = (BentStyle) getModel().getData(i, 0);
			if (aBentStyle.equals(currentBentStyle)) {
				getComboBoxComponent().setCurrentIndex(i);
				break;
			}
		}
	}

	private WStringListModel getModel() {
		if (this.model == null) {
			this.model = new WStringListModel(getComboBoxComponent());
			this.model.addString("AUTO");
			this.model.setData(0, 0, BentStyle.AUTO);
			this.model.addString("HVH");
			this.model.setData(1, 0, BentStyle.HVH);
			this.model.addString("VHV");
			this.model.setData(2, 0, BentStyle.VHV);
			this.model.addString("HV");
			this.model.setData(3, 0, BentStyle.HV);
			this.model.addString("VH");
			this.model.setData(4, 0, BentStyle.VH);
			this.model.addString("FREE");
			this.model.setData(5, 0, BentStyle.FREE);
		}
		return this.model;
	}

	private WComboBox getComboBoxComponent() {
		if (this.comboBoxComponent == null) {
			this.comboBoxComponent = new WComboBox();
			this.comboBoxComponent.setMargin(new WLength(10), EnumSet.of(Side.Right));
			this.comboBoxComponent.setModel(getModel());
			this.comboBoxComponent.changed().addListener(this, new Signal.Listener() {
				public void trigger() {
					int row = getComboBoxComponent().getCurrentIndex();
					BentStyle selectedBentStyle = (BentStyle) model.getData(model.getIndex(row, 0));
					setValue(selectedBentStyle);
				}
			});
		}
		return this.comboBoxComponent;
	}

}
