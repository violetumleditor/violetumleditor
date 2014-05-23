package com.horstmann.violet;

import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;

import eu.webtoolkit.jwt.WCompositeWidget;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WImage;
import eu.webtoolkit.jwt.WPushButton;

public class GraphToolsBarWidget extends WCompositeWidget {

	private IGraphToolsBar graphToolsBar;
	
	public GraphToolsBarWidget(IGraphToolsBar graphToolsBar, WContainerWidget parent) {
		super(parent);
		this.graphToolsBar = graphToolsBar;
		WPushButton pushButton = new WPushButton();
		
		
	}
	
	

}
