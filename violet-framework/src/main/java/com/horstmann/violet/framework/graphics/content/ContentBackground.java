package com.horstmann.violet.framework.graphics.content;

import com.horstmann.violet.framework.graphics.content.ContentInsideShape;

import java.awt.*;
import java.awt.List;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 28.12.2015
 */
public class ContentBackground extends ContentInsideShape
{
    public ContentBackground(ContentInsideShape contentShape, Color color)
    {
        this.color = color;
        this.setContent(contentShape);
    }

    public final Color getBackgroundColor()
    {
        return color;
    }
    public final void setBackgroundColor(Color color)
    {
        this.color = color;
    }

    @Override
    public void draw(Graphics2D g2) {
        if(null!=color) {
            Color oldColor = g2.getColor();
            g2.setColor(color);
            g2.fill(getShape());
            g2.setColor(oldColor);
        }
        getContent().draw(g2);
    }

    protected Shape getShape()
    {
        return ((ContentInsideShape)getContent()).getShape();
    }

    private Color color;
}
