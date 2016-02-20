package com.horstmann.violet.framework.property.string;

import com.horstmann.violet.framework.property.string.decorator.OneLineString;

/**
 * This class is a container for a single line of text
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
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
        oneLineString = lineText.getOneLineString().clone();
    }
    @Override
    public void deserializeSupport(Converter converter)
    {
        super.deserializeSupport(converter);
        oneLineString = new OneLineString();
        setPadding(0,10);
        setText(text);
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

    /**
     * @see EditableString#setText(String)
     */
    @Override
    final public void setText(String text)
    {
        this.text = text;
        oneLineString = converter.toLineString(this.text);
        setLabelText(toDisplay());
        notifyAboutChange();
    }

    /**
     * @see EditableString#toDisplay()
     */
    @Override
    final public String toDisplay()
    {
        return getOneLineString().toDisplay();
    }

    /**
     * @see EditableString#toEdit()
     */
    @Override
    final public String toEdit()
    {
        return getOneLineString().toEdit();
    }

    /**
     * @see Object#toString()
     */
    @Override
    final public String toString()
    {
        return getOneLineString().toString();
    }

    /**
     * @return one line string
     */
    private OneLineString getOneLineString()
    {
        if(null == oneLineString)
        {
            oneLineString = new OneLineString();
            setPadding(0,10);
            setText(text);
        }
        return oneLineString;
    }

    private String text = "";
    private transient OneLineString oneLineString;
}
