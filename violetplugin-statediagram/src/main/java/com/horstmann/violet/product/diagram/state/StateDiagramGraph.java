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

import java.util.ArrayList;
import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.AbstractGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.AbstractEdge;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.common.NoteEdge;
import com.horstmann.violet.product.diagram.common.NoteNode;

/**
 * An UML state diagram.
 */
public class StateDiagramGraph extends AbstractGraph
{
    public List<INode> getNodePrototypes()
    {
        return NODE_PROTOTYPES;
    }

    public List<IEdge> getEdgePrototypes()
    {
        return EDGE_PROTOTYPES;
    }

    private static final List<INode> NODE_PROTOTYPES = new ArrayList<INode>();

    private static final List<IEdge> EDGE_PROTOTYPES = new ArrayList<IEdge>();

    static
    {
        CircularInitialStateNode circularInitialStateNode = new CircularInitialStateNode();
        circularInitialStateNode.setResourcePrefix(("node1"));
        NODE_PROTOTYPES.add(circularInitialStateNode);
        
        StateNode stateNode = new StateNode();
        stateNode.setResourcePrefix(("node0"));
        NODE_PROTOTYPES.add(stateNode);
        
        CircularFinalStateNode circularFinalStateNode = new CircularFinalStateNode();
        circularFinalStateNode.setResourcePrefix(("node2"));
        NODE_PROTOTYPES.add(circularFinalStateNode);
        
        NoteNode noteNode = new NoteNode();
        noteNode.setResourcePrefix(("node3"));
        NODE_PROTOTYPES.add(noteNode);

        StateTransitionEdge stateTransitionEdge = new StateTransitionEdge();
        stateTransitionEdge.setResourcePrefix(("edge0"));
        EDGE_PROTOTYPES.add(stateTransitionEdge);
        
        NoteEdge noteEdge = new NoteEdge();
        noteEdge.setResourcePrefix(("edge1"));
        EDGE_PROTOTYPES.add(noteEdge);

        for (IEdge proto : EDGE_PROTOTYPES) {
        	((AbstractEdge)proto).setResourceBundleName(StateDiagramConstant.STATE_DIAGRAM_STRINGS);
        }
        for (INode proto : NODE_PROTOTYPES) {
        	((AbstractNode)proto).setResourceBundleName(StateDiagramConstant.STATE_DIAGRAM_STRINGS);
        }
    }

}
