package com.horstmann.violet.framework.util.string;

import com.horstmann.violet.framework.util.string.decorator.OneLineString;

/**
 * Created by Adrian Bobrowski on 16.12.2015.
 */
public class SingleLineString extends AbstractLineString {
    public SingleLineString(Converter converter) {
        super(converter);
    }

    public SingleLineString() {
        super();
    }

    @Override
    final public void setText(String text)
    {
        this.oneLineString = this.converter.convertTextToLineString(text);
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
        cloned.converter = converter;
        return cloned;
    }

    private OneLineString oneLineString = new OneLineString();
}
