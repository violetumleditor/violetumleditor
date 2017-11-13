package com.horstmann.violet.product.diagram.classes;

import java.util.*;

import com.horstmann.violet.product.diagram.abstracts.AbstractGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;

import com.horstmann.violet.product.diagram.classes.edge.*;
import com.horstmann.violet.product.diagram.classes.node.*;

import com.horstmann.violet.product.diagram.common.edge.NoteEdge;
import com.horstmann.violet.product.diagram.common.node.NoteNode;

/**
 * A UML class diagram.
 */
public class ClassDiagramGraph extends AbstractGraph {
	public List<INode> getNodePrototypes() {
		return NODE_PROTOTYPES;
	}

	public List<IEdge> getEdgePrototypes() {
		return EDGE_PROTOTYPES;
	}

	private static final List<INode> NODE_PROTOTYPES = new ArrayList<INode>(Arrays.asList(new ClassNode(),
			new InterfaceNode(), new EnumNode(), new PackageNode(), new BallAndSocketNode(), new NoteNode()));

	private static final List<IEdge> EDGE_PROTOTYPES = new ArrayList<IEdge>(
			Arrays.asList(new DependencyEdge(), new InheritanceEdge(), new InterfaceInheritanceEdge(),
					new AssociationEdge(), new AggregationEdge(), new CompositionEdge(), new NoteEdge()));

	public void CheckConstraints() {
		CheckRecursionConstraint();
		CheckBiDirectionalConstraint();
		CheckInheritanceConstraint();

	}

	/**
	 * Check for multiple recursive relationships on class nodes and warn the user 
	 */
	private void CheckRecursionConstraint() {

		Collection<INode> nodes = getAllNodes();
		for (INode node : nodes) {
			boolean hasRecursiveRelationship = false;
			if (node instanceof ClassNode) {
				List<IEdge> edges = ((ClassNode) node).getConnectedEdges();
				for (IEdge edge : edges) {
					if (edge.getStartNode() == edge.getEndNode()) {
						if (hasRecursiveRelationship) {
							ClassNode cnode = (ClassNode) node;
							System.out.println("Warning: Multiple recursive relationships in node "
									+ (cnode.getName().toDisplay().isEmpty() ? "<Unnamed Class>" : cnode.getName()));
						}
						hasRecursiveRelationship = true;
					}
				}

			}

		}
	}

	/**
	 * Check for bi-directional aggregation/composition relationships between class nodes and warn the user 
	 */
	private void CheckBiDirectionalConstraint() {
		Collection<IEdge> edges = getAllEdges();
		IEdge[] edgesArray = edges.toArray(new IEdge[edges.size()]);

		for (int i = 0; i < edgesArray.length; i++) {
			if (!(edgesArray[i] instanceof CompositionEdge || edgesArray[i] instanceof AggregationEdge)) {
				continue; // Edge is not Composition or Aggregation edge
			}
			for (int j = 0; j < edgesArray.length; j++) {
				if (j <= i) { // Check if two edges have already been compared
					continue; // Edges already checked
				}
				if (!(edgesArray[j] instanceof CompositionEdge || edgesArray[j] instanceof AggregationEdge)) {
					continue; // Edge is not a Composition or Aggregation edge
				}
				if (edgesArray[i].getStartNode() == edgesArray[j].getEndNode()) {
					if (edgesArray[j].getStartNode() == edgesArray[i].getEndNode()) {
						ClassNode class1 = (ClassNode) edgesArray[i].getStartNode();
						ClassNode class2 = (ClassNode) edgesArray[i].getEndNode();
						System.out.println("Warning: Multiple composition/aggregation relationships between "
								+ (class1.getName().toDisplay().isEmpty() ? "<Unnamed Class>" : class1.getName())
								+ " and "
								+ (class2.getName().toDisplay().isEmpty() ? "<Unnamed Class>" : class2.getName()));
					}
				}

			}

		}

	}

	private void CheckInheritanceConstraint() {
		//TODO

	}
}
