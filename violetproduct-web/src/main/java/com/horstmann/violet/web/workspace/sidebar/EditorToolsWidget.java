package com.horstmann.violet.web.workspace.sidebar;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JButton;

import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.web.workspace.editorpart.EditorPartWidget;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.editorpart.behavior.CutCopyPasteBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.UndoRedoCompoundBehavior;
import com.horstmann.violet.workspace.sidebar.SideBar;

import eu.webtoolkit.jwt.AlignmentFlag;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WBoxLayout;
import eu.webtoolkit.jwt.WBoxLayout.Direction;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WGridLayout;
import eu.webtoolkit.jwt.WLabel;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLength.Unit;
import eu.webtoolkit.jwt.WLink;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WResource;
import eu.webtoolkit.jwt.servlet.WebRequest;
import eu.webtoolkit.jwt.servlet.WebResponse;

@ResourceBundleBean(resourceReference = SideBar.class)
public class EditorToolsWidget extends WContainerWidget {

	@ResourceBundleBean(key = "zoomin")
	private JButton bZoomIn;
	@ResourceBundleBean(key = "zoomout")
	private JButton bZoomOut;
	@ResourceBundleBean(key = "undo")
	private JButton bUndo;
	@ResourceBundleBean(key = "redo")
	private JButton bRedo;
	@ResourceBundleBean(key = "delete")
	private JButton bDelete;
	@ResourceBundleBean(key = "cut")
	private JButton bCut;
	@ResourceBundleBean(key = "copy")
	private JButton bCopy;
	@ResourceBundleBean(key = "paste")
	private JButton bPaste;
	@ResourceBundleBean(key = "title.standardbuttons.text")
	private String title;

	private EditorPartWidget editorPartWidget;
	private WBoxLayout mainLayout;
	private WLabel titleLabel;
	private WGridLayout buttonLayout;
	private WPushButton undoButton;
	private WPushButton redoButton;
	private WPushButton cutButton;
	private WPushButton copyButton;
	private WPushButton pasteButton;
	private WPushButton deleteButton;
	
	private String deploymentPath;

	public EditorToolsWidget(EditorPartWidget editorPartWidget) {
		super();
		ResourceBundleInjector.getInjector().inject(this);
		this.editorPartWidget = editorPartWidget;
		setLayout(getMainLayout());
		setMinimumSize(new WLength(100, Unit.Percentage), new WLength(120, Unit.Pixel));
	}
	
	private WBoxLayout getMainLayout() {
		if (this.mainLayout == null) {
			this.mainLayout = new WBoxLayout(Direction.TopToBottom);
			this.mainLayout.addWidget(getTitleLabel());
			WContainerWidget wContainerWidget = new WContainerWidget();
			wContainerWidget.setLayout(getButtonLayout());
			wContainerWidget.setContentAlignment(AlignmentFlag.AlignCenter, AlignmentFlag.AlignMiddle);
			this.mainLayout.addWidget(wContainerWidget);
			this.mainLayout.setContentsMargins(0, 0, 0, 0);
		}
		return this.mainLayout;
	}
	
	private WLabel getTitleLabel() {
		if (this.titleLabel == null) {
			this.titleLabel = new WLabel(this.title);
			this.titleLabel.setStyleClass("darktitle");
			this.titleLabel.setMinimumSize(new WLength(100, Unit.Percentage),new WLength(22, Unit.Pixel));
			this.titleLabel.setMaximumSize(new WLength(100, Unit.Percentage),new WLength(22, Unit.Pixel));
		}
		return this.titleLabel;
	}
	
	
	private WGridLayout getButtonLayout() {
		if (this.buttonLayout == null) {
			this.buttonLayout = new WGridLayout();
			this.buttonLayout.addWidget(getUndoButton(), 0, 0, AlignmentFlag.AlignMiddle);
			this.buttonLayout.addWidget(getRedoButton(), 0, 1, AlignmentFlag.AlignMiddle);
			this.buttonLayout.addWidget(getDeleteButton(), 0, 2, AlignmentFlag.AlignMiddle);
			this.buttonLayout.addWidget(getCutButton(), 1, 0, AlignmentFlag.AlignMiddle);
			this.buttonLayout.addWidget(getCopyButton(), 1, 1, AlignmentFlag.AlignMiddle);
			this.buttonLayout.addWidget(getPasteButton(), 1, 2, AlignmentFlag.AlignMiddle);
		}
		return this.buttonLayout;
	}

	private WPushButton getUndoButton() {
		if (this.undoButton == null) {
			this.undoButton = getWPushButton(this.bUndo);
			this.undoButton.clicked().addListener(this.undoButton, new Signal1.Listener<WMouseEvent>() {
				public void trigger(WMouseEvent e1) {
					UndoRedoCompoundBehavior undoRedoBehavior = getUndoRedoBehavior();
					if (undoRedoBehavior != null) {
						undoRedoBehavior.undo();
					}
					editorPartWidget.update();
				}
			});
		}
		return this.undoButton;
	}

