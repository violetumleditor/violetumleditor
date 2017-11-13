package com.horstmann.violet.application.menu;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.io.*;
import java.util.List;


import java.util.ArrayList;

public class GeneratePieChart {
	
    private String nameArr[];
    private int outArr[];
    private int numObjects = 0;
    
    public String[] getNameArr() {
        return nameArr;
    }
    
    public int[] getOutArr() {
        return outArr;
    }
    
    public int getNumObj() {
        return numObjects;
    }
    
    public void setNameArr(String[] nameArr) {
        this.nameArr = nameArr;
    }
    
    public void setOutArr(int[] outArr) {
        this.outArr = outArr;
    }
    
    public void setNumObjects(int numObjects)  {
        this.numObjects = numObjects;
    }
    
    GeneratePieChart(){
    	readFile("stats/test1.txt");
    	createPieChart();
    }
	
    public void readFile(String inFile){
        BufferedReader br = null;
        
        List<String> nameObjects = new ArrayList<String>();
        List<Integer> numOut = new ArrayList<Integer>();
        
        try {
            //File from sequence Diagram containing information
            br = new BufferedReader(new FileReader(inFile));
            String str;
            
            //read lines and put inforamtion into 3 different Lists
            while((str = br.readLine()) != null){
                String tempName = str.substring(0, str.indexOf(":"));
                nameObjects.add(tempName);
                
                String out = str.substring(str.indexOf(":"), str.indexOf(","));
                String tempOut = out.substring(1);
                numOut.add(Integer.parseInt(tempOut));
                
            }
            
            //Convert lists to arrays
            nameArr = nameObjects.toArray(new String[0]);
            outArr = toIntArray(numOut);
            
            numObjects = nameArr.length;

        }
        
        catch (IOException e){
            e.printStackTrace();
        }
        finally{
            try {
                br.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    //Used to create int array
    int[] toIntArray(List<Integer> list){
        int[] intArr = new int[list.size()];
        for(int i = 0;i < intArr.length;i++){
            intArr[i] = list.get(i);
        }
        return intArr;
    }
    
    public void createPieChart() {
		// create a dataset...
		DefaultPieDataset data = new DefaultPieDataset();
		for(int i = 0; i<getNumObj() ; i++){
			data.setValue(nameArr[i], outArr[i]);
		}

		// create a chart...
		JFreeChart chart = ChartFactory.createPieChart(
				"Pie Chart",
				data,
				true,    // legend?
				true,    // tooltips?
				false    // URLs?
		);
		// create and display a frame...
		ChartFrame frame = new ChartFrame("Statistics", chart);
		frame.setSize(800, 800);
		frame.setVisible(true);
    }
}
