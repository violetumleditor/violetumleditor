package com.horstmann.violet.framework.swingextension;

import javax.swing.JLabel;

public class UnderLinableJLabel extends JLabel {

	public UnderLinableJLabel() {
		super();
		setUI(new UnderlinableBasicLabelUI(this));
	}

	public void setUnderlined(boolean isUnderlined) {
		this.isUnderlined = isUnderlined;
	}
	
	public boolean isUnderlined() {
		return this.isUnderlined;
	}

	private boolean isUnderlined;

}
