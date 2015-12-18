package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

/**
 * Created by Adrian Bobrowski on 16.12.2015.
 */
public class SmallSizeDecorator extends OneLineStringDecorator {

    public SmallSizeDecorator(OneLineString decoratedOneLineString)
    {
        super(decoratedOneLineString);
    }

    @Override
    public String getHTML()
    {
        return "<font size=-1>" + decoratedOneLineString.getHTML() + "</font>";
    }
}
