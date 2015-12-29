package com.horstmann.violet.framework.graphics.content;

import com.horstmann.violet.framework.graphics.Separator;
import com.horstmann.violet.framework.graphics.content.Content;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian Bobrowski on 21.12.2015.
 */
public abstract class GroupContent extends Content {
    protected abstract void measureAndSetSize();

    @Override
    protected final void setWidth(int width)
    {
        super.setWidth(width);
        for (Content content: contents) {
            content.setWidth(width);
        }
    }
    @Override
    protected final void setHeight(int height)
    {
        super.setHeight(height);
        for (Content content: contents) {
            content.setHeight(height);
        }
    }

    public final void refresh()
    {
        measureAndSetSize();
    }

    public final void add(Content content)
    {
        contents.add(content);
        refresh();
    }

    public void setSeparator(Separator separator) {
        if(null==separator)
        {
            separator = Separator.EMPTY;
        }
        this.separator = separator;
    }

    protected Separator separator;
    protected List<Content> contents = new ArrayList<Content>();
}
