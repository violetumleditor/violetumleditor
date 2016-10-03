package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import com.horstmann.violet.product.diagram.abstracts.IColorable;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.sidebar.colortools.ColorChoice;
import com.horstmann.violet.workspace.sidebar.colortools.IColorChoiceBar;
import com.horstmann.violet.workspace.sidebar.colortools.IColorChoiceChangeListener;

public class ColorizeBehavior extends AbstractEditorPartBehavior
{

    public ColorizeBehavior(IWorkspace workspace, IColorChoiceBar colorChoiceBar)
    {
        this.workspace = workspace;
        this.editorPart = workspace.getEditorPart();
        this.behaviorManager = editorPart.getBehaviorManager();
        this.colorChoiceBar = colorChoiceBar;
        colorChoiceBar.addColorChoiceChangeListener(new IColorChoiceChangeListener()
        {

            @Override
            public void onColorChoiceChange(ColorChoice newColorChoice)
            {
                currentColorChoice = newColorChoice;
                ColorizeBehavior.this.editorPart.getSwingComponent().setCursor(IColorChoiceBar.CUTSOM_CURSOR);
            }
        });
    }

    @Override
    public void onMouseClicked(MouseEvent event)
    {
        this.editorPart.getSwingComponent().setCursor(this.defaultCursor);
        if (event.getClickCount() > 1)
        {
            return;
        }
        if (event.getButton() != MouseEvent.BUTTON1)
        {
            return;
        }
        if (currentColorChoice == null)
        {
            return;
        }
        double zoom = this.workspace.getEditorPart().getZoomFactor();
        Point2D mouseLocation = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        INode node = this.workspace.getGraphFile().getGraph().findNode(mouseLocation);
        if (node != null && IColorable.class.isInstance(node)) {
        	IColorable colorableElement = (IColorable) node;
        	this.behaviorManager.fireBeforeChangingColorOnElement(colorableElement);
            colorableElement.setBackgroundColor(this.currentColorChoice.getBackgroundColor());
            colorableElement.setBorderColor(this.currentColorChoice.getBorderColor());
            colorableElement.setTextColor(this.currentColorChoice.getTextColor());
            this.behaviorManager.fireAfterChangingColorOnElement(colorableElement);
            this.currentColorChoice = null;
            this.colorChoiceBar.resetSelection();
            return;
        }
    	IEdge edge = this.workspace.getGraphFile().getGraph().findEdge(mouseLocation);
    	if (edge != null && IColorable.class.isInstance(edge)) {
    		IColorable colorableElement = (IColorable) edge;
        	this.behaviorManager.fireBeforeChangingColorOnElement(colorableElement);
            colorableElement.setBackgroundColor(this.currentColorChoice.getBackgroundColor());
            colorableElement.setBorderColor(this.currentColorChoice.getBorderColor());
            colorableElement.setTextColor(this.currentColorChoice.getBorderColor());
            this.behaviorManager.fireAfterChangingColorOnElement(colorableElement);
            this.currentColorChoice = null;
            this.colorChoiceBar.resetSelection();
            return;
        }	
    }

    @Override
    public void onMouseDragged(MouseEvent event)
    {
        if (this.currentColorChoice == null)
        {
            return;
        }
        this.editorPart.getSwingComponent().setCursor(IColorChoiceBar.CUTSOM_CURSOR);
    }

    private IEditorPart editorPart;
    private IColorChoiceBar colorChoiceBar;
    private IWorkspace workspace;
    private ColorChoice currentColorChoice = null;
    private Cursor defaultCursor = Cursor.getDefaultCursor();
    private IEditorPartBehaviorManager behaviorManager;

}
