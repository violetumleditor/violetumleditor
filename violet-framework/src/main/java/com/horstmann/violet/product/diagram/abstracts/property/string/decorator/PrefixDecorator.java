package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

/**
 * This class adds a prefix
 *
 * @author Adrian Bobrowski
 * @date 17.12.2015
 */
public class PrefixDecorator extends OneLineStringDecorator
{
    public PrefixDecorator(OneLineString decoratedOneLineString, String prefix)
    {
        super(decoratedOneLineString);
        this.setPrefix(prefix);
    }

    public final void setPrefix(String prefix)
    {
        if(null == prefix)
        {
            prefix = "";
        }
        this.prefix = prefix;
    }

    @Override
    public String toDisplay()
    {
        return prefix + " " + decoratedOneLineString.toDisplay();
    }

    private String prefix = "";
}