package com.horstmann.violet.framework.graphics.content;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 * This class fills in the background shape of the color
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 28.12.2015
 */
public class ContentBackground extends ContentInsideShape
{
    public ContentBackground(ContentInsideShape contentShape, Color color)
    {
        this.color = color;
        this.setContent(contentShape);
    }

    /**
     * @return background color
     */
    public final Color getBackgroundColor()
    {
        return color;
    }

    /**
     * sets the color, which want to filled shape
     * @param color
     */
    public final void setBackgroundColor(Color color)
    {
        this.color = color;
    }

    /**
     * @see Content#draw(Graphics2D)
     */
    @Override
    public void draw(Graphics2D graphics) {
        if(null!=color) {
            Color oldColor = graphics.getColor();
            graphics.setColor(color);
            graphics.fill(getShape());
            graphics.setColor(oldColor);
        }
        getContent().draw(graphics);
    }

    /**
     * @see ContentInsideShape#getShape()
     */
    protected Shape getShape()
    {
        return ((ContentInsideShape)getContent()).getShape();
    }

    private Color color;
}
