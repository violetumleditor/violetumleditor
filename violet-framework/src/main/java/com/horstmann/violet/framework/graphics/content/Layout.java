package com.horstmann.violet.framework.graphics.content;

import com.horstmann.violet.framework.graphics.Separator;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class groups together elements of type Content
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 21.12.2015
 */
public abstract class Layout extends Content
{
    /**
     * @param beforeOffset
     * @param content
     * @return point offset relation to the previous point
     */
    protected abstract Point2D getNextOffset(Point2D beforeOffset, Content content);

    /**
     * @param offset
     * @return the starting point for the separator
     */
    protected abstract Point2D getStartPointSeparator(Point2D offset);

    /**
     * @param offset
     * @return the ending point for the separator
     */
    protected abstract Point2D getEndPointSeparator(Point2D offset);

    /**
     * add a content to layout
     * @param content
     */
    public void add(Content content)
    {
        if(null == content)
        {
            throw new NullPointerException("Content can't be null");
        }
        content.addParent(this);
        contents.add(content);
        refresh();
    }

    /**
     * removes a content from layout
     * @param content
     */
    public void remove(Content content)
    {
        if(null == content)
        {
            throw new NullPointerException("Content can't be null");
        }
        content.removeParent(this);
        contents.remove(content);
        refresh();
    }

    /**
     * @return separator which separates the components in layout
     */
    public final Separator getSeparator()
    {
        return separator;
    }

    /**
     * sets a separator which separates the components in layout
     * @param separator
     */
    public final void setSeparator(Separator separator) {
        if(null==separator)
        {
            separator = Separator.EMPTY;
        }
        this.separator = separator;
    }

    /**
     * @return a list of elements in layout
     */
    protected final List<Content> getContents()
    {
        return contents;
    }

    /**
     * Refresh all the elements in layout
     *
     * @see Content#refreshDown()
     */
    @Override
    protected void refreshDown()
    {
        for (Content content: getContents())
        {
            content.refreshDown();
        }
        super.refreshDown();
    }

    /**
     * @see Content#draw(Graphics2D)
     */
    @Override
    public final void draw(Graphics2D graphics)
    {
        Content content = null;
        Point2D offset = new Point2D.Double(0,0);
        Iterator<Content> iterator = contents.iterator();

        if(iterator.hasNext())
        {
            content = iterator.next();
            content.draw(graphics, offset);
        }
        while(iterator.hasNext())
        {
            offset = getNextOffset(offset, content);

            content = iterator.next();
            content.draw(graphics, offset);
            separator.draw(graphics, getStartPointSeparator(offset), getEndPointSeparator(offset));
        }
    }

    /**
     * @param content
     * @return locations of the content relative to the inside layout
     */
    public Point2D getLocation(Content content)
    {
        int index = contents.indexOf(content);
        Point2D offset = new Point2D.Double(0,0);

        for(int i = 0; i < index; ++i)
        {
            offset = getNextOffset(offset, contents.get(i));
        }
        return offset;
    }

    private Separator separator = Separator.EMPTY;
    private List<Content> contents = new ArrayList<Content>();
}
