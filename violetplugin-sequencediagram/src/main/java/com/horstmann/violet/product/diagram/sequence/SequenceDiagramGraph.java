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

package com.horstmann.violet.product.diagram.sequence;

import java.awt.geom.Point2D;
import java.beans.XMLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.horstmann.violet.framework.util.Statistics;
import com.horstmann.violet.framework.util.XMLManager;
import com.horstmann.violet.product.diagram.abstracts.AbstractGraph;
import com.horstmann.violet.product.diagram.abstracts.StatisticalGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.common.edge.NoteEdge;
import com.horstmann.violet.product.diagram.common.node.NoteNode;
import com.horstmann.violet.product.diagram.sequence.edge.AsynchronousCallEdge;
import com.horstmann.violet.product.diagram.sequence.edge.ReturnEdge;
import com.horstmann.violet.product.diagram.sequence.edge.SynchronousCallEdge;
import com.horstmann.violet.product.diagram.sequence.node.ActivationBarNode;
import com.horstmann.violet.product.diagram.sequence.node.CombinedFragmentNode;
import com.horstmann.violet.product.diagram.sequence.node.LifelineNode;

/**
 * A UML sequence diagram.
 */
public class SequenceDiagramGraph extends AbstractGraph implements StatisticalGraph
{
    @Override
    public boolean addNode(INode newNode, Point2D p)
    {
        INode foundNode = findNode(p);
        if (foundNode == null && newNode.getClass().isAssignableFrom(ActivationBarNode.class)) {
            return false;
        }
        return super.addNode(newNode, p);
    }

    public List<INode> getNodePrototypes() {
        return NODE_PROTOTYPES;
    }

    public List<IEdge> getEdgePrototypes() {
        return EDGE_PROTOTYPES;
    }
    
    public Statistics getMessagesPerObject() {
    	
    	// Collect the data in 2 Lists
    	List<String> objectNames = new ArrayList<>();
    	List<Integer> numOfMessages = new ArrayList<>();
    	
    	Collection<INode> nodes = getAllNodes();
    	for (INode node : nodes) {
    		if (node instanceof LifelineNode) {
    			LifelineNode concept = (LifelineNode) node;
    			
    			// List of children
    			List<INode> children = concept.getChildren();
    			int msgCounter = 0;
    			for (INode child : children) {
    				if (child instanceof ActivationBarNode) {
    					ActivationBarNode actBar = (ActivationBarNode) child;
    					msgCounter += actBar.getConnectedEdges().size();
    				}
    			}
    			
    			// add the information to the lists
    			objectNames.add(concept.getName().toString());
    			numOfMessages.add(msgCounter);
    		}
    	}
    	
    	// Put data in Statistics object
    	Statistics stat = new Statistics();
    	stat.setChart("Number of Messages per Object", objectNames, numOfMessages);
    	
    	return stat;
    }
    
    
	@Override
	public void evaluateStatistics() {
		List<Statistics> list = new ArrayList<>();
    	
    	list.add(getMessagesPerObject());			// Add messages per object
    	// list.add(getObjectsNames());				// Once it's implemented
    	// list.add(getOutgoingMessagesPerObject())
    	// list.add(getreturnMessagesPerObject())	
    	
    	XMLManager<Statistics> manager = new XMLManager<>();
    	manager.writeXML(list);
	}

    @Override
	public void evaluateViolations() {
		// Collect Violations
    	// Put them in Strings
    	// Call Warning display window once it's implemented
	}


	private static final List<INode> NODE_PROTOTYPES = new ArrayList<INode>(Arrays.asList(
            new LifelineNode(),
            new ActivationBarNode(),
            new CombinedFragmentNode(),
            new NoteNode()
    ));

    private static final List<IEdge> EDGE_PROTOTYPES = new ArrayList<IEdge>(Arrays.asList(
            new SynchronousCallEdge(),
            new AsynchronousCallEdge(),
            new ReturnEdge(),
            new NoteEdge()
    ));
    
}
