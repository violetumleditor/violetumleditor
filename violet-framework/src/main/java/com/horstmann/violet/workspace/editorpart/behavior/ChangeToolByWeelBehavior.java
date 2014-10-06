package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.event.InputEvent;
import java.awt.event.MouseWheelEvent;

import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;

public class ChangeToolByWeelBehavior extends AbstractEditorPartBehavior
{

    private IGraphToolsBar graphToolsBar;
    
    public ChangeToolByWeelBehavior(IGraphToolsBar graphToolsBar)
    {
        this.graphToolsBar = graphToolsBar;
    }


    @Override
    public void onMouseWheelMoved(MouseWheelEvent event)
    {
        boolean isCtrl = (event.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0;
        if (isCtrl) {
            return;
        }
        int scroll = event.getUnitsToScroll();
        if (scroll > 0)
        {
            this.graphToolsBar.selectNextTool();
        }
        if (scroll < 0)
        {
            this.graphToolsBar.selectPreviousTool();
        }
    }
    
  
}
