package com.horstmann.violet.framework.graphics.shape;

import com.horstmann.violet.framework.graphics.content.Content;
import com.horstmann.violet.framework.graphics.content.ContentInsideShape;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by Adrian Bobrowski on 28.12.2015.
 */
public class ContentInsideRectangle extends ContentInsideShape
{
    public ContentInsideRectangle(Content content) {
        setContent(content);
    }

    @Override
    public void refresh()
    {
        setShape(new Rectangle(getContent().getWidth(), getContent().getHeight()));
        super.refresh();
    }
}
