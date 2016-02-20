package com.horstmann.violet.framework.property.string.decorator;

/**
 * This class adds a prefix
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 17.12.2015
 */
public class PrefixDecorator extends OneLineStringDecorator
{
    public PrefixDecorator(OneLineString decoratedOneLineString, String prefix)
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
     * @see OneLineString#toDisplay()
     */
    @Override
    public String toDisplay()
    {
        return prefix + " " + decoratedOneLineString.toDisplay();
    }

    private String prefix = "";
}