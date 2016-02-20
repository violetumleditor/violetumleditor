package com.horstmann.violet.framework.property.string.decorator;

/**
 * This class makes the bold text
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 12.12.2015
 */
public class BoldDecorator extends OneLineStringDecorator
{
    public BoldDecorator(OneLineString decoratedOneLineString)
    {
        super(decoratedOneLineString);
    }

    /**
     * @see OneLineString#toDisplay()
     */
    @Override
    public String toDisplay()
    {
        return "<b>" + decoratedOneLineString.toDisplay() + "</b>";
    }
}