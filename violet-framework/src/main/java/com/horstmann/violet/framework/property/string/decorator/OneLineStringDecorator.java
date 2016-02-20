package com.horstmann.violet.framework.property.string.decorator;

/**
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
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

    /**
     * @see OneLineString#toDisplay()
     */
    public String toDisplay()
    {
        return decoratedOneLineString.toDisplay();
    }
    /**
     * @see OneLineString#toEdit()
     */
    public String toEdit()
    {
        return decoratedOneLineString.toEdit();
    }

    protected OneLineString decoratedOneLineString;
}