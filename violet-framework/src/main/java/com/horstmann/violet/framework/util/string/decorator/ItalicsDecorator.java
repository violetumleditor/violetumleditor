package com.horstmann.violet.framework.util.string.decorator;

/**
 * Created by Adrian Bobrowski on 12.12.2015.
 */
public class ItalicsDecorator extends OneLineStringDecorator {

    public ItalicsDecorator(OneLineString decoratedOneLineString)
    {
        super(decoratedOneLineString);
    }

    @Override
    public String getHTML()
    {
        return "<i>" + decoratedOneLineString.getHTML() + "</i>";
    }
}