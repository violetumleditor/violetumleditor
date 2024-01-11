package com.horstmann.violet.workspace.editorpart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.ISelectable;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;

public class EditorPartSelectionHandler implements IEditorPartSelectionHandler
{

    public void setSelectedElement(ISelectable selectableGraphElement)
    {
        this.selectedElements.clear();
        addSelectedElement(selectableGraphElement);
    }


    public void updateSelectedElements(ISelectable[] selectableGraphElement)
    {
        for (int i = 0; i < selectableGraphElement.length; i++)
        {
            if (isElementAlreadySelected(selectableGraphElement[i]))
            {
                addSelectedElement(selectableGraphElement[i]);
            }
        }
    }


    public void addSelectedElement(ISelectable selectableGraphElement)
    {
        if (this.selectedElements.contains(selectableGraphElement))
        {
            this.removeElementFromSelection(selectableGraphElement);
        }
        this.selectedElements.add(selectableGraphElement);
    }


    public void removeElementFromSelection(ISelectable selectableGraphElement)
    {
        if (this.selectedElements.contains(selectableGraphElement))
        {
            int i = this.selectedElements.indexOf(selectableGraphElement);
            this.selectedElements.remove(i);
        }
    }

    public boolean isElementAlreadySelected(ISelectable selectableGraphElement)
    {
        if (this.selectedElements.contains(selectableGraphElement)) return true;
        return false;
    }

    public void clearSelection()
    {
        this.selectedElements.clear();
    }

    public ISelectable getLastSelectedElement()
    {
        return getLastElement(this.selectedElements);
    }


    public boolean isElementSelectedAtLeast()
    {
        return this.selectedElements.size() > 0;
    }


    public List<ISelectable> getSelectedElements()
    {
        return Collections.unmodifiableList(this.selectedElements);
    }

    @Override
    public GraphTool getSelectedTool()
    {
        return this.selectedTool;
    }

    @Override
    public void setSelectedTool(GraphTool graphTool)
    {
        this.selectedTool = graphTool;
    }
    
    private <T> T getLastElement(List<T> list)
    {
        int size = list.size();
        if (size <= 0)
        {
            return null;
        }
        return list.get(size - 1);
    }

    private List<ISelectable> selectedElements = new ArrayList<ISelectable>();
    
    private GraphTool selectedTool;

}
