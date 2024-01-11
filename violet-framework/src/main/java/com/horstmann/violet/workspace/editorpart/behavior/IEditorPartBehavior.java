package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;

import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.IColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;

public interface IEditorPartBehavior
{

    public void onMousePressed(MouseEvent event);
    
    public void onMouseDragged(MouseEvent event);
    
    public void onMouseReleased(MouseEvent event);
    
    public void onMouseClicked(MouseEvent event);
    
    public void onMouseMoved(MouseEvent event);
    
    public void onMouseWheelMoved(MouseWheelEvent event);
    
    public void onToolSelected(GraphTool selectedTool);
    
    public void onNodeSelected(INode node);

    public void onEdgeSelected(IEdge edge);
    
    public void beforeEditingNode(INode node);

    public void whileEditingNode(INode node, PropertyChangeEvent event);
    
    public void afterEditingNode(INode node);
    
    public void beforeEditingEdge(IEdge edge);
    
    public void whileEditingEdge(IEdge edge, PropertyChangeEvent event);
    
    public void afterEditingEdge(IEdge edge);
    
    public void beforeRemovingSelectedElements();
    
    public void afterRemovingSelectedElements();
    
    public void beforeAddingNodeAtPoint(INode node, Point2D location);
    
    public void afterAddingNodeAtPoint(INode node, Point2D location);
    
    public void beforeAddingEdgeAtPoints(IEdge edge, Point2D startPoint, Point2D endPoint);
    
    public void afterAddingEdgeAtPoints(IEdge edge, Point2D startPoint, Point2D endPoint);
    
    public void beforeChangingTransitionPointsOnEdge(IEdge edge);

    public void afterChangingTransitionPointsOnEdge(IEdge edge);
    
    public void beforeChangingColorOnElement(IColorableNode element);

    public void afterChangingColorOnElement(IColorableNode element);
    
    public void onPaint(Graphics2D g2);
    
}
