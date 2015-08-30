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

package com.horstmann.violet.product.diagram.usecase;

import java.util.ArrayList;
import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.AbstractGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.AbstractEdge;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.property.ArrowHead;
import com.horstmann.violet.product.diagram.abstracts.property.BentStyle;
import com.horstmann.violet.product.diagram.abstracts.property.LineStyle;
import com.horstmann.violet.product.diagram.common.NoteEdge;
import com.horstmann.violet.product.diagram.common.NoteNode;

/**
 * A UML use case diagram.
 */
public class UseCaseDiagramGraph extends AbstractGraph
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
        ActorNode actorNode = new ActorNode();
        actorNode.setResourcePrefix("node0");
        NODE_PROTOTYPES.add(actorNode);

        UseCaseNode useCaseNode = new UseCaseNode();
        useCaseNode.setResourcePrefix("node1");
        NODE_PROTOTYPES.add(useCaseNode);

        NoteNode noteNode = new NoteNode();
        noteNode.setResourcePrefix("node2");
        NODE_PROTOTYPES.add(noteNode);

        UseCaseRelationshipEdge communication = new UseCaseRelationshipEdge();
        communication.setBentStyle(BentStyle.STRAIGHT);
        communication.setLineStyle(LineStyle.SOLID);
        communication.setEndArrowHead(ArrowHead.NONE);
        communication.setResourcePrefix("edge0");
        EDGE_PROTOTYPES.add(communication);

        UseCaseRelationshipEdge extendRel = new UseCaseRelationshipEdge();
        extendRel.setBentStyle(BentStyle.STRAIGHT);
        extendRel.setLineStyle(LineStyle.DOTTED);
        extendRel.setEndArrowHead(ArrowHead.V);
        extendRel.setMiddleLabel("\u00ABextend\u00BB");
        extendRel.setResourcePrefix("edge1");
        EDGE_PROTOTYPES.add(extendRel);

        UseCaseRelationshipEdge includeRel = new UseCaseRelationshipEdge();
        includeRel.setBentStyle(BentStyle.STRAIGHT);
        includeRel.setLineStyle(LineStyle.DOTTED);
        includeRel.setEndArrowHead(ArrowHead.V);
        includeRel.setMiddleLabel("\u00ABinclude\u00BB");
        includeRel.setResourcePrefix("edge2");
        EDGE_PROTOTYPES.add(includeRel);

        UseCaseRelationshipEdge generalization = new UseCaseRelationshipEdge();
        generalization.setBentStyle(BentStyle.STRAIGHT);
        generalization.setLineStyle(LineStyle.SOLID);
        generalization.setEndArrowHead(ArrowHead.TRIANGLE);
        generalization.setResourcePrefix("edge3");
        EDGE_PROTOTYPES.add(generalization);

        NoteEdge noteEdge = new NoteEdge();
        noteEdge.setResourcePrefix("edge4");
        EDGE_PROTOTYPES.add(noteEdge);
        
        for (IEdge proto : EDGE_PROTOTYPES) {
        	((AbstractEdge)proto).setResourceBundleName(UseCaseDiagramConstant.USECASE_DIAGRAM_STRINGS);
        }
        for (INode proto : NODE_PROTOTYPES) {
        	((AbstractNode)proto).setResourceBundleName(UseCaseDiagramConstant.USECASE_DIAGRAM_STRINGS);
        }
    }

}
