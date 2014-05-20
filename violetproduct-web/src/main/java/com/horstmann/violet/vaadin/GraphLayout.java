package com.horstmann.violet.vaadin;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.common.NoteNode;
import com.vaadin.ui.AbsoluteLayout;

public class GraphLayout extends AbsoluteLayout {

	private IGraph graph;
	
	private Map<INode, NodeWrapperWidget> nodeMap = new HashMap<INode, NodeWrapperWidget>();
	
	public GraphLayout(IGraph graph) {
		super();
		this.graph = graph;
		repaint();
	}
	
	public void repaint() {
		Rectangle2D clipBounds = this.graph.getClipBounds();
		double w = clipBounds.getWidth();
		double h = clipBounds.getHeight();
		setWidth((float) w, Unit.PIXELS);
		setHeight((float) h, Unit.PIXELS);
		
		List<INode> specialNodes = new ArrayList<INode>();

        int count = 0;
        int z = 0;
        Collection<INode> nodes = this.graph.getAllNodes();
        while (count < nodes.size())
        {
            for (INode n : nodes)
            {

                if (n.getZ() == z)
                {
                    if (n instanceof NoteNode)
                    {
                        specialNodes.add(n);
                    }
                    else
                    {
                        if (!this.nodeMap.containsKey(n)) {
                        	NodeWrapperWidget widget = new NodeWrapperWidget(n);
                        	this.nodeMap.put(n, widget);
                        	this.addComponent(widget);
                        }
                    	NodeWrapperWidget widget = this.nodeMap.get(n);
                    	widget.markAsDirty();
                    	Point2D locationOnGraph = n.getLocationOnGraph();
                    	ComponentPosition componentPosition = getPosition(widget);
                    	componentPosition.setLeft((float) locationOnGraph.getX(), Unit.PIXELS);
                    	componentPosition.setTop((float) locationOnGraph.getY(), Unit.PIXELS);
                    }
                    count++;
                }
            }
            z++;
        }

 
//        // Special nodes are always drawn upon other elements
//        for (INode n : specialNodes)
//        {
//            // Translate g2 if node has parent
//            INode p = n.getParent();
//            Point2D nodeLocationOnGraph = n.getLocationOnGraph();
//            Point2D nodeLocation = n.getLocation();
//            Point2D g2Location = new Point2D.Double(nodeLocationOnGraph.getX() - nodeLocation.getX(), nodeLocationOnGraph.getY()
//                    - nodeLocation.getY());
//            g2.translate(g2Location.getX(), g2Location.getY());
//            n.draw(g2);
//            // Restore g2 original location
//            g2.translate(-g2Location.getX(), -g2Location.getY());
//        }
	}
	
	

}
