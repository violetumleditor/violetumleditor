package com.horstmann.violet.framework.graphics.shape;

import com.horstmann.violet.framework.graphics.content.Content;
import com.horstmann.violet.framework.graphics.content.ContentInsideShape;

import java.awt.*;

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
    private Rectangle createRectangle()
    {
        return new Rectangle(getContent().getWidth(), getContent().getHeight());
    }
}
