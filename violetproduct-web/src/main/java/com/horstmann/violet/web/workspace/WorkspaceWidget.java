package com.horstmann.violet.web.workspace;

import com.horstmann.violet.web.workspace.editorpart.EditorPartWidget;
import com.horstmann.violet.web.workspace.editorpart.behavior.EditSelectedBehavior;
import com.horstmann.violet.web.workspace.sidebar.SideBarWidget;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.sidebar.ISideBar;

import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WHBoxLayout;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLength.Unit;
import eu.webtoolkit.jwt.WPanel;
import eu.webtoolkit.jwt.WScrollArea;
import eu.webtoolkit.jwt.WScrollArea.ScrollBarPolicy;

public class WorkspaceWidget extends WContainerWidget {
	
	
	private SideBarWidget sideBarWidget;
	private EditorPartWidget editorPartWidget;
	private IWorkspace workspace;
	private WHBoxLayout mainLayout;
	private WPanel editorPartPanel;
	private WScrollArea editorPartScrollArea;
	
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
			this.mainLayout.addWidget(getEditorPartPanel(), 1);
		}
		return this.mainLayout;
	}
	
	private SideBarWidget getSideBarWidget() {
		if (this.sideBarWidget == null) {
			ISideBar sideBar = this.workspace.getSideBar();
			this.sideBarWidget = new SideBarWidget(sideBar, getEditorPartWidget());
			this.sideBarWidget.setWidth(new WLength(230, Unit.Pixel));
		}
		return this.sideBarWidget;
	}
	
	private EditorPartWidget getEditorPartWidget() {
		if (this.editorPartWidget == null) {
			IEditorPart editorPart = this.workspace.getEditorPart();
			this.editorPartWidget = new EditorPartWidget(editorPart);
			this.editorPartWidget.resize(2000, 768);
		}
		return this.editorPartWidget;
	}
	
	private WPanel getEditorPartPanel() {
		if (this.editorPartPanel == null) {
			this.editorPartPanel = new WPanel();
			this.editorPartPanel.setCentralWidget(getEditorPartScrollArea());
			this.editorPartPanel.setWidth(new WLength(100, Unit.Percentage));
			this.editorPartPanel.setHeight(new WLength(100, Unit.Percentage));
		}
		return this.editorPartPanel;
	}

	
	private WScrollArea getEditorPartScrollArea() {
		if (this.editorPartScrollArea == null) {
			this.editorPartScrollArea = new WScrollArea();
			this.editorPartScrollArea.setWidget(getEditorPartWidget());
			this.editorPartScrollArea.setHorizontalScrollBarPolicy(ScrollBarPolicy.ScrollBarAlwaysOn);
			this.editorPartScrollArea.setVerticalScrollBarPolicy(ScrollBarPolicy.ScrollBarAlwaysOn);
			this.editorPartScrollArea.setWidth(new WLength(100, Unit.Percentage));
			this.editorPartScrollArea.setHeight(new WLength(100, Unit.Percentage));
		}
		return this.editorPartScrollArea;
	}
}
