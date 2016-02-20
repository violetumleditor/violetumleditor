package com.horstmann.violet.framework.property.string.decorator;

/**
 * This class makes italic text
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 12.12.2015
 */
public class ItalicsDecorator extends OneLineStringDecorator
{
    public ItalicsDecorator(OneLineString decoratedOneLineString)
    {
        super(decoratedOneLineString);
    }

    /**
     * @see OneLineString#toDisplay()
     */
    @Override
    public String toDisplay()
    {
        return "<i>" + decoratedOneLineString.toDisplay() + "</i>";
    }
}