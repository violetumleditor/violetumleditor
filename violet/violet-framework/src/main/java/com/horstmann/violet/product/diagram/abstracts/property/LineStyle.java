/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.product.diagram.abstracts.property;

import java.awt.BasicStroke;
import java.awt.Stroke;

import com.horstmann.violet.framework.util.SerializableEnumeration;

/**
 * This class defines line styles of various shapes.
 */
public class LineStyle extends SerializableEnumeration
{
    /**
     * Private constructor. Note that this class is nearly construct as a singleton.
     */
    private LineStyle()
    {
    }

    /**
     * Gets a stroke with which to draw this line style.
     * 
     * @return the stroke object that strokes this line style
     */
    public Stroke getStroke()
    {
        if (this == DOTTED) return getDottedStroke();
        return getSolidStroke();
    }

    /**
     * @return a dotted stroke
     */
    private Stroke getDottedStroke()
    {
        if (dottedStroke == null)
        {
            float[] dash = new float[]
            {
                    3.0f,
                    3.0f
            };
            dottedStroke = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        }
        return dottedStroke;
    }

    /**
     * @return a solid stroke
     */
    private Stroke getSolidStroke()
    {
        if (solidStroke == null)
        {
            solidStroke = new BasicStroke();
        }
        return solidStroke;
    }

    /** a solid stroke */
    private transient Stroke solidStroke;

    /** a dotted stroke */
    private transient Stroke dottedStroke;

    /** The unique solid linestyle instance */
    public static final LineStyle SOLID = new LineStyle();

    /** The unique dotted linestyle instance */
    public static final LineStyle DOTTED = new LineStyle();

}
