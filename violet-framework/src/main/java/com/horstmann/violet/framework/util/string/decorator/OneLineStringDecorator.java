package com.horstmann.violet.framework.util.string.decorator;

import com.horstmann.violet.framework.util.string.OneLineString;

/**
 * Created by Adrian Bobrowski on 12.12.2015.
 */
public class OneLineStringDecorator extends OneLineString {
    protected String labelPrefix = "";
    protected OneLineString decoratedOneLineString;

    public OneLineStringDecorator(OneLineString decoratedOneLineString)
    {
        this.decoratedOneLineString = decoratedOneLineString;
    }

    @Override
    public String toHTML()
    {
        return decoratedOneLineString.toHTML();
    }
    @Override
    public String toLabel()
    {
        if(true == this.labelPrefix.isEmpty())
        {
            return decoratedOneLineString.toLabel();
        }
        return this.labelPrefix + " " + decoratedOneLineString.toLabel();
    }
}
