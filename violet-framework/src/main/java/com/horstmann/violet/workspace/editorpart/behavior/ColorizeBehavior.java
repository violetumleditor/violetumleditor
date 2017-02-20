package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.*;
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
    /**
     * @param workspace
     * @param colorChoiceBar
     */
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
        double zoom = this.workspace.getEditorPart().getZoomFactor();
        Point2D mouseLocation = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        this.editorPart.getSwingComponent().setCursor(this.defaultCursor);
        Object object = this.workspace.getGraphFile().getGraph().findEdge(mouseLocation);

        if (event.getClickCount() > 1)
        {
            return;
        }

        int mouseEvent = event.getButton();
        switch (mouseEvent)
        {
            case MouseEvent.BUTTON1:
                onLeftMouseButtonClicked(event, object);
                break;
            case MouseEvent.BUTTON2:
                onMiddleMouseButtonClicked(event, object);
                break;
            default:
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

    /**
     * Responsible for selecting and change color depending on the selected object.
     * The object is retrieved from the workspace of the mouse position.
     * @param event
     */
    private void onLeftMouseButtonClicked(MouseEvent event, Object object)
    {
        if (currentColorChoice == null)
        {
            return;
        }
        if (!isCorolable(object))
        {
            return;
        }

        if (isEdge(object) || isNode(object)) {
            IColorable colorableElement = (IColorable) object;

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

    /**
     * Responsible for selecting and change color depending on the selected edge.
     * For selected other edge or workspace, last selected edge change its color to orginal(last) color.
     * @param event
     * @param object
     */
    private void onMiddleMouseButtonClicked(MouseEvent event, Object object)
    {
        if(isEdge(object) && isCorolable(object)) {
            IColorable selectedEdge = (IColorable) object;

            if (latestChosenEdge != selectedEdge && latestChosenEdgeColor != null) {
                latestChosenEdge.setBorderColor(latestChosenEdgeColor);
            }

            latestChosenEdge = selectedEdge;
            latestChosenEdgeColor = selectedEdge.getBorderColor();

            this.behaviorManager.fireBeforeChangingColorOnElement(selectedEdge);
            selectedEdge.setBackgroundColor(backlightColor);
            selectedEdge.setBorderColor(backlightColor);
            selectedEdge.setTextColor(backlightColor);
            this.behaviorManager.fireAfterChangingColorOnElement(selectedEdge);
            return;
        }
        latestChosenEdge.setBorderColor(latestChosenEdgeColor);
        latestChosenEdgeColor = null;
    }

    private boolean isCorolable(Object object)
    {
        return IColorable.class.isInstance(object);
    }

    private boolean isEdge(Object object)
    {
        return IEdge.class.isInstance(object);
    }

    private boolean isNode(Object object)
    {
        return INode.class.isInstance(object);
    }

    private IEditorPart editorPart;
    private IColorChoiceBar colorChoiceBar;
    private IWorkspace workspace;
    private ColorChoice currentColorChoice = null;
    private Cursor defaultCursor = Cursor.getDefaultCursor();
    private IEditorPartBehaviorManager behaviorManager;
    private IColorable latestChosenEdge = null;
    private Color latestChosenEdgeColor = null;
    private Color backlightColor = Color.BLUE;
}