package com.horstmann.violet.product.diagram.abstracts.edge.arrowhead;

import java.awt.*;
import java.awt.geom.GeneralPath;

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
		
		//White is a special case (aggregate) - should not be colorable
		mIsFilled = !(filledColor.getRGB() == Color.white.getRGB());
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

	//This setting is ignored for a diamond head.
	@Override
    public void setFilledColor(Color filledColor) {
		//Should only be able to set the fill color on a composition
		//Not on an aggregate
		
		//Disable for now - Compositions should have a default 
		//Fill color of black, not gray
		//if (mIsFilled) super.setFilledColor(filledColor);
	}
	
	private final boolean mIsFilled;
}
