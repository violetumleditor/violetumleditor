package com.horstmann.violet.framework.graphics.content;

import com.horstmann.violet.framework.property.string.LineText;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * This class defines the dimensions and manner of drawing element with text
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 21.12.2015
 */
public class TextContent extends Content implements LineText.ChangeListener
{
    /**
     * @param text
     */
    public TextContent(LineText text)
    {
        this.text = text;
        this.text.addChangeListener(this);
    }

    /**
     * @see LineText.ChangeListener#onChange()
     */
    @Override
    public void onChange()
    {
        refresh();
    }

    /**
     * @see Content#draw(Graphics2D)
     */
    @Override
    public void draw(Graphics2D graphics)
    {
        text.draw(graphics, getBounds());
    }

    /**
     * @see Content#refreshUp()
     */
    @Override
    protected void refreshUp()
    {
        setOptimalSize();
        super.refreshUp();
    }

    /**
     * @see Content#refreshDown()
     */
    @Override
    protected void refreshDown()
    {
        setOptimalSize();
        super.refreshDown();
    }

    /**
     * @return minimal bounds of this element
     */
    public Rectangle2D getMinimalBounds()
    {
        Rectangle2D textMinimalBounds = text.getBounds();
        Rectangle2D contentMinimalBounds = super.getMinimalBounds();
        return new Rectangle2D.Double(
                contentMinimalBounds.getX(),
                contentMinimalBounds.getY(),
                Math.max(contentMinimalBounds.getWidth(), textMinimalBounds.getWidth()),
                Math.max(contentMinimalBounds.getHeight(), textMinimalBounds.getHeight())
        );
    }

    /**
     * Sets the optimal size adapted to the contained text
     */
    private void setOptimalSize()
    {
        Rectangle2D minimalBounds = getMinimalBounds();
        setWidth(minimalBounds.getWidth());
        setHeight(minimalBounds.getHeight());
    }

    private LineText text;
}
