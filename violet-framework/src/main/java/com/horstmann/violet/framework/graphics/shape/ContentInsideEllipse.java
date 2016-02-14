package com.horstmann.violet.framework.graphics.shape;

import com.horstmann.violet.framework.graphics.content.Content;
import com.horstmann.violet.framework.graphics.content.ContentInsideShape;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * This class enters the "Content" in the ellipse
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 28.12.2015
 */
public class ContentInsideEllipse extends ContentInsideShape
{
    public ContentInsideEllipse(Content content, double width, double height)
    {
        this(content, width/height);
    }
    public ContentInsideEllipse(Content content)
    {
        this(content, (double)DEFAULT_WIDTH/DEFAULT_HEIGHT);
    }
    public ContentInsideEllipse(Content content, double aspectRatio)
    {
        this.aspectRatio = aspectRatio;
        setContent(content);
    }

    /**
     * @see Content#refreshUp()
     */
    @Override
    public void refreshUp()
    {
        setShape(createEllipse());
        super.refreshUp();
    }

    /**
     * @see Content#refreshDown()
     */
    @Override
    protected void refreshDown()
    {
        setShape(createEllipse());
        super.refreshDown();
    }

    /**
     * @return ellipse described on content
     */
    private Ellipse2D createEllipse()
    {
        Rectangle2D contentBounds = getContent().getBounds();
        double width = Math.sqrt(contentBounds.getWidth() * contentBounds.getWidth() + aspectRatio * aspectRatio * contentBounds.getHeight() * contentBounds.getHeight());
        double height = width / aspectRatio;

        return new Ellipse2D.Double(0,0,width, height);
    }

    private double aspectRatio;

    private static final int DEFAULT_WIDTH = 110;
    private static final int DEFAULT_HEIGHT = 40;
}
