package com.horstmann.violet.product.diagram.abstracts.property.string;

/**
 *
 * @author Adrian Bobrowski
 * @date 07.01.2016
 */
public interface EditableString
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
     * Sets a new text
     *
     * @param text to be set
     */
    void setText(String text);
}
