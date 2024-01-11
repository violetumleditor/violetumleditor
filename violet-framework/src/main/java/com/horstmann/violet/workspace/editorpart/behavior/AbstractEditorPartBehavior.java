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


public abstract class AbstractEditorPartBehavior implements IEditorPartBehavior
{
    @Override
    public void afterAddingEdgeAtPoints(IEdge edge, Point2D startPoint, Point2D endPoint)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterAddingNodeAtPoint(INode node, Point2D location)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterEditingEdge(IEdge edge)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterEditingNode(INode node)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterRemovingSelectedElements()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeAddingEdgeAtPoints(IEdge edge, Point2D startPoint, Point2D endPoint)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeAddingNodeAtPoint(INode node, Point2D location)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeEditingEdge(IEdge edge)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeEditingNode( INode node)
    {
        // TODO Auto-generated method stub

    }
    
    @Override
    public void whileEditingEdge(IEdge edge, PropertyChangeEvent event)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void whileEditingNode(INode node, PropertyChangeEvent event)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void beforeRemovingSelectedElements()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMouseDragged(MouseEvent event)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMousePressed(MouseEvent event)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMouseReleased(MouseEvent event)
    {
        // TODO Auto-generated method stub

    }
    
    @Override
    public void onMouseClicked(MouseEvent event)
    {
        // TODO Auto-generated method stub

    }
    
    @Override
    public void onMouseMoved(MouseEvent event)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMouseWheelMoved(MouseWheelEvent event)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onToolSelected(GraphTool selectedTool)
    {
        // TODO Auto-generated method stub

    }
    
    @Override
    public void onEdgeSelected(IEdge edge)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onNodeSelected(INode node)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void beforeChangingTransitionPointsOnEdge(IEdge edge)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void afterChangingTransitionPointsOnEdge(IEdge edge)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void beforeChangingColorOnElement(IColorableNode element)
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void afterChangingColorOnElement(IColorableNode element)
    {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void onPaint(Graphics2D g2)
    {
        // TODO Auto-generated method stub
        
    }
    
}
