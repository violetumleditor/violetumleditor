package com.horstmann.violet.product.diagram.communication;

import java.util.*;

import com.horstmann.violet.product.diagram.abstracts.AbstractGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;

import com.horstmann.violet.product.diagram.common.edge.NoteEdge;
import com.horstmann.violet.product.diagram.common.node.NoteNode;
import com.horstmann.violet.product.diagram.communication.edge.DirectionEdge;
import com.horstmann.violet.product.diagram.communication.node.ActorNode;
import com.horstmann.violet.product.diagram.communication.node.ObjectCommuNode;
/**
 * 
 * @author Artur Ratajczak
 *
 */
public class CommunicationDiagramGraph extends AbstractGraph
{
    @Override
    public List<INode> getNodePrototypes()
    {
        return NODE_PROTOTYPES;
    }

    @Override
    public List<IEdge> getEdgePrototypes()
    {
        return EDGE_PROTOTYPES;
    }

    private static final List<INode> NODE_PROTOTYPES = new ArrayList<INode>(Arrays.asList(
            new ObjectCommuNode(),
            new ActorNode(),
            new NoteNode()
    ));

    private static final List<IEdge> EDGE_PROTOTYPES = new ArrayList<IEdge>(Arrays.asList(
            new DirectionEdge(),
            new NoteEdge()
    ));
}
