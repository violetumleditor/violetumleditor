package com.horstmann.violet.framework.swingextension;

import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicLabelUI;

public class UnderlinableBasicLabelUI extends BasicLabelUI {
	
	public UnderlinableBasicLabelUI(UnderLinableJLabel label) {
		super();
		this.label = label;
	}


	@Override
	protected void paintEnabledText(JLabel l, Graphics g, String s, int textX, int textY) {
		super.paintEnabledText(l, g, s, textX, textY);
		if (this.label.isUnderlined()) {
			textY = textY + l.getFontMetrics(l.getFont()).getDescent() + 1;
			g.drawLine(textX, textY, textX + l.getFontMetrics(l.getFont()).stringWidth(l.getText()), textY);
		}
	}
	
	private UnderLinableJLabel label;
}
