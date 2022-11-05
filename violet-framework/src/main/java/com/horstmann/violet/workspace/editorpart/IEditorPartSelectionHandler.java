package com.horstmann.violet.workspace.editorpart;

import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.ISelectableGraphElement;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;

public interface IEditorPartSelectionHandler {

	public abstract void setSelectedElement(ISelectableGraphElement selectableGraphElement);

	public abstract void addSelectedElement(ISelectableGraphElement selectableGraphElement);

	public abstract void removeElementFromSelection(ISelectableGraphElement selectableGraphElement);

	public abstract boolean isElementAlreadySelected(ISelectableGraphElement selectableGraphElement);

	public abstract void clearSelection();

	public abstract ISelectableGraphElement getLastSelectedElement();

	public abstract boolean isElementSelectedAtLeast();

	public abstract List<ISelectableGraphElement> getSelectedElements();

	public abstract void setSelectedTool(GraphTool graphTool);
	
	public abstract GraphTool getSelectedTool();

}