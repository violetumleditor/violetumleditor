package com.horstmann.violet.product.diagram.property.text.decorator;

/**
 * This class makes the bold text
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 12.12.2015
 */
public class BoldDecorator extends OneLineTextDecorator
{
    public BoldDecorator(OneLineText decoratedOneLineString)
    {
        super(decoratedOneLineString);
    }

    /**
     * @see OneLineText#toDisplay()
     */
    @Override
    public String toDisplay()
    {
        return "<b>" + decoratedOneLineString.toDisplay() + "</b>";
    }
}