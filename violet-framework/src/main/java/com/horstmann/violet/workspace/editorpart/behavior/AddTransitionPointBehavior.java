package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.IGridSticker;
import com.horstmann.violet.product.diagram.abstracts.ISelectable;
import com.horstmann.violet.product.diagram.abstracts.edge.EdgeTransitionPoint;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.edge.ITransitionPoint;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;

public class AddTransitionPointBehavior extends AbstractEditorPartBehavior
{

    public AddTransitionPointBehavior(IEditorPart editorPart, IGraphToolsBar graphToolsBar)
    {
        this.editorPart = editorPart;
        this.selectionHandler = editorPart.getSelectionHandler();
        this.graphToolsBar = graphToolsBar;
        this.behaviorManager = editorPart.getBehaviorManager();
    }

    @Override
    public void onMouseMoved(MouseEvent event)
    {
        if (!isPrerequisitesOK())
        {
            return;
        }
        boolean isMouseOnEdgePath = isMouseOnEdgePath(event);
        if (isMouseOnEdgePath)
        {
            if (this.initialCursor == null)
            {
                this.initialCursor = this.editorPart.getSwingComponent().getCursor();
            }
            this.editorPart.getSwingComponent().setCursor(this.transitionCursor);
        }
        if (!isMouseOnEdgePath)
        {
            if (this.initialCursor != null)
            {
                this.editorPart.getSwingComponent().setCursor(this.initialCursor);
                this.initialCursor = null;
            }
        }
    }

    @Override
    public void onMousePressed(MouseEvent event)
    {
        if (event.getClickCount() > 1)
        {
            return;
        }
        if (event.getButton() != MouseEvent.BUTTON1)
        {
            return;
        }
        if (!isPrerequisitesOK())
        {
            return;
        }
        if (!isMouseOnEdgePath(event))
        {
            return;
        }
        if (isMouseOnTransitionPoint(event))
        {
            return;
        }
        this.isReadyToAddTransitionPoint = false;
        double zoom = editorPart.getZoomFactor();
        IGridSticker gridSticker = editorPart.getGraph().getGridSticker();
        final Point2D mousePoint = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        this.newTransitionPointLocation = mousePoint;
        this.newTransitionPointLocation = gridSticker.snap(this.newTransitionPointLocation);
    }

    @Override
    public void onMouseDragged(MouseEvent event)
    {
    	if (!this.isReadyToAddTransitionPoint && this.newTransitionPointLocation != null) {
    		if (!isMouseOnEdgePath(event)) {
    			this.isReadyToAddTransitionPoint = true;
    		}
    	}
    	
    	if (this.isReadyToAddTransitionPoint && !this.isTransitionPointAdded)
        {
            // We add transition point only if a dragging action is detected.
            // If we added it on mouse pressed, it will produce a conflict with
            // other click-based actions such as EditSeletedBehavior
            startUndoRedoCapture();
            addNewTransitionPoint(event);
            stopUndoRedoCapture();
            this.editorPart.getSwingComponent().invalidate();
            this.isTransitionPointAdded = true;
        }
    }

    @Override
    public void onMouseReleased(MouseEvent event)
    {
        this.newTransitionPointLocation = null;
        this.isReadyToAddTransitionPoint = false;
        this.isTransitionPointAdded = false;
        this.selectedEdge = null;
    }

    private IEdge getSelectedEdge()
    {
        if (this.selectedEdge == null)
        {
            if (this.selectionHandler.getSelectedElements().size() == 1)
            {
            	ISelectable element = this.selectionHandler.getSelectedElements().get(0);
                if (IEdge.class.isInstance(element)) {
                	this.selectedEdge = (IEdge) element;
                }
            }
        }
        return this.selectedEdge;
    }

    private boolean isPrerequisitesOK()
    {
        if (this.selectionHandler.getSelectedElements().size() != 1)
        {
            return false;
        }
        if (getSelectedEdge() == null) {
        	return false;
        }
        if (getSelectedEdge().isTransitionPointsSupported())
        {
            return true;
        }
        return false;
    }

