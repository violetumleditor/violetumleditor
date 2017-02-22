package com.horstmann.violet.workspace.editorpart.behavior;

import com.horstmann.violet.product.diagram.abstracts.IColorable;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;

public class SwingRepaintingBehavior implements IEditorPartBehavior
{

    private IEditorPart editorPart;

    public SwingRepaintingBehavior(IEditorPart editorPart)
    {
        this.editorPart = editorPart;
    }

    @Override
    public void onToolSelected(GraphTool selectedTool)
    {
        //this.editorPart.getSwingComponent().repaint();
    }

    @Override
    public void onMouseReleased(MouseEvent event)
    {
        //this.editorPart.getSwingComponent().repaint();
    }

    @Override
    public void onMouseClicked(MouseEvent event)
    {
        this.editorPart.getSwingComponent().repaint();
    }

    @Override
    public void onMousePressed(MouseEvent event)
    {
        //this.editorPart.getSwingComponent().repaint();
    }

    @Override
    public void onMouseDragged(MouseEvent event)
    {
        //this.editorPart.getSwingComponent().repaint();
    }

    @Override
    public void onMouseMoved(MouseEvent event)
    {
        // Nothing to do
    }

    @Override
    public void onMouseWheelMoved(MouseWheelEvent event)
    {
        // Nothing to do
    }

    @Override
    public void beforeRemovingSelectedElements()
    {
        // Nothing to do
    }

    @Override
    public void beforeEditingNode(INode node)
    {
        // nothing to do
    }

    @Override
    public void beforeEditingEdge(IEdge edge)
    {
        // nothing to do
    }

    @Override
    public void beforeAddingNodeAtPoint(INode node, Point2D location)
    {
        // nothing to do
    }

    @Override
    public void beforeAddingEdgeAtPoints(IEdge edge, Point2D startPoint, Point2D endPoint)
    {
        // nothing to do
    }

    @Override
    public void afterRemovingSelectedElements()
    {
        this.editorPart.getSwingComponent().invalidate();
        this.editorPart.getSwingComponent().repaint();
    }

    @Override
    public void afterEditingNode(INode node)
    {
        this.editorPart.getSwingComponent().invalidate();
        this.editorPart.getSwingComponent().repaint();
    }

    @Override
    public void afterEditingEdge(IEdge edge)
    {
        this.editorPart.getSwingComponent().invalidate();
        this.editorPart.getSwingComponent().repaint();
    }

    @Override
    public void afterAddingNodeAtPoint(INode node, Point2D location)
    {
        this.editorPart.getSwingComponent().invalidate();
        this.editorPart.getSwingComponent().repaint();
    }

    @Override
    public void afterAddingEdgeAtPoints(IEdge edge, Point2D startPoint, Point2D endPoint)
    {
        this.editorPart.getSwingComponent().invalidate();
        this.editorPart.getSwingComponent().repaint();
    }

    @Override
    public void onPaint(Graphics2D g2)
    {
        // nothing to do

    }

    @Override
    public void onEdgeSelected(IEdge edge)
    {
        this.editorPart.getSwingComponent().invalidate();
        this.editorPart.getSwingComponent().repaint();
    }

    @Override
    public void onNodeSelected(INode node)
    {
        this.editorPart.getSwingComponent().invalidate();
        this.editorPart.getSwingComponent().repaint();
    }

    @Override
    public void whileEditingEdge(IEdge edge, PropertyChangeEvent event)
    {
        this.editorPart.getSwingComponent().invalidate();
        this.editorPart.getSwingComponent().repaint();
    }

    @Override
    public void whileEditingNode(INode node, PropertyChangeEvent event)
    {
        this.editorPart.getSwingComponent().invalidate();
        this.editorPart.getSwingComponent().repaint();
    }

    @Override
    public void beforeChangingTransitionPointsOnEdge(IEdge edge)
    {
        // Nothing to do
    }

    @Override
    public void afterChangingTransitionPointsOnEdge(IEdge edge)
    {
        // Nothing to do

    }

    @Override
    public void beforeChangingColorOnElement(IColorable element)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterChangingColorOnElement(IColorable element)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void handleKeyEvent(KeyEvent event)
    {
        return;
    }
}
