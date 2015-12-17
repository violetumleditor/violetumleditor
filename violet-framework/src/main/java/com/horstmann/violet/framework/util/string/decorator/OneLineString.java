package com.horstmann.violet.framework.util.string.decorator;

import java.io.Serializable;

/**
 * Created by Adrian Bobrowski on 12.12.2015.
 */
public class OneLineString implements Serializable, Cloneable{

    public OneLineString(){}
    public OneLineString(String text)
    {
        setText(text);
    }

    public boolean contains(String sentence)
    {
        return replaceForUnification(text).toLowerCase().contains(replaceForUnification(sentence).toLowerCase());
    }

    public String getHTML()
    {
        return this.escapeHtml(text);
    }
    public String getText()
    {
        return text;
    }

    final public void setText(String text)
    {
        if(null == text)
        {
            text = "";
        }
        this.text = text;
    }

    private String escapeHtml(String sentence)
    {
        return replaceForUnification(sentence).replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#x27;").replace("/", "&#x2F;");
    }

    protected String replaceForUnification(String sentence)
    {
        return sentence.replace("<<", "«").replace(">>", "»");
    }

    protected String removeDuplicateWhitespace(String sentence)
    {
        return sentence.replaceAll("\\s+", " ");
    }

    public OneLineString clone() {
        OneLineString cloned = new OneLineString();
        cloned.text = text;
        return cloned;
    }

    protected String text = "";
}