    private boolean isSelectedToolOK()
    {
        if (getSelectedEdge() == null)
        {
            return false;
        }
        GraphTool selectedTool = this.graphToolsBar.getSelectedTool();
        if (GraphTool.SELECTION_TOOL.equals(selectedTool))
        {
            return true;
        }
        if (selectedTool.getNodeOrEdge().getClass().isInstance(getSelectedEdge()))
        {
            return true;
        }
        return false;
    }

    private boolean isMouseOnTransitionPoint(MouseEvent event)
    {
        if (getSelectedEdge() == null)
        {
            return false;
        }
        double zoom = this.editorPart.getZoomFactor();
        final Point2D mousePoint = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        final double MAX_DIST = 5;
        for (ITransitionPoint aTransitionPoint : getSelectedEdge().getTransitionPoints())
        {
            Point2D p = aTransitionPoint.toPoint2D();
        	if (p.distance(mousePoint) <= MAX_DIST)
            {
                return true;
            }
        }
        return false;
    }

    private void addNewTransitionPoint(MouseEvent event)
    {
        if (getSelectedEdge() == null)
        {
            return;
        }
        ITransitionPoint[] transitionPoints = getSelectedEdge().getTransitionPoints();
        if (transitionPoints.length == 0)
        {
            List<ITransitionPoint> newTransitionPointList = new ArrayList<ITransitionPoint>();
            newTransitionPointList.add(EdgeTransitionPoint.fromPoint2D(this.newTransitionPointLocation));
            getSelectedEdge().setTransitionPoints(newTransitionPointList.toArray(new ITransitionPoint[newTransitionPointList.size()]));
            return;
        }
        if (transitionPoints.length > 0)
        {
            List<Point2D> pointsToTest = new ArrayList<Point2D>();
            Line2D connectionPoints = getSelectedEdge().getConnectionPoints();
            pointsToTest.add(connectionPoints.getP1());
            pointsToTest.addAll(Arrays.stream(transitionPoints).map(t -> t.toPoint2D()).toList());
            pointsToTest.add(connectionPoints.getP2());
            Point2D lineToTestStartingPoint = pointsToTest.get(0);
            final double MAX_DIST = 5;
            for (int i = 1; i < pointsToTest.size(); i++)
            {
                Point2D lineToTestEndingPoint = pointsToTest.get(i);
                Line2D lineToTest = new Line2D.Double(lineToTestStartingPoint, lineToTestEndingPoint);
                if (lineToTest.ptSegDist(this.newTransitionPointLocation) <= MAX_DIST)
                {
                    List<ITransitionPoint> newTransitionPointList = new ArrayList<ITransitionPoint>();
                    newTransitionPointList.addAll(Arrays.asList(transitionPoints));
                    newTransitionPointList.add(i - 1, EdgeTransitionPoint.fromPoint2D(this.newTransitionPointLocation));
                    getSelectedEdge().setTransitionPoints(
                            newTransitionPointList.toArray(new ITransitionPoint[newTransitionPointList.size()]));
                    return;
                }
                lineToTestStartingPoint = lineToTestEndingPoint;
            }
            return;
        }

    }

    private boolean isMouseOnEdgePath(MouseEvent event)
    {
        if (getSelectedEdge() == null)
        {
            return false;
        }
        double zoom = this.editorPart.getZoomFactor();
        final Point2D mousePoint = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        return getSelectedEdge().contains(mousePoint);
    }

    private void startUndoRedoCapture()
    {
        if (getSelectedEdge() == null)
        {
            return;
        }
        this.behaviorManager.fireBeforeChangingTransitionPointsOnEdge(getSelectedEdge());
    }

    private void stopUndoRedoCapture()
    {
        if (getSelectedEdge() == null)
        {
            return;
        }
        this.behaviorManager.fireAfterChangingTransitionPointsOnEdge(getSelectedEdge());
    }
    
   

    private IEditorPartBehaviorManager behaviorManager;

    private IEditorPartSelectionHandler selectionHandler;

    private IEditorPart editorPart;

    private IGraphToolsBar graphToolsBar;

    private boolean isReadyToAddTransitionPoint = false;

    private boolean isTransitionPointAdded = false;

    private Point2D newTransitionPointLocation = null;

    private IEdge selectedEdge = null;

    private Cursor initialCursor = null;

    private Cursor transitionCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
    
    private static final int DRAG_TRIGGER_MIN_GAP = 50;

}
