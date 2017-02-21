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

import java.awt.*;
import java.awt.geom.Point2D;

import com.horstmann.violet.product.diagram.property.choiceList.IconChoiceList;
import com.horstmann.violet.product.diagram.abstracts.edge.linestyle.LineStyle;

import javax.swing.*;

/**
 * This class defines line styles of various shapes.
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 */
public class LineStyleChoiceList extends IconChoiceList<Stroke>
{
    /**
     * Default constructor
     */
    public LineStyleChoiceList()
    {
        super(LINE_STYLES_ICONS, LINE_STYLES);
    }

    /**
     * Copy constructor
     *
     * @param copyElement
     */
    protected LineStyleChoiceList(LineStyleChoiceList copyElement)
    {
        super(copyElement);
    }

    @Override
    public LineStyleChoiceList clone()
    {
        return new LineStyleChoiceList(this);
    }

    public static final Stroke SOLID = LineStyle.SOLID;
    public static final Stroke DOTTED = LineStyle.DOTTED;

    private static Stroke[] LINE_STYLES;
    private static LineStyleIcon[] LINE_STYLES_ICONS;

    static
    {
        LINE_STYLES = new Stroke[]{
                SOLID,
                DOTTED,
        };

        LINE_STYLES_ICONS = new LineStyleIcon[LINE_STYLES.length];
        for (int i = 0; i < LINE_STYLES.length; i++)
        {
            LINE_STYLES_ICONS[i] = new LineStyleIcon(LINE_STYLES[i]);
        }
    }

    private static final class LineStyleIcon implements Icon
    {
        public LineStyleIcon(Stroke lineStyle)
        {
            this.lineStyle = lineStyle;
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
            Stroke oldStroke = graphics.getStroke();

            Point2D startPoint = new Point2D.Double(WIDTH-1, HEIGHT/2 );
            Point2D endPoint = new Point2D.Double(5, HEIGHT/2 );

            graphics.setColor(Color.BLACK);
            graphics.setStroke(lineStyle);

            graphics.drawLine((int)startPoint.getX(),(int)startPoint.getY(), (int)endPoint.getX(),(int)endPoint.getY());

            graphics.setStroke(oldStroke);
            graphics.setColor(oldColor);
        }

        private final Stroke lineStyle;
        private static final int WIDTH = 35;
        private static final int HEIGHT = 20;
    }
}
