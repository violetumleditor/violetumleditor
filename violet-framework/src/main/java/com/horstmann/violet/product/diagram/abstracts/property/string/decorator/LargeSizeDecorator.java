package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

/**
 * This class increases text
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 16.12.2015
 */
public class LargeSizeDecorator extends OneLineStringDecorator
{
    public LargeSizeDecorator(OneLineString decoratedOneLineString)
    {
        this(decoratedOneLineString,1);
    }
    public LargeSizeDecorator(OneLineString decoratedOneLineString, int increase)
    {
        super(decoratedOneLineString);
        this.increase = increase;
    }

    /**
     * @see OneLineString#toDisplay()
     */
    @Override
    public String toDisplay()
    {
        return "<font size=+" + increase + ">" + decoratedOneLineString.toDisplay() + "</font>";
    }

    private int increase;
}
