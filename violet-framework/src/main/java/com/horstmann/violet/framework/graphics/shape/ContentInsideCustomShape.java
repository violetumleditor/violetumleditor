package com.horstmann.violet.framework.graphics.shape;

import com.horstmann.violet.framework.graphics.content.Content;
import com.horstmann.violet.framework.graphics.content.ContentInsideShape;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Adrian Bobrowski on 28.12.2015.
 */
public class ContentInsideCustomShape extends ContentInsideShape
{
    public interface ShapeCreator
    {
        Shape createShape(int contentWidth, int contentHeight);
    }

    public ContentInsideCustomShape(Content content, ShapeCreator customShapeCreator) {
        if(null == customShapeCreator)
        {
            throw new NullPointerException("Shape creator can't be null");
        }
        this.customShapeCreator = customShapeCreator;
        setContent(content);
    }

    @Override
    public void refresh()
    {
        setShape(customShapeCreator.createShape(getContent().getWidth(), getContent().getHeight()));
        super.refresh();
    }

    protected Point2D getShapeOffset()
    {
        Rectangle2D shapeBounds = getShape().getBounds();

        return new Point2D.Double(
            (shapeBounds.getWidth() - getContent().getWidth()) / 2,
            (shapeBounds.getHeight() - getContent().getHeight()) / 2
        );
    }

    private ShapeCreator customShapeCreator;
}
