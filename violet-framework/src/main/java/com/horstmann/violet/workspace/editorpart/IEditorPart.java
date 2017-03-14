package com.horstmann.violet.workspace.editorpart;

import java.util.List;

import javax.swing.JComponent;

import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;

/**
 * Defines the editor behaviour (an editor is something embedding an IGraph)
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public interface IEditorPart
{

    /**
     * Returns the graph handled by the editor
     */
    public abstract IGraph getGraph();

    /**
     * Removes the selected node or edges.
     */
    public abstract void removeSelected();


    /**
     * @return currently selected node list
     */
    public abstract List<INode> getSelectedNodes();
    
    /**
     * @return currently selected edge list
     */
    public abstract List<IEdge> getSelectedEdges();
    
    /**
     * Clears node and edges selection
     */
    public abstract void clearSelection();
    
    /**
     * Selects a node_old
     * @param node
     */
    public abstract void selectElement(INode node);

    /**
     * Changes the zoom of this editor. The zoom is 1 by default and is multiplied by sqrt(2) for each positive stem or divided by
     * sqrt(2) for each negative step.
     * 
     * @param steps the number of steps by which to change the zoom. A positive value zooms in, a negative value zooms out.
     */
    public abstract void changeZoom(int steps);
    
    /**
     * @return current zoom factor
     */
    public abstract double getZoomFactor();
    
    /**
     * @return the grid used to keep elements aligned
     */
    public abstract IGrid getGrid();
    
    /**
     * Grows drawing area
     */
    public abstract void growDrawingArea();
    
    /**
     * Clips drawing area
     */
    public abstract void clipDrawingArea();

    /**
     * @return the awt object displaying this editor part
     */
    public abstract JComponent getSwingComponent();
    
    /**
     * @return object that manages node and edges selection
     */
    public abstract IEditorPartSelectionHandler getSelectionHandler();
    
    /**
     * @return manager used to declare new editor behaviors and how to send events between behaviors
     */
    public abstract IEditorPartBehaviorManager getBehaviorManager();
    
   

}