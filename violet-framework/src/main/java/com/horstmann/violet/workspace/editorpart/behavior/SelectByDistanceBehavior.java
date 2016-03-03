package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;

/**
 * Allows to select the next or the previous element (node_old or edge) by specifying a distance with the current one
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class SelectByDistanceBehavior extends AbstractEditorPartBehavior
{

    public SelectByDistanceBehavior(IEditorPart editorPart)
    {
        this.editorPart = editorPart;
        this.graph = editorPart.getGraph();
        this.selectionHandler = editorPart.getSelectionHandler();
        this.behaviorManager = editorPart.getBehaviorManager();
    }

    
    /**
     * Selects another graph element.
     * 
     * @param distanceFormCurrentElement distance from the currently selected element. For example : -1 for the previous element and
     *            +1 for the next one.
     */
    public void selectAnotherGraphElement(int distanceFromCurrentElement)
    {
        ArrayList<Object> selectables = new ArrayList<Object>();
        selectables.addAll(graph.getAllNodes());
        selectables.addAll(graph.getAllEdges());
        if (selectables.size() == 0) return;
        java.util.Collections.sort(selectables, new java.util.Comparator<Object>()
        {
            public int compare(Object obj1, Object obj2)
            {
                double x1;
                double y1;
                if (obj1 instanceof INode)
                {
                    Rectangle2D bounds = ((INode) obj1).getBounds();
                    x1 = bounds.getX();
                    y1 = bounds.getY();
                }
                else
                {
                    Point2D start = ((IEdge) obj1).getConnectionPoints().getP1();
                    x1 = start.getX();
                    y1 = start.getY();
                }
                double x2;
                double y2;
                if (obj2 instanceof INode)
                {
                    Rectangle2D bounds = ((INode) obj2).getBounds();
                    x2 = bounds.getX();
                    y2 = bounds.getY();
                }
                else
                {
                    Point2D start = ((IEdge) obj2).getConnectionPoints().getP1();
                    x2 = start.getX();
                    y2 = start.getY();
                }
                if (y1 < y2) return -1;
                if (y1 > y2) return 1;
                if (x1 < x2) return -1;
                if (x1 > x2) return 1;
                return 0;
            }
        });
        int index;
        Object lastSelected = null;
        if (selectionHandler.isNodeSelectedAtLeast())
        {
            lastSelected = selectionHandler.getLastSelectedNode();
        }
        if (selectionHandler.isEdgeSelectedAtLeast())
        {
            lastSelected = selectionHandler.getLastSelectedEdge();
        }
        if (lastSelected == null) index = 0;
        else index = selectables.indexOf(lastSelected) + distanceFromCurrentElement;
        while (index < 0)
            index += selectables.size();
        index %= selectables.size();
        Object toSelect = selectables.get(index);
        if (toSelect instanceof INode)
        {
            selectionHandler.setSelectedElement((INode) toSelect);
            behaviorManager.fireOnNodeSelected((INode) toSelect);
        }
        if (toSelect instanceof IEdge)
        {
            selectionHandler.setSelectedElement((IEdge) toSelect);
            behaviorManager.fireOnEdgeSelected((IEdge) toSelect);
        }
        this.editorPart.getSwingComponent().invalidate();
    }
    
    private IEditorPart editorPart;

    private IGraph graph;

    private IEditorPartSelectionHandler selectionHandler;
    
    private IEditorPartBehaviorManager behaviorManager;

}
