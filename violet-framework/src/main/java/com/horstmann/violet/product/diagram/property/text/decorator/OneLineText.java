package com.horstmann.violet.product.diagram.property.text.decorator;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.horstmann.violet.product.diagram.property.text.EditableText;

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
        this.text = text;
    }

    /**
     * Sets a new plain text
     *
     * @param text to be set
     */
    @Override
    public final void setText(EditableText text)
    {
        setText(text.toEdit());
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
    	String result = replaceForUnification(sentence);
    	result = replaceSpecialChars(result);
    	result = result.replaceAll(" ", "&nbsp;");
    	result = restoreHTMLTags(result);
    	return result;
    }
    
    /**
     * Restore html tag because they previously be considered as special chars 
     * @param input string
     * @return the replaced string
     */
    private String restoreHTMLTags(String in) {
    	Pattern pattern = Pattern.compile("&\\#60;(\\w+)(&nbsp;.+)*&\\#62;((.*))&\\#60;/\\1\\&#62;");
    	Matcher matcher = pattern.matcher(in);
    	StringBuffer stringBuffer = new StringBuffer();
    	boolean somethingFound = false;
        while (matcher.find()) {
        	somethingFound = true;
        	String tag = matcher.group(1);
        	String attributes = matcher.group(2);
        	String innerHtml = matcher.group(3);
        	if (attributes == null) {
        		attributes = "";
        	}
        	attributes = attributes.replace("&nbsp;", " ");
        	attributes = restoreSpecialChars(attributes);
        	if (innerHtml == null) {
        		innerHtml = "";
        	}
            String result = "<" + tag + attributes + ">" + innerHtml + "</" + tag + ">";
            matcher.appendReplacement(stringBuffer, result);
        }
        if (somethingFound) {
        	matcher.appendTail(stringBuffer);
        	return restoreHTMLTags(stringBuffer.toString());
        }
        if (stringBuffer.length() > 0) {
        	return stringBuffer.toString();
        }
        return in;
    }
    
    
    /**
     * Replace all non ascii chars to their unicode equivalent number 
     * @param input string
     * @return the replaced string
     */
    private String replaceSpecialChars(String in) {
    	StringBuilder out = new StringBuilder();
    	for(int i = 0; i < in.length(); i++) {
    	    char c = in.charAt(i);
    	    if(c < 31 || c > 126 || "<>\"'\\&".indexOf(c) >= 0) {
    	        out.append("&#" + (int) c + ";");
    	    } else {
    	        out.append(c);
    	    }
    	}
    	return out.toString();
    }

    
    /**
     * Restore all non ascii chars from their unicode equivalent number 
     * @param input string
     * @return the replaced string
     */
    private String restoreSpecialChars(String in) {
    	Pattern pattern = Pattern.compile("&\\#(\\d+);");
    	Matcher matcher = pattern.matcher(in);
    	StringBuffer stringBuffer = new StringBuffer();
    	while (matcher.find()) {
    		matcher.appendReplacement(stringBuffer, "" + ((char) Integer.parseInt(matcher.group(1))));
    	}
    	return stringBuffer.toString();
    }
    
    private String text;
}
