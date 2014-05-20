package com.horstmann.violet.vaadin;

import java.io.File;
import java.net.URL;

import com.horstmann.violet.UMLEditorWebServlet;
import com.horstmann.violet.framework.file.GraphFile;
import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.LocalFile;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.Workspace;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;

public class WorkspacePanel extends Panel {

	private IGraph graph;
	private IWorkspace workspace;

	public WorkspacePanel() {
		super();
		try {
			URL resource = UMLEditorWebServlet.class.getResource("test.class.violet.html");
			IFile aFile = new LocalFile(new File(resource.getFile()));
			GraphFile graphFile = new GraphFile(aFile);
			this.workspace = new Workspace(graphFile);
			final IEditorPart editorPart = workspace.getEditorPart();
			final IEditorPartBehaviorManager behaviorManager = editorPart.getBehaviorManager();
			this.graph = editorPart.getGraph();
			GraphLayout graphLayout = new GraphLayout(graph);
			setContent(graphLayout);
		} catch (Exception e) {
			Notification.show("Error", "Cannot initialize workspace", Notification.Type.ERROR_MESSAGE);
		}

	}

}
