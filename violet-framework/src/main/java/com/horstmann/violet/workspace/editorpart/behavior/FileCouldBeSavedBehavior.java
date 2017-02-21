package com.horstmann.violet.workspace.editorpart.behavior;

import com.horstmann.violet.framework.file.IGraphFile;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class FileCouldBeSavedBehavior extends AbstractEditorPartBehavior
{

    public FileCouldBeSavedBehavior(IGraphFile graphFile)
    {
        this.graphFile = graphFile;
    }

    @Override
    public void afterAddingEdgeAtPoints(IEdge edge, Point2D startPoint, Point2D endPoint)
    {
        graphFile.setSaveRequired();
    }

    @Override
    public void afterAddingNodeAtPoint(INode node, Point2D location)
    {
        graphFile.setSaveRequired();
    }

    @Override
    public void afterEditingEdge(IEdge edge)
    {
        graphFile.setSaveRequired();
    }

    @Override
    public void afterEditingNode(INode node)
    {
        graphFile.setSaveRequired();
    }

    @Override
    public void afterRemovingSelectedElements()
    {
        graphFile.setSaveRequired();
    }


    @Override
    public void onMouseDragged(MouseEvent event)
    {
        graphFile.setSaveRequired();
        // FIXME : when behaviorHandler will manage new events such as onNodeDragged
    }

    private IGraphFile graphFile;
    
}
