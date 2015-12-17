package com.horstmann.violet.product.diagram.abstracts.property;

import com.horstmann.violet.framework.util.string.AbstractLineString;
import com.horstmann.violet.framework.util.string.SingleLineString;
import com.horstmann.violet.framework.util.string.decorator.OneLineString;

/**
 * Created by Adrian Bobrowski on 16.12.2015.
 */
public class SingleLineText extends LineText {

    public SingleLineText() {
        singleLineString = new SingleLineString();
    }

    public SingleLineText(SingleLineString singleLineString) {
        this.singleLineString = singleLineString;
    }


    @Override
    public void setText(String text) {
        singleLineString.setText(text);

        this.setLabelText(singleLineString.getHTML());
    }

    @Override
    public String getText() {
        return singleLineString.getText();
    }

    public String toString() {
        return singleLineString.getText().replace('\n', '|');
    }

    public SingleLineText clone() {
        SingleLineText cloned = new SingleLineText();
        cloned.singleLineString = singleLineString; // TODO potrzeban funkcja clone
        return cloned;
    }

    private SingleLineString singleLineString = null;
}
