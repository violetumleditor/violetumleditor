package com.horstmann.violet.product.diagram.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

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

    private static final List<INode> NODE_PROTOTYPES = new ArrayList<INode>();

    private static final List<IEdge> EDGE_PROTOTYPES = new ArrayList<IEdge>();

    static
    {
        ResourceBundle rs = ResourceBundle.getBundle(ClassDiagramConstant.CLASS_DIAGRAM_STRINGS, Locale.getDefault());

        ClassNode node0 = new ClassNode();
        node0.setToolTip(rs.getString("node0.tooltip"));
        NODE_PROTOTYPES.add(node0);

        InterfaceNode node1 = new InterfaceNode();
        node1.setToolTip(rs.getString("node1.tooltip"));
        NODE_PROTOTYPES.add(node1);

        EnumNode node2 = new EnumNode();
        node2.setToolTip(rs.getString("node2.tooltip"));
        NODE_PROTOTYPES.add(node2);

        PackageNode node3 = new PackageNode();
        node3.setToolTip(rs.getString("node3.tooltip"));
        NODE_PROTOTYPES.add(node3);

        NoteNode node4 = new NoteNode();
        node4.setToolTip(rs.getString("node4.tooltip"));
        NODE_PROTOTYPES.add(node4);

        BallAndSocketNode node5 = new BallAndSocketNode();
        node5.setToolTip(rs.getString("node5.tooltip"));
        NODE_PROTOTYPES.add(node5);

        DependencyEdge dependency = new DependencyEdge();
        dependency.setToolTip(rs.getString("edge0.tooltip"));
        EDGE_PROTOTYPES.add(dependency);

        InheritanceEdge inheritance = new InheritanceEdge();
        inheritance.setToolTip(rs.getString("edge1.tooltip"));
        EDGE_PROTOTYPES.add(inheritance);

        InterfaceInheritanceEdge interfaceInheritance = new InterfaceInheritanceEdge();
        interfaceInheritance.setToolTip(rs.getString("edge2.tooltip"));
        EDGE_PROTOTYPES.add(interfaceInheritance);

        AssociationEdge association = new AssociationEdge();
        association.setToolTip(rs.getString("edge3.tooltip"));
        EDGE_PROTOTYPES.add(association);

        AggregationEdge aggregation = new AggregationEdge();
        aggregation.setToolTip(rs.getString("edge4.tooltip"));
        EDGE_PROTOTYPES.add(aggregation);

        CompositionEdge composition = new CompositionEdge();
        composition.setToolTip(rs.getString("edge5.tooltip"));
        EDGE_PROTOTYPES.add(composition);

        NoteEdge noteEdge = new NoteEdge();
        noteEdge.setToolTip(rs.getString("edge6.tooltip"));
        EDGE_PROTOTYPES.add(noteEdge);
    }
}
