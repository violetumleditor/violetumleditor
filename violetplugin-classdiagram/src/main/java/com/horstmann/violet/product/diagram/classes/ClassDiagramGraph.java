package com.horstmann.violet.product.diagram.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.horstmann.violet.framework.property.LineStyleChoiceList;
import com.horstmann.violet.product.diagram.abstracts.AbstractGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;

import com.horstmann.violet.product.diagram.classes.nodes.*;

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

        BasePropertyEdge dependency = new BasePropertyEdge();
//        dependency.setEndArrowHead(ArrowheadChoiceList.V);
//        dependency.setStartArrowHead(ArrowheadChoiceList.NONE);
//        dependency.setLineStyle(LineStyleChoiceList.DOTTED);
        dependency.setToolTip(rs.getString("edge0.tooltip"));
        EDGE_PROTOTYPES.add(dependency);

        BasePropertyEdge inheritance = new BasePropertyEdge();
//        inheritance.setEndArrowHead(ArrowheadChoiceList.TRIANGLE);
//        inheritance.setStartArrowHead(ArrowheadChoiceList.NONE);
//        inheritance.setLineStyle(LineStyleChoiceList.SOLID);
        inheritance.setToolTip(rs.getString("edge1.tooltip"));
        EDGE_PROTOTYPES.add(inheritance);

        BasePropertyEdge interfaceInheritance = new BasePropertyEdge();
//        interfaceInheritance.setEndArrowHead(ArrowheadChoiceList.TRIANGLE);
//        interfaceInheritance.setStartArrowHead(ArrowheadChoiceList.NONE);
//        interfaceInheritance.setLineStyle(LineStyleChoiceList.DOTTED);
        interfaceInheritance.setToolTip(rs.getString("edge2.tooltip"));
        EDGE_PROTOTYPES.add(interfaceInheritance);

        AdvancedPropertyEdge association = new AdvancedPropertyEdge();
//        association.setEndArrowHead(ArrowheadChoiceList.V);
//        association.setStartArrowHead(ArrowheadChoiceList.NONE);
//        association.setLineStyle(LineStyleChoiceList.SOLID);
        association.setToolTip(rs.getString("edge3.tooltip"));
        EDGE_PROTOTYPES.add(association);

        BasePropertyEdge aggregation = new BasePropertyEdge();
//        aggregation.setEndArrowHead(ArrowheadChoiceList.DIAMOND);
//        aggregation.setStartArrowHead(ArrowheadChoiceList.NONE);
//        aggregation.setLineStyle(LineStyleChoiceList.SOLID);
        aggregation.setToolTip(rs.getString("edge4.tooltip"));
        EDGE_PROTOTYPES.add(aggregation);

        BasePropertyEdge composition = new BasePropertyEdge();
//        composition.setEndArrowHead(ArrowheadChoiceList.BLACK_DIAMOND);
//        composition.setStartArrowHead(ArrowheadChoiceList.NONE);
//        composition.setLineStyle(LineStyleChoiceList.SOLID);
        composition.setToolTip(rs.getString("edge5.tooltip"));
        EDGE_PROTOTYPES.add(composition);

        NoteEdge noteEdge = new NoteEdge();
        noteEdge.setToolTip(rs.getString("edge6.tooltip"));
        EDGE_PROTOTYPES.add(noteEdge);

        AdvancedPropertyEdge customEdge = new AdvancedPropertyEdge();
        customEdge.setToolTip(rs.getString("edge8.tooltip"));
        EDGE_PROTOTYPES.add(customEdge);
    }
    
 
    

}
