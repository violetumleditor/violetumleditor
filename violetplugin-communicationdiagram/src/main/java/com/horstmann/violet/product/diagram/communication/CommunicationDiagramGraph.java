package com.horstmann.violet.product.diagram.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.horstmann.violet.product.diagram.abstracts.AbstractGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;

import com.horstmann.violet.product.diagram.communication.edge.DirectionEdge;
import com.horstmann.violet.product.diagram.communication.node.ActorNode;
import com.horstmann.violet.product.diagram.communication.node.ObjectNodeCommu;
/**
 * 
 * @author Artur Ratajczak
 *
 */
public class CommunicationDiagramGraph extends AbstractGraph {
	
	@Override
	public List<INode> getNodePrototypes() {
		return NODE_PROTOTYPES;
	}

	@Override
	public List<IEdge> getEdgePrototypes() {
		return EDGE_PROTOTYPES;
	}

	 private static final List<INode> NODE_PROTOTYPES = new ArrayList<INode>();
	 private static final List<IEdge> EDGE_PROTOTYPES = new ArrayList<IEdge>();
	 
	 static{
		 ResourceBundle rs = ResourceBundle.getBundle(CommunicationDiagramConstant.COMMUNICATION_DIAGRAM_STRINGS, Locale.getDefault());
		 
		 	ObjectNodeCommu node0 = new ObjectNodeCommu();
	        node0.setToolTip(rs.getString("node0.tooltip"));
	        NODE_PROTOTYPES.add(node0);
	        
	        ActorNode node1 = new ActorNode();
	        node1.setToolTip(rs.getString("node1.tooltip"));
	        NODE_PROTOTYPES.add(node1);
	        
	        DirectionEdge edge0 = new DirectionEdge();
	        edge0.setToolTip(rs.getString("edge0.tooltip"));
	        EDGE_PROTOTYPES.add(edge0);
	 }
}
