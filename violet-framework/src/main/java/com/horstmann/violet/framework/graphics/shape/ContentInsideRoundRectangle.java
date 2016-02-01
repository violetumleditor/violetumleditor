package com.horstmann.violet.framework.graphics.shape;

import com.horstmann.violet.framework.graphics.content.Content;
import com.horstmann.violet.framework.graphics.content.ContentInsideShape;

import java.awt.geom.RoundRectangle2D;

/**
 * This class enters the "Content" in the round rectangle
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 28.12.2015
 */
public class ContentInsideRoundRectangle extends ContentInsideShape
{
    public ContentInsideRoundRectangle(Content content)
    {
        this(content, DEFAULT_ARC);
    }
    public ContentInsideRoundRectangle(Content content, double arc)
    {
        this(content, arc, arc);
    }
    public ContentInsideRoundRectangle(Content content, double arcWidth, double arcHeight)
    {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        setContent(content);
    }

    /**
     * @see Content#refreshUp()
     */
    @Override
    public void refreshUp()
    {
        setShape(createRoundRectangle());
        super.refreshUp();
    }

    /**
     * @see Content#refreshDown()
     */
    @Override
    protected void refreshDown()
    {
        setShape(createRoundRectangle());
        super.refreshDown();
    }

    /**
     * @return round rectangle described on content
     */
    private RoundRectangle2D createRoundRectangle()
    {
        return new RoundRectangle2D.Double(0,0, getContent().getWidth() + (arcWidth - arcWidth /Math.sqrt(2)), getContent().getHeight() + (arcHeight - arcHeight /Math.sqrt(2)), arcWidth, arcHeight);
    }

    private double arcWidth;
    private double arcHeight;

    private static final int DEFAULT_ARC = 15;
}
