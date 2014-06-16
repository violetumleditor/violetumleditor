package com.horstmann.violet;

import com.horstmann.violet.workspace.sidebar.ISideBar;

import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WVBoxLayout;

public class SideBarWidget extends WContainerWidget {
	
	private GraphToolsBarWidget graphToolsBarWidget;

	private ISideBar sideBar;
	
	private WVBoxLayout mainLayout;

	public SideBarWidget(ISideBar sideBar) {
		super();
		this.sideBar = sideBar;
		setLayout(getMainLayout());
	}
	
	private WVBoxLayout getMainLayout() {
		if (this.mainLayout == null) {
			this.mainLayout = new WVBoxLayout();
			this.mainLayout.addWidget(getGraphToolsBarWidget());
		}
		return this.mainLayout;
	}
	
	
	private GraphToolsBarWidget getGraphToolsBarWidget() {
		if (this.graphToolsBarWidget == null) {
			this.graphToolsBarWidget = new GraphToolsBarWidget(this.sideBar.getGraphToolsBar(), this);
		}
		return this.graphToolsBarWidget;
	}
	
	
}
