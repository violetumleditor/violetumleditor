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
import java.io.InputStream;

import javax.servlet.ServletOutputStream;

import org.apache.commons.io.IOUtils;

import com.horstmann.violet.framework.file.GraphFile;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.plugin.PluginLoader;
import com.horstmann.violet.product.diagram.classes.ClassDiagramGraph;
import com.horstmann.violet.web.workspace.WorkspaceWidget;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.Workspace;

import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WBootstrapTheme;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLink;
import eu.webtoolkit.jwt.WLink.Type;
import eu.webtoolkit.jwt.WResource;
import eu.webtoolkit.jwt.WLength.Unit;
import eu.webtoolkit.jwt.servlet.WebRequest;
import eu.webtoolkit.jwt.servlet.WebResponse;

/**
 * A program for editing UML diagrams.
 */
public class UMLEditorWebApplication extends WApplication {

	private static boolean FACTORY_INITIALIZED = false;

	@InjectedBean
	private PluginLoader pluginLoader;

	/**
	 * Default constructor
	 * 
	 * @param filesToOpen
	 * @throws IOException
	 */
	public UMLEditorWebApplication(WEnvironment env) throws IOException {
		super(env);
		createDefaultWorkspace();
		
	}

	private void createDefaultWorkspace() throws IOException {
		setTheme(new WBootstrapTheme());
		useStyleSheet(new WLink(new WResource() {
			@Override
			protected void handleRequest(WebRequest request, WebResponse response) throws IOException {
				ClassLoader classLoader = this.getClass().getClassLoader();
				ServletOutputStream outputStream = response.getOutputStream();
				response.setContentType("text/css");
				InputStream inputStream = classLoader.getResourceAsStream("/violet.css");
				IOUtils.copy(inputStream, outputStream);
				inputStream.close();
				outputStream.close();
			}
			
		}));
		//URL resource = getClass().getResource("test.class.violet.html");
		//IFile aFile = new LocalFile(new File(resource.getFile()));
		GraphFile graphFile = new GraphFile(ClassDiagramGraph.class);
		IWorkspace workspace = new Workspace(graphFile);
		workspace.getAWTComponent().setSize(800, 600);
		workspace.getAWTComponent().prepareLayout();
		WorkspaceWidget workspaceWidget = new WorkspaceWidget(workspace);
		WContainerWidget root = getRoot();
		root.setWidth(new WLength(100,  Unit.Percentage));
		root.setWidth(new WLength(100,  Unit.Percentage));
		root.setStyleClass("root");
		root.addWidget(workspaceWidget);
		root.mouseMoved().setBlocked(true);
		
	}

}