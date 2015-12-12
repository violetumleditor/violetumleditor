package com.horstmann.violet.framework.util.string;

/**
 * Created by Adrian Bobrowski on 12.12.2015.
 */
public class OneLineString implements ILineString {
    protected String sentence;

    public OneLineString()
    {
        this.sentence = "";
    }

    public OneLineString(String sentence)
    {
        this.sentence = sentence;
    }

    public void setText(String sentence)
    {
        this.sentence = sentence;
    }

    @Override
    public String toHTML()
    {
        return this.escapeHtml(sentence);
    }
    @Override
    public String toLabel()
    {
        return sentence;
    }

    protected String escapeHtml(String sentence)
    {
        return sentence.replace("&", "&amp;").replace("<<", "«").replace(">>", "»").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#x27;").replace("/", "&#x2F;");
    }
}
