package com.horstmann.violet.framework.graphics.shape;

import com.horstmann.violet.framework.graphics.content.Content;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by Adrian Bobrowski on 28.12.2015.
 */
public class ContentInsideRoundRectangle extends ContentInsideShape
{
    public ContentInsideRoundRectangle(Content content) {
        this(content, DEFAULT_ARC);
    }
    public ContentInsideRoundRectangle(Content content, double arc) {
        this(content, arc, arc);
    }
    public ContentInsideRoundRectangle(Content content, double arcw, double arch) {
        this.content = content;
        this.arcw = arcw;
        this.arch = arch;
    }

    protected Shape getShape()
    {
        content.refresh();
        Rectangle2D contentBounds = content.getBounds();
        return new RoundRectangle2D.Double(0,0, contentBounds.getWidth() + (arcw-arcw/Math.sqrt(2)), contentBounds.getHeight() + (arch - arch/Math.sqrt(2)), arcw, arch);
    }

    private double arcw;
    private double arch;

    private static final int DEFAULT_ARC = 15;
}
