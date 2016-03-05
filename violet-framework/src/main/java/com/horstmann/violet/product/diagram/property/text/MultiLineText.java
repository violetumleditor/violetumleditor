
package com.horstmann.violet.product.diagram.property.text;

import com.horstmann.violet.product.diagram.property.text.decorator.OneLineText;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class is a container for multi lines of text
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 16.12.2015
 */
public class MultiLineText extends LineText
{
    private interface Command
    {
        String execute(OneLineText oneLineString);
    }

    public MultiLineText()
    {
        super();
        setPadding(2,8);
    }
    public MultiLineText(Converter converter)
    {
        super(converter);
        setPadding(1,8);
    }
    protected MultiLineText(MultiLineText lineText) throws CloneNotSupportedException
    {
        super(lineText);
        rows = new ArrayList<OneLineText>(lineText.getRows());
    }

    @Override
    public void reconstruction(Converter converter)
    {
        super.reconstruction(converter);
        rows = new ArrayList<OneLineText>();
        setPadding(2,8);
        setText(text);
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
        this.text = removeDuplicateEnter(text);
        getRows().clear();
        String[] array = this.text.split("\n", -1);

        for (String rawRow: array)
        {
            getRows().add(converter.toLineString(rawRow));
        }
        setLabelText(toDisplay());

        notifyAboutChange();
    }

    /**
     * @see EditableText#toDisplay()
     */
    @Override
    final public String toDisplay()
    {
        return implode(TO_DISPLAY, "<br>");
    }

    /**
     * @see EditableText#toEdit()
     */
    @Override
    final public String toEdit()
    {
        return implode(TO_EDIT, "\n");
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return implode(TO_STRING, "|");
    }


    private String removeDuplicateEnter(String sentence)
    {
        return sentence.replaceAll("\\n+", "\n");
    }

    /**
     * Join rows with a glue string
     * @param command command to call
     * @param glue string between each row
     * @return a string containing a string representation of all rows, with the glue string between each row.
     */
    private String implode(Command command, String glue)
    {
        Iterator<OneLineText> iterator = getRows().iterator();
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

    /**
     * @return list of rows
     */
    private List<OneLineText> getRows()
    {
        if(null == rows)
        {
            rows = new ArrayList<OneLineText>();
            setPadding(2,8);
            setText(text);
        }
        return rows;
    }

    private String text = "";
    private transient List<OneLineText> rows;

    private static final Command TO_DISPLAY = new Command(){
        @Override
        public String execute(OneLineText oneLineString)
        {
            return oneLineString.toDisplay();
        }
    };
    private static final Command TO_EDIT = new Command(){
        @Override
        public String execute(OneLineText oneLineString)
        {
            return oneLineString.toEdit();
        }
    };
    private static final Command TO_STRING = new Command(){
        @Override
        public String execute(OneLineText oneLineString)
        {
            return oneLineString.toString();
        }
    };
}
