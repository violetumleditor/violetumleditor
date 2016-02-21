package com.horstmann.violet.framework.property.text.decorator;

/**
 * This class decreases text
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 16.12.2015
 */
public class SmallSizeDecorator extends OneLineTextDecorator
{
    public SmallSizeDecorator(OneLineText decoratedOneLineString)
    {
        this(decoratedOneLineString,1);
    }
    public SmallSizeDecorator(OneLineText decoratedOneLineString, int decreases)
    {
        super(decoratedOneLineString);
        if(0>=decreases)
        {
            throw new IllegalArgumentException("decreases have to positive number");
        }
        this.decreases = decreases;
    }

    /**
     * @see OneLineText#toDisplay()
     */
    @Override
    public String toDisplay()
    {
        return "<font size=-" + decreases + ">" + decoratedOneLineString.toDisplay() + "</font>";
    }

    private int decreases;
}
