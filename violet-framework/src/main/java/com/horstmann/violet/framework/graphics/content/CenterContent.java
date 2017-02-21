package com.horstmann.violet.framework.graphics.content;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 19.02.2016
 */
public class CenterContent extends Content
{
    /**
     * Default constructor
     * @param content
     * @throws NullPointerException
     */
    public CenterContent(Content content)
    {
        if(null == content)
        {
            throw new NullPointerException("content can't be null");
        }

        content.addParent(this);
        this.content = content;
        content.refresh();
//        updateOffsetPoint();
    }

    /**
     * @see Content#refreshDown()
     */
    @Override
    protected void refreshDown()
    {
        updateOffsetPoint();
        super.refreshDown();
        content.refreshDown();
    }

    @Override
    protected void refreshUp()
    {
        Rectangle2D minimalBounds = getMinimalBounds();

        setWidth(minimalBounds.getWidth());
        setHeight(minimalBounds.getHeight());

        super.refreshUp();
    }

    /**
     * @return minimal bounds of this element
     */
    @Override
    public Rectangle2D getMinimalBounds()
    {
        return content.getMinimalBounds();
    }

    /**
     * Defines how to draw element
     *
     * @param graphics
     */
    @Override
    public void draw(Graphics2D graphics)
    {
        content.draw(graphics, offset);
    }

    private void updateOffsetPoint()
    {
        Rectangle2D selfBounds = getBounds();
        Rectangle2D contentBounds = content.getBounds();

        offset = new Point2D.Double(
                selfBounds.getX() + (selfBounds.getWidth() - contentBounds.getWidth())/2,
                selfBounds.getY() + (selfBounds.getHeight() - contentBounds.getHeight())/2
        );
    }

    private Point2D offset;
    private Content content;
}
