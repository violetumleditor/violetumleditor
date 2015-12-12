package com.horstmann.violet.framework.util.string.decorator;

import com.horstmann.violet.framework.util.string.ILineString;

/**
 * Created by Adrian Bobrowski on 12.12.2015.
 */
public class OneLineStringDecorator implements ILineString {
    protected ILineString decoratedOneLineString;

    public OneLineStringDecorator(ILineString decoratedOneLineString)
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
        return decoratedOneLineString.toLabel();
    }
}
