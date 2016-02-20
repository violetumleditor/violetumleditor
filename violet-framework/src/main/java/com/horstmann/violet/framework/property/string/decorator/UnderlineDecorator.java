package com.horstmann.violet.framework.property.string.decorator;

/**
 * This class makes the underlined text
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 12.12.2015
 */
public class UnderlineDecorator extends OneLineStringDecorator
{
    public UnderlineDecorator(OneLineString decoratedOneLineString)
    {
        super(decoratedOneLineString);
    }

    /**
     * @see OneLineString#toDisplay()
     */
    @Override
    public String toDisplay()
    {
        return "<u>" + decoratedOneLineString.toDisplay() + "</u>";
    }
}
