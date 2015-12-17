package com.horstmann.violet.framework.util.string.decorator;

/**
 * Created by Adrian Bobrowski on 17.12.2015.
 */
public class PrefixDecorator extends OneLineStringDecorator {

    public PrefixDecorator(OneLineString decoratedOneLineString, String prefix)
    {
        super(decoratedOneLineString);
        this.setPrefix(prefix);
    }

    public final void setPrefix(String prefix)
    {
        if(null == prefix)
        {
            prefix = "";
        }
        this.prefix = prefix;
    }

    @Override
    public String getHTML()
    {
        return prefix + " " + decoratedOneLineString.getHTML();
    }

    public OneLineStringDecorator clone() {
        return new PrefixDecorator(this.decoratedOneLineString.clone(), prefix);
    }

    protected String prefix = "";
}