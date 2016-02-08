package com.horstmann.violet.workspace.sidebar.graphtools;

import java.awt.event.MouseEvent;

/**
 * Listener to be implemented and registered by each class that needs to know toolbar actions
 * 
 * @author Viigoo
 * 
 */
public interface IGraphToolsBarMouseListener
{

    void onMouseToolClicked(GraphTool selectedTool);
    
    void onMouseToolDragged(MouseEvent event);
    
    void onMouseToolReleased(MouseEvent event);
    
}