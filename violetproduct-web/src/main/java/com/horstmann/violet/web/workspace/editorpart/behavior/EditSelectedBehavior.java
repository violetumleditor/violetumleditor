package com.horstmann.violet.web.workspace.editorpart.behavior;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.web.property.PropertyEditorWidget;
import com.horstmann.violet.web.workspace.editorpart.EditorPartWidget;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartSelectionHandler;
import com.horstmann.violet.workspace.editorpart.behavior.AbstractEditorPartBehavior;

import eu.webtoolkit.jwt.Signal;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WDialog;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WText;
import eu.webtoolkit.jwt.WLength.Unit;

@ResourceBundleBean(resourceReference = com.horstmann.violet.workspace.editorpart.behavior.EditSelectedBehavior.class)
public class EditSelectedBehavior extends AbstractEditorPartBehavior {

	private EditorPartWidget editorPartWidget;
	private IEditorPartSelectionHandler selectionHandler;
	private IEditorPart editorPart;
	private IGraph graph;

	@InjectedBean
	private DialogFactory dialogFactory;

	@ResourceBundleBean(key = "edit.properties.title")
	private String dialogTitle;

	@ResourceBundleBean(key = "edit.properties.empty_bean_message")
	private String uneditableBeanMessage;
	

	public EditSelectedBehavior(IEditorPart editorPart, EditorPartWidget editorPartWidget) {
		BeanInjector.getInjector().inject(this);
		ResourceBundleInjector.getInjector().inject(this);
		this.editorPartWidget = editorPartWidget;
		this.editorPart = editorPart;
		this.graph = editorPart.getGraph();
		this.selectionHandler = editorPart.getSelectionHandler();
	}

	@Override
	public void onMouseClicked(MouseEvent event) {
		boolean isClickEvent = (event.getID() == MouseEvent.MOUSE_CLICKED);
		boolean isButton1Clicked = (event.getButton() == MouseEvent.BUTTON1);
		boolean isDoubleClick = (event.getClickCount() == 2);
		if (isClickEvent && isButton1Clicked && isDoubleClick) {
			double zoom = editorPart.getZoomFactor();
			Point2D mouseLocation = new Point2D.Double(event.getX() / zoom, event.getY() / zoom);
			this.selectionHandler.clearSelection();
			INode node = this.graph.findNode(mouseLocation);
			IEdge edge = this.graph.findEdge(mouseLocation);
			if (node != null) {
				this.selectionHandler.setSelectedElement(node);
			} else if (edge != null) {
				this.selectionHandler.addSelectedElement(edge);
			}
			editSelected();
		}
	}

	public void editSelected() {
		final Object edited = selectionHandler.isNodeSelectedAtLeast() ? selectionHandler.getLastSelectedNode() : selectionHandler.getLastSelectedEdge();
		if (edited == null) {
			return;
		}
		PropertyEditorWidget editorWidget = new PropertyEditorWidget(edited, this.editorPart, this.editorPartWidget);

		final WDialog dialog = new WDialog(this.dialogTitle);
		WContainerWidget dialogContainer = dialog.getContents();
		if (editorWidget.isEditable()) {
			dialogContainer.addWidget(editorWidget);
		}
		if (!editorWidget.isEditable()) {
			WText wText = new WText(this.uneditableBeanMessage);
			dialogContainer.addWidget(wText);
		}
		WPushButton doneButton = new WPushButton("Done");
		doneButton.clicked().addListener(doneButton, new Signal.Listener() {
			public void trigger() {
				dialog.accept();
			}
		});
		WContainerWidget footerContainer = dialog.getFooter();
		footerContainer.addWidget(doneButton);
		dialog.setModal(false);
		dialog.rejectWhenEscapePressed(true);
		dialog.show();
	}

}
