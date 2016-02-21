package com.horstmann.violet.framework.property.text.decorator;

/**
 * This class makes the underlined text
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 12.12.2015
 */
public class UnderlineDecorator extends OneLineTextDecorator
{
    public UnderlineDecorator(OneLineText decoratedOneLineString)
    {
        super(decoratedOneLineString);
    }

    /**
     * @see OneLineText#toDisplay()
     */
    @Override
    public String toDisplay()
    {
        return "<u>" + decoratedOneLineString.toDisplay() + "</u>";
    }
}
