package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 12.12.2015
 */
public class OneLineStringDecorator extends OneLineString
{
    public OneLineStringDecorator(OneLineString decoratedOneLineString)
    {
        this.decoratedOneLineString = decoratedOneLineString;
    }

    public OneLineStringDecorator clone()
    {
        return new OneLineStringDecorator(this.decoratedOneLineString.clone());
    }

    public String toDisplay()
    {
        return decoratedOneLineString.toDisplay();
    }
    public String toEdit()
    {
        return decoratedOneLineString.toEdit();
    }

    protected OneLineString decoratedOneLineString;
}