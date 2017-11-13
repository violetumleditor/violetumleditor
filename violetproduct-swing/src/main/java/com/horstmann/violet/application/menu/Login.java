package com.horstmann.violet.application.menu;
import java.io.*;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login {

	public JFrame frame;
	private JTextField userField;
	private JTextField passwordField;
		
	String username;
	String password;
	
	public JMenuItem pieChartMenuItem;
	public JMenuItem averageMenuItem;

	public Login() 
	{
		createLogin();
	}

	//Create the login window
	private void createLogin() {
		frame = new JFrame();
		frame.setSize(500,500);
		frame.getContentPane().setLayout(null);
		
		JLabel userLabel = new JLabel("UserName:");
		userLabel.setBounds(37, 26, 89, 27);
		frame.getContentPane().add(userLabel);
		
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(39, 75, 65, 21);
		frame.getContentPane().add(passwordLabel);
		
		userField = new JTextField();
		userField.setBounds(99, 29, 86, 20);
		frame.getContentPane().add(userField);
		userField.setColumns(10);
		
		passwordField = new JTextField();
		passwordField.setBounds(99, 75, 86, 20);
		frame.getContentPane().add(passwordField);
		passwordField.setColumns(10);
		
		JButton buttonLogin = new JButton("Login");
		buttonLogin.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				readFile("login/Login.txt");
			}
		});
		buttonLogin.setBounds(81, 124, 89, 23);
		frame.getContentPane().add(buttonLogin);
	}
	
	
	public void readFile(String inFile)
	{
		Scanner scanner;
		
		try 
		{
			scanner = new Scanner(new FileReader(inFile));
			
			while (scanner.hasNextLine()) 
			{
			     username = scanner.nextLine();
			     password = scanner.nextLine();
			}
			
			if(userField.getText().equals(username) && passwordField.getText().equals(password))
			{
				JOptionPane.showMessageDialog(frame, "Logged in", "Login", JOptionPane.DEFAULT_OPTION);
				
				frame.setVisible(false);
				
				pieChartMenuItem.setEnabled(true);
				averageMenuItem.setEnabled(true);
			}
			else
			{
				JOptionPane.showMessageDialog(frame, "Login unsucssesful", "Login", JOptionPane.ERROR_MESSAGE);
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
	    
	}
	
}
