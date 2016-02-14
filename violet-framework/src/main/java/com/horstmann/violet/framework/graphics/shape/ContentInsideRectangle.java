package com.horstmann.violet.framework.graphics.shape;

import com.horstmann.violet.framework.graphics.content.Content;
import com.horstmann.violet.framework.graphics.content.ContentInsideShape;

import java.awt.geom.Rectangle2D;

/**
 * This class enters the "Content" in the rectangle
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 28.12.2015
 */
public class ContentInsideRectangle extends ContentInsideShape
{
    public ContentInsideRectangle(Content content)
    {
        setContent(content);
    }

    /**
     * @see Content#refreshUp()
     */
    @Override
    public void refreshUp()
    {
        setShape(createRectangle());
        super.refreshUp();
    }

    /**
     * @see Content#refreshDown()
     */
    @Override
    protected void refreshDown()
    {
        setShape(createRectangle());
        super.refreshDown();
    }

    /**
     * @return rectangle described on content
     */
    private Rectangle2D createRectangle()
    {
        return new Rectangle2D.Double(0,0,getContent().getWidth(), getContent().getHeight());
    }
}
