package com.horstmann.violet.web.workspace.sidebar;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;

import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;

import eu.webtoolkit.jwt.Orientation;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WAnchor;
import eu.webtoolkit.jwt.WCompositeWidget;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WImage;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLength.Unit;
import eu.webtoolkit.jwt.WMenu;
import eu.webtoolkit.jwt.WMenuItem;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WResource;
import eu.webtoolkit.jwt.WStackedWidget;
import eu.webtoolkit.jwt.WText;
import eu.webtoolkit.jwt.servlet.WebRequest;
import eu.webtoolkit.jwt.servlet.WebResponse;

public class GraphToolsBarWidget extends WCompositeWidget {

	private IGraphToolsBar graphToolsBar;
	
	private List<WMenuItem> graphToolButtonList = new ArrayList<WMenuItem>();
	
	private static final String UNSELECTED_GRAPHTOOL_CSS_CLASS = "btn-info";
	
	private static final String SELECTED_GRAPHTOOL_CSS_CLASS = "btn-primary";

	public GraphToolsBarWidget(final IGraphToolsBar graphToolsBar,
			WContainerWidget parent) {
		super(parent);
		this.graphToolsBar = graphToolsBar;
		
		
	    WContainerWidget container = new WContainerWidget();
	    WStackedWidget contents = new WStackedWidget();
	    WMenu menu = new WMenu(contents, Orientation.Vertical, container);
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

		setImplementation(container);
		
	}

	private WMenuItem getMenuItemFromGraphTool(
			final IGraphToolsBar graphToolsBar, final GraphTool aGraphTool) {
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
		final WMenuItem graphToolButton = new WMenuItem(aGraphTool.getLabel());
		WImage wImage = new WImage(iconResource, "icon");
		WAnchor wAnchor = getAnchor(graphToolButton);
		wAnchor.insertWidget(0, getSpaceText());
		wAnchor.insertWidget(0, wImage);
		graphToolButton.clicked().addListener(graphToolButton, new Signal1.Listener<WMouseEvent>() {
		    public void trigger(WMouseEvent e1) {
		        graphToolsBar.setSelectedTool(aGraphTool);
		    }
		});
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
}
