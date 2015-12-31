package com.horstmann.violet.framework.graphics.content;

import com.horstmann.violet.framework.graphics.content.ContentInsideShape;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by Adrian Bobrowski on 28.12.2015.
 */
public class ContentBorder extends ContentInsideShape
{
    public ContentBorder(ContentInsideShape contentShape, Color color) {
        this.color = color;
        this.contentShape = contentShape;

        this.setContent(contentShape);
    }

    public final Color getColor()
    {
        return color;
    }
    public final void setColor(Color color)
    {
        this.color = color;
    }

    @Override
    public void draw(Graphics2D g2) {
        contentShape.draw(g2);
        if(null!=color) {
            Color oldColor = g2.getColor();
            g2.setColor(color);
            g2.draw(getShape());
            g2.setColor(oldColor);
        }
    }

    protected Shape getShape()
    {
        return contentShape.getShape();
    }

    private Color color;
    private ContentInsideShape contentShape;
}
