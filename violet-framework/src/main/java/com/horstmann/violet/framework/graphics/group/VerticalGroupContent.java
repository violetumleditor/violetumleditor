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
public class VerticalGroupContent extends GroupContent {
    @Override
    protected void measureAndSetSize() {
        Rectangle2D bounds;
        int tmpWidth;
        height = 0;

        for (Content content: contents) {
            bounds = content.getBounds();
            tmpWidth = (int)bounds.getWidth();
            height += (int)bounds.getHeight();
            if(width<tmpWidth)
            {
                width = tmpWidth;
            }
        }
        setWidth(width);
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
            offset = new Point2D.Double(offset.getX(),offset.getY() + content.getBounds().getHeight());
        }
        while(iterator.hasNext())
        {
            separator.draw(g2, new Point2D.Double(0, offset.getY()), new Point2D.Double(width, offset.getY()));
            content = iterator.next();
            content.draw(g2, offset);
            offset = new Point2D.Double(offset.getX(),offset.getY() + content.getBounds().getHeight());
        }
        g2.translate(-point.getX(), -point.getY());
    }
}
