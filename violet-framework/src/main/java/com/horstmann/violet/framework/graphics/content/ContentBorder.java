package com.horstmann.violet.framework.graphics.content;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 * This class is surrounded by the shape of the color
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 28.12.2015
 */
public class ContentBorder extends ContentInsideShape
{
    public ContentBorder(ContentInsideShape contentShape, Color color)
    {
        contentShape.addParent(this);
        this.setBorderColor(color);
        this.setContent(contentShape);
    }

    /**
     * @return border color
     */
    public final Color getBorderColor()
    {
        return color;
    }

    /**
     * sets the color, which want to surrounded by shape
     * @param color
     */
    public final void setBorderColor(Color color)
    {
        this.color = color;
    }

    /**
     * @see Content#draw(Graphics2D)
     */
    @Override
    public void draw(Graphics2D graphics)
    {
        if(null!=color)
        {
            Color oldColor = graphics.getColor();
            graphics.setColor(color);
            graphics.draw(getShape());
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
