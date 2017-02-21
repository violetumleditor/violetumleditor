package com.horstmann.violet.framework.graphics.shape;

import com.horstmann.violet.framework.graphics.content.Content;
import com.horstmann.violet.framework.graphics.content.ContentInsideShape;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * This class enters the "Content" in the custom shape
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 28.12.2015
 */
public class ContentInsideCustomShape extends ContentInsideShape
{
    public interface ShapeCreator
    {
        /**
         * @param contentWidth width of rectangle
         * @param contentHeight height of rectangle
         * @return shape described in the rectangle
         */
        Shape createShape(double contentWidth, double contentHeight);
    }

    public ContentInsideCustomShape(Content content, ShapeCreator customShapeCreator)
    {
        if(null == customShapeCreator)
        {
            throw new NullPointerException("Shape creator can't be null");
        }
        this.customShapeCreator = customShapeCreator;
        setContent(content);
    }

    /**
     * @see Content#refreshUp()
     */
    @Override
    public void refreshUp()
    {
        setShape(createCustomShape());
        super.refreshUp();
    }

    /**
     * @see Content#refreshDown()
     */
    @Override
    protected void refreshDown()
    {
        setShape(createCustomShape());
        super.refreshDown();
    }

    /**
     * @return custom shape described on content
     */
    private Shape createCustomShape()
    {
        return customShapeCreator.createShape(getContent().getWidth(), getContent().getHeight());
    }

    /**
     * @see ContentInsideShape#getShapeOffset()
     */
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
