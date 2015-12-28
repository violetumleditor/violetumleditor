package com.horstmann.violet.framework.graphics.content;

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
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void draw(Graphics2D g2, Point2D offset) {
        Color oldColor = g2.getColor();
        g2.setColor(color);
        g2.translate(offset.getX(), offset.getY());
        g2.draw(getShape());
        g2.translate(-offset.getX(), -offset.getY());
        g2.setColor(oldColor);
        contentShape.draw(g2, offset);
    }

    protected Shape getShape()
    {
        return contentShape.getShape();
    }

    private Color color;
    private ContentInsideShape contentShape;
}
