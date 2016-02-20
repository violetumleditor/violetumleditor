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

package com.horstmann.violet.framework.property;

import java.awt.*;
import java.awt.geom.Point2D;

import com.horstmann.violet.framework.property.choiceList.IconChoiceList;
import com.horstmann.violet.product.diagram.abstracts.edge.arrowhead.*;

import javax.swing.*;

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
        super(ARROWHEAD_ICONS, ARROWHEADS);
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

    @Override
    public ArrowheadChoiceList clone()
    {
        return new ArrowheadChoiceList(this);
    }

    public static final Arrowhead NONE= new Arrowhead();
    public static final Arrowhead V = new VArrowhead();
    public static final Arrowhead TRIANGLE_WHITE = new TriangleArrowhead(Color.white);
    public static final Arrowhead TRIANGLE_BLACK = new TriangleArrowhead(Color.black);
    public static final Arrowhead DIAMOND_WHITE = new DiamondArrowhead(Color.white);
    public static final Arrowhead DIAMOND_BLACK = new DiamondArrowhead(Color.black);
    public static final Arrowhead X = new XArrowhead();

    private static Arrowhead[] ARROWHEADS;
    private static ArrowheadIcon[] ARROWHEAD_ICONS;

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
