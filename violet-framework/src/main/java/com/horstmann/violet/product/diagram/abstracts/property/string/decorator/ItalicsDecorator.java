package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

/**
 * Created by Adrian Bobrowski on 12.12.2015.
 */
public class ItalicsDecorator extends OneLineStringDecorator
{
    public ItalicsDecorator(OneLineString decoratedOneLineString)
    {
        super(decoratedOneLineString);
    }

    @Override
    public String toDisplay()
    {
        return "<i>" + decoratedOneLineString.toDisplay() + "</i>";
    }
}