package com.seanregan.javaimport;

/**
 * Interface for all Nodes that should be
 * paresable
 * @author Sean
 */
public interface IJavaParseable {
	/**
	 * Sets the file to import
	 * @param refernce the absolute path of the file to import
	 */
	public void   setFileReference(String refernce);
	
	/**
	 * Gets the file that is currently set to be imported
	 * @return a String containing the absolute path to the
	 * currently imported file
	 */
	public String getFileReference();
	
	/**
	 * Sets the class name of from the file to import
	 * @param name a String containing the class from the file
	 * to import
	 */
	public void   setClassName(String name);
	
	/**
	 * Gets the name of the class to import from 
	 * the imported file
	 * @return a String containing the name of the class
	 * to import
	 */
	public String getClassName();
	
	/**
	 * Parses the specified class within the specified file and
	 * populates the results within the node
	 */
	public void	parseAndPopulate();
}
