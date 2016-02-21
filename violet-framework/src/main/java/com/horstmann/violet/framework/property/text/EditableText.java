package com.horstmann.violet.framework.property.text;

/**
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 07.01.2016
 */
public interface EditableText
{
    /**
     * Get formatted text with HTML tags
     *
     * @return formatted text
     */
    String toDisplay();

    /**
     * Get plain text for editing
     *
     * @return plain text
     */
    String toEdit();

    /**
     * Sets a new plain text
     *
     * @param text to be set
     */
    void setText(String text);
}
