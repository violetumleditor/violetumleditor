package com.horstmann.violet.framework.graphics.content;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Adrian Bobrowski on 21.12.2015.
 */
public abstract class Content {

    public abstract void draw(Graphics2D g2, Point2D point);
    public void draw(Graphics2D g2) {
        draw(g2, new Point2D.Double(0,0));
    }

    public void refresh(){}

    public final void setMinWidth(int minWidth)
    {
        this.minWidth = minWidth;
        if(width < minWidth)
        {
            setWidth(minWidth);
        }
    }
    public final void setMinHeight(int minHeight)
    {
        this.minHeight = minHeight;
        if(height < minHeight)
        {
            setHeight(minHeight);
        }
    }

    public Rectangle2D getBounds()
    {
        return new Rectangle2D.Double(0,0,width,height);
    }

    protected void setWidth(int width)
    {
        this.width = width;
    }
    protected void setHeight(int height)
    {
        this.height = height;
    }



    protected int minWidth;
    protected int minHeight;
    protected int width;
    protected int height;
}
