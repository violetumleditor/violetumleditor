package com.horstmann.violet.framework.graphics.content;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * This class enters the "Content" in the shape
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 28.12.2015
 */
public abstract class ContentInsideShape extends Content
{
    /**
     * @see Content#draw(Graphics2D)
     */
    @Override
    public void draw(Graphics2D graphics)
    {
        content.draw(graphics, getShapeOffset());
    }

    /**
     * Checks whether a point contained in the shape
     * @param point
     * @return true if the point contains in shape otherwise false
     */
    @Override
    public boolean contains(Point2D point)
    {
        return getShape().contains(point);
    }

    /**
     * @see Content#refreshUp()
     */
    @Override
    protected void refreshUp()
    {
        Rectangle2D shapeBounds = getShape().getBounds();

        setWidth((int)shapeBounds.getWidth());
        setHeight((int)shapeBounds.getHeight());

        super.refreshUp();
    }

    /**
     * @see Content#refreshDown()
     */
    @Override
    protected void refreshDown()
    {
        //TODO rethink if you really need to set the height and width
        content.setWidth(getWidth());
        content.setHeight(getHeight());
        content.refreshDown();
        super.refreshDown();
    }

    /**
     * @return shape described on content
     */
    protected Shape getShape()
    {
        return shape;
    }

    /**
     * set shape described on content
     * @param shape
     */
    protected final void setShape(Shape shape)
    {
        if(null == shape)
        {
            throw new NullPointerException("Shape can't be null");
        }
        this.shape = shape;
    }

    /**
     * @return content
     */
    protected final Content getContent()
    {
        return content;
    }

    /**
     * set new content for shape
     * @param content
     */
    protected final void setContent(Content content)
    {
        if(null != this.content)
        {
            this.content.removeParent(this);
        }
        if(null == content)
        {
            content = new EmptyContent();
        }

        content.addParent(this);
        this.content = content;
        refreshUp();
    }

    /**
     * Calculates the offset of content to make it in the center of the shape
     * @return offset
     */
    protected Point2D getShapeOffset()
    {
        Rectangle2D shapeBounds = getShape().getBounds();

        return new Point2D.Double(
            (shapeBounds.getWidth() - content.getWidth()) / 2,
            (shapeBounds.getHeight() - content.getHeight()) / 2
        );
    }

    private Content content;
    private Shape shape;
}
