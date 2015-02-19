package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import com.horstmann.violet.product.diagram.abstracts.IColorable;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.sidebar.colortools.ColorChoice;
import com.horstmann.violet.workspace.sidebar.colortools.IColorChoiceBar;
import com.horstmann.violet.workspace.sidebar.colortools.IColorChoiceChangeListener;

public class ColorizeBehavior extends AbstractEditorPartBehavior
{

    public ColorizeBehavior(IWorkspace workspace, IColorChoiceBar colorChoiceBar)
    {
        this.workspace = workspace;
        this.editorPart = workspace.getEditorPart();
        colorChoiceBar.addColorChoiceChangeListener(new IColorChoiceChangeListener()
        {

            @Override
            public void onColorChoiceChange(ColorChoice newColorChoice)
            {
                currentColorChoice = newColorChoice;
                ColorizeBehavior.this.editorPart.getSwingComponent().setCursor(transitionCursor);
            }
        });
    }

    @Override
    public void onMouseClicked(MouseEvent event)
    {
        if (event.getClickCount() > 1)
        {
            this.editorPart.getSwingComponent().setCursor(this.defaultCursor);
            return;
        }
        if (event.getButton() != MouseEvent.BUTTON1)
        {
            this.editorPart.getSwingComponent().setCursor(this.defaultCursor);
            return;
        }
        if (currentColorChoice == null)
        {
            this.editorPart.getSwingComponent().setCursor(this.defaultCursor);
            return;
        }
        double zoom = this.workspace.getEditorPart().getZoomFactor();
        Point2D mouseLocation = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        INode node = this.workspace.getGraphFile().getGraph().findNode(mouseLocation);
        if (node != null && IColorable.class.isInstance(node))
        {
            IColorable colorableNode = (IColorable) node;
            colorableNode.setBackgroundColor(this.currentColorChoice.getBackgroundColor());
            colorableNode.setBorderColor(this.currentColorChoice.getBorderColor());
            colorableNode.setTextColor(this.currentColorChoice.getTextColor());
        }
        this.currentColorChoice = null;
        this.editorPart.getSwingComponent().setCursor(this.defaultCursor);
    }

    @Override
    public void onMouseDragged(MouseEvent event)
    {
        if (this.currentColorChoice == null)
        {
            return;
        }
        this.editorPart.getSwingComponent().setCursor(this.transitionCursor);
    }

    private IWorkspace workspace;
    private IEditorPart editorPart;
    private ColorChoice currentColorChoice = null;
    private Cursor defaultCursor = Cursor.getDefaultCursor();
    private Cursor transitionCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);

}
