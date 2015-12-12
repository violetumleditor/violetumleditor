package com.horstmann.violet.framework.util.string.decorator;

import com.horstmann.violet.framework.util.string.ILineString;

/**
 * Created by Adrian Bobrowski on 12.12.2015.
 */
public class UnderlineOneLineStringDecorator extends OneLineStringDecorator {
    protected String labelPrefix;

    public UnderlineOneLineStringDecorator(ILineString decoratedOneLineString)
    {
        super(decoratedOneLineString);
        this.labelPrefix = "";
    }

    public UnderlineOneLineStringDecorator(ILineString decoratedOneLineString, String labelPrefix)
    {
        super(decoratedOneLineString);
        this.labelPrefix = labelPrefix;
    }

    @Override
    public String toHTML()
    {
        return "<u>" + decoratedOneLineString.toHTML() + "</u>";
    }
    @Override
    public String toLabel()
    {
        return this.labelPrefix + decoratedOneLineString.toLabel();
    }
}
