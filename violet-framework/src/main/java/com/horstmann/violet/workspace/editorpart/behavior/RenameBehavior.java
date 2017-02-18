package com.horstmann.violet.workspace.editorpart.behavior;

import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.IRenameableNode;

public class RenameBehavior extends AbstractEditorPartBehavior {
    public RenameBehavior(IGraph graph)
    {
        if(graph == null)
        {
            throw new NullPointerException("Graph can not be null.");
        }
        this.graph = graph;
    }

    @Override
    public void afterEditingNode(INode node)
    {
        AbstractNode editedNode;
        try
        {
            editedNode = (AbstractNode)node;
        }
        catch (ClassCastException exception)
        {
            exception.printStackTrace();
            return;
        }
        newName = editedNode.getName().toEdit();
        if(!oldName.isEmpty() && newName != null && !newName.equals(oldName))
        {
            renameNodes();
        }
    }

    @Override
    public void beforeEditingNode(INode node)
    {
        AbstractNode editedNode;
        if(node instanceof AbstractNode)
        {
            editedNode = (AbstractNode)node;
            String nodeName = editedNode.getName().toEdit();
            if (nodeName != null)
            {
                oldName = editedNode.getName().toEdit();
            }
        }
    }

    private void renameNodes() {
        for(INode node: graph.getAllNodes()) {
            if(node instanceof IRenameableNode) {
                IRenameableNode renameableNode = (IRenameableNode)node;
                renameableNode.replaceNodeOccurrences(oldName, newName);
            }
        }
    }

    private String oldName="";
    private String newName="";
    private IGraph graph;
}