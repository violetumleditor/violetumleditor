package com.horstmann.violet.framework.util.string.decorator;

import com.horstmann.violet.framework.util.string.OneLineString;

/**
 * Created by Adrian Bobrowski on 16.12.2015.
 */
public class LargeSizeOneLineStringDecorator   extends OneLineStringDecorator {

    public LargeSizeOneLineStringDecorator(OneLineString decoratedOneLineString)
    {
        super(decoratedOneLineString);
    }

    public LargeSizeOneLineStringDecorator(OneLineString decoratedOneLineString, String regex)
    {
        super(decoratedOneLineString, regex);
    }

    @Override
    public String toHTML()
    {
        return getHtml("<font size=+2>", "</font>");
    }
}
