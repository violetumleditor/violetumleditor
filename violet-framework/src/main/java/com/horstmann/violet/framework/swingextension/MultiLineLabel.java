package com.horstmann.violet.framework.swingextension;

import java.awt.Component;
import java.awt.Graphics;
import java.util.StringTokenizer;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.plaf.ComponentUI;

public class MultiLineLabel extends JComponent {

	public MultiLineLabel() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setUI(new ComponentUI() {
			@Override
			public void paint(Graphics a, JComponent b) {
				for (Component c : b.getComponents()) {
					UnderLinableJLabel l = (UnderLinableJLabel) c;
					l.getUI().paint(a, l);
				}
		    }
		});
	}

	public void setText(String text) {
		this.text = text;
		updateContent();
	}

	public String getText() {
		return this.text;
	}

	public void setHorizontalAlignment(int horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}

	public int getHorizontalAlignment() {
		return horizontalAlignment;
	}

	private void updateContent() {
		this.removeAll();
		StringTokenizer st = new StringTokenizer(this.text, "\n");
		while (st.hasMoreTokens()) {
			UnderLinableJLabel inter = new UnderLinableJLabel();
			inter.setText(st.nextToken());
			inter.setUnderlined(this.isUnderlined);
			inter.setHorizontalAlignment(this.horizontalAlignment);
			inter.setFont(getFont());
			this.add(inter);
		}
	}
	
	
	

	public void setUnderlined(boolean isUnderlined) {
		this.isUnderlined = isUnderlined;
	}

	public boolean isUnderlined() {
		return this.isUnderlined;
	}

	private boolean isUnderlined = false;
	private int horizontalAlignment = JLabel.LEFT;
	private String text = "";

}