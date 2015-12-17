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
        return sentence.replace("&", "&amp;").replace("<<", "«").replace(">>", "»").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#x27;").replace("/", "&#x2F;");
    }

    private String text = "";
}
