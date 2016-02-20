package com.horstmann.violet.product.diagram.common.edge.arrowhead;

import java.awt.geom.GeneralPath;

/**
 * Array head type : this head is a X
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 20.02.2016
 */
public class XArrowhead extends Arrowhead
{
    public XArrowhead()
    {
        super();
    }

    @Override
    public GeneralPath getPath()
    {
        final double CROSS_ANGLE = Math.PI / 4;

        GeneralPath path = new GeneralPath();
        double x = 0.75 * ARROW_LENGTH * Math.cos(CROSS_ANGLE);
        double y = 0.75 * ARROW_LENGTH * Math.sin(CROSS_ANGLE);

        path.moveTo((float) x, (float) y);
        path.lineTo((float) -x, (float) -y);
        path.moveTo((float) x, (float) -y);
        path.lineTo((float) -x, (float) y);

        return path;
    }
}
