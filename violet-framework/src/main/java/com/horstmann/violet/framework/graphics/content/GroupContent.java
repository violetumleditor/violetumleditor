package com.horstmann.violet.framework.graphics.content;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian Bobrowski on 21.12.2015.
 */
public abstract class GroupContent extends Content{
    protected abstract void measureAndSetSize();

    @Override
    protected final void setWidth(int width)
    {
        this.width = width;
        for (Content content: contents) {
            content.setWidth(width);
        }
    }
    @Override
    protected final void setHeight(int height)
    {
        this.height = height;
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

    protected List<Content> contents = new ArrayList<Content>();
}
