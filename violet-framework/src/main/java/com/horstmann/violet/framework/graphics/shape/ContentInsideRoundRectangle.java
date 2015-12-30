package com.horstmann.violet.framework.graphics.shape;

import com.horstmann.violet.framework.graphics.content.Content;
import com.horstmann.violet.framework.graphics.content.ContentInsideShape;

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
        this.arcw = arcw;
        this.arch = arch;
        setContent(content);
    }

    @Override
    public void refresh()
    {
        setShape(new RoundRectangle2D.Double(0,0, getContent().getWidth() + (arcw-arcw/Math.sqrt(2)), getContent().getHeight() + (arch - arch/Math.sqrt(2)), arcw, arch));
        super.refresh();
    }

    private double arcw;
    private double arch;

    private static final int DEFAULT_ARC = 15;
}
