package com.horstmann.violet.web.workspace.sidebar;

import com.horstmann.violet.web.workspace.editorpart.EditorPartWidget;
import com.horstmann.violet.workspace.sidebar.ISideBar;

import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WVBoxLayout;

public class SideBarWidget extends WContainerWidget {
	
	private EditorToolsWidget editorToolsWidget;
	private GraphToolsBarWidget graphToolsBarWidget;
	private WContainerWidget colorToolsWidget;
	private WVBoxLayout mainLayout;

	
	private ISideBar sideBar;
	private EditorPartWidget editorPartWidget;


	public SideBarWidget(ISideBar sideBar, EditorPartWidget editorPartWidget) {
		super();
		this.sideBar = sideBar;
		this.editorPartWidget = editorPartWidget;
		setLayout(getMainLayout());
		setStyleClass("sidebar");
	}
	

	
	
	private WVBoxLayout getMainLayout() {
		if (this.mainLayout == null) {
			this.mainLayout = new WVBoxLayout();
			this.mainLayout.addWidget(getEditorToolsWidget());
			this.mainLayout.addWidget(getGraphToolsBarWidget());
			this.mainLayout.addWidget(getColorToolsWidget(), 1);
			this.mainLayout.setContentsMargins(0, 0, 0, 0);
		}
		return this.mainLayout;
	}
	
	

	
	
	private EditorToolsWidget getEditorToolsWidget() {
		if (this.editorToolsWidget == null) {
			this.editorToolsWidget = new EditorToolsWidget(this.editorPartWidget);
		}
		return this.editorToolsWidget;
	}
	
	private GraphToolsBarWidget getGraphToolsBarWidget() {
		if (this.graphToolsBarWidget == null) {
			this.graphToolsBarWidget = new GraphToolsBarWidget(this.sideBar.getGraphToolsBar(), this);
		}
		return this.graphToolsBarWidget;
	}
	
	private WContainerWidget getColorToolsWidget() {
		if (this.colorToolsWidget == null) {
			this.colorToolsWidget = new WContainerWidget();
		}
		return this.colorToolsWidget;
	}
	
	
}
