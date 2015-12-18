package com.horstmann.violet.product.diagram.abstracts.property.string;

import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.OneLineString;


public class SingleLineText extends LineText {
    public SingleLineText() {
        super();
    }

    public SingleLineText(Converter converter) {
        super(converter);
    }

    @Override
    final public void setText(String text)
    {
        oneLineString = converter.toLineString(text);
        setLabelText(getHTML());
    }

    @Override
    final public String getText()
    {
        return oneLineString.getText();
    }

    @Override
    final public String getHTML()
    {
        return oneLineString.getHTML();
    }

    @Override
    public String toString() {
        return oneLineString.getText();
    }

    @Override
    public SingleLineText clone() {
        SingleLineText cloned = new SingleLineText(converter);
        cloned.oneLineString = oneLineString.clone();
        return cloned;
    }

    private OneLineString oneLineString = new OneLineString();
}
