package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

/**
 * This class decreases text
 *
 * @author Adrian Bobrowski
 * @date 16.12.2015
 */
public class SmallSizeDecorator extends OneLineStringDecorator
{
    public SmallSizeDecorator(OneLineString decoratedOneLineString)
    {
        this(decoratedOneLineString,1);
    }
    public SmallSizeDecorator(OneLineString decoratedOneLineString, int decreases)
    {
        super(decoratedOneLineString);
        this.decreases = decreases;
    }

    @Override
    public String toDisplay()
    {
        return "<font size=-" + decreases + ">" + decoratedOneLineString.toDisplay() + "</font>";
    }

    private int decreases;
}
