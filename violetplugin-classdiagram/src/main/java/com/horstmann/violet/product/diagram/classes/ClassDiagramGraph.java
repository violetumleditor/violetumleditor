package com.horstmann.violet.product.diagram.classes;

import java.util.ArrayList;
import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.AbstractGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.AbstractEdge;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.classes.edges.AggregationEdge;
import com.horstmann.violet.product.diagram.classes.edges.AssociationEdge;
import com.horstmann.violet.product.diagram.classes.edges.CompositionEdge;
import com.horstmann.violet.product.diagram.classes.edges.DependencyEdge;
import com.horstmann.violet.product.diagram.classes.edges.InheritanceEdge;
import com.horstmann.violet.product.diagram.classes.edges.InterfaceInheritanceEdge;
import com.horstmann.violet.product.diagram.classes.nodes.ClassNode;
import com.horstmann.violet.product.diagram.classes.nodes.InterfaceNode;
import com.horstmann.violet.product.diagram.classes.nodes.PackageNode;
import com.horstmann.violet.product.diagram.common.NoteEdge;
import com.horstmann.violet.product.diagram.common.NoteNode;

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

    private static final List<INode> NODE_PROTOTYPES = new ArrayList<INode>();

    private static final List<IEdge> EDGE_PROTOTYPES = new ArrayList<IEdge>();

    static
    {
        ClassNode node0 = new ClassNode();
        node0.setResourcePrefix("node0");
        NODE_PROTOTYPES.add(node0);

        InterfaceNode node1 = new InterfaceNode();
        node1.setResourcePrefix("node1");
        NODE_PROTOTYPES.add(node1);

        PackageNode node2 = new PackageNode();
        node2.setResourcePrefix("node2");
        NODE_PROTOTYPES.add(node2);

        NoteNode node3 = new NoteNode();
        node3.setResourcePrefix("node3");
        NODE_PROTOTYPES.add(node3);

//        ImageNode node4 = new ImageNode();
//        node4.setResourceName("node4");
//        NODE_PROTOTYPES.add(node4);
        DependencyEdge dependency = new DependencyEdge();
        dependency.setResourcePrefix("edge0");
        EDGE_PROTOTYPES.add(dependency);

        InheritanceEdge inheritance = new InheritanceEdge();
        inheritance.setResourcePrefix(("edge1"));
        EDGE_PROTOTYPES.add(inheritance);

        InterfaceInheritanceEdge interfaceInheritance = new InterfaceInheritanceEdge();
        interfaceInheritance.setResourcePrefix("edge2");
        EDGE_PROTOTYPES.add(interfaceInheritance);

        AssociationEdge association = new AssociationEdge();
        association.setResourcePrefix("edge3");
        EDGE_PROTOTYPES.add(association);

        AggregationEdge aggregation = new AggregationEdge();
        aggregation.setResourcePrefix("edge4");
        EDGE_PROTOTYPES.add(aggregation);

        CompositionEdge composition = new CompositionEdge();
        composition.setResourcePrefix("edge5");
        EDGE_PROTOTYPES.add(composition);

        NoteEdge noteEdge = new NoteEdge();
        noteEdge.setResourcePrefix("edge6");
        EDGE_PROTOTYPES.add(noteEdge);

        for (IEdge proto : EDGE_PROTOTYPES) {
        	((AbstractEdge)proto).setResourceBundleName(ClassDiagramConstant.CLASS_DIAGRAM_STRINGS);
        }
        for (INode proto : NODE_PROTOTYPES) {
        	((AbstractNode)proto).setResourceBundleName(ClassDiagramConstant.CLASS_DIAGRAM_STRINGS);
        }
    }

}
