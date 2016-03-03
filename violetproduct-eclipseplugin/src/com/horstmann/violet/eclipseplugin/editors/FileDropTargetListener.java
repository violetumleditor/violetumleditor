/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

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

package com.horstmann.violet.eclipseplugin.editors;

import java.awt.Point;
import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;

import com.horstmann.violet.eclipseplugin.tools.JavaFileModel;
import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.LocalFile;
import com.horstmann.violet.product.diagram.abstracts.node_old.INode;
import com.horstmann.violet.product.diagram.communication.ClassDiagramGraph;
import com.horstmann.violet.product.diagram.common.DiagramLink;
import com.horstmann.violet.product.diagram.common.DiagramLinkNode;
import com.horstmann.violet.product.diagram.sequence.SequenceDiagramGraph;
import com.horstmann.violet.workspace.editorpart.IEditorPart;

/**
 * Class used to manage drag n' drop between Eclipse package explorer and
 * Violet's editor instance. In other words, this is the most important class to
 * import Java files into diagrams.
 * 
 * @author Alexandre de Pellegrin
 */
public class FileDropTargetListener implements DropTargetListener {

	/**
	 * Default constructor
	 * 
	 * @param graphPanel
	 *            containing the target graph
	 */
	public FileDropTargetListener(IEditorPart graphPanel) {
		this.UMLGraphPanel = graphPanel;
	}

	/**
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragEnter(DropTargetEvent event) {
		// Nothing to do here
	}

	/**
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragLeave(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragLeave(DropTargetEvent event) {
		// Nothing to do here
	}

	/**
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOperationChanged(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOperationChanged(DropTargetEvent event) {
		// Nothing to do here
	}

	/**
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOver(DropTargetEvent event) {
		// Nothing to do here
	}

	/**
	 * @see org.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void drop(DropTargetEvent event) {
		Point UMLGraphPanelLocationOnScreen = this.UMLGraphPanel.getSwingComponent().getLocationOnScreen();
		Point mouseLocationOnUMLGraphPanel = new Point(event.x - UMLGraphPanelLocationOnScreen.x, event.y - UMLGraphPanelLocationOnScreen.y);

		Object dropObject = event.data;
		// A drop has occurred, copy over the data
		if (dropObject != null && dropObject instanceof IResource[]) {
			IResource rs = ((IResource[]) dropObject)[0];
			this.UMLGraphPanel.getSwingComponent().repaint();
			if (VIOLET_FILE_EXTENSION.equals(rs.getFileExtension().toLowerCase())) {
				addVioletLinkNode(event, mouseLocationOnUMLGraphPanel, rs);
				return;
			}
			if (JAVA_FILE_EXTENSION.equals(rs.getFileExtension().toLowerCase())) {
				importJavaFile(event, mouseLocationOnUMLGraphPanel, rs);
				return;
			}
		}
		event.detail = DND.DROP_NONE;
	}

	/**
	 * Parses a Java file and creates a new node_old in the current diagram with its
	 * properties
	 */
	private void importJavaFile(DropTargetEvent event, Point mouseLocationOnUMLGraphPanel, IResource rs) {
		event.detail = DND.DROP_LINK;
		File javaFile = rs.getLocation().toFile();
		JavaFileModel jfm = new JavaFileModel(javaFile);
		INode node = null;

		boolean added = false;

		// For class diagram
		if (this.UMLGraphPanel.getGraph().getClass().equals(ClassDiagramGraph.class)) {
			node = jfm.getClassNode();
			added = this.UMLGraphPanel.getGraph().addNode(node, mouseLocationOnUMLGraphPanel);
		}

		// For sequence diagram
		if (this.UMLGraphPanel.getGraph().getClass().equals(SequenceDiagramGraph.class)) {
			node = jfm.getLifelineNode();
			added = this.UMLGraphPanel.getGraph().addNode(node, mouseLocationOnUMLGraphPanel);
		}

		if (added) {
			this.UMLGraphPanel.selectElement(node);
			this.UMLGraphPanel.getSwingComponent().repaint();
		}
	}

	/**
	 * When the dropped file has Violet's extension, creates a diagram link on
	 * the current one
	 */
	private void addVioletLinkNode(DropTargetEvent event, Point mouseLocationOnUMLGraphPanel, IResource rs) {
		try {
			event.detail = DND.DROP_LINK;
			File violetFile = rs.getLocation().toFile();
			IFile localFile = new LocalFile(violetFile);
			DiagramLink link = new DiagramLink();
			link.setFile(localFile);
			DiagramLinkNode linkNode = new DiagramLinkNode();
			linkNode.setDiagramLink(link);
			boolean added = this.UMLGraphPanel.getGraph().addNode(linkNode, mouseLocationOnUMLGraphPanel);
			if (added) {
				this.UMLGraphPanel.selectElement(linkNode);
				this.UMLGraphPanel.getSwingComponent().repaint();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see org.eclipse.swt.dnd.DropTargetListener#dropAccept(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dropAccept(DropTargetEvent event) {
		// Nothing to do here
	}

	/** Java file extension */
	private static final String JAVA_FILE_EXTENSION = "java";

	/** violet file extension */
	private static final String VIOLET_FILE_EXTENSION = "violet";

	/** Current UML Graph Panel */
	private IEditorPart UMLGraphPanel;

}
