package com.horstmann.violet.framework.util.string.decorator;

import com.horstmann.violet.framework.util.string.OneLineString;

/**
 * Created by Adrian Bobrowski on 12.12.2015.
 */
public class OneLineStringDecorator extends OneLineString {
    protected String regex = null;
    protected OneLineString decoratedOneLineString;

    public OneLineStringDecorator(OneLineString decoratedOneLineString)
    {
        this.decoratedOneLineString = decoratedOneLineString;
    }
    public OneLineStringDecorator(OneLineString decoratedOneLineString, String regex)
    {
        this.decoratedOneLineString = decoratedOneLineString;
        if(null != regex)
        {
            this.regex = regex.replace("<<", "«").replace(">>", "»").toLowerCase();
        }
    }

    @Override
    public String toHTML()
    {
        return this.getHtml("","");
    }
    @Override
    public String toEditor()
    {
        return decoratedOneLineString.toEditor();
    }

    private boolean isRegexContains() {
        return null == regex || regex.isEmpty() || decoratedOneLineString.toHTML().toLowerCase().contains(regex);
    }

    private String removeRegex()
    {
        if(null == regex || regex.isEmpty())
        {
            return decoratedOneLineString.toHTML();
        }
        return decoratedOneLineString.toHTML().replace(regex, "");
    }

    final protected String getHtml(String prefix, String suffix)
    {
        if(this.isRegexContains())
        {
            return prefix + this.removeRegex() + suffix;
        }
        return decoratedOneLineString.toHTML();
    }
}