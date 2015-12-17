package com.horstmann.violet.framework.util.string.decorator;

/**
 * Created by Adrian Bobrowski on 12.12.2015.
 */
public abstract class OneLineStringDecorator extends OneLineString {
    public OneLineStringDecorator(OneLineString decoratedOneLineString)
    {
        this.decoratedOneLineString = decoratedOneLineString;
    }

    public String getHTML()
    {
        return decoratedOneLineString.getHTML();
    }
    public String getText()
    {
        return decoratedOneLineString.getText();
    }

    protected OneLineString decoratedOneLineString;
}