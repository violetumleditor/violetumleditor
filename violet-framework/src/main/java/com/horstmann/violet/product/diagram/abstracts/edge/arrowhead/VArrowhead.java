package com.horstmann.violet.product.diagram.abstracts.edge.arrowhead;

import java.awt.geom.GeneralPath;

/**
 * Array head type : this head is a V
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 20.02.2016
 */
public class VArrowhead extends Arrowhead
{
    public VArrowhead()
    {
        super();
    }

    @Override
    public GeneralPath getPath()
    {
        GeneralPath path = new GeneralPath();
        double x = ARROW_LENGTH * Math.cos(ARROW_ANGLE);
        double y = ARROW_LENGTH * Math.sin(ARROW_ANGLE);

        path.moveTo((float) x, (float) y);
        path.lineTo((float) 0, (float) 0);
        path.lineTo((float) x, (float) -y);

        return path;
    }
    
    @Override
    public Arrowhead clone() {
    	return new VArrowhead();
    }
}
