package com.horstmann.violet.workspace.editorpart.behavior;

import com.horstmann.violet.framework.util.KeyModifierUtil;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.IGridSticker;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.Workspace;
import com.horstmann.violet.workspace.WorkspacePanel;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class DragGraphBehavior extends AbstractEditorPartBehavior
{

    private Workspace workspace = null;
    
    private IEditorPart editorPart;
    
    private IGridSticker grid;
    
    private Point2D initialMousePoint = null;
    
    private Point2D lastMousePoint = null;
    
    private Cursor initialCursor = null;
    
    private Cursor dragCursor = new Cursor(Cursor.HAND_CURSOR);
    
    private int initialHorizontalScrollBarValue = -1;
    
    private int initialVerticalScrollBarValue = -1;

    private boolean isReadyForDragging = false;
    
    public DragGraphBehavior(Workspace workspace)
    {
        this.workspace = workspace;
        this.editorPart = workspace.getEditorPart();
        this.grid = workspace.getEditorPart().getGraph().getGridSticker();
    }
    

    @Override
    public void onMousePressed(MouseEvent event)
    {
        if (event.getClickCount() > 1)
        {
            return;
        }
        if (event.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        
        if (!KeyModifierUtil.isCtrl(event)) {
            return;
        }
        
        IEditorPart editorPart = this.workspace.getEditorPart();
        double zoom = editorPart.getZoomFactor();
        final Point2D mousePointOnGraph = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        if (!isMouseOnNode(mousePointOnGraph) && !isMouseOnEdge(mousePointOnGraph))
        {
            WorkspacePanel workspacePanel = this.workspace.getAWTComponent();
            JScrollPane scrollableEditorPart = workspacePanel.getScrollableEditorPart();
            JScrollBar verticalScrollBar = scrollableEditorPart.getVerticalScrollBar();
            JScrollBar horizontalScrollBar = scrollableEditorPart.getHorizontalScrollBar();

            this.isReadyForDragging = true;
            this.initialMousePoint = event.getLocationOnScreen();
            this.initialHorizontalScrollBarValue = horizontalScrollBar.getValue();
            this.initialVerticalScrollBarValue = verticalScrollBar.getValue();
            this.initialCursor = scrollableEditorPart.getCursor();
            
            scrollableEditorPart.setCursor(this.dragCursor);
        }
    }

    private boolean isMouseOnNode(Point2D mouseLocation)
    {
        IEditorPart editorPart = this.workspace.getEditorPart();
        IGraph graph = editorPart.getGraph();
        INode node = graph.findNode(mouseLocation);
        if (node == null)
        {
            return false;
        }
        return true;
    }
    
    private boolean isMouseOnEdge(Point2D mouseLocation)
    {
        IEditorPart editorPart = this.workspace.getEditorPart();
        IGraph graph = editorPart.getGraph();
        IEdge edge = graph.findEdge(mouseLocation);
        if (edge == null)
        {
            return false;
        }
        return true;
    }

    @Override
    public void onMouseDragged(MouseEvent event)
    {
        if (!isReadyForDragging)
        {
            return;
        }
        
        Point rawMousePoint = event.getLocationOnScreen();
        double dx = rawMousePoint.getX() - initialMousePoint.getX();
        double dy = rawMousePoint.getY() - initialMousePoint.getY();
        
        double zoom = this.editorPart.getZoomFactor();
        Point2D mousePoint = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        Point2D snappedMousePoint = this.grid.snap(mousePoint);
        if (!snappedMousePoint.equals(lastMousePoint)) {
            WorkspacePanel workspacePanel = this.workspace.getAWTComponent();
            JScrollPane scrollableEditorPart = workspacePanel.getScrollableEditorPart();
            JScrollBar verticalScrollBar = scrollableEditorPart.getVerticalScrollBar();
            JScrollBar horizontalScrollBar = scrollableEditorPart.getHorizontalScrollBar();
            horizontalScrollBar.setValue(this.initialHorizontalScrollBarValue - (int) dx);
            verticalScrollBar.setValue(this.initialVerticalScrollBarValue - (int) dy);
            this.editorPart.getSwingComponent().invalidate();
            this.editorPart.getSwingComponent().repaint();
        }
        this.lastMousePoint = snappedMousePoint;
    }

    @Override
    public void onMouseReleased(MouseEvent event)
    {
        WorkspacePanel workspacePanel = this.workspace.getAWTComponent();
        JScrollPane scrollableEditorPart = workspacePanel.getScrollableEditorPart();
        scrollableEditorPart.setCursor(this.initialCursor);

        this.initialMousePoint = null;
        this.initialHorizontalScrollBarValue = -1;
        this.initialVerticalScrollBarValue = -1;
        this.isReadyForDragging = false;
        this.initialCursor = null;
    }


}