	private WPushButton getRedoButton() {
		if (this.redoButton == null) {
			this.redoButton = getWPushButton(this.bRedo);
			this.redoButton.clicked().addListener(this.redoButton, new Signal1.Listener<WMouseEvent>() {
				public void trigger(WMouseEvent e1) {
					UndoRedoCompoundBehavior undoRedoBehavior = getUndoRedoBehavior();
					if (undoRedoBehavior != null) {
						undoRedoBehavior.redo();
					}
					editorPartWidget.update();
				}
			});
		}
		return this.redoButton;
	}

	private WPushButton getCutButton() {
		if (this.cutButton == null) {
			this.cutButton = getWPushButton(this.bCut);
			this.cutButton.clicked().addListener(this.cutButton, new Signal1.Listener<WMouseEvent>() {
				public void trigger(WMouseEvent e1) {
					CutCopyPasteBehavior cutCopyPasteBehavior = getCutCopyPasteBehavior();
					cutCopyPasteBehavior.cut();
					editorPartWidget.update();
				}
			});
		}
		return this.cutButton;
	}

	private WPushButton getCopyButton() {
		if (this.copyButton == null) {
			this.copyButton = getWPushButton(this.bCopy);
			this.copyButton.clicked().addListener(this.copyButton, new Signal1.Listener<WMouseEvent>() {
				public void trigger(WMouseEvent e1) {
					CutCopyPasteBehavior cutCopyPasteBehavior = getCutCopyPasteBehavior();
					cutCopyPasteBehavior.copy();
					editorPartWidget.update();
				}
			});
		}
		return this.copyButton;
	}

	private WPushButton getPasteButton() {
		if (this.pasteButton == null) {
			this.pasteButton = getWPushButton(this.bPaste);
			this.pasteButton.clicked().addListener(this.pasteButton, new Signal1.Listener<WMouseEvent>() {
				public void trigger(WMouseEvent e1) {
					CutCopyPasteBehavior cutCopyPasteBehavior = getCutCopyPasteBehavior();
					cutCopyPasteBehavior.paste();
					editorPartWidget.update();
				}
			});
		}
		return this.pasteButton;
	}

	private WPushButton getDeleteButton() {
		if (this.deleteButton == null) {
			this.deleteButton = getWPushButton(this.bDelete);
			this.deleteButton.clicked().addListener(this.deleteButton, new Signal1.Listener<WMouseEvent>() {
				public void trigger(WMouseEvent e1) {
					editorPartWidget.getEditorPart().removeSelected();
					editorPartWidget.update();
				}
			});
		}
		return this.deleteButton;
	}

	private WPushButton getWPushButton(final JButton aSwingButton) {
		WPushButton aPushButton = new WPushButton();
		aPushButton.setToolTip(aSwingButton.getText());
		Icon icon = aSwingButton.getIcon();
		WLink iconLink = getIconLink(icon);
		aPushButton.setIcon(iconLink);
		return aPushButton;
	}

	private WLink getIconLink(final Icon icon) {
		WResource iconResource = new WResource() {

			@Override
			protected void handleRequest(WebRequest request, WebResponse response) throws IOException {
				BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);

				Graphics g = bi.createGraphics();
				// paint the Icon to the BufferedImage.
				icon.paintIcon(null, g, 0, 0);
				response.setContentType("image/png");
				ImageIO.write(bi, "png", response.getOutputStream());
			}
		};
		String url = iconResource.getUrl();
		if (!url.startsWith(getDeploymentPath())) {
			url = getDeploymentPath() + "/" + url;
		}
		WLink wLink = new WLink(url);
		return wLink;
	}

	/**
	 * Looks for UndoRedoBehavior on the current editor part
	 * 
	 * @return the first UndoRedoBehavior object found or null
	 */
	private UndoRedoCompoundBehavior getUndoRedoBehavior() {
		IEditorPartBehaviorManager behaviorManager = this.editorPartWidget.getEditorPart().getBehaviorManager();
		List<UndoRedoCompoundBehavior> found = behaviorManager.getBehaviors(UndoRedoCompoundBehavior.class);
		if (found.size() != 1) {
			return null;
		}
		return found.get(0);
	}

	/**
	 * Looks for CutCopyPasteBehavior on the current editor part
	 * 
	 * @return the first CutCopyPasteBehavior object found or null
	 */
	private CutCopyPasteBehavior getCutCopyPasteBehavior() {
		IEditorPart activeEditorPart = this.editorPartWidget.getEditorPart();
		IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
		List<CutCopyPasteBehavior> found = behaviorManager.getBehaviors(CutCopyPasteBehavior.class);
		if (found.size() != 1) {
			return null;
		}
		return found.get(0);
	}
	
	private String getDeploymentPath() {
		if (this.deploymentPath == null) {
			WApplication wApplication = WApplication.getInstance();
			WEnvironment environment = wApplication.getEnvironment();
			this.deploymentPath = environment.getDeploymentPath();
		}
		return this.deploymentPath;
	}

}
