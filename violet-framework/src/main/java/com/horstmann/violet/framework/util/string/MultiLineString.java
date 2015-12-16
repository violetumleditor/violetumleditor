package com.horstmann.violet.framework.util.string;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class MultiLineString implements ILineString {
    protected List<OneLineString> rows = new ArrayList<OneLineString>();

    public MultiLineString() {

    }

    public MultiLineString(List<OneLineString> rows) {
        this.rows = rows;
    }

    public int count()
    {
        return this.rows.size();
    }

    //todo wyjątek !!!
    public OneLineString getLine(int number)
    {
        return this.rows.get(number);
    }

    @Override
    public String toHTML()
    {
        Iterator<OneLineString> iterator = rows.iterator();

        if(iterator.hasNext())
        {
            StringBuilder ret = new StringBuilder(iterator.next().toHTML());

            while(iterator.hasNext())
            {
                ret.append("<br>").append(iterator.next().toHTML());
            }
            return ret.toString();
        }
        return "";
    }
    @Override
    public String toEditor()
    {
        Iterator<OneLineString> iterator = rows.iterator();
        if(iterator.hasNext())
        {
            StringBuilder ret = new StringBuilder(iterator.next().toEditor());

            while(iterator.hasNext())
            {
                ret.append("\n").append(iterator.next().toEditor());
            }
            return ret.toString();
        }
        return "";
    }
}
