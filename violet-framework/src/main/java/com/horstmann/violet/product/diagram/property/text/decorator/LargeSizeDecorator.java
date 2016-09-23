package com.horstmann.violet.product.diagram.property.text.decorator;

/**
 * This class increases text
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 16.12.2015
 */
public class LargeSizeDecorator extends OneLineTextDecorator
{
    public LargeSizeDecorator(OneLineText decoratedOneLineString)
    {
        this(decoratedOneLineString,1);
    }
    public LargeSizeDecorator(OneLineText decoratedOneLineString, int increase)
    {
        super(decoratedOneLineString);

        if(0>=increase)
        {
            throw new IllegalArgumentException("increase have to positive number");
        }
        this.increase = increase;
    }

    /**
     * @see OneLineText#toDisplay()
     */
    @Override
    public String toDisplay()
    {
        return "<font size=+" + increase + ">" + decoratedOneLineString.toDisplay() + "</font>";
    }

    private int increase;
}
