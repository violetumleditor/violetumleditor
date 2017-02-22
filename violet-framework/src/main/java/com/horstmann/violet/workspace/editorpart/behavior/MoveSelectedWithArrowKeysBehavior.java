package com.horstmann.violet.workspace.editorpart.behavior;

import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.IGridSticker;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;

import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

/**
 * A class is responsible for moving nodes with arrow keys.
 */
public class MoveSelectedWithArrowKeysBehavior extends AbstractEditorPartBehavior
{
    /**
     * Remembered graph from editor part.
     */
    private IGraph graph;

    private IEditorPartSelectionHandler selectionHandler;

    private IEditorPart editorPart;

    private List<INode> selectedNodes;

    private IGridSticker gridSticker;

    private final static double DX = 15;

    private final static double DY = 15;


    public MoveSelectedWithArrowKeysBehavior(final IEditorPart editorPart)
    {
        this.editorPart = editorPart;
        this.graph = editorPart.getGraph();
        this.selectionHandler = editorPart.getSelectionHandler();
    }

    /**
     * Handles key events. If event keycode given is an arrow key, all selected nodes are moved towards
     * certain direction.
     * @param event is taken from event delegate
     */
    @Override
    public void handleKeyEvent(final KeyEvent event)
    {
        int keyCode = event.getKeyCode();
        if (!isArrow(keyCode))
        {
            return;
        }

        GraphTool selectedTool = this.selectionHandler.getSelectedTool();
        if (IEdge.class.isInstance(selectedTool.getNodeOrEdge()))
        {
            return;
        }

        INode lastNode = selectionHandler.getLastSelectedNode();
        if (lastNode == null)
        {
            return;
        }

        Rectangle2D bounds = lastNode.getBounds();
        selectedNodes = selectionHandler.getSelectedNodes();
        for (INode node : selectedNodes)
        {
            bounds.add(node.getBounds());
        }
        gridSticker = graph.getGridSticker();
        moveOnProperKeyCode(keyCode);
    }

    /**
     * Moves selected nodes by direction depended on keyCode value
     * @param keyCode is the code of pressed key.
     */
    private void moveOnProperKeyCode(final int keyCode)
    {
        switch (keyCode)
        {
            case KeyEvent.VK_UP:
                move(0, -DY);
                break;
            case KeyEvent.VK_DOWN:
                move(0, DY);
                break;
            case KeyEvent.VK_RIGHT:
                move(DX, 0);
                break;
            case KeyEvent.VK_LEFT:
                move(-DX, 0);
                break;
            default:
                break;
        }
    }

    /**
     * Checks if keyCode is other than arrow keys
     * @param keyCode is the code of pressed key.
     * @return false if keyCode doesn't context to an arrow key
     */
    private boolean isArrow(final int keyCode)
    {
        return
                keyCode == KeyEvent.VK_UP ||
                keyCode == KeyEvent.VK_DOWN ||
                keyCode == KeyEvent.VK_LEFT ||
                keyCode == KeyEvent.VK_RIGHT;
    }

    /**
     * Moves selected nodes by values set in dx,dy.
     * @param dx value of an x axis
     * @param dy value of an y axis
     */
    private void move(final double dx, final double dy)
    {
        moveSelectedNodes(dx, dy);
        moveSelectedEdges(dx, dy);
        editorPart.getSwingComponent().invalidate();
        editorPart.getSwingComponent().repaint();
    }

    /**
     * Sets new location for all selected nodes, based on last location
     * @param dx value of an x axis
     * @param dy value of on y axis
     */
    private void moveSelectedNodes(final double dx, final double dy)
    {
        for (INode node : selectedNodes)
        {
            if (selectedNodes.contains(node.getParent()))
            {
                continue; // parents are responsible for translating their
                          //   children
            }

            Point2D currentNodeLocation = node.getLocation();
            Double locationX = currentNodeLocation.getX();
            Double locationY = currentNodeLocation.getY();
            Point2D futureNodeLocation = new Point2D.Double(locationX + dx, locationY + dy);
            Point2D fixedFutureNodeLocation = gridSticker.snap(futureNodeLocation);
            if (!currentNodeLocation.equals(fixedFutureNodeLocation))
            {
                node.setLocation(fixedFutureNodeLocation);
            }
        }
    }

    /**
     * Sets new location for all selected edges, based on last location
     * @param dx value of an x axis
     * @param dy value of an y axis
     */
    private void moveSelectedEdges(final double dx, final double dy)
    {
        // Drag transition points on edges
        Iterator<IEdge> iterateOnEdges = graph.getAllEdges().iterator();
        while (iterateOnEdges.hasNext())
        {
            IEdge edge = iterateOnEdges.next();
            INode startingNode = edge.getStartNode();
            INode endingNode = edge.getEndNode();
            if (selectedNodes.contains(startingNode) && selectedNodes.contains(endingNode))
            {
                Point2D[] transitionPoints = edge.getTransitionPoints();
                for (Point2D aTransitionPoint : transitionPoints)
                {
                    double newTransitionPointLocationX = aTransitionPoint.getX() + dx;
                    double newTransitionPointLocationY = aTransitionPoint.getY() + dy;
                    aTransitionPoint.setLocation(newTransitionPointLocationX, newTransitionPointLocationY);
                }
                edge.setTransitionPoints(transitionPoints);
            }
        }

    }

}
