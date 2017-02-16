package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.event.MouseEvent;

import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;

public class ResetGraphToolBarOnRightClickBehavior extends AbstractEditorPartBehavior
{

    public ResetGraphToolBarOnRightClickBehavior(IGraphToolsBar graphToolsBar)
    {
    	this.graphToolsBar = graphToolsBar;
    }
    
    
    @Override
    public void onMouseClicked(MouseEvent event) {
    	boolean isButton3Clicked = (event.getButton() == MouseEvent.BUTTON3);
        if (event.getClickCount() == 1 && isButton3Clicked)
        {
            this.graphToolsBar.reset();
        }
    }

    private IGraphToolsBar graphToolsBar;

  
}
