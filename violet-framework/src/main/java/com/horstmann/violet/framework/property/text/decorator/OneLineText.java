package com.horstmann.violet.framework.property.text.decorator;

import com.horstmann.violet.framework.property.text.EditableText;

import java.io.Serializable;

/**
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 12.12.2015
 */
public class OneLineText implements Serializable, Cloneable, EditableText
{
    public OneLineText()
    {
        this("");
    }
    public OneLineText(String text)
    {
        setText(text);
    }

    /**
     * Creates and returns a copy of OneLineText.
     *
     * @return
     */
    public OneLineText clone()
    {
        OneLineText cloned = new OneLineText(text);
        return cloned;
    }

    /**
     * Sets a new text
     *
     * @param text to be set
     */
    @Override
    public final void setText(String text)
    {
        if(null == text)
        {
            text = "";
        }
        this.text = removeDuplicateWhitespace(text);
    }

    /**
     * Get formatted text with HTML tags
     *
     * @return formatted text
     */
    @Override
    public String toDisplay()
    {
        return this.escapeHtml(text);
    }

    /**
     * Get plain text for editing
     *
     * @return plain text
     */
    @Override
    public String toEdit()
    {
        return text;
    }

    /**
     * Get text
     *
     * @return text
     */
    @Override
    public final String toString()
    {
        return toEdit();
    }

    /**
     * Returns true if and only if this string contains the specified
     * sequence of char values.
     *
     * @param sentence the sequence to search for
     * @return true if this string contains sentence, false otherwise
     */
    public final boolean contains(String sentence)
    {
        return replaceForUnification(text).toLowerCase().contains(replaceForUnification(sentence).toLowerCase());
    }

    /**
     * Returns the index within this string of the first occurrence of the
     * specified substring.
     *
     * @param   sentence the substring to search for.
     * @return  the index of the first occurrence of the specified substring,
     *          or {@code -1} if there is no such occurrence.
     */
    public final int find(String sentence)
    {
        return replaceForUnification(text).toLowerCase().indexOf(replaceForUnification(sentence).toLowerCase());
    }

    /**
     * Remove all multiple spaces to only one space
     *
     * @param sentence
     * @return cleared string
     */
    protected final String removeDuplicateWhitespace(String sentence)
    {
        return sentence.replaceAll("\\s+", " ").trim();
    }

    /**
     * Replace all special characters for unification text.
     *
     * @param sentence input string
     * @return the replaced string
     */
    protected final String replaceForUnification(String sentence)
    {
        return sentence.replace("<<", "«").replace(">>", "»");
    }

    /**
     * Escapes all special characters to their corresponding entity reference (e.g. &lt;).
     *
     * @param sentence the (unescaped) input string
     * @return the escaped string
     */
    private String escapeHtml(String sentence)
    {
        return replaceForUnification(sentence).replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#x27;").replace("/", "&#x2F;");
    }

    private String text;
}
