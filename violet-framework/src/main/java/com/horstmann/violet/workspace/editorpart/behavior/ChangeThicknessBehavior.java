package com.horstmann.violet.workspace.editorpart.behavior;

import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.ISelectable;
import com.horstmann.violet.product.diagram.abstracts.edge.AbstractEdge;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.sidebar.thicknesstools.IThicknessChangeListener;
import com.horstmann.violet.workspace.sidebar.thicknesstools.IThicknessChoiceBar;

public class ChangeThicknessBehavior extends AbstractEditorPartBehavior
{

    public ChangeThicknessBehavior(final IWorkspace workspace, final IThicknessChoiceBar thicknessChoiceBar)
    {
        this.behaviorManager = workspace.getEditorPart().getBehaviorManager();
        thicknessChoiceBar.addThicknessChangeListener(new IThicknessChangeListener()
        {
            @Override
            public void onThicknessChange(int newThickness)
            {
                List<ISelectable> selectedElements = workspace.getEditorPart().getSelectionHandler().getSelectedElements();
                for (ISelectable element : selectedElements)
                {
                    if (element instanceof AbstractNode)
                    {
                        ((AbstractNode) element).setBorderWidth(newThickness);
                    }
                    if (element instanceof AbstractEdge)
                    {
                        ((AbstractEdge) element).setBorderWidth(newThickness);
                    }
                }
                workspace.getEditorPart().getSwingComponent().repaint();
            }
        });
    }

    private IEditorPartBehaviorManager behaviorManager;
}
