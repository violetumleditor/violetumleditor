
package com.horstmann.violet.product.diagram.abstracts.property.string;

import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.OneLineString;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 16.12.2015
 */
public class MultiLineText extends LineText
{
    private interface Command
    {
        String execute(OneLineString oneLineString);
    }

    public MultiLineText()
    {
        super();
        setPadding(1,8);
    }
    public MultiLineText(Converter converter)
    {
        super(converter);
        setPadding(1,8);
    }
    protected MultiLineText(MultiLineText lineText) throws CloneNotSupportedException
    {
        super(lineText);
        rows = new ArrayList<OneLineString>(lineText.rows);
    }

    @Override
    public final MultiLineText clone()
    {
        return (MultiLineText)super.clone();
    }

    @Override
    protected MultiLineText copy() throws CloneNotSupportedException
    {
        return new MultiLineText(this);
    }

    @Override
    final public void setText(String text)
    {
        rows.clear();
        String[] array = text.split("\n", -1);

        for (String rawRow: array)
        {
            rows.add(converter.toLineString(rawRow));
        }
        setLabelText(toDisplay());

        notifyAboutChange();
    }

    @Override
    final public String toDisplay()
    {
        return implode(TO_DISPLAY, "<br>");
    }

    @Override
    final public String toEdit()
    {
        return implode(TO_EDIT, "\n");
    }

    @Override
    public String toString()
    {
        return implode(TO_STRING, "|");
    }

    private String implode(Command command, String glue)
    {
        Iterator<OneLineString> iterator = rows.iterator();
        if(iterator.hasNext())
        {
            StringBuilder ret = new StringBuilder(command.execute(iterator.next()));

            while(iterator.hasNext())
            {
                ret.append(glue).append(command.execute(iterator.next()));
            }
            return ret.toString();
        }
        return "";
    }

    private List<OneLineString> rows = new ArrayList<OneLineString>();

    private final static Command TO_DISPLAY = new Command(){
        @Override
        public String execute(OneLineString oneLineString)
        {
            return oneLineString.toDisplay();
        }
    };
    private final static Command TO_EDIT = new Command(){
        @Override
        public String execute(OneLineString oneLineString)
        {
            return oneLineString.toEdit();
        }
    };
    private final static Command TO_STRING = new Command(){
        @Override
        public String execute(OneLineString oneLineString)
        {
            return oneLineString.toString();
        }
    };
}
