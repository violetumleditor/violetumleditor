package com.horstmann.violet.property;

import java.util.EnumSet;

import com.horstmann.violet.product.diagram.abstracts.property.BentStyle;

import eu.webtoolkit.jwt.Side;
import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WComboBox;
import eu.webtoolkit.jwt.WCompositeWidget;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WStringListModel;

public abstract class BentStyleEditorWidget extends WCompositeWidget {

	private BentStyle bentStyle;

	private WComboBox comboBoxComponent;

	public BentStyleEditorWidget(BentStyle bentStyle) {
		super();
		this.bentStyle = bentStyle;
		setImplementation(getComboBoxComponent());
	}

	public abstract void onValueChanged();

	public BentStyle getBentStyle() {
		return this.bentStyle;
	}

	private WComboBox getComboBoxComponent() {
		if (this.comboBoxComponent == null) {
			this.comboBoxComponent = new WComboBox();
			this.comboBoxComponent.setMargin(new WLength(10), EnumSet.of(Side.Right));
			final WStringListModel model = new WStringListModel(this.comboBoxComponent);
			model.addString("AUTO");
			model.setData(0, 0, BentStyle.AUTO);
			model.addString("HVH");
			model.setData(1, 0, BentStyle.HVH);
			model.addString("VHV");
			model.setData(2, 0, BentStyle.VHV);
			model.addString("HV");
			model.setData(3, 0, BentStyle.HV);
			model.addString("VH");
			model.setData(4, 0, BentStyle.VH);
			model.addString("FREE");
			model.setData(5, 0, BentStyle.FREE);
			this.comboBoxComponent.setModel(model);
			for (int i = 0; i < model.getRowCount(); i++) {
				BentStyle aBentStyle = (BentStyle) model.getData(i, 0);
				if (aBentStyle.equals(BentStyleEditorWidget.this.bentStyle)) {
					this.comboBoxComponent.setCurrentIndex(i);
					break;
				}
			}
			this.comboBoxComponent.changed().addListener(this, new Signal.Listener() {
				public void trigger() {
					int row = getComboBoxComponent().getCurrentIndex();
					BentStyleEditorWidget.this.bentStyle = (BentStyle) model.getData(model.getIndex(row, 0));
					BentStyleEditorWidget.this.onValueChanged();
				}
			});
		}
		return this.comboBoxComponent;
	}

}
