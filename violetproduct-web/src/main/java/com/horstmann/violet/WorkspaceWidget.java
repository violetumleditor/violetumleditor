package com.horstmann.violet;

import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.sidebar.ISideBar;

import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WHBoxLayout;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLength.Unit;

public class WorkspaceWidget extends WContainerWidget {
	
	
	private SideBarWidget sideBarWidget;
	private EditorPartWidget editorPartWidget;
	private IWorkspace workspace;
	private WHBoxLayout mainLayout;
	
	public WorkspaceWidget(IWorkspace workspace) {
		super();
		this.workspace = workspace;
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
			this.mainLayout.addWidget(getSideBarWidget());
			this.mainLayout.addWidget(getEditorPartWidget(), 1);
		}
		return this.mainLayout;
	}
	
	private SideBarWidget getSideBarWidget() {
		if (this.sideBarWidget == null) {
			ISideBar sideBar = this.workspace.getSideBar();
			this.sideBarWidget = new SideBarWidget(sideBar);
			this.sideBarWidget.setWidth(new WLength(150, Unit.Pixel));
		}
		return this.sideBarWidget;
	}
	
	private EditorPartWidget getEditorPartWidget() {
		if (this.editorPartWidget == null) {
			IEditorPart editorPart = this.workspace.getEditorPart();
			this.editorPartWidget = new EditorPartWidget(editorPart);
			this.editorPartWidget.resize(1024, 768);
		}
		return this.editorPartWidget;
	}
	

}
