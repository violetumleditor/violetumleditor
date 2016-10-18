package com.seanregan.javaimport;

/**
 *
 * @author Sean
 */
public interface IJavaParseable {
	public void   setFileReference(String refernce);
	public String getFileReference();
	
	public void   setClassName(String name);
	public String getClassName();
	
	public void	parseAndPopulate();
}
