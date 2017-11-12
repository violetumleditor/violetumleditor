package com.horstmann.violet.application.gui;

import java.util.Scanner;
import java.io.File;

public class AccountChecker
{	
	public static boolean VerifyAccount(String username, String password) 
	{
		File f = new File("accounts/" + username + ".txt");
		try {
			Scanner fs = new Scanner(f);
			String accPassword = fs.nextLine();
			
			// Compare the password.
			if (accPassword.equals(password)) {
				return true;
			}
		} catch (Exception e) {}
		
		return false;
	}
}
