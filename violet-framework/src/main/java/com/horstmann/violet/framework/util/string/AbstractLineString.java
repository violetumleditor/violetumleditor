package com.horstmann.violet.framework.util.string;

import com.horstmann.violet.framework.util.string.decorator.OneLineString;

import java.io.Serializable;

/**
 * Created by Adrian Bobrowski on 12.12.2015.
 */
public abstract class AbstractLineString implements Serializable, Cloneable{
    public AbstractLineString() {
        converter = new Converter() {
            @Override
            public OneLineString convertTextToLineString(String text)
            {
                return new OneLineString(text);
            }
        };
    }
    public AbstractLineString(Converter converter) {
        this.converter = converter;
    }

    public abstract void setText(String text);
    public abstract String getText();
    public abstract String getHTML();

    protected Converter converter;
}
