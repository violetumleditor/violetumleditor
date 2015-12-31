package com.horstmann.violet.framework.graphics.content;

import com.sun.javafx.geom.Vec4d;
import com.sun.javafx.scene.layout.region.Margins;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by Adrian Bobrowski on 21.12.2015.
 */
public abstract class Content
{
    public abstract void draw(Graphics2D g2);

    public final void draw(Graphics2D g2, Point2D offset)
    {
        g2.translate(offset.getX(), offset.getY());
        draw(g2);
        g2.translate(-offset.getX(), -offset.getY());
    }

    public final Rectangle2D getBounds()
    {
        return bounds;
    }
    public final int getX()
    {
        return (int)getBounds().getX();
    }
    public final int getY()
    {
        return (int)getBounds().getY();
    }
    public final int getWidth()
    {
        return (int)getBounds().getWidth();
    }
    public final int getHeight()
    {
        return (int)getBounds().getHeight();
    }
    public final void setMinWidth(int minWidth){
        if(0<=minWidth) {
            this.minWidth = minWidth;
            refresh();
        }
    }
    public final void setMinHeight(int minHeight){
        if(0<=minHeight) {
            this.minHeight = minHeight;
            refresh();
        }
    }

    public void refresh()
    {
        bounds.setRect(getX(), getY(), Math.max(getWidth(), minWidth), Math.max(getHeight(), minHeight));

        for (Content parent: parents ) {
            parent.refresh();
        }
    }

    protected final void addParent(Content parent)
    {
        if(null == parent)
        {
            throw new NullPointerException("parent can't be null");
        }
        parents.add(parent);
    }

    protected final void removeParent(Content parent)
    {
        if(null == parent)
        {
            throw new NullPointerException("parent can't be null");
        }
        parents.remove(parent);
    }

    protected final void setWidth(int width)
    {
        bounds.setRect(getX(), getY(), Math.max(width, minWidth), getHeight());
    }
    protected final void setHeight(int height)
    {
        bounds.setRect(getX(), getY(), getWidth(), Math.max(height, minHeight));
    }

    private ArrayList<Content> parents = new ArrayList<Content>();

    private Rectangle2D bounds = new Rectangle2D.Double(0,0,0,0);

    private int minWidth = 0;
    private int minHeight = 0;
}
