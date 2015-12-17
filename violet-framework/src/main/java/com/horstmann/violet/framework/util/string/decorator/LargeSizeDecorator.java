package com.horstmann.violet.framework.util.string.decorator;

/**
 * Created by Adrian Bobrowski on 16.12.2015.
 */
public class LargeSizeDecorator extends OneLineStringDecorator {

    public LargeSizeDecorator(OneLineString decoratedOneLineString)
    {
        super(decoratedOneLineString);
    }

    @Override
    public String getHTML()
    {
        return "<font size=+2>" + decoratedOneLineString.getHTML() + "</font>";
    }
}
