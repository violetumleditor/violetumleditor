package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;

public class SelectByLassoBehavior extends AbstractEditorPartBehavior
{

    public SelectByLassoBehavior(IEditorPart editorPart, IGraphToolsBar graphToolsBar)
    {
        this.editorPart = editorPart;
        this.graph = editorPart.getGraph();
        this.selectionHandler = editorPart.getSelectionHandler();
        this.graphToolsBar = graphToolsBar;
    }

    @Override
    public void onMousePressed(MouseEvent event)
    {
        if (event.getClickCount() > 1) {
            return;
        }
        if (event.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        if (!GraphTool.SELECTION_TOOL.equals(this.graphToolsBar.getSelectedTool())) {
            return;
        }
        double zoom = editorPart.getZoomFactor();
        final Point2D mousePoint = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        if (!isMouseOnNodeOrEdge(mousePoint))
        {
            resetSelectedElements();
            mouseDownPoint = mousePoint;
            lastMousePoint = mousePoint;
        }
    }

    @Override
    public void onMouseDragged(MouseEvent event)
    {
        if (mouseDownPoint == null)
        {
            return;
        }
        double zoom = editorPart.getZoomFactor();
        Point2D mousePoint = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        lastMousePoint = mousePoint;
        boolean isCtrl = (event.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0;
        double x1 = mouseDownPoint.getX();
        double y1 = mouseDownPoint.getY();
        double x2 = mousePoint.getX();
        double y2 = mousePoint.getY();
        Rectangle2D.Double lasso = new Rectangle2D.Double(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
        Iterator<INode> iter = graph.getAllNodes().iterator();
        while (iter.hasNext())
        {
            INode n = (INode) iter.next();
            Rectangle2D bounds = n.getBounds();
            if (!isCtrl && !lasso.contains(bounds))
            {
                selectionHandler.removeElementFromSelection(n);
            }
            else if (lasso.contains(bounds))
            {
                selectionHandler.addSelectedElement(n);
            }
        }
    }

    @Override
    public void onMouseReleased(MouseEvent event)
    {
        mouseDownPoint = null;
        lastMousePoint = null;
    }

    private boolean isMouseOnNodeOrEdge(Point2D mouseLocation)
    {
        INode node = this.graph.findNode(mouseLocation);
        IEdge edge = this.graph.findEdge(mouseLocation);
        if (node == null && edge == null)
        {
            return false;
        }
        return true;
    }

    private void resetSelectedElements()
    {
        this.selectionHandler.clearSelection();
    }


    @Override
    public void onPaint(Graphics2D g2)
    {
        if (mouseDownPoint == null || lastMousePoint == null)
        {
            return;
        }
        Color oldColor = g2.getColor();
        g2.setColor(PURPLE);
        double x1 = mouseDownPoint.getX();
        double y1 = mouseDownPoint.getY();
        double x2 = lastMousePoint.getX();
        double y2 = lastMousePoint.getY();
        Rectangle2D.Double lasso = new Rectangle2D.Double(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
        g2.draw(lasso);
        g2.setColor(oldColor);
    }

    private static final Color PURPLE = new Color(0.7f, 0.4f, 0.7f);

    private Point2D mouseDownPoint = null;

    private Point2D lastMousePoint = null;

    private IGraph graph;

    private IEditorPartSelectionHandler selectionHandler;

    private IEditorPart editorPart;
    
    private IGraphToolsBar graphToolsBar;

}
