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

package com.horstmann.violet.product.diagram.state;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.content.RelativeGroupContent;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.framework.graphics.shape.ContentInsideEllipse;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.sidebar.colortools.ColorToolsBarPanel;

/**
 * An initial or final node_old (bull's eye) in a state or activity diagram.
 */
public class CircularFinalStateNode extends AbstractNode
{
    public CircularFinalStateNode()
    {
        super();
        createContentStructure();
    }

    protected CircularFinalStateNode(CircularFinalStateNode node) throws CloneNotSupportedException
    {
        super(node);
        createContentStructure();
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new CircularFinalStateNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        EmptyContent insideEmptyContent = new EmptyContent();
        insideEmptyContent.setMinHeight(DEFAULT_DIAMETER);
        insideEmptyContent.setMinWidth(DEFAULT_DIAMETER);

        ContentInsideShape contentInsideShape = new ContentInsideEllipse(insideEmptyContent, 1);
        ContentBackground contentBackground = new ContentBackground(contentInsideShape, ColorToolsBarPanel.PASTEL_GREY.getBorderColor());

        ContentInsideShape contentOutsideShape = new ContentInsideCustomShape(contentBackground, new ContentInsideCustomShape.ShapeCreator() {
            @Override
            public Shape createShape(int contentWidth, int contentHeight) {
                return new Ellipse2D.Double(0,0,contentWidth+DEFAULT_GAP, contentHeight+DEFAULT_GAP);
            }
        });

        contentBackground = new ContentBackground(contentOutsideShape, ColorToolsBarPanel.PASTEL_GREY.getBackgroundColor());
        ContentBorder contentBorder = new ContentBorder(contentBackground, ColorToolsBarPanel.PASTEL_GREY.getBorderColor());

        setContent(contentBorder);
    }

    /** default node_old diameter */
    private static int DEFAULT_DIAMETER = 12;

    /** default gap between the main circle and the ring for a final node_old */
    private static int DEFAULT_GAP = 6;

}
