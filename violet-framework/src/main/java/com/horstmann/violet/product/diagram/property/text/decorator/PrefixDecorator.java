package com.horstmann.violet.product.diagram.property.text.decorator;

/**
 * This class adds a prefix
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 17.12.2015
 */
public class PrefixDecorator extends OneLineTextDecorator
{
    public PrefixDecorator(OneLineText decoratedOneLineString, String prefix)
    {
        super(decoratedOneLineString);
        this.setPrefix(prefix);
    }

    /**
     * sets prefix
     * @param prefix
     */
    public final void setPrefix(String prefix)
    {
        if(null == prefix)
        {
            prefix = "";
        }
        this.prefix = prefix;
    }

    /**
     * @see OneLineText#toDisplay()
     */
    @Override
    public String toDisplay()
    {
    	return getPrefixToDisplay() + decoratedOneLineString.toDisplay();
    }
    
    /**
     * Allows to display prefix only if string to display is not empty
     * 
     * @return string
     */
    private String getPrefixToDisplay() {
    	if (isStringToDisplayEmpty()) {
    		return "";
    	}
    	return " " + this.prefix + " ";
    }
    
    
    /**
     * Checks if text to display is empty
     * 
     * @return true if empty
     */
    private boolean isStringToDisplayEmpty() {
    	if (decoratedOneLineString == null) {
    		return true;
    	}
    	String toDisplay = decoratedOneLineString.toDisplay();
		if (toDisplay == null) {
    		return true;
    	}
		if (toDisplay.length() == 0) {
			return true;
		}
    	return false;
    }

    private String prefix = "";
}