package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
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
    }

    @Override
    public void onMousePressed(MouseEvent event)
    {
        if (event.getClickCount() > 1)
        {
            return;
        }
        if (event.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        if (!isPrerequisitesOK()) 
        {
        	return;
        }
        if (!isSelectedToolOK()) 
        {
        	return;
        }
        if (isMouseOnTransitionPoint(event)) 
        {
        	return;
        }
        addNewTransitionPoint(event);
    }

    

    private boolean isPrerequisitesOK() {
    	if (this.selectionHandler.getSelectedEdges().size() == 1) {
    		IEdge selectedEdge = this.selectionHandler.getSelectedEdges().get(0);
    		if (selectedEdge.isTransitionPointsSupported()) {
    			return true;
    		}
        }
        return false;
    }
    
    private boolean isSelectedToolOK() {
    	GraphTool selectedTool = this.graphToolsBar.getSelectedTool();
 		if (GraphTool.SELECTION_TOOL.equals(selectedTool))
        {
            return true;
        }
 		IEdge selectedEdge = this.selectionHandler.getSelectedEdges().get(0);
 		if (selectedTool.getNodeOrEdge().getClass().isInstance(selectedEdge))
 		{
 			return true;
 		}
    	return false;
    }


    private boolean isMouseOnTransitionPoint(MouseEvent event) {
    	IEdge selectedEdge = this.selectionHandler.getSelectedEdges().get(0);
        double zoom = editorPart.getZoomFactor();
        final Point2D mousePoint = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        final double MAX_DIST = 5;
        for (Point2D aTransitionPoint : selectedEdge.getTransitionPoints()) {
            if (aTransitionPoint.distance(mousePoint) <= MAX_DIST) {
                return true;
            }
        }
        return false;
    }
    
    private void addNewTransitionPoint(MouseEvent event) {
    	double zoom = editorPart.getZoomFactor();
        final Point2D mousePoint = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
        IEdge selectedEdge = this.selectionHandler.getSelectedEdges().get(0);
        Point2D[] transitionPoints = selectedEdge.getTransitionPoints();
        List<Point2D> pointsToTest = new ArrayList<Point2D>();
        Line2D connectionPoints = selectedEdge.getConnectionPoints();
        pointsToTest.add(connectionPoints.getP1());
        pointsToTest.addAll(Arrays.asList(transitionPoints));
        pointsToTest.add(connectionPoints.getP2());
        Point2D lineToTestStartingPoint = pointsToTest.get(0);
        final double MAX_DIST = 5;
        for (int i = 1; i < pointsToTest.size(); i++)
        {
            Point2D lineToTestEndingPoint = pointsToTest.get(i);
            Line2D lineToTest = new Line2D.Double(lineToTestStartingPoint, lineToTestEndingPoint);
            if (lineToTest.ptLineDist(mousePoint) <= MAX_DIST) {
            	List<Point2D> newTransitionPointList = new ArrayList<Point2D>();
            	newTransitionPointList.addAll(Arrays.asList(transitionPoints));
            	newTransitionPointList.add(i - 1, mousePoint);
            	selectedEdge.setTransitionPoints(newTransitionPointList.toArray(new Point2D[newTransitionPointList.size()]));
            	return;
            }
            lineToTestStartingPoint = lineToTestEndingPoint;
        }
    }
    
    
    private IEditorPartSelectionHandler selectionHandler;

    private IEditorPart editorPart;

    private IGraphToolsBar graphToolsBar;

}
