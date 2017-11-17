package com.horstmann.violet.framework.util;

import java.util.List;

public class Statistics {
	/**
	 * Static Initialization
	 */ 
	public static final String LIST = "List";
	public static final String CHART = "Chart";
	public static final String WARNING = "Warning";
	
	/**
	 * Member Variables
	 */ 
	private String type;
	private String name;
	private List<String> sectorNames;
	private List<Integer> sectorSizes;
	private String warningMessage;
	
	
	/**
	 * Type-Sensitive Setters
	 */ 
	public void setList(String name, List<String> sectorNames) {
		this.type = LIST;
		this.name = name;
		this.sectorNames = sectorNames;
	}
	
	public void setChart(String name, List<String> sectorNames, List<Integer> sectorSizes) {
		this.type = CHART;
		this.name = name;
		this.sectorNames = sectorNames;
		this.sectorSizes = sectorSizes;
	}
	
	public void setWarning(String name, String warningMessage) {
		this.type = WARNING;
		this.name = name;
		this.warningMessage = warningMessage;
	}
	
	
	/**
	 * ToString
	 */ 
	@Override
	public String toString() {
		
		if (type == null || type.equals("")) {
			return "";
		}
		
		String result = "---" + type + "---" + "\n" + name + ":\n";
		
		// List
		if (type.equals(LIST)) {
			result += "Size: " + sectorNames.size() + "\n";
			for (String s : sectorNames) {
				result += s + "\n";
			}
			return result;
		}
		
		// Chart
		if (type.equals(CHART)) {
			for (int i = 0; i < sectorNames.size(); i++) {
				result += sectorNames.get(i) + " : " + sectorSizes.get(i) + "\n";
			}
			return result;
		}
		
		// Warning
		if (type.equals(WARNING)) {
			result += warningMessage;
			return result;
		}
		
		return super.toString();
	}
	
	
	
	/**
	 * Default Getters & Setters
	 */ 
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<String> getSectorNames() {
		return sectorNames;
	}
	
	public void setSectorNames(List<String> sectorNames) {
		this.sectorNames = sectorNames;
	}
	
	public List<Integer> getSectorSizes() {
		return sectorSizes;
	}
	public void setSectorSizes(List<Integer> sectorSizes) {
		this.sectorSizes = sectorSizes;
	}
	
	public String getWarningMessage() {
		return warningMessage;
	}
	
	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}
}
