package com.horstmann.violet.framework.util.string;

import com.horstmann.violet.framework.util.string.decorator.OneLineString;

/**
 * Created by Adrian Bobrowski on 16.12.2015.
 */
public class SingleLineString extends AbstractLineString {
    @Override
    protected OneLineString convertTextToLineString(String text)
    {
        return new OneLineString(text);
    }

    @Override
    final public void setText(String text)
    {
        this.oneLineString = this.convertTextToLineString(text);
    }

    @Override
    final public String getText()
    {
        return oneLineString.getText();
    }

    @Override
    final public String getHTML()
    {
        return oneLineString.getHTML();
    }


    public SingleLineString clone() {
        SingleLineString cloned = new SingleLineString();
        cloned.oneLineString = oneLineString.clone();
        return cloned;
    }

    private OneLineString oneLineString = new OneLineString();
}
