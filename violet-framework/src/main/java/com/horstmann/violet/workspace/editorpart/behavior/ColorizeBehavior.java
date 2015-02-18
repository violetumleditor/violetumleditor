package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.Cursor;
import java.awt.event.MouseEvent;

import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.sidebar.colortools.ColorChoice;
import com.horstmann.violet.workspace.sidebar.colortools.IColorChoiceBar;
import com.horstmann.violet.workspace.sidebar.colortools.IColorChoiceChangeListener;

public class ColorizeBehavior extends AbstractEditorPartBehavior
{

    public ColorizeBehavior(IWorkspace workspace, IColorChoiceBar colorChoiceBar)
    {
        this.workspace = workspace;
        colorChoiceBar.addColorChoiceChangeListener(new IColorChoiceChangeListener()
        {

            @Override
            public void onColorChoiceChange(ColorChoice newColorChoice)
            {
                currentColorChoice = newColorChoice;
                initialCursor = ColorizeBehavior.this.workspace.getAWTComponent().getCursor();
                ColorizeBehavior.this.workspace.getAWTComponent().setCursor(transitionCursor);
            }
        });
    }

    @Override
    public void onMouseClicked(MouseEvent event)
    {
        if (event.getClickCount() > 1)
        {
            return;
        }
        if (event.getButton() != MouseEvent.BUTTON1)
        {
            return;
        }
        this.currentColorChoice = null;
        this.workspace.getAWTComponent().setCursor(this.initialCursor);
    }
    
    
    @Override
    public void onMouseDragged(MouseEvent event)
    {
        if (this.currentColorChoice == null) {
            return;
        }
        this.workspace.getAWTComponent().setCursor(this.transitionCursor);
    }

    private IWorkspace workspace;
    private ColorChoice currentColorChoice = null;
    private Cursor transitionCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
    private Cursor initialCursor = null;

}
