package com.horstmann.violet.application.menu;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GenerateAverage {
	private double avgOut;
	private double avgRec;
	private int numObj;
	
	public GenerateAverage() {
		readFile("stats/test1.txt");
		createAverageWindow();
	}
	
	private void readFile(String inFile) {
		BufferedReader br = null;
		
		List<String> nameObjects = new ArrayList<String>();
		List<Integer> numOut = new ArrayList<Integer>();
		List<Integer> numRec = new ArrayList<Integer>();
		
		try {
			//File from sequence Diagram containing information
			br = new BufferedReader(new FileReader(inFile));
			
			String str;
			
			//read lines and put information into 3 different Lists
			while((str = br.readLine()) != null) {
				String tempName = str.substring(0, str.indexOf(":"));
			    nameObjects.add(tempName);
			    
			    String out = str.substring(str.indexOf(":"), str.indexOf(","));
			    String tempOut = out.substring(1);
			    numOut.add(Integer.parseInt(tempOut));
			    
			    String rec = str.substring(str.indexOf(","));
			    String tempRec = rec.substring(1);
			    numRec.add(Integer.parseInt(tempRec));
			}
			numObj = nameObjects.size();
			// Get the total number of outgoing messages.
			avgOut = averageOut(numOut, numObj);
			
			// Get the total number of receiving messages.
			avgRec = averageRec(numRec, numObj);
		} catch (IOException e) {
            e.printStackTrace();
		} finally {
			try {
				br.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void createAverageWindow() {
		// Create the frame.
		JFrame frame = new JFrame("Averages of the sequence diagram");

		Container c = frame.getContentPane();
		Dimension d = new Dimension(400,300);
		
		JPanel panel = new JPanel();
		
		c.setPreferredSize(d);
		JLabel numObjs = new JLabel("Total number of objects: " + numObj);
		panel.add(numObjs, BorderLayout.CENTER);
		
		JLabel avgOuts = new JLabel("Average number of outgoing messages per object: " + avgOut);
		panel.add(avgOuts, BorderLayout.CENTER);
		
		JLabel avgRecs = new JLabel("Average number of receiving messages per object: " + avgRec);
		panel.add(avgRecs, BorderLayout.CENTER);
		
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
	
	private static double averageOut(List<Integer> numOut, int size) {
		int total = 0;
		for (int i = 0; i < size; i++) {
			total += numOut.get(i);
		}
		return total/size;
	}
	
	private static double averageRec(List<Integer> numRec, int size) {
		int total = 0;
		for (int i = 0; i < size; i++) {
			total += numRec.get(i);
		}
		return total/size;
	}
	
	public double getAvgOut() {
		return avgOut;
	}
	
	public void setAvgOut(double avgOut) {
		this.avgOut = avgOut;
	}
	
	public double getAvgRec() {
		return avgRec;
	}
	
	public void setAvgRec(double avgRec) {
		this.avgRec = avgRec;
	}
	
	public int getNumObj() {
		return numObj;
	}
	
	public void setNumObj(int numObj) {
		this.numObj = numObj;
	}
}
