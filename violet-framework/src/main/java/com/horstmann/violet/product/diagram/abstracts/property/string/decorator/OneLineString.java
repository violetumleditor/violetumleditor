package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

import com.horstmann.violet.product.diagram.abstracts.property.string.EditableString;

import java.io.Serializable;

/**
 * Created by Adrian Bobrowski on 12.12.2015.
 */
public class OneLineString implements Serializable, Cloneable, EditableString
{
    public OneLineString()
    {
        this("");
    }
    public OneLineString(String text)
    {
        setText(text);
    }

    public OneLineString clone()
    {
        OneLineString cloned = new OneLineString();
        return cloned;
    }

    @Override
    public String toDisplay()
    {
        return this.escapeHtml(text);
    }
    @Override
    public String toEdit()
    {
        return text;
    }

    @Override
    public final String toString()
    {
        return toEdit();
    }

    @Override
    public final void setText(String text)
    {
        if(null == text)
        {
            text = "";
        }
        this.text = removeDuplicateWhitespace(text);
    }

    public final boolean contains(String sentence)
    {
        return replaceForUnification(text).toLowerCase().contains(replaceForUnification(sentence).toLowerCase());
    }

    public final int find(String sentence)
    {
        return replaceForUnification(text).toLowerCase().indexOf(replaceForUnification(sentence).toLowerCase());
    }

    protected String replaceForUnification(String sentence)
    {
        return sentence.replace("<<", "«").replace(">>", "»");
    }

    protected String removeDuplicateWhitespace(String sentence)
    {
        return sentence.replaceAll("\\s+", " ").trim();
    }

    private String escapeHtml(String sentence)
    {
        return replaceForUnification(sentence).replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#x27;").replace("/", "&#x2F;");
    }

    private String text;
}
