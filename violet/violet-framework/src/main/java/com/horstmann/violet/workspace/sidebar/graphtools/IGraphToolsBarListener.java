package com.horstmann.violet.workspace.sidebar.graphtools;

/**
 * Listener to be implmenented and registered by each class that needs to know toolbar actions
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public interface IGraphToolsBarListener
{

    /**
     * Invoked when a tool is selected
     * 
     * @param selectedNodeOrEdge the selected tool
     */
    void toolSelectionChanged(GraphTool selectedTool);

}