package com.horstmann.violet.application.gui;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class LoginFrame extends JFrame {
	public LoginFrame()
	{
		super("Log in");
		
		this.add(new LoginMenuPanel(this));
		this.setSize(500, 500);
	}
}
