package com.horstmann.violet.framework.graphics.shape;

import com.horstmann.violet.framework.graphics.content.Content;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by Adrian Bobrowski on 28.12.2015.
 */
public class ContentInsideEllipse extends ContentInsideShape
{
    public ContentInsideEllipse(Content content, int width, int height) {
        this(content, (double)width/height);
    }
    public ContentInsideEllipse(Content content) {
        this(content, (double)DEFAULT_WIDTH/DEFAULT_HEIGHT);
    }
    public ContentInsideEllipse(Content content, double aspectRatio) {
        this.content = content;
        this.aspectRatio = aspectRatio;
    }

    protected Shape getShape()
    {
        content.refresh();
        Rectangle2D contentBounds = content.getBounds();
        double width = Math.sqrt(contentBounds.getWidth() * contentBounds.getWidth() + aspectRatio * aspectRatio * contentBounds.getHeight() * contentBounds.getHeight());
        double height = width / aspectRatio;
        return new Ellipse2D.Double(0,0,(int) width, (int) height);
    }

    private double aspectRatio;

    private static final int DEFAULT_WIDTH = 110;
    private static final int DEFAULT_HEIGHT = 40;
}
