package com.horstmann.violet.web.workspace.sidebar;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Icon;

import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.workspace.sidebar.SideBar;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBarListener;

import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WAnchor;
import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WCompositeWidget;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WImage;
import eu.webtoolkit.jwt.WLabel;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLength.Unit;
import eu.webtoolkit.jwt.WLink;
import eu.webtoolkit.jwt.WMemoryResource;
import eu.webtoolkit.jwt.WMenu;
import eu.webtoolkit.jwt.WMenuItem;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WResource;
import eu.webtoolkit.jwt.WStackedWidget;
import eu.webtoolkit.jwt.WText;
import eu.webtoolkit.jwt.WVBoxLayout;
import eu.webtoolkit.jwt.servlet.WebRequest;
import eu.webtoolkit.jwt.servlet.WebResponse;

@ResourceBundleBean(resourceReference = SideBar.class)
public class GraphToolsBarWidget extends WCompositeWidget {

	private IGraphToolsBar graphToolsBar;
	
	private List<WMenuItem> graphToolButtonList = new ArrayList<WMenuItem>();
	
	private static final String UNSELECTED_GRAPHTOOL_CSS_CLASS = "btn-info";
	
	private static final String SELECTED_GRAPHTOOL_CSS_CLASS = "btn-primary";
	
	private Map<GraphTool, WMenuItem> graphToolCache = new HashMap<GraphTool, WMenuItem>();

	
	private WContainerWidget mainContainerWidget;
	private WContainerWidget toolsContainerWidget;
	private WLabel titleLabel;

	
	@ResourceBundleBean(key = "title.diagramtools.text")
	private String title;
	
	
	private String deploymentPath;
	

	public GraphToolsBarWidget(final IGraphToolsBar graphToolsBar,
			WContainerWidget parent) {
		super(parent);
		ResourceBundleInjector.getInjector().inject(this);
		this.graphToolsBar = graphToolsBar;
		setImplementation(getMainContainerWidget());
		
	}
	
	private WContainerWidget getMainContainerWidget() {
		if (this.mainContainerWidget == null) {
			this.mainContainerWidget = new WContainerWidget();
			WVBoxLayout layout = new WVBoxLayout();
			layout.addWidget(getTitleLabel());
			layout.addWidget(getToolsContainerWidget());
			layout.setContentsMargins(0, 0, 0, 0);
			this.mainContainerWidget.setLayout(layout);
			this.mainContainerWidget.setWidth(new WLength(100, Unit.Percentage));
		}
		return this.mainContainerWidget;
	}
	
	
	private WContainerWidget getToolsContainerWidget() {
		if (this.toolsContainerWidget == null) {
			this.toolsContainerWidget = new WContainerWidget();
		    WStackedWidget contents = new WStackedWidget();
		    WMenu menu = new WMenu(contents, this.toolsContainerWidget);
		    menu.setStyleClass("nav nav-pills nav-stacked");
		    menu.setWidth(new WLength(200));
		    
			for (final GraphTool aGraphTool : this.graphToolsBar.getNodeTools()) {
				WMenuItem graphToolMenuItem = getMenuItemFromGraphTool(graphToolsBar,	aGraphTool);
				menu.addItem(graphToolMenuItem);
				this.graphToolButtonList.add(graphToolMenuItem);
			}
			for (final GraphTool aGraphTool : this.graphToolsBar.getEdgeTools()) {
				WMenuItem graphToolMenuItem = getMenuItemFromGraphTool(graphToolsBar,	aGraphTool);
				menu.addItem(graphToolMenuItem);
				this.graphToolButtonList.add(graphToolMenuItem);
			}
			addListener(this.graphToolsBar);
		}
		return this.toolsContainerWidget;
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
	
	
	private void addListener(IGraphToolsBar graphToolsBar) {
		graphToolsBar.addListener(new IGraphToolsBarListener() {
			@Override
			public void toolSelectionChanged(GraphTool selectedTool) {
				if (!graphToolCache.containsKey(selectedTool)) {
					return;
				}
				WMenuItem wMenuItem = graphToolCache.get(selectedTool);
				wMenuItem.select();
			}
		});
	}
	
	
	

	private WMenuItem getMenuItemFromGraphTool(
			final IGraphToolsBar graphToolsBar, final GraphTool aGraphTool) {
		if (this.graphToolCache.containsKey(aGraphTool)) {
			return this.graphToolCache.get(aGraphTool);
		}
		final WMenuItem graphToolButton = new WMenuItem(aGraphTool.getLabel());
		WResource iconResource = new WResource() {

			@Override
			protected void handleRequest(WebRequest request,
					WebResponse response) throws IOException {
				Icon icon = aGraphTool.getIcon();
				BufferedImage bi = new BufferedImage(icon.getIconWidth(),
						icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
				
				Graphics g = bi.createGraphics();
				// paint the Icon to the BufferedImage.
				icon.paintIcon(null, g, 0, 0);
				response.setContentType("image/png");
				ImageIO.write(bi, "png", response.getOutputStream());
			}
		};
		iconResource.suggestFileName(aGraphTool.getLabel());
		iconResource.generateUrl();
		String url = iconResource.getUrl();
		if (!url.startsWith(getDeploymentPath())) {
			url = getDeploymentPath() + "/" + url;
		}
		WImage wImage = new WImage(new WLink(url));
		WAnchor wAnchor = getAnchor(graphToolButton);
		wAnchor.insertWidget(0, getSpaceText());
		wAnchor.insertWidget(0, wImage);
		graphToolButton.clicked().addListener(graphToolButton, new Signal1.Listener<WMouseEvent>() {
		    public void trigger(WMouseEvent e1) {
		        graphToolsBar.setSelectedTool(aGraphTool);
		    }
		});
		this.graphToolCache.put(aGraphTool, graphToolButton);
		return graphToolButton;
	}
	
	
	private WAnchor getAnchor(WMenuItem menuItem) {
		for (int i = 0; i < menuItem.getCount(); ++i) {
			WAnchor result = ((menuItem.getWidget(i)) instanceof WAnchor ? (WAnchor) (menuItem
					.getWidget(i))
					: null);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	private WText getSpaceText() {
		WText spaceText = new WText(" ");
		spaceText.setWidth(new WLength(10, Unit.Pixel));
		return spaceText;
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
