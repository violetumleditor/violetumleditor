package com.horstmann.violet.product.diagram.classes;
import com.horstmann.violet.product.diagram.abstracts.StatisticalGraph;

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
public class ClassDiagramGraph extends AbstractGraph implements StatisticalGraph{
	
	public void evaluateStatistics() {
		
	}
	public List<String> evaluateViolations(){
		List<String> violations = new ArrayList<String>();
		violations.addAll(CheckRecursionConstraint());
		violations.addAll(CheckBiDirectionalConstraint());
		violations.addAll(CheckInheritanceConstraint());
		return violations;

	}
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


	/**
	 * Check for multiple recursive relationships on class nodes and warn the user 
	 */
	private List<String> CheckRecursionConstraint() {
		List<String> violations = new ArrayList<String>();

		Collection<INode> nodes = getAllNodes();
		for (INode node : nodes) {
			boolean hasRecursiveRelationship = false;
			if (node instanceof ClassNode) {
				List<IEdge> edges = ((ClassNode) node).getConnectedEdges();
				for (IEdge edge : edges) {
					if (edge.getStartNode() == edge.getEndNode()) {
						if (hasRecursiveRelationship) {
							ClassNode cnode = (ClassNode) node;
							violations.add("Multiple recursive relationships in node "
									+ (cnode.getName().toDisplay().isEmpty() ? "<Unnamed Class>" : cnode.getName()));
						}
						hasRecursiveRelationship = true;
					}
				}

			}

		}
		return violations;
	}

	/**
	 * Check for bi-directional aggregation/composition relationships between class nodes and warn the user 
	 */
	private List<String> CheckBiDirectionalConstraint() {
		List<String> violations = new ArrayList<String>();
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
				if (edgesArray[i].getStartNode() == edgesArray[j].getStartNode()) {
					continue; //Both nodes are the same
				}
				if (edgesArray[i].getStartNode() == edgesArray[j].getEndNode()) {
					if (edgesArray[j].getStartNode() == edgesArray[i].getEndNode()) {
						ClassNode class1 = (ClassNode) edgesArray[i].getStartNode();
						ClassNode class2 = (ClassNode) edgesArray[i].getEndNode();
						violations.add("Multiple composition/aggregation relationships between "
								+ (class1.getName().toDisplay().isEmpty() ? "<Unnamed Class>" : class1.getName())
								+ " and "
								+ (class2.getName().toDisplay().isEmpty() ? "<Unnamed Class>" : class2.getName()));
					}
				}

			}

		}
		return violations;

	}

	private List<String> CheckInheritanceConstraint(){
		List<String> violations = new ArrayList<String>();
        Collection <IEdge> edges1 = getAllEdges();
        IEdge[] edgesArray1 = edges1.toArray(new IEdge[edges1.size()]);
        for (int i = 0; i < edgesArray1.length; i++) {
            // if the edge is one of these
            if(edgesArray1[i] instanceof AggregationEdge || edgesArray1[i] instanceof CompositionEdge || edgesArray1[i] instanceof InheritanceEdge ){
                        for(int j=1; j<edgesArray1.length;j++){
                            //and if the next edge is one these
                            if(j>i && (edgesArray1[j] instanceof AggregationEdge || edgesArray1[j] instanceof CompositionEdge || edgesArray1[j] instanceof InheritanceEdge))
                            {
                                //and they are connected (the first edge starts at node and the second edge ends at the same node and vice versa)
                                if(edgesArray1[i].getStartNode()== edgesArray1[j].getEndNode() && (edgesArray1[j].getStartNode()== edgesArray1[i].getEndNode())) {
                                	ClassNode class1 = (ClassNode) edgesArray1[i].getStartNode();
                                	ClassNode class2 = (ClassNode) edgesArray1[i].getEndNode();
                                	

                                	violations.add("Bi-directional inheritance with composite or aggregation between " 
                                			+ (class1.getName().toDisplay().isEmpty() ? "<Unnamed Class>" : class1.getName())
            								+ " and "
            								+ (class2.getName().toDisplay().isEmpty() ? "<Unnamed Class>" : class2.getName()));
                                }
                           
                            }
                            
            
                        }
        
            }

        }

        return violations;
    }
}
