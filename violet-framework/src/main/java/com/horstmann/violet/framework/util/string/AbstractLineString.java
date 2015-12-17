package com.horstmann.violet.framework.util.string;

import com.horstmann.violet.framework.util.string.decorator.OneLineString;

import java.io.Serializable;

/**
 * Created by Adrian Bobrowski on 12.12.2015.
 */
public abstract class AbstractLineString implements Serializable, Cloneable{
    protected abstract OneLineString convertTextToLineString(String text);

    public abstract void setText(String text);
    public abstract String getText();
    public abstract String getHTML();
}
