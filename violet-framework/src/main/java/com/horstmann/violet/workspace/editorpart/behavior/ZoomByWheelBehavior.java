package com.horstmann.violet.workspace.editorpart.behavior;

import com.horstmann.violet.framework.util.KeyModifierUtil;
import com.horstmann.violet.workspace.editorpart.IEditorPart;

import java.awt.event.MouseWheelEvent;

public class ZoomByWheelBehavior extends AbstractEditorPartBehavior
{

    private IEditorPart editorPart;

    public ZoomByWheelBehavior(final IEditorPart editorPart)
    {
        this.editorPart = editorPart;
    }

    @Override
    public void onMouseWheelMoved(final MouseWheelEvent event)
    {
        if (!KeyModifierUtil.isCtrl(event))
        {
            return;
        }
        final int scroll = event.getUnitsToScroll();
        if (scroll < 0)
        {
            this.editorPart.zoomIn();
        }
        if (scroll > 0)
        {
            this.editorPart.zoomOut();
        }
    }

}
