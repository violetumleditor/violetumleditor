package com.horstmann.violet.framework.util;

import java.util.List;

import javax.swing.JOptionPane;

import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.dialog.DialogFactoryMode;

public class Warnings {
	public static void showWarning(List<String> messages) {
		
		if (messages.size() == 0) {
    		messages.add("Diagram does not contain any violations.");
    	}
		
		// Put Messages in a String
    	String multiLineMessage = "The graph violates the following constraints:\n\n";
    	for (String msg : messages) {
    		multiLineMessage += msg + "\n";
    	}
    	
		// Show Messages in a Dialog
    	DialogFactory factory = new DialogFactory(DialogFactoryMode.INTERNAL);
    	JOptionPane options = new JOptionPane();
    	options.setMessage(multiLineMessage);
    	factory.showDialog(options, "Warnings!", false);
	}
}
