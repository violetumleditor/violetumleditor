package com.horstmann.violet.workspace.editorpart.behavior;

import com.horstmann.violet.product.diagram.abstracts.IColorable;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;

public abstract class AbstractEditorPartBehavior implements IEditorPartBehavior
{
    @Override
    public void afterAddingEdgeAtPoints(IEdge edge, Point2D startPoint, Point2D endPoint)
    {
        
    }

    @Override
    public void afterAddingNodeAtPoint(INode node, Point2D location)
    {
       
    }

    @Override
    public void afterEditingEdge(IEdge edge)
    {
    }

    @Override
    public void afterEditingNode(INode node)
    {

    }

    @Override
    public void afterRemovingSelectedElements()
    {
    
    }

    @Override
    public void beforeAddingEdgeAtPoints(IEdge edge, Point2D startPoint, Point2D endPoint)
    {
    
    }

    @Override
    public void beforeAddingNodeAtPoint(INode node, Point2D location)
    {
    
    }

    @Override
    public void beforeEditingEdge(IEdge edge)
    {
    
    }

    @Override
    public void beforeEditingNode(INode node)
    {
    
    }

    @Override
    public void whileEditingEdge(IEdge edge, PropertyChangeEvent event)
    {
    
    }

    @Override
    public void whileEditingNode(INode node, PropertyChangeEvent event)
    {
    
    }

    @Override
    public void beforeRemovingSelectedElements()
    {
    
    }

    @Override
    public void onMouseDragged(MouseEvent event)
    {
    
    }

    @Override
    public void onMousePressed(MouseEvent event)
    {
    
    }

    @Override
    public void onMouseReleased(MouseEvent event)
    {
    
    }

    @Override
    public void onMouseClicked(MouseEvent event)
    {
    
    }

    @Override
    public void onMouseMoved(MouseEvent event)
    {
    
    }

    @Override
    public void onMouseWheelMoved(MouseWheelEvent event)
    {
    
    }

    @Override
    public void onToolSelected(GraphTool selectedTool)
    {
    
    }

    @Override
    public void onEdgeSelected(IEdge edge)
    {
    
    }

    @Override
    public void onNodeSelected(INode node)
    {
    
    }

    @Override
    public void beforeChangingTransitionPointsOnEdge(IEdge edge)
    {
    
    }

    @Override
    public void afterChangingTransitionPointsOnEdge(IEdge edge)
    {
    
    }

    @Override
    public void beforeChangingColorOnElement(IColorable element)
    {
    
    }

    @Override
    public void afterChangingColorOnElement(IColorable element)
    {
    
    }

    @Override
    public void onPaint(Graphics2D g2)
    {
    
    }

    /**
     * Default behaviour, do nothing, implementations should react accordingly.
     */
    @Override
    public void handleKeyEvent(KeyEvent event)
    {
        return;
    }
}
