package com.horstmann.violet.workspace.editorpart.behavior;

import com.horstmann.violet.product.diagram.abstracts.IColorable;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;

/**
 * Describes all editors events which can be listen to.
 */
public interface IEditorPartBehavior
{
    
    /**
     * Action performed when mouse is pressed.
     * @param event mouse event.
     */
    void onMousePressed(MouseEvent event);

    /**
     * Action performed when mouse is dragged (pressed and moved).
     * @param event mouse event.
     */
    void onMouseDragged(MouseEvent event);

    /**
     * Action performed when mouse is released.
     * @param event mouse event.
     */
    void onMouseReleased(MouseEvent event);

    /**
     * Action performed when mouse is clicked (pressed and released).
     * @param event mouse event.
     */
    void onMouseClicked(MouseEvent event);

    /**
     * Action performed when mouse is moved.
     * @param event mouse event.
     */
    void onMouseMoved(MouseEvent event);

    /**
     * Action performed when mouse wheel is rotated.
     * @param event mouse wheel event.
     */
    void onMouseWheelMoved(MouseWheelEvent event);

    /**
     * Action performed when specified tool is selected.
     * @param selectedTool selected graph tool.
     */
    void onToolSelected(GraphTool selectedTool);

    /**
     * Action performed when specified node is selected.
     * @param node selected node.
     */
    void onNodeSelected(INode node);

    /**
     * Action performed when specified edge is selected.
     * @param edge selected edge.
     */
    void onEdgeSelected(IEdge edge);

    /**
     * Action performed before editing specified node.
     * @param node edited node.
     */
    void beforeEditingNode(INode node);

    /**
     * Action performed while editing specified node.
     * @param node edited node.
     */
    void whileEditingNode(INode node, PropertyChangeEvent event);

    /**
     * Action performed after editing specified node.
     * @param node edited node.
     */
    void afterEditingNode(INode node);

    /**
     * Action performed before editing specified edge.
     * @param edge edited edge.
     */
    void beforeEditingEdge(IEdge edge);

    /**
     * Action performed while editing specified edge.
     * @param edge edited edge.
     */
    void whileEditingEdge(IEdge edge, PropertyChangeEvent event);

    /**
     * Action performed after editing specified edge.
     * @param edge edited edge.
     */
    void afterEditingEdge(IEdge edge);

    /**
     * Action performed before selected elements are removed.
     */
    void beforeRemovingSelectedElements();

    /**
     * Action performed after selected elements are removed.
     */
    void afterRemovingSelectedElements();

    /**
     * Action performed before specified node is added.
     * @param node added node.
     * @param location location of added node.
     */
    void beforeAddingNodeAtPoint(INode node, Point2D location);

    /**
     * Action performed after specified node is added.
     * @param node added node.
     * @param location location of added node.
     */
    void afterAddingNodeAtPoint(INode node, Point2D location);

    /**
     * Action performed before specified edge is added.
     * @param edge added edge.
     * @param startPoint start edge point.
     * @param endPoint end edge point.
     */
    void beforeAddingEdgeAtPoints(IEdge edge, Point2D startPoint, Point2D endPoint);

    /**
     * Action performed after specified edge is added.
     * @param edge added edge.
     * @param startPoint start edge point.
     * @param endPoint end edge point.
     */
    void afterAddingEdgeAtPoints(IEdge edge, Point2D startPoint, Point2D endPoint);

    /**
     * Action performed before transition points on the edge are changed.
     * @param edge edge which points are changed.
     */
    void beforeChangingTransitionPointsOnEdge(IEdge edge);

    /**
     * Action performed after transition points on the edge are changed.
     * @param edge edge which points are changed.
     */
    void afterChangingTransitionPointsOnEdge(IEdge edge);

    /**
     * Action performed before color of element is changed.
     * @param element colored element.
     */
    void beforeChangingColorOnElement(IColorable element);

    /**
     * Action performed after color of element is changed.
     * @param element colored element.
     */
    void afterChangingColorOnElement(IColorable element);

    /**
     * Action performed when specified graphic is painted.
     * @param g2 painted graphic.
     */
    void onPaint(Graphics2D g2);
    
}
