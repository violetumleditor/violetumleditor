package com.horstmann.violet.framework.graphics.shape;

import com.horstmann.violet.framework.graphics.content.Content;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by Adrian Bobrowski on 28.12.2015.
 */
public class ContentInsideRectangle extends ContentInsideShape
{
    public ContentInsideRectangle(Content content) {
        this.content = content;
    }

    protected Shape getShape()
    {
        Rectangle2D contentBounds = content.getBounds();
        return new Rectangle((int)contentBounds.getWidth(), (int) contentBounds.getHeight());
    }
}
