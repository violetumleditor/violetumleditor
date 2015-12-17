package com.horstmann.violet.framework.util.string;

import com.horstmann.violet.framework.util.string.decorator.OneLineString;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class MultiLineString extends AbstractLineString {
    @Override
    protected OneLineString convertTextToLineString(String text)
    {
        return new OneLineString(text);
    }

    @Override
    final public void setText(String text)
    {
        rows.clear();
        String[] array = text.split("\n", -1);

        for (String rawRow: array) {
            rows.add(this.convertTextToLineString(rawRow));
        }
    }

    @Override
    final public String getText()
    {
        Iterator<OneLineString> iterator = rows.iterator();
        if(iterator.hasNext())
        {
            StringBuilder ret = new StringBuilder(iterator.next().getText());

            while(iterator.hasNext())
            {
                ret.append("\n").append(iterator.next().getText());
            }
            return ret.toString();
        }
        return "";
    }

    @Override
    final public String getHTML()
    {
        Iterator<OneLineString> iterator = rows.iterator();

        if(iterator.hasNext())
        {
            StringBuilder ret = new StringBuilder(iterator.next().getHTML());

            while(iterator.hasNext())
            {
                ret.append("<br>").append(iterator.next().getHTML());
            }
            return ret.toString();
        }
        return "";
    }

    public MultiLineString clone() {
        MultiLineString cloned = new MultiLineString();
        cloned.rows = new ArrayList<OneLineString>(rows);
        return cloned;
    }

    final public int count()
    {
        return this.rows.size();
    }

    private List<OneLineString> rows = new ArrayList<OneLineString>();
}
