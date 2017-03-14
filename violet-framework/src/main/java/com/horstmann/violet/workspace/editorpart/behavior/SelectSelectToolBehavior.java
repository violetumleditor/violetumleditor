package com.horstmann.violet.workspace.editorpart.behavior;

import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;

public class SelectSelectToolBehavior extends AbstractEditorPartBehavior{
	
	private IGraphToolsBar graphToolsBar;
	
	public SelectSelectToolBehavior(IGraphToolsBar graphToolsBar){
		this.graphToolsBar = graphToolsBar;
	}
	
	public void selectSelectTool(){
		this.graphToolsBar.reset();
	}
}
