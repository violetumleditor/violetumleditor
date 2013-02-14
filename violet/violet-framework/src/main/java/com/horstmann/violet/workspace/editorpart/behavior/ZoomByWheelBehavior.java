package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.event.InputEvent;
import java.awt.event.MouseWheelEvent;

import com.horstmann.violet.workspace.editorpart.IEditorPart;

public class ZoomByWheelBehavior extends AbstractEditorPartBehavior
{

    private IEditorPart editorPart;

    public ZoomByWheelBehavior(IEditorPart editorPart)
    {
        this.editorPart = editorPart;
    }

    @Override
    public void onMouseWheelMoved(MouseWheelEvent event)
    {
        boolean isCtrl = (event.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0;
        if (!isCtrl)
        {
            return;
        }
        int scroll = event.getUnitsToScroll();
        if (scroll < 0)
        {
            this.editorPart.changeZoom(1);
        }
        if (scroll > 0)
        {
            this.editorPart.changeZoom(-1);
        }
    }

}
