package com.horstmann.violet.product.diagram.abstracts.edge.arrowhead;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.io.ObjectInputStream.GetField;

/**
 * Array head type : this head is a diamond
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 20.02.2016
 */
public class DiamondArrowhead extends Arrowhead
{
    public DiamondArrowhead(Color filledColor)
    {
        super(filledColor);
    }

    @Override
    public GeneralPath getPath()
    {
        GeneralPath path = new GeneralPath();
        double x = ARROW_LENGTH * Math.cos(ARROW_ANGLE);
        double y = ARROW_LENGTH * Math.sin(ARROW_ANGLE);

        path.moveTo((float) 0, (float) 0);
        path.lineTo((float) x, (float) y);
        path.lineTo((float) 2*x, (float) 0);
        path.lineTo((float) x, (float) -y);
        path.lineTo((float) 0, (float) 0);

        return path;
    }
    
	@Override
	public Arrowhead clone() {
		return new DiamondArrowhead(getFilledColor());
	}
}
