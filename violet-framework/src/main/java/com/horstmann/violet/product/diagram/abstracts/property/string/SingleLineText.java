package com.horstmann.violet.product.diagram.abstracts.property.string;

import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.OneLineString;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 16.12.2015
 */
public class SingleLineText extends LineText
{
    public SingleLineText()
    {
        super();
        setPadding(0,10);
        oneLineString = new OneLineString();
    }
    public SingleLineText(Converter converter)
    {
        super(converter);
        setPadding(0,10);
        oneLineString = new OneLineString();
    }
    protected SingleLineText(SingleLineText lineText) throws CloneNotSupportedException
    {
        super(lineText);
        oneLineString = lineText.oneLineString.clone();
    }

    @Override
    public SingleLineText clone()
    {
        return (SingleLineText)super.clone();
    }

    @Override
    protected SingleLineText copy() throws CloneNotSupportedException
    {
        return new SingleLineText(this);
    }

    @Override
    final public void setText(String text)
    {
        oneLineString = converter.toLineString(text);
        setLabelText(toDisplay());
        notifyAboutChange();
    }

    @Override
    final public String toDisplay()
    {
        return oneLineString.toDisplay();
    }

    @Override
    final public String toEdit()
    {
        return oneLineString.toEdit();
    }

    @Override
    final public String toString()
    {
        return oneLineString.toString();
    }

    private OneLineString oneLineString;
}
