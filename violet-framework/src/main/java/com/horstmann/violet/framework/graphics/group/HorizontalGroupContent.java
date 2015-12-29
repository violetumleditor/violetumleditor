package com.horstmann.violet.framework.graphics.group;

import com.horstmann.violet.framework.graphics.content.Content;
import com.horstmann.violet.framework.graphics.content.GroupContent;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

/**
 * Created by Adrian Bobrowski on 21.12.2015.
 */
public class HorizontalGroupContent extends GroupContent {
    @Override
    protected void measureAndSetSize()
    {
        Rectangle2D bounds;
        int tmpHeight;
        width = 0;

        for (Content content: contents) {
            bounds = content.getBounds();
            tmpHeight = (int)bounds.getHeight();
            width += (int)bounds.getWidth();
            if(height<tmpHeight)
            {
                height = tmpHeight;
            }
        }
        setHeight(height);
    }

    @Override
    public void draw(Graphics2D g2, Point2D point) {
        refresh();

        g2.translate(point.getX(), point.getY());

        Content content = null;
        Point2D offset = new Point2D.Double(0,0);
        Iterator<Content> iterator = contents.iterator();

        if(iterator.hasNext())
        {
            content = iterator.next();
            content.draw(g2, offset);
            offset = new Point2D.Double(offset.getX() + content.getBounds().getWidth(),offset.getY());
        }
        while(iterator.hasNext())
        {
            separator.draw(g2, new Point2D.Double(offset.getX(), 0), new Point2D.Double(offset.getX(), height));
            content = iterator.next();
            content.draw(g2, offset);
            point = new Point2D.Double(offset.getX() + content.getBounds().getWidth(),offset.getY());
        }
        g2.translate(-point.getX(), -point.getY());
    }
}
