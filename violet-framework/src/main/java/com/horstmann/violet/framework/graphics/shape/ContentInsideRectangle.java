package com.horstmann.violet.framework.graphics.shape;

import com.horstmann.violet.framework.graphics.content.Content;
import com.horstmann.violet.framework.graphics.content.ContentInsideShape;

import java.awt.*;

/**
 * Created by Adrian Bobrowski on 28.12.2015.
 */
public class ContentInsideRectangle extends ContentInsideShape
{
    public ContentInsideRectangle(Content content) {
        setContent(content);
    }

    @Override
    public void refreshUp()
    {
        setShape(createRectangle());
        super.refreshUp();
    }
    @Override
    protected void refreshDown()
    {
        setShape(createRectangle());
        super.refreshDown();
    }

    private Rectangle createRectangle()
    {
        return new Rectangle(getContent().getWidth(), getContent().getHeight());
    }
}
