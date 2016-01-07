package com.horstmann.violet.product.diagram.abstracts.property.string;

/**
 * Created by Adrian Bobrowski on 07.01.2016.
 */
public interface EditableString {
    String toDisplay();

    String toEdit();

    void setText(String text);
}
