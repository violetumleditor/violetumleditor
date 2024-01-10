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

package com.horstmann.violet.product.diagram.property;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import javax.swing.Icon;

import com.horstmann.violet.product.diagram.abstracts.edge.arrowhead.Arrowhead;
import com.horstmann.violet.product.diagram.abstracts.edge.arrowhead.DiamondArrowhead;
import com.horstmann.violet.product.diagram.abstracts.edge.arrowhead.TriangleArrowhead;
import com.horstmann.violet.product.diagram.abstracts.edge.arrowhead.VArrowhead;
import com.horstmann.violet.product.diagram.abstracts.edge.arrowhead.XArrowhead;
import com.horstmann.violet.product.diagram.property.choiceList.IconChoiceList;

/**
 * This class defines arrow heads of various shapes.
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 */
public class ArrowheadChoiceList extends IconChoiceList<Arrowhead>
{
    /**
     * Default constructor
     */
    public ArrowheadChoiceList()
    {
        super(MODEL.clone());
        
    }

    /**
     * Copy constructor
     *
     * @param copyElement
     */
    protected ArrowheadChoiceList(ArrowheadChoiceList copyElement)
    {
        super(copyElement);
    }

    private ArrowheadChoiceList(ArrowheadIcon[] keys, Arrowhead[] values)
    {
        super(keys, values);
    }
    
    @Override
    public ArrowheadChoiceList clone()
    {
    	Arrowhead[] ARROWHEADS_CLONE = new Arrowhead[]{
                NONE.clone(),
                V.clone(),
                TRIANGLE_WHITE.clone(),
                TRIANGLE_BLACK.clone(),
                DIAMOND_WHITE.clone(),
                DIAMOND_BLACK.clone(),
                X.clone(),
        };
    	ArrowheadChoiceList clone = new ArrowheadChoiceList(ARROWHEAD_ICONS, ARROWHEADS_CLONE);
    	clone.setSelectedValue(getSelectedValue());
		return clone;
    }
    


    public static final Arrowhead NONE= new Arrowhead("NONE");
    public static final Arrowhead V = new VArrowhead();
    public static final Arrowhead TRIANGLE_WHITE = new TriangleArrowhead("Triangle White", Color.white);
    public static final Arrowhead TRIANGLE_BLACK = new TriangleArrowhead("Triangle Black", Color.black);
    public static final Arrowhead DIAMOND_WHITE = new DiamondArrowhead("Diamond White", Color.white);
    public static final Arrowhead DIAMOND_BLACK = new DiamondArrowhead("Diamond Black", Color.black);
    public static final Arrowhead X = new XArrowhead();

    private static Arrowhead[] ARROWHEADS;
    private static ArrowheadIcon[] ARROWHEAD_ICONS;
    private static ArrowheadChoiceList MODEL;

    static
    {
        ARROWHEADS = new Arrowhead[]{
                NONE,
                V,
                TRIANGLE_WHITE,
                TRIANGLE_BLACK,
                DIAMOND_WHITE,
                DIAMOND_BLACK,
                X,
        };

        ARROWHEAD_ICONS = new ArrowheadIcon[ARROWHEADS.length];
        for (int i = 0; i < ARROWHEADS.length; i++)
        {
            ARROWHEAD_ICONS[i] = new ArrowheadIcon(ARROWHEADS[i]);
        }
        MODEL = new ArrowheadChoiceList(ARROWHEAD_ICONS, ARROWHEADS);
    }

    private static final class ArrowheadIcon implements Icon
    {
        public ArrowheadIcon(Arrowhead arrowhead)
        {
            this.arrowhead = arrowhead;
        }

        @Override
        public int getIconWidth()
        {
            return WIDTH;
        }

        @Override
        public int getIconHeight()
        {
            return HEIGHT;
        }

        @Override
        public void paintIcon(Component component, Graphics g, int x, int y)
        {
            Graphics2D graphics = (Graphics2D)g;
            Color oldColor = graphics.getColor();

            Rectangle arrowheadBounds = arrowhead.getPath().getBounds();
            Point2D startPoint = new Point2D.Double(WIDTH-1, HEIGHT/2 );
            Point2D endPoint = new Point2D.Double(5-arrowheadBounds.getX(), HEIGHT/2 );

            graphics.setColor(Color.BLACK);

            graphics.drawLine((int)startPoint.getX(),(int)startPoint.getY(), (int)endPoint.getX(),(int)endPoint.getY());

            arrowhead.draw(graphics, startPoint, endPoint);
            graphics.setColor(oldColor);
        }

        private final Arrowhead arrowhead;
        private static final int WIDTH = 35;
        private static final int HEIGHT = 20;
    }
}
