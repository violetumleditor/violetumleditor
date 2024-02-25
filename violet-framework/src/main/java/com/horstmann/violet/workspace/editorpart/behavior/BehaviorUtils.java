package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.IResizableNode;
import com.horstmann.violet.workspace.editorpart.IEditorPart;

public class BehaviorUtils {
	
    public static boolean isCursorOnResizePoint(IEditorPart editorPart, MouseEvent event) {
    	IGraph graph = editorPart.getGraph();
    	double zoom = editorPart.getZoomFactor();
    	Point currentLocation = event.getPoint();
        double x = currentLocation.getX() / zoom;
        double y = currentLocation.getY() / zoom;
        currentLocation.setLocation(x, y);
    	
        // Check if mouse on node
    	INode node = graph.findNode(currentLocation);
    	if (node == null) {
    		return false;
    	}
    	
    	// Check if node is resizable
    	if (!IResizableNode.class.isInstance(node)) {
    		return false;
    	}
    	
    	// Check if mouse on "resize" anchor zone
    	IResizableNode resizableNode = (IResizableNode) node;
        Rectangle2D resizablePoint = resizableNode.getResizableDragPoint();
        if (resizablePoint == null) {
        	return false;
        }
		return resizablePoint.contains(currentLocation);
    }


}
