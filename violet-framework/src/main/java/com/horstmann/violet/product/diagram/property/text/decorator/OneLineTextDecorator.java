package com.horstmann.violet.product.diagram.property.text.decorator;

/**
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 12.12.2015
 */
public class OneLineTextDecorator extends OneLineText
{
    public OneLineTextDecorator(OneLineText decoratedOneLineString)
    {
        this.decoratedOneLineString = decoratedOneLineString;
    }

    public OneLineTextDecorator clone()
    {
        return new OneLineTextDecorator(this.decoratedOneLineString.clone());
    }

    /**
     * @see OneLineText#toDisplay()
     */
    public String toDisplay()
    {
        return decoratedOneLineString.toDisplay();
    }
    /**
     * @see OneLineText#toEdit()
     */
    public String toEdit()
    {
        return decoratedOneLineString.toEdit();
    }

    protected OneLineText decoratedOneLineString;
}