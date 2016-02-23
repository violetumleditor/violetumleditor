package com.horstmann.violet.workspace.editorpart.behavior;

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
 * Undo/Redo behavior triggered when node and edges are removed
 * 
 * @author Alexandre de Pellegrin
 *
 */
public class UndoRedoOnRemoveBehavior extends AbstractEditorPartBehavior
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
     * Keeps all the node attached to the graph before the remove action
     */
    private List<INode> nodesOnGraphBeforeRemove = new ArrayList<INode>();

    /**
     * Keeps all the edges attached to the graph before the remove action
     */
    private List<IEdge> edgesOnGraphBeforeRemove = new ArrayList<IEdge>();

    /**
     * Default constructor
     * 
     * @param editorPart
     * @param compoundBehavior
     */
    public UndoRedoOnRemoveBehavior(IEditorPart editorPart, UndoRedoCompoundBehavior compoundBehavior)
    {
        this.editorPart = editorPart;
        this.compoundBehavior = compoundBehavior;
    }

    @Override
    public void beforeRemovingSelectedElements()
    {
        this.nodesOnGraphBeforeRemove.clear();
        this.edgesOnGraphBeforeRemove.clear();
        this.nodesOnGraphBeforeRemove.addAll(this.editorPart.getGraph().getAllNodes());
        this.edgesOnGraphBeforeRemove.addAll(this.editorPart.getGraph().getAllEdges());
    }

    @Override
    public void afterRemovingSelectedElements()
    {
        List<INode> nodesOnGraphAfterAction = new ArrayList<INode>(this.editorPart.getGraph().getAllNodes());
        List<IEdge> edgesOnGraphAfterAction = new ArrayList<IEdge>(this.editorPart.getGraph().getAllEdges());

        List<INode> nodesReallyRemoved = new ArrayList<INode>();
        nodesReallyRemoved.addAll(this.nodesOnGraphBeforeRemove);
        nodesReallyRemoved.removeAll(nodesOnGraphAfterAction);

        List<IEdge> edgesReallyRemoved = new ArrayList<IEdge>();
        edgesReallyRemoved.addAll(this.edgesOnGraphBeforeRemove);
        edgesReallyRemoved.removeAll(edgesOnGraphAfterAction);

        this.compoundBehavior.startHistoryCapture();
        CompoundEdit capturedEdit = this.compoundBehavior.getCurrentCapturedEdit();

        for (final IEdge aSelectedEdge : edgesReallyRemoved)
        {
            UndoableEdit edit = new AbstractUndoableEdit()
            {
                @Override
                public void undo() throws CannotUndoException
                {
                    IGraph graph = editorPart.getGraph();
                    graph.connect(aSelectedEdge, aSelectedEdge.getStartNode(), aSelectedEdge.getStartLocation(),
                            aSelectedEdge.getEndNode(), aSelectedEdge.getEndLocation(), aSelectedEdge.getTransitionPoints());
                    super.undo();
                }

                @Override
                public void redo() throws CannotRedoException
                {
                    super.redo();
                    IGraph graph = editorPart.getGraph();
                    graph.removeEdge(aSelectedEdge);
                }
            };
            capturedEdit.addEdit(edit);
        }

        List<INode> filteredNodes = removeChildren(nodesReallyRemoved);
        for (final INode aSelectedNode : filteredNodes)
        {

            UndoableEdit edit = new AbstractUndoableEdit()
            {
                @Override
                public void undo() throws CannotUndoException
                {
                    IGraph graph = editorPart.getGraph();
                    graph.addNode(aSelectedNode, aSelectedNode.getLocationOnGraph());
                    super.undo();
                }

                @Override
                public void redo() throws CannotRedoException
                {
                    super.redo();
                    IGraph graph = editorPart.getGraph();
                    graph.removeNode(aSelectedNode);
                }
            };
            capturedEdit.addEdit(edit);
        }

        this.compoundBehavior.stopHistoryCapture();
        this.nodesOnGraphBeforeRemove.clear();
        this.edgesOnGraphBeforeRemove.clear();
    }

    /**
     * Checks if ancestorNode is a parent node_old of child node_old
     * 
     * @param childNode
     * @param ancestorNode
     * @return b
     */
    private boolean isAncestorRelationship(INode childNode, INode ancestorNode)
    {
        INode parent = childNode.getParent();
        if (parent == null)
        {
            return false;
        }
        List<INode> fifo = new ArrayList<INode>();
        fifo.add(parent);
        while (!fifo.isEmpty())
        {
            INode aParentNode = fifo.get(0);
            fifo.remove(0);
            if (aParentNode.equals(ancestorNode))
            {
                return true;
            }
            INode aGranParent = aParentNode.getParent();
            if (aGranParent != null)
            {
                fifo.add(aGranParent);
            }
        }
        return false;
    }

    /**
     * Takes a list of node and removes from this list all node which have ancestors node_old in this list.<br/>
     * 
     * @param nodes the list to filter
     * @return the filtered list
     */
    private List<INode> removeChildren(List<INode> nodes)
    {
        List<INode> result = new ArrayList<INode>();
        for (INode aNode : nodes)
        {
            boolean isOrphelin = true;
            for (INode aParent : nodes)
            {
                boolean isAncestorRelationship = isAncestorRelationship(aNode, aParent);
                if (isAncestorRelationship)
                {
                    isOrphelin = false;
                }
            }
            if (isOrphelin)
            {
                result.add(aNode);
            }
        }
        return result;
    }

}
