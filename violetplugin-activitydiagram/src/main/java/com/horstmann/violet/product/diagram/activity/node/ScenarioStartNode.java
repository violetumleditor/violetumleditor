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

import com.horstmann.violet.framework.graphics.content.ContentBackground;
import com.horstmann.violet.framework.graphics.content.ContentInsideShape;
import com.horstmann.violet.framework.graphics.content.EmptyContent;
import com.horstmann.violet.framework.graphics.shape.ContentInsideEllipse;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.activity.ActivityDiagramConstant;
import com.horstmann.violet.workspace.sidebar.colortools.ColorToolsBarPanel;

/**
 * An initial node_old (bull's eye) in an activity diagram.
 */
public class ScenarioStartNode extends AbstractNode
{
    public ScenarioStartNode()
    {
        super();
        setToolTip(ActivityDiagramConstant.ACTIVITY_DIAGRAM.getString("scenario_start_node.tooltip"));
        createContentStructure();
    }

    protected ScenarioStartNode(ScenarioStartNode node) throws CloneNotSupportedException
    {
        super(node);
        createContentStructure();
    }

    @Override
    protected ScenarioStartNode copy() throws CloneNotSupportedException
    {
        return new ScenarioStartNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        EmptyContent emptyContent = new EmptyContent();
        emptyContent.setMinHeight(DIAMETER);
        emptyContent.setMinWidth(DIAMETER);

        ContentInsideShape contentInsideShape = new ContentInsideEllipse(emptyContent, 1);

        ContentBackground contentBackground = new ContentBackground(contentInsideShape, ColorToolsBarPanel.PASTEL_GREY.getBorderColor());
        setContent(contentBackground);
    }
    
    @Override
    public boolean addConnection(IEdge e)
    {
        return e.getEndNode() != null && this != e.getEndNode();
    }

    /** default node_old diameter */
    private static int DIAMETER = 12;
}
