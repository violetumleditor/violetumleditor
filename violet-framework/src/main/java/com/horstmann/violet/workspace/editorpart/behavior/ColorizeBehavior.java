package com.horstmann.violet.workspace.editorpart.behavior;

import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.IColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.sidebar.colortools.ColorChoice;
import com.horstmann.violet.workspace.sidebar.colortools.IColorChoiceBar;
import com.horstmann.violet.workspace.sidebar.colortools.IColorChoiceChangeListener;

public class ColorizeBehavior extends AbstractEditorPartBehavior
{

    public ColorizeBehavior(final IWorkspace workspace, final IColorChoiceBar colorChoiceBar)
    {
        this.behaviorManager = workspace.getEditorPart().getBehaviorManager();
        colorChoiceBar.addColorChoiceChangeListener(new IColorChoiceChangeListener()
        {

            @Override
            public void onColorChoiceChange(ColorChoice newColorChoice)
            {
                List<INode> selectedNodes = workspace.getEditorPart().getSelectedNodes();
                List<IEdge> selectedEdges = workspace.getEditorPart().getSelectedEdges();
            	for (INode node : selectedNodes) {
            		if (node != null && IColorableNode.class.isInstance(node)) {
                    	IColorableNode colorableElement = (IColorableNode) node;
                    	updateColor(colorableElement, newColorChoice);
            		}
            	}
            	for (IEdge edge : selectedEdges) {
            		if (edge != null && IColorableNode.class.isInstance(edge)) {
                    	IColorableNode colorableElement = (IColorableNode) edge;
                    	updateColor(colorableElement, newColorChoice);
            		}
            	}
            }
        });
    }
    
    
    private void updateColor(IColorableNode colorableElement, ColorChoice currentColorChoice) {
    	this.behaviorManager.fireBeforeChangingColorOnElement(colorableElement);
        colorableElement.setBackgroundColor(currentColorChoice.getBackgroundColor());
        colorableElement.setBorderColor(currentColorChoice.getBorderColor());
        colorableElement.setTextColor(currentColorChoice.getTextColor());
        this.behaviorManager.fireAfterChangingColorOnElement(colorableElement);
    }



    private IEditorPartBehaviorManager behaviorManager;

}
