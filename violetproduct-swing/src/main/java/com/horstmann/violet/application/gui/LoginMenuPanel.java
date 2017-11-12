package com.horstmann.violet.application.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class LoginMenuPanel extends JPanel implements ActionListener {
	
	JPanel username, password, buttons;
	JButton cmdLogin;
	JTextField txtUsername;
	JPasswordField txtPassword;
	
	JFrame parent;
	
	public LoginMenuPanel(JFrame frame)
	{
		this.parent = frame;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        username = new JPanel();
        username.setLayout(new BoxLayout(username, BoxLayout.LINE_AXIS));
        
        password = new JPanel();
        password.setLayout(new BoxLayout(password, BoxLayout.LINE_AXIS));
        
        buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.LINE_AXIS));
        
        
        cmdLogin = new JButton("Login");
        buttons.add(Box.createRigidArea(new Dimension(118, 30)));
        buttons.add(cmdLogin);
        cmdLogin.addActionListener(this);
        
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        
        JLabel lblUsername = new JLabel("Username   ");
        JLabel lblPassword = new JLabel("Password   ");
        
        
        txtUsername.setMaximumSize(new Dimension(120, 30));
        txtPassword.setMaximumSize(new Dimension(120, 30));
        username.add(lblUsername); 
        username.add(txtUsername);
       
        password.add(lblPassword);
        password.add(txtPassword);
        
        this.add(Box.createRigidArea(new Dimension(30, 30)));
        this.add(username);
        this.add(Box.createRigidArea(new Dimension(30, 10)));
        this.add(password);
        this.add(Box.createRigidArea(new Dimension(30, 10)));
        this.add(buttons);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object b = e.getSource();
		if (b == cmdLogin) {
			String username = txtUsername.getText();
			String password = txtPassword.getText();
			
			if (AccountChecker.VerifyAccount(username, password)) {
				this.parent.getDefaultCloseOperation();
			}
		}
	}
}
