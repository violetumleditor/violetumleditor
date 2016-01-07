package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

/**
 * Created by Adrian Bobrowski on 12.12.2015.
 */
public class BoldDecorator extends OneLineStringDecorator
{
    public BoldDecorator(OneLineString decoratedOneLineString)
    {
        super(decoratedOneLineString);
    }

    @Override
    public String toDisplay()
    {
        return "<b>" + decoratedOneLineString.toDisplay() + "</b>";
    }
}