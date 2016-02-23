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
import java.util.List;

import com.horstmann.violet.framework.property.BentStyleChoiceList;
import com.horstmann.violet.product.diagram.abstracts.AbstractGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.activity.edge.ActivityTransitionEdge;
import com.horstmann.violet.product.diagram.activity.node.*;
import com.horstmann.violet.product.diagram.common.edge.NoteEdge;
import com.horstmann.violet.product.diagram.common.node.NoteNode;

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
    public boolean connect(IEdge e, INode start, Point2D startLocation, INode end, Point2D endLocation, Point2D[] transitionPoints)
    {
        if (!ActivityTransitionEdge.class.isInstance(e))
        {
            return super.connect(e, start, startLocation, end, endLocation, transitionPoints);
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
        return super.connect(e, start, startLocation, end, endLocation, transitionPoints);
    }

    private static final List<INode> NODE_PROTOTYPES = new ArrayList<INode>();

    private static final List<IEdge> EDGE_PROTOTYPES = new ArrayList<IEdge>();

    static
    {
        ScenarioStartNode node5 = new ScenarioStartNode();
        node5.setToolTip(ActivityResource.ACTIVITY.getString("node5.tooltip"));
        NODE_PROTOTYPES.add(node5);

        ActivityNode node0 = new ActivityNode();
        node0.setToolTip(ActivityResource.ACTIVITY.getString("node0.tooltip"));
        NODE_PROTOTYPES.add(node0);

        DecisionNode node1 = new DecisionNode();
        node1.setToolTip(ActivityResource.ACTIVITY.getString("node1.tooltip"));
        NODE_PROTOTYPES.add(node1);

        SynchronizationBarNode node2 = new SynchronizationBarNode();
        node2.setToolTip(ActivityResource.ACTIVITY.getString("node2.tooltip"));
        NODE_PROTOTYPES.add(node2);

        SignalSendingNode node3 = new SignalSendingNode();
        node3.setToolTip(ActivityResource.ACTIVITY.getString("node3.tooltip"));
        NODE_PROTOTYPES.add(node3);

        SignalReceiptNode node4 = new SignalReceiptNode();
        node4.setToolTip(ActivityResource.ACTIVITY.getString("node4.tooltip"));
        NODE_PROTOTYPES.add(node4);

        ScenarioEndNode node6 = new ScenarioEndNode();
        node6.setToolTip(ActivityResource.ACTIVITY.getString("node6.tooltip"));
        NODE_PROTOTYPES.add(node6);

        WaitTimeActionNode node8 = new WaitTimeActionNode();
        node8.setToolTip(ActivityResource.ACTIVITY.getString("node8.tooltip"));
        NODE_PROTOTYPES.add(node8);

        PageLinkNode node9 = new PageLinkNode();
        node9.setToolTip(ActivityResource.ACTIVITY.getString("node9.tooltip"));
        NODE_PROTOTYPES.add(node9);

        NoteNode node7 = new NoteNode();
        node7.setToolTip(ActivityResource.ACTIVITY.getString("node7.tooltip"));
        NODE_PROTOTYPES.add(node7);

        ActivityTransitionEdge transition = new ActivityTransitionEdge();
        transition.setToolTip(ActivityResource.ACTIVITY.getString("edge0.tooltip"));
//        transition.setEndArrowHead(ArrowheadChoiceList.V);
//        transition.setStartArrowHead(ArrowheadChoiceList.NONE);
//        transition.setLineStyle(LineStyleChoiceList.SOLID);
        EDGE_PROTOTYPES.add(transition);

        NoteEdge noteEdge = new NoteEdge();
        noteEdge.setToolTip(ActivityResource.ACTIVITY.getString("edge1.tooltip"));
        EDGE_PROTOTYPES.add(noteEdge);
    }
}
