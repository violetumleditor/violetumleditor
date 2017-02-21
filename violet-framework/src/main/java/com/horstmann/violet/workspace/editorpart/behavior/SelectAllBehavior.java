package com.horstmann.violet.workspace.editorpart.behavior;

import java.util.ArrayList;

import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;

public class SelectAllBehavior extends AbstractEditorPartBehavior
{


    public SelectAllBehavior(IEditorPart editorPart, IGraphToolsBar graphToolsBar)
    {
        this.editorPart = editorPart;
        this.graph = editorPart.getGraph();
        this.selectionHandler = editorPart.getSelectionHandler();
        this.behaviorManager = editorPart.getBehaviorManager();
        this.graphToolsBar = graphToolsBar;
    }

    
    /**
     * Selects all graph element.
     * 
     */
    public void selectAllGraphElements()
    {
        ArrayList<Object> selectables = new ArrayList<Object>();
        selectables.addAll(graph.getAllNodes());
        selectables.addAll(graph.getAllEdges());
        if (selectables.size() == 0) return;
        selectionHandler.clearSelection();
        for (Object toSelect : selectables) {
        	if (toSelect instanceof INode)
            {
                selectionHandler.addSelectedElement((INode) toSelect);
                behaviorManager.fireOnNodeSelected((INode) toSelect);
            }
            if (toSelect instanceof IEdge)
            {
                selectionHandler.addSelectedElement((IEdge) toSelect);
                behaviorManager.fireOnEdgeSelected((IEdge) toSelect);
            }
        }
        editorPart.getSwingComponent().invalidate();
        graphToolsBar.reset();
    }

    private IEditorPart editorPart;
    
    private IGraph graph;

    private IEditorPartSelectionHandler selectionHandler;
    
    private IEditorPartBehaviorManager behaviorManager;
    
    private IGraphToolsBar graphToolsBar;

    
}
