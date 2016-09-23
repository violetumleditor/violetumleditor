package com.horstmann.violet.workspace.editorpart.behavior;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;

import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.editorpart.IEditorPart;

/**
 * Undo/Redo behavior triggered when node and edges are added
 * 
 * @author Alexandre de Pellegrin
 *
 */
public class UndoRedoOnAddBehavior extends AbstractEditorPartBehavior
{

    /**
     * The concerned workspace
     */
    private IEditorPart editorPart;

    /**
     * The global undo/redo behavior which contains all individual undo/redo behaviors
     */
    private UndoRedoCompoundBehavior compoundBehavior;
   
    /**
     * Keeps all the node attached to the graph before the add action
     */
    private List<INode> nodesOnGraphBeforeAdd = new ArrayList<INode>();
    
    /**
     * Keeps all the edges attached to the graph before the add action
     */
    private List<IEdge> edgesOnGraphBeforeAdd = new ArrayList<IEdge>();
    


    /**
     * Default constructor
     * @param editorPart
     * @param compoundBehavior
     */
    public UndoRedoOnAddBehavior(IEditorPart editorPart, UndoRedoCompoundBehavior compoundBehavior)
    {
        this.editorPart = editorPart;
        this.compoundBehavior = compoundBehavior;
    }

  
    
    
    @Override
    public void beforeAddingNodeAtPoint(INode node, Point2D location)
    {
        this.nodesOnGraphBeforeAdd.clear();
        this.edgesOnGraphBeforeAdd.clear();
        this.nodesOnGraphBeforeAdd.addAll(this.editorPart.getGraph().getAllNodes());
        this.edgesOnGraphBeforeAdd.addAll(this.editorPart.getGraph().getAllEdges());
    }

    @Override
    public void afterAddingNodeAtPoint(final INode node, final Point2D location)
    {
        List<INode> nodesOnGraphAfterAction = new ArrayList<INode>(this.editorPart.getGraph().getAllNodes());
        List<IEdge> edgesOnGraphAfterAction = new ArrayList<IEdge>(this.editorPart.getGraph().getAllEdges());

        List<INode> nodesReallyAdded = new ArrayList<INode>();
        nodesReallyAdded.addAll(nodesOnGraphAfterAction);
        nodesReallyAdded.removeAll(this.nodesOnGraphBeforeAdd);
        
        List<IEdge> edgesReallyAdded = new ArrayList<IEdge>();
        edgesReallyAdded.addAll(edgesOnGraphAfterAction);
        edgesReallyAdded.removeAll(this.edgesOnGraphBeforeAdd);
        
        this.compoundBehavior.startHistoryCapture();
        CompoundEdit capturedEdit = this.compoundBehavior.getCurrentCapturedEdit();

        for (final INode aSelectedNode : nodesReallyAdded)
        {
            
            
            UndoableEdit edit = new AbstractUndoableEdit()
            {
                @Override
                public void undo() throws CannotUndoException
                {
                    IGraph graph = editorPart.getGraph();
                    graph.removeNode(aSelectedNode);
                    super.undo();
                }

                @Override
                public void redo() throws CannotRedoException
                {
                    super.redo();
                    IGraph graph = editorPart.getGraph();
                    graph.addNode(aSelectedNode, aSelectedNode.getLocationOnGraph());
                }
            };
            capturedEdit.addEdit(edit);
        }
        
        for (final IEdge aSelectedEdge : edgesReallyAdded)
        {
            UndoableEdit edit = new AbstractUndoableEdit()
            {
                @Override
                public void undo() throws CannotUndoException
                {
                    IGraph graph = editorPart.getGraph();
                    graph.removeEdge(aSelectedEdge);
                    super.undo();
                }

                @Override
                public void redo() throws CannotRedoException
                {
                    super.redo();
                    IGraph graph = editorPart.getGraph();
                    graph.connect(aSelectedEdge, aSelectedEdge.getStartNode(), aSelectedEdge.getStartLocation(), aSelectedEdge.getEndNode(), aSelectedEdge.getEndLocation(), aSelectedEdge.getTransitionPoints());
                }
            };
            capturedEdit.addEdit(edit);
        }

        
        
        this.compoundBehavior.stopHistoryCapture();
        this.nodesOnGraphBeforeAdd.clear();
        this.edgesOnGraphBeforeAdd.clear();
    }

    @Override
    public void beforeAddingEdgeAtPoints(IEdge edge, Point2D startPoint, Point2D endPoint)
    {
        this.nodesOnGraphBeforeAdd.clear();
        this.edgesOnGraphBeforeAdd.clear();
        this.nodesOnGraphBeforeAdd.addAll(this.editorPart.getGraph().getAllNodes());
        this.edgesOnGraphBeforeAdd.addAll(this.editorPart.getGraph().getAllEdges());
    }
    
    @Override
    public void afterAddingEdgeAtPoints(final IEdge edge, final Point2D startPoint, final Point2D endPoint)
    {
        List<INode> nodesOnGraphAfterAction = new ArrayList<INode>(this.editorPart.getGraph().getAllNodes());
        List<IEdge> edgesOnGraphAfterAction = new ArrayList<IEdge>(this.editorPart.getGraph().getAllEdges());

        List<INode> nodesReallyAdded = new ArrayList<INode>();
        nodesReallyAdded.addAll(nodesOnGraphAfterAction);
        nodesReallyAdded.removeAll(this.nodesOnGraphBeforeAdd);
        
        List<IEdge> edgesReallyAdded = new ArrayList<IEdge>();
        edgesReallyAdded.addAll(edgesOnGraphAfterAction);
        edgesReallyAdded.removeAll(this.edgesOnGraphBeforeAdd);
        
        this.compoundBehavior.startHistoryCapture();
        CompoundEdit capturedEdit = this.compoundBehavior.getCurrentCapturedEdit();

        for (final INode aSelectedNode : nodesReallyAdded)
        {
            
            
            UndoableEdit edit = new AbstractUndoableEdit()
            {
                @Override
                public void undo() throws CannotUndoException
                {
                    IGraph graph = editorPart.getGraph();
                    graph.removeNode(aSelectedNode);
                    super.undo();
                }

                @Override
                public void redo() throws CannotRedoException
                {
                    super.redo();
                    IGraph graph = editorPart.getGraph();
                    graph.addNode(aSelectedNode, aSelectedNode.getLocationOnGraph());
                }
            };
            capturedEdit.addEdit(edit);
        }
        
        for (final IEdge aSelectedEdge : edgesReallyAdded)
        {
            UndoableEdit edit = new AbstractUndoableEdit()
            {
                @Override
                public void undo() throws CannotUndoException
                {
                    IGraph graph = editorPart.getGraph();
                    graph.removeEdge(aSelectedEdge);
                    super.undo();
                }

                @Override
                public void redo() throws CannotRedoException
                {
                    super.redo();
                    IGraph graph = editorPart.getGraph();
                    graph.connect(aSelectedEdge, aSelectedEdge.getStartNode(), aSelectedEdge.getStartLocation(), aSelectedEdge.getEndNode(), aSelectedEdge.getEndLocation(), aSelectedEdge.getTransitionPoints());
                }
            };
            capturedEdit.addEdit(edit);
        }

        
        
        this.compoundBehavior.stopHistoryCapture();
        this.nodesOnGraphBeforeAdd.clear();
        this.edgesOnGraphBeforeAdd.clear();
    }


}
