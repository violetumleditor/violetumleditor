package com.horstmann.violet.framework.graphics.content;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Adrian Bobrowski on 28.12.2015.
 */
public abstract class ContentInsideShape extends Content
{
    @Override
    public void draw(Graphics2D g2) {
        content.draw(g2, getShapeOffset());
    }

    @Override
    public boolean contains(Point2D p)
    {
        return getShape().contains(p);
    }

    @Override
    protected void refreshUp()
    {
        Rectangle2D shapeBounds = getShape().getBounds();

        setWidth((int)shapeBounds.getWidth());
        setHeight((int)shapeBounds.getHeight());

        super.refreshUp();
    }
    @Override
    protected void refreshDown()
    {
        content.setWidth(getWidth());
        content.setHeight(getHeight());
        content.refreshDown();
        super.refreshDown();
    }

    protected Shape getShape()
    {
        return shape;
    }
    protected final void setShape(Shape shape)
    {
        if(null == shape)
        {
            throw new NullPointerException("Shape can't be null");
        }
        this.shape = shape;
    }

    protected final Content getContent()
    {
        return content;
    }
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
