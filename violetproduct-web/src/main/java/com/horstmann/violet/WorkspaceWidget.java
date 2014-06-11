package com.horstmann.violet;

import com.horstmann.violet.framework.file.IGraphFile;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.Workspace;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;

import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WHBoxLayout;

public class WorkspaceWidget extends WContainerWidget {
	
	private IWorkspace workspace;

	private WHBoxLayout mainLayout;
	private EditorPartWidget editorPartWidget;
	private GraphToolsBarWidget graphToolsBarWidget;

	public WorkspaceWidget(IWorkspace workspace) {
		super();
		this.workspace = workspace;
		this.workspace.getAWTComponent().setSize(800, 600);
		this.workspace.getAWTComponent().prepareLayout();
		setLayout(getMainLayout());
		addSpecificBehavior();
	}
	
	private void addSpecificBehavior() {
		EditSelectedBehavior editSelectedBehavior = new EditSelectedBehavior(this.workspace.getEditorPart(), editorPartWidget);
		IEditorPartBehaviorManager behaviorManager = this.workspace.getEditorPart().getBehaviorManager();
		behaviorManager.addBehavior(editSelectedBehavior);
	}
	
	private WHBoxLayout getMainLayout() {
		if (this.mainLayout == null) {
			this.mainLayout = new WHBoxLayout();
			this.mainLayout.addWidget(getGraphToolsBarWidget());
			this.mainLayout.addWidget(getEditorPartWidget());
		}
		return this.mainLayout;
	}
	
	private EditorPartWidget getEditorPartWidget() {
		if (this.editorPartWidget == null) {
			this.editorPartWidget = new EditorPartWidget(this.workspace.getEditorPart());
			this.editorPartWidget.resize(1024, 768);
		}
		return this.editorPartWidget;
	}
	
	
	private GraphToolsBarWidget getGraphToolsBarWidget() {
		if (this.graphToolsBarWidget == null) {
			this.graphToolsBarWidget = new GraphToolsBarWidget(workspace.getSideBar().getGraphToolsBar(), this);
		}
		return this.graphToolsBarWidget;
	}

}
