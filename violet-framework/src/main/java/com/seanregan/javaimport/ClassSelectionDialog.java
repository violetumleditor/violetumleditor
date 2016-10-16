package com.seanregan.javaimport;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Sean
 */
public class ClassSelectionDialog {
	private final JDialog mDialog;
	private final JList   mClassList;
	
	private SelectionListener mSelectionListener = null;
	
	public interface SelectionListener {
		void onSelectionMade(String sel);
		void onCancelled();
	}
	
	public ClassSelectionDialog(List<String> classItems) {
		//Dialog box
		mDialog = new JDialog();
		
		//Create the Panel to hold our elements
		JPanel optionPanel  = new JPanel();
		optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
		optionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		//Add descriptive label
		JLabel descLabel = new JLabel("Choose the class from the Java file to import:");
		optionPanel.add(descLabel);
		
		//Add each class as an item to the list
		DefaultListModel listModel = new DefaultListModel();
		for (String c : classItems) {
			listModel.addElement(c);
		}

		//Create list of classes
		mClassList = new JList(listModel);
		mClassList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		optionPanel.add(new JScrollPane(mClassList));

		//Create a "Select" button
		JButton selectButton = new JButton("Select");
		selectButton.addActionListener(mSelectPressed);
		optionPanel.add(selectButton);

		//Dialog box settings
		mDialog.setTitle("Select a class");
		mDialog.setModal(true);
		mDialog.getContentPane().add(optionPanel);
		mDialog.pack();
		mDialog.setResizable(false);
		mDialog.addWindowListener(mWindowListener);
	}
	
	public void setSelectionListener(SelectionListener listener) {
		mSelectionListener = listener;
	}
	
	public void show() {
		mDialog.setLocationRelativeTo(null);
		mDialog.setVisible(true);
	}
	
	private final ActionListener mSelectPressed = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (mClassList.getSelectedIndex() != -1) {
				if (mSelectionListener != null) {
					mSelectionListener.onSelectionMade((String)(mClassList.getSelectedValue()));
				}
				
				mDialog.setVisible(false);
			}
		}
	};
	
	private final WindowListener mWindowListener = new WindowListener() {
		@Override
		public void windowOpened(WindowEvent e) {

		}

		@Override
		public void windowClosing(WindowEvent e) {

		}

		@Override
		public void windowClosed(WindowEvent e) {
			if (mSelectionListener != null) mSelectionListener.onCancelled();
		}

		@Override
		public void windowIconified(WindowEvent e) {

		}

		@Override
		public void windowDeiconified(WindowEvent e) {

		}

		@Override
		public void windowActivated(WindowEvent e) {

		}

		@Override
		public void windowDeactivated(WindowEvent e) {

		}
	};
}
