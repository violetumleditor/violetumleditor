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

package com.horstmann.violet.product.diagram.activity.node;

import java.awt.geom.Point2D;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideEllipse;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.product.diagram.activity.ActivityDiagramConstant;
import com.horstmann.violet.workspace.sidebar.colortools.ColorToolsBarPanel;

/**
 * A final node_old (bull's eye) in an activity diagram.
 */
public class ScenarioEndNode extends AbstractNode
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
    protected ScenarioEndNode copy() throws CloneNotSupportedException
    {
        return new ScenarioEndNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        RelativeLayout relativeLayout = new RelativeLayout();

        EmptyContent insideEmptyContent = new EmptyContent();
        insideEmptyContent.setMinHeight(DIAMETER);
        insideEmptyContent.setMinWidth(DIAMETER);

        ContentInsideShape contentInsideShape = new ContentInsideEllipse(insideEmptyContent, 1);
        ContentBackground contentBackground = new ContentBackground(contentInsideShape, ColorToolsBarPanel.PASTEL_GREY.getBackgroundColor());
        ContentBorder contentBorder = new ContentBorder(contentInsideShape, ColorToolsBarPanel.PASTEL_GREY.getBorderColor());
        relativeLayout.add(contentBackground);
        relativeLayout.add(contentBorder);

        insideEmptyContent = new EmptyContent();
        insideEmptyContent.setMinHeight(DIAMETER - GAP);
        insideEmptyContent.setMinWidth(DIAMETER - GAP);

        contentInsideShape = new ContentInsideEllipse(insideEmptyContent, 1);
        contentBackground = new ContentBackground(contentInsideShape, ColorToolsBarPanel.PASTEL_GREY.getBorderColor());
        relativeLayout.add(contentBackground, new Point2D.Double(GAP / Math.sqrt(2), GAP / Math.sqrt(2)));

        setContent(relativeLayout);
    }

    @Override
    public String getToolTip()
    {
        return ActivityDiagramConstant.ACTIVITY_DIAGRAM_RESOURCE.getString("tooltip.scenario_end_node");
    }

    @Override
    public boolean addConnection(IEdge edge)
    {
        return edge.getEndNode() != null && this != edge.getEndNode();
    }

    /** default node_old diameter */
    private static final int DIAMETER = 12;

    /** default gap between the main circle and the ring for a final node_old */
    private static final int GAP = 4;
}
