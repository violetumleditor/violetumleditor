package com.horstmann.violet.web.workspace;

import com.horstmann.violet.web.workspace.editorpart.EditorPartWidget;
import com.horstmann.violet.web.workspace.editorpart.behavior.EditSelectedBehavior;
import com.horstmann.violet.web.workspace.sidebar.SideBarWidget;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.sidebar.ISideBar;

import eu.webtoolkit.jwt.Key;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WHBoxLayout;
import eu.webtoolkit.jwt.WKeyEvent;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLength.Unit;
import eu.webtoolkit.jwt.WScrollArea;
import eu.webtoolkit.jwt.WScrollArea.ScrollBarPolicy;

public class WorkspaceWidget extends WContainerWidget {
	
	
	private SideBarWidget sideBarWidget;
	private EditorPartWidget editorPartWidget;
	private IWorkspace workspace;
	private WHBoxLayout mainLayout;
	private WScrollArea editorPartScrollArea;
	
	public WorkspaceWidget(IWorkspace workspace) {
		super();
		this.workspace = workspace;
		setLayout(getMainLayout());
		addSpecificBehavior();
		setWidth(new WLength(100,  Unit.Percentage));
		setHeight(new WLength(100,  Unit.Percentage));
	}
	
	public IWorkspace getWorkspace() {
		return this.workspace;
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
			this.mainLayout.addWidget(getEditorPartScrollArea());
			this.mainLayout.setContentsMargins(0, 0, 0, 0);
		}
		return this.mainLayout;
	}
	
	private SideBarWidget getSideBarWidget() {
		if (this.sideBarWidget == null) {
			ISideBar sideBar = this.workspace.getSideBar();
			this.sideBarWidget = new SideBarWidget(sideBar, getEditorPartWidget());
			this.sideBarWidget.setMinimumSize(new WLength(230, Unit.Pixel), new WLength(100, Unit.Percentage));
			this.sideBarWidget.setWidth(new WLength(230, Unit.Pixel));
			this.sideBarWidget.setHeight(new WLength(100, Unit.Percentage));
		}
		return this.sideBarWidget;
	}
	
	public EditorPartWidget getEditorPartWidget() {
		if (this.editorPartWidget == null) {
			IEditorPart editorPart = this.workspace.getEditorPart();
			this.editorPartWidget = new EditorPartWidget(editorPart);
			this.editorPartWidget.setWidth(new WLength(100, Unit.Percentage));
			this.editorPartWidget.setHeight(new WLength(100, Unit.Percentage));
		}
		return this.editorPartWidget;
	}
	
	
	private WScrollArea getEditorPartScrollArea() {
		if (this.editorPartScrollArea == null) {
			this.editorPartScrollArea = new WScrollArea();
			this.editorPartScrollArea.setWidget(getEditorPartWidget());
			this.editorPartScrollArea.setHorizontalScrollBarPolicy(ScrollBarPolicy.ScrollBarAlwaysOff);
			this.editorPartScrollArea.setVerticalScrollBarPolicy(ScrollBarPolicy.ScrollBarAlwaysOff);
			this.editorPartScrollArea.setWidth(new WLength(100, Unit.Percentage));
			this.editorPartScrollArea.setHeight(new WLength(100, Unit.Percentage));
		}
		return this.editorPartScrollArea;
	}
	
	
	
}
