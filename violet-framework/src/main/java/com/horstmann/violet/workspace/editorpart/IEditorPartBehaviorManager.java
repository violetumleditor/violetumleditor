package com.horstmann.violet.workspace.editorpart;

import com.horstmann.violet.product.diagram.abstracts.IColorable;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.editorpart.behavior.IEditorPartBehavior;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.util.List;

public interface IEditorPartBehaviorManager
{

    public abstract void addBehavior(IEditorPartBehavior newBehavior);

    public abstract List<IEditorPartBehavior> getBehaviors();

    public abstract <T extends IEditorPartBehavior> List<T> getBehaviors(Class<T> type);

    public abstract void fireOnMousePressed(MouseEvent event);

    public abstract void fireOnMouseDragged(MouseEvent event);

    public abstract void fireOnMouseReleased(MouseEvent event);

    public abstract void fireOnMouseClicked(MouseEvent event);

    public abstract void fireOnMouseMoved(MouseEvent event);

    public abstract void fireOnMouseWheelMoved(MouseWheelEvent event);

    public abstract void fireBeforeEditingNode(INode node);

    public abstract void fireWhileEditingNode(INode node, PropertyChangeEvent event);

    public abstract void fireAfterEditingNode(INode node);

    public abstract void fireBeforeEditingEdge(IEdge edge);

    public abstract void fireWhileEditingEdge(IEdge edge, PropertyChangeEvent event);

    public abstract void fireAfterEditingEdge(IEdge edge);

    public abstract void fireBeforeRemovingSelectedElements();

    public abstract void fireAfterRemovingSelectedElements();

    public abstract void fireBeforeAddingNodeAtPoint(INode node, Point2D location);

    public abstract void fireAfterAddingNodeAtPoint(INode node, Point2D location);

    public abstract void fireBeforeAddingEdgeAtPoints(IEdge edge, Point2D startPoint, Point2D endPoint);

    public abstract void fireAfterAddingEdgeAtPoints(IEdge edge, Point2D startPoint, Point2D endPoint);

    public abstract void fireOnNodeSelected(INode node);

    public abstract void fireOnEdgeSelected(IEdge edge);

    public abstract void fireBeforeChangingTransitionPointsOnEdge(IEdge edge);

    public abstract void fireAfterChangingTransitionPointsOnEdge(IEdge edge);

    public abstract void fireBeforeChangingColorOnElement(IColorable element);

    public abstract void fireAfterChangingColorOnElement(IColorable element);

}
