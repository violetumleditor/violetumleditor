package com.horstmann.violet.property;

import com.horstmann.violet.product.diagram.abstracts.property.MultiLineString;

import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WCompositeWidget;
import eu.webtoolkit.jwt.WTextArea;

public abstract class MultilineStringEditorWidget extends WCompositeWidget {

	private MultiLineString multiLineString;

	private WTextArea textAreaComponent;

	public MultilineStringEditorWidget(MultiLineString multiLineString) {
		super();
		this.multiLineString = multiLineString;
		setImplementation(getTextAreaComponent());
	}

	public abstract void onValueChanged();

	public MultiLineString getMultiLineString() {
		return this.multiLineString;
	}

	private WTextArea getTextAreaComponent() {
		if (this.textAreaComponent == null) {
			this.textAreaComponent = new WTextArea(this.multiLineString.getText());
			this.textAreaComponent.changed().addListener(this, new Signal.Listener() {
				public void trigger() {
					MultilineStringEditorWidget.this.multiLineString.setText(getTextAreaComponent().getText());
				}
			});
		}
		return this.textAreaComponent;
	}

}
