package com.horstmann.violet.framework.util.string.decorator;

import com.horstmann.violet.framework.util.string.OneLineString;

/**
 * Created by Adrian Bobrowski on 12.12.2015.
 */
public class UnderlineOneLineStringDecorator extends OneLineStringDecorator {

    public UnderlineOneLineStringDecorator(OneLineString decoratedOneLineString)
    {
        super(decoratedOneLineString);
    }

    public UnderlineOneLineStringDecorator(OneLineString decoratedOneLineString, String labelPrefix)
    {
        super(decoratedOneLineString);
        this.labelPrefix = labelPrefix;
    }

    @Override
    public String toHTML()
    {
        return "<u>" + decoratedOneLineString.toHTML() + "</u>";
    }
}
