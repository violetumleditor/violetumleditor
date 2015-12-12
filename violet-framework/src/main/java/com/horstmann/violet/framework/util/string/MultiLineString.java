package com.horstmann.violet.framework.util.string;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class MultiLineString implements ILineString {
    protected List<ILineString> rows = new ArrayList<ILineString>();

    @Override
    public String toHTML()
    {
        Iterator<ILineString> iterator = rows.iterator();

        if(iterator.hasNext())
        {
            StringBuilder ret = new StringBuilder(iterator.next().toHTML());

            while(iterator.hasNext())
            {
                ret.append(iterator.next().toHTML()).append("<br>");
            }
            return ret.toString();
        }
        return "";
    }
    @Override
    public String toLabel()
    {
        Iterator<ILineString> iterator = rows.iterator();
        if(iterator.hasNext())
        {
            StringBuilder ret = new StringBuilder(iterator.next().toHTML());

            while(iterator.hasNext())
            {
                ret.append(iterator.next().toLabel()).append("\n");
            }
            return ret.toString();
        }
        return "";
    }
}
