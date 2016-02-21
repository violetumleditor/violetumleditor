package com.horstmann.violet.framework.property.text.decorator;

/**
 * This class makes italic text
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 12.12.2015
 */
public class ItalicsDecorator extends OneLineTextDecorator
{
    public ItalicsDecorator(OneLineText decoratedOneLineString)
    {
        super(decoratedOneLineString);
    }

    /**
     * @see OneLineText#toDisplay()
     */
    @Override
    public String toDisplay()
    {
        return "<i>" + decoratedOneLineString.toDisplay() + "</i>";
    }
}