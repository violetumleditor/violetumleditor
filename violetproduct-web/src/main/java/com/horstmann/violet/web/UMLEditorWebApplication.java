/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2002 Cay S. Horstmann (http://horstmann.com)

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.web;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.horstmann.violet.framework.file.GraphFile;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.plugin.PluginLoader;
import com.horstmann.violet.product.diagram.classes.ClassDiagramGraph;
import com.horstmann.violet.web.workspace.WorkspaceWidget;
import com.horstmann.violet.web.workspace.editorpart.EditorPartWidget;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.Workspace;
import com.horstmann.violet.workspace.editorpart.IEditorPart;

import eu.webtoolkit.jwt.Key;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WBootstrapTheme;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WKeyEvent;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLength.Unit;
import eu.webtoolkit.jwt.WLink;
import eu.webtoolkit.jwt.WMemoryResource;
import eu.webtoolkit.jwt.WWidget;

/**
 * A program for editing UML diagrams.
 */
public class UMLEditorWebApplication extends WApplication {

	@InjectedBean
	private PluginLoader pluginLoader;

	/**
	 * Default constructor
	 * 
	 * @param filesToOpen
	 * @throws IOException
	 */
	public UMLEditorWebApplication(WEnvironment env) {
		super(env);
	}

	
	public void init() throws IOException {
		setConfiguration();
		createDefaultWorkspace();
		addGlobalKeyListener();
	}
	
	private void setConfiguration() throws IOException {
		WMemoryResource faviconResource = new WMemoryResource("image/png");
		faviconResource.setData(IOUtils.resourceToByteArray("/violet.png"));
		getEnvironment().getServer().getConfiguration().setFavicon(faviconResource.getUrl());
	}
	
	private void createDefaultWorkspace() throws IOException {
		WBootstrapTheme theme = new WBootstrapTheme();
		setTheme(theme);
		WMemoryResource cssResource = new WMemoryResource("text/css");
		cssResource.setData(IOUtils.resourceToByteArray("/violet.css"));
		useStyleSheet(new WLink(cssResource));
		
		GraphFile graphFile = new GraphFile(ClassDiagramGraph.class);
		IWorkspace workspace = new Workspace(graphFile);
		workspace.getAWTComponent().prepareLayout();
		WorkspaceWidget workspaceWidget = new WorkspaceWidget(workspace);
		WContainerWidget root = getRoot();
		root.setWidth(new WLength(100,  Unit.Percentage));
		root.setHeight(new WLength(100,  Unit.Percentage));
		root.setStyleClass("root");
		root.addWidget(workspaceWidget);
		root.mouseMoved().setBlocked(true);
	}
	
	
	private void addGlobalKeyListener() {
		globalKeyWentUp().addListener(this, (WKeyEvent event) -> {
			Key key = event.getKey();
			List<WWidget> children = getRoot().getChildren();
			for (WWidget aWidget : children) {
				if (WorkspaceWidget.class.isInstance(aWidget)) {
					WorkspaceWidget workspaceWidget = (WorkspaceWidget) aWidget;
					if (Key.Key_Delete.equals(key)) {
						IWorkspace workspace = workspaceWidget.getWorkspace();
						IEditorPart editorPart = workspace.getEditorPart();
						editorPart.removeSelected();
						EditorPartWidget editorPartWidget = workspaceWidget.getEditorPartWidget();
						editorPartWidget.update();
					}
				}
			}
		});
	}
	


}