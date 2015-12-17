package com.horstmann.violet.framework.util.string.decorator;

/**
 * Created by Adrian Bobrowski on 12.12.2015.
 */
public class BoldDecorator extends OneLineStringDecorator {

    public BoldDecorator(OneLineString decoratedOneLineString)
    {
        super(decoratedOneLineString);
    }

    @Override
    public String getHTML()
    {
        return "<b>" + decoratedOneLineString.getHTML() + "</b>";
    }
}