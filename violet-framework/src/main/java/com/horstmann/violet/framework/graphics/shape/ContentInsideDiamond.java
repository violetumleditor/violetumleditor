package com.horstmann.violet.framework.graphics.shape;

import com.horstmann.violet.framework.graphics.content.Content;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 27.02.2016
 */
public class ContentInsideDiamond extends ContentInsideCustomShape
{
    public ContentInsideDiamond(Content content)
    {
        this(content, 45);
    }

    /**
     * @param content
     * @param degrees left and right degrees
     */
    public ContentInsideDiamond(Content content, double degrees)
    {
        super(content, new DiamondShape(degrees));
    }

    private static final class DiamondShape implements ContentInsideCustomShape.ShapeCreator
    {
        public DiamondShape(double degrees)
        {
            if(0 == (degrees)%90 && 0 != (degrees)%180)
            {
                throw new ArithmeticException("tangent is infinity");
            }
            tangent = Math.tan(Math.toRadians(degrees));
        }

        /**
         * @param contentWidth  width of diamond
         * @param contentHeight height of diamond
         * @return shape described in the diamond
         */
        @Override
        public Shape createShape(double contentWidth, double contentHeight)
        {
            double width = contentWidth + contentHeight * tangent;
            double height = contentHeight + contentWidth / tangent;

            GeneralPath diamond = new GeneralPath();
            diamond.moveTo(0, height/2);
            diamond.lineTo(width/2, 0);
            diamond.lineTo(width, height/2);
            diamond.lineTo(width/2, height);
            diamond.lineTo(0, height/2);
            return diamond;
        }

        private double tangent;
    }
}
