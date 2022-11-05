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

package com.horstmann.violet.product.diagram.activity;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.AbstractGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.activity.edge.ActivityTransitionEdge;
import com.horstmann.violet.product.diagram.activity.node.ActivityNode;
import com.horstmann.violet.product.diagram.activity.node.DecisionNode;
import com.horstmann.violet.product.diagram.activity.node.PageLinkNode;
import com.horstmann.violet.product.diagram.activity.node.ScenarioEndNode;
import com.horstmann.violet.product.diagram.activity.node.ScenarioStartNode;
import com.horstmann.violet.product.diagram.activity.node.SignalReceiptNode;
import com.horstmann.violet.product.diagram.activity.node.SignalSendingNode;
import com.horstmann.violet.product.diagram.activity.node.SynchronizationBarNode;
import com.horstmann.violet.product.diagram.activity.node.WaitTimeActionNode;
import com.horstmann.violet.product.diagram.common.edge.NoteEdge;
import com.horstmann.violet.product.diagram.common.node.NoteNode;
import com.horstmann.violet.product.diagram.property.BentStyleChoiceList;

/**
 * An UML activity diagram.
 */
public class ActivityDiagramGraph extends AbstractGraph
{
    public List<INode> getNodePrototypes()
    {
        return NODE_PROTOTYPES;
    }

    public List<IEdge> getEdgePrototypes()
    {
        return EDGE_PROTOTYPES;
    }

    @Override
    public boolean connect(IEdge e, INode start, Point2D startLocation, INode end, Point2D endLocation)
    {
        if (!ActivityTransitionEdge.class.isInstance(e))
        {
            return super.connect(e, start, startLocation, end, endLocation);
        }
        ActivityTransitionEdge transitionEdge = (ActivityTransitionEdge) e;
        if (DecisionNode.class.isInstance(start))
        {
            boolean isSyncBarAtEnd = SynchronizationBarNode.class.isInstance(end);
            if (isSyncBarAtEnd)
            {
                // For syn bar, we want to connect edge to north or south points
                transitionEdge.getBentStyleChoiceList().setSelectedValue(BentStyleChoiceList.AUTO);
            }
            if (!isSyncBarAtEnd)
            {
                // For all the other cases, decision node_old are connected from east or west
                transitionEdge.getBentStyleChoiceList().setSelectedValue(BentStyleChoiceList.HV);
            }
        }
        return super.connect(e, start, startLocation, end, endLocation);
    }

    private static final List<INode> NODE_PROTOTYPES = new ArrayList<INode>(Arrays.asList(
            new ScenarioStartNode(),
            new ActivityNode(),
            new DecisionNode(),
            new SignalSendingNode(),
            new SignalReceiptNode(),
            new SynchronizationBarNode(),
            new ScenarioEndNode(),
            new WaitTimeActionNode(),
            new PageLinkNode(),
            new NoteNode()
    ));

    private static final List<IEdge> EDGE_PROTOTYPES = new ArrayList<IEdge>(Arrays.asList(
            new ActivityTransitionEdge(),
            new NoteEdge()
    ));
}
