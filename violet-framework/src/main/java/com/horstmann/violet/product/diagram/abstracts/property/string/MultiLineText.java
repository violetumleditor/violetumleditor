
package com.horstmann.violet.product.diagram.abstracts.property.string;

import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.OneLineString;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultiLineText extends LineText {
    private interface Command
    {
        String execute(OneLineString oneLineString);
    }

    public MultiLineText() {
        super();
    }

    public MultiLineText(Converter converter) {
        super(converter);
    }

    @Override
    final public void setText(String text)
    {
        rows.clear();
        String[] array = text.split("\n", -1);

        for (String rawRow: array) {
            rows.add(converter.toLineString(rawRow));
        }

        setLabelText(getHTML());
    }

    @Override
    final public String getText()
    {
        return implode(new Command(){
            @Override
            public String execute(OneLineString oneLineString)
            {
                return oneLineString.getText();
            }
        }, "\n");
    }

    @Override
    final public String getHTML()
    {
        return implode(new Command(){
            @Override
            public String execute(OneLineString oneLineString)
            {
                return oneLineString.getHTML();
            }
        }, "<br>");
    }

    @Override
    public String toString() {
        return getText().replace('\n', '|');
    }

    @Override
    public MultiLineText clone() {
        MultiLineText cloned = new MultiLineText(converter);
        cloned.rows = new ArrayList<OneLineString>(rows);
        return cloned;
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
}
