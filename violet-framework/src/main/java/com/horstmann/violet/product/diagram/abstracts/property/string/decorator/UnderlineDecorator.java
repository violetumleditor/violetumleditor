package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

/**
 * Created by Adrian Bobrowski on 12.12.2015.
 */
public class UnderlineDecorator extends OneLineStringDecorator
{
    public UnderlineDecorator(OneLineString decoratedOneLineString)
    {
        super(decoratedOneLineString);
    }

    @Override
    public String toDisplay()
    {
        return "<u>" + decoratedOneLineString.toDisplay() + "</u>";
    }
}
