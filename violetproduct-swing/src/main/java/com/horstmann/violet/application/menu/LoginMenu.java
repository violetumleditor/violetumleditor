package com.horstmann.violet.application.menu;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenu;

import com.horstmann.violet.application.gui.LoginFrame;
import com.horstmann.violet.application.gui.MainFrame;

public class LoginMenu extends JMenu
{
	private JFrame mainFrame;
	
	public LoginMenu(MainFrame mainFrame)
	{
		this.setText("Login");
		this.mainFrame = mainFrame;
		
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				new LoginFrame().setVisible(true);
			}
		});
	}
}
