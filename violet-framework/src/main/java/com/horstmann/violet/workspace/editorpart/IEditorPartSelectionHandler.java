package com.horstmann.violet.workspace.editorpart;

import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.ISelectable;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;

public interface IEditorPartSelectionHandler {

	public abstract void setSelectedElement(ISelectable selectableGraphElement);

	public abstract void addSelectedElement(ISelectable selectableGraphElement);

	public abstract void removeElementFromSelection(ISelectable selectableGraphElement);

	public abstract boolean isElementAlreadySelected(ISelectable selectableGraphElement);

	public abstract void clearSelection();

	public abstract ISelectable getLastSelectedElement();

	public abstract boolean isElementSelectedAtLeast();

	public abstract List<ISelectable> getSelectedElements();

	public abstract void setSelectedTool(GraphTool graphTool);
	
	public abstract GraphTool getSelectedTool();

}