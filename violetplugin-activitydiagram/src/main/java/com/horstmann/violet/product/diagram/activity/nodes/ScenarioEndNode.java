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

package com.horstmann.violet.product.diagram.activity.nodes;

import java.awt.*;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideEllipse;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.sidebar.colortools.ColorToolsBarPanel;

/**
 * A final node_old (bull's eye) in an activity diagram.
 */
public class ScenarioEndNode extends ColorableNode
{
    public ScenarioEndNode()
    {
        super();
        createContentStructure();
    }

    protected ScenarioEndNode(ScenarioEndNode node) throws CloneNotSupportedException
    {
        super(node);
        createContentStructure();
    }

    @Override
    protected INode copy() throws CloneNotSupportedException {
        return new ScenarioEndNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        EmptyContent outsideEmptyContent = new EmptyContent();
        outsideEmptyContent.setMinHeight(DEFAULT_DIAMETER);
        outsideEmptyContent.setMinWidth(DEFAULT_DIAMETER);
        ContentInsideShape contentOutsideShape = new ContentInsideEllipse(outsideEmptyContent, 1);
        setBackground(new ContentBackground(contentOutsideShape, getBackgroundColor()));
        setBorder(new ContentBorder(getBackground(), getBorderColor()));

        setBackgroundColor(ColorToolsBarPanel.PASTEL_GREY.getBackgroundColor());
        setBorderColor(ColorToolsBarPanel.PASTEL_GREY.getBorderColor());

        EmptyContent insideEmptyContent = new EmptyContent();
        insideEmptyContent.setMinHeight(DEFAULT_DIAMETER - 2*DEFAULT_GAP);
        insideEmptyContent.setMinWidth(DEFAULT_DIAMETER - 2*DEFAULT_GAP);
        ContentInsideShape contentInsideShape = new ContentInsideEllipse(insideEmptyContent, 1);

        RelativeGroupContent relativeGroupContent = new RelativeGroupContent();
        relativeGroupContent.add(getBorder(), new Point(0,0));
        relativeGroupContent.add(new ContentBackground(contentInsideShape, getBorderColor()), new Point.Double(DEFAULT_GAP*Math.sqrt(2),DEFAULT_GAP*Math.sqrt(2)));

        setContent(relativeGroupContent);
    }


    @Override
    public boolean addConnection(IEdge e)
    {
        return e.getEnd() != null && this != e.getEnd();
    }


    /** default node_old diameter */
    private static int DEFAULT_DIAMETER = 14;

    /** default gap between the main circle and the ring for a final node_old */
    private static int DEFAULT_GAP = 2;
}
