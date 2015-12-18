package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

/**
 * Created by Adrian Bobrowski on 12.12.2015.
 */
public class UnderlineDecorator extends OneLineStringDecorator {

    public UnderlineDecorator(OneLineString decoratedOneLineString)
    {
        super(decoratedOneLineString);
    }

    @Override
    public String getHTML()
    {
        return "<u>" + decoratedOneLineString.getHTML() + "</u>";
    }
}
