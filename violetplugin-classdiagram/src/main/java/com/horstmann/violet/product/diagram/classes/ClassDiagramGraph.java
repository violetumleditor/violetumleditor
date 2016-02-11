package com.horstmann.violet.product.diagram.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.horstmann.violet.product.diagram.abstracts.AbstractGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.property.ArrowHead;
import com.horstmann.violet.product.diagram.abstracts.property.LineStyle;
import com.horstmann.violet.product.diagram.classes.nodes.ClassNode;
import com.horstmann.violet.product.diagram.classes.nodes.PackageNode;
import com.horstmann.violet.product.diagram.classes.nodes.InterfaceNode;
import com.horstmann.violet.product.diagram.common.edge.AdvancedPropertyEdge;
import com.horstmann.violet.product.diagram.common.edge.BasePropertyEdge;
import com.horstmann.violet.product.diagram.common.edge.NoteEdge;
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
        ResourceBundle rs = ResourceBundle.getBundle(ClassDiagramConstant.CLASS_DIAGRAM_STRINGS, Locale.getDefault());

        ClassNode node0 = new ClassNode();
        node0.setToolTip(rs.getString("node0.tooltip"));
        NODE_PROTOTYPES.add(node0);

        InterfaceNode node1 = new InterfaceNode();
        node1.setToolTip(rs.getString("node1.tooltip"));
        NODE_PROTOTYPES.add(node1);

        PackageNode node2 = new PackageNode();
        node2.setToolTip(rs.getString("node2.tooltip"));
        NODE_PROTOTYPES.add(node2);

        NoteNode node3 = new NoteNode();
        node3.setToolTip(rs.getString("node3.tooltip"));
        NODE_PROTOTYPES.add(node3);

//        ImageNode node4 = new ImageNode();
//        node4.setToolTip(rs.getString("node4.tooltip"));
//        NODE_PROTOTYPES.add(node4);

        BasePropertyEdge dependency = new BasePropertyEdge();
        dependency.setEndArrowHead(ArrowHead.V);
        dependency.setStartArrowHead(ArrowHead.NONE);
        dependency.setLineStyle(LineStyle.DOTTED);
        dependency.setToolTip(rs.getString("edge0.tooltip"));
        EDGE_PROTOTYPES.add(dependency);

        BasePropertyEdge inheritance = new BasePropertyEdge();
        inheritance.setEndArrowHead(ArrowHead.TRIANGLE);
        inheritance.setStartArrowHead(ArrowHead.NONE);
        inheritance.setLineStyle(LineStyle.SOLID);
        inheritance.setToolTip(rs.getString("edge1.tooltip"));
        EDGE_PROTOTYPES.add(inheritance);

        BasePropertyEdge interfaceInheritance = new BasePropertyEdge();
        interfaceInheritance.setEndArrowHead(ArrowHead.TRIANGLE);
        interfaceInheritance.setStartArrowHead(ArrowHead.NONE);
        interfaceInheritance.setLineStyle(LineStyle.DOTTED);
        interfaceInheritance.setToolTip(rs.getString("edge2.tooltip"));
        EDGE_PROTOTYPES.add(interfaceInheritance);

        BasePropertyEdge association = new BasePropertyEdge();
        association.setEndArrowHead(ArrowHead.V);
        association.setStartArrowHead(ArrowHead.NONE);
        association.setLineStyle(LineStyle.SOLID);
        association.setToolTip(rs.getString("edge3.tooltip"));
        EDGE_PROTOTYPES.add(association);

        BasePropertyEdge aggregation = new BasePropertyEdge();
        aggregation.setEndArrowHead(ArrowHead.DIAMOND);
        aggregation.setStartArrowHead(ArrowHead.NONE);
        aggregation.setLineStyle(LineStyle.SOLID);
        aggregation.setToolTip(rs.getString("edge4.tooltip"));
        EDGE_PROTOTYPES.add(aggregation);

        BasePropertyEdge composition = new BasePropertyEdge();
        composition.setEndArrowHead(ArrowHead.BLACK_DIAMOND);
        composition.setStartArrowHead(ArrowHead.NONE);
        composition.setLineStyle(LineStyle.SOLID);
        composition.setToolTip(rs.getString("edge5.tooltip"));
        EDGE_PROTOTYPES.add(composition);

        NoteEdge noteEdge = new NoteEdge();
        noteEdge.setToolTip(rs.getString("edge6.tooltip"));
        EDGE_PROTOTYPES.add(noteEdge);

        AdvancedPropertyEdge customEdge = new AdvancedPropertyEdge();
        customEdge.setToolTip(rs.getString("edge7.tooltip"));
        EDGE_PROTOTYPES.add(customEdge);
    }

}
