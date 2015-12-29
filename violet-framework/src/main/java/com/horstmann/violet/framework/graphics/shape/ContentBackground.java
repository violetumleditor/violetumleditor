package com.horstmann.violet.framework.graphics.shape;

import com.horstmann.violet.framework.graphics.shape.ContentInsideShape;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by Adrian Bobrowski on 28.12.2015.
 */
public class ContentBackground extends ContentInsideShape
{
    public ContentBackground(ContentInsideShape contentShape, Color color) {
        this.color = color;
        this.contentShape = contentShape;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void draw(Graphics2D g2, Point2D offset) {
        refresh();
        if(null!=color) {
            Color oldColor = g2.getColor();
            g2.setColor(color);
            g2.translate(offset.getX(), offset.getY());
            g2.fill(getShape());
            g2.translate(-offset.getX(), -offset.getY());
            g2.setColor(oldColor);
        }
        contentShape.draw(g2, offset);
    }

    protected Shape getShape()
    {
        return contentShape.getShape();
    }

    private Color color;
    private ContentInsideShape contentShape;
}
