package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

/**
 * Created by Adrian Bobrowski on 16.12.2015.
 */
public class SmallSizeDecorator extends OneLineStringDecorator
{
    public SmallSizeDecorator(OneLineString decoratedOneLineString)
    {
        this(decoratedOneLineString,1);
    }
    public SmallSizeDecorator(OneLineString decoratedOneLineString, int increase)
    {
        super(decoratedOneLineString);
        this.reduction = increase;
    }

    @Override
    public String toDisplay()
    {
        return "<font size=-" + reduction + ">" + decoratedOneLineString.toDisplay() + "</font>";
    }

    private int reduction;
}
