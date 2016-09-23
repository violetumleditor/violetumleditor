package com.horstmann.violet.product.diagram.classes;

import java.util.*;

import com.horstmann.violet.product.diagram.abstracts.AbstractGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;

import com.horstmann.violet.product.diagram.classes.edge.*;
import com.horstmann.violet.product.diagram.classes.node.*;

import com.horstmann.violet.product.diagram.common.edge.NoteEdge;
import com.horstmann.violet.product.diagram.common.node.NoteNode;

/**
 * A UML class diagram.
 */
public class ClassDiagramGraph extends AbstractGraph
{
    public List<INode> getNodePrototypes()
    {
        return NODE_PROTOTYPES;
    }

    public List<IEdge> getEdgePrototypes()
    {
        return EDGE_PROTOTYPES;
    }

    private static final List<INode> NODE_PROTOTYPES = new ArrayList<INode>(Arrays.asList(
            new ClassNode(),
            new InterfaceNode(),
            new EnumNode(),
            new PackageNode(),
            new BallAndSocketNode(),
            new NoteNode()
    ));

    private static final List<IEdge> EDGE_PROTOTYPES = new ArrayList<IEdge>(Arrays.asList(
            new DependencyEdge(),
            new InheritanceEdge(),
            new InterfaceInheritanceEdge(),
            new AssociationEdge(),
            new AggregationEdge(),
            new CompositionEdge(),
            new NoteEdge()
    ));
}
