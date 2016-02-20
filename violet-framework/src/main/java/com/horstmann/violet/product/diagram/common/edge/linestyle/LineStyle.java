package com.horstmann.violet.product.diagram.common.edge.linestyle;

import java.awt.*;

/**
 * This class defines line styles.
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 20.02.2016
 */
public class LineStyle
{
    /**
     * Solid stroke
     */
    public static final Stroke SOLID = new BasicStroke();

    /**
     * Dotted stroke
     */
    public static final Stroke DOTTED = new BasicStroke(
            1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{ 3.0f,3.0f }, 0.0f
    );
}
