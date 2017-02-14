package com.horstmann.violet.product.diagram.property.text.decorator;

public class HideDecorator extends OneLineTextDecorator
{
    private final String HIDE="...()";

    public HideDecorator(OneLineText decoratedOneLineString)
    {
        super(decoratedOneLineString);
    }

    /**
     * @see OneLineText#toDisplay()
     */
    @Override
    public String toDisplay()
    {
        return HIDE;
    }
}