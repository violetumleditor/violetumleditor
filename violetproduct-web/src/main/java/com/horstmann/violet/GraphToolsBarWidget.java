package com.horstmann.violet;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;

import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;

import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WCompositeWidget;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WPushButton;
import eu.webtoolkit.jwt.WResource;
import eu.webtoolkit.jwt.WVBoxLayout;
import eu.webtoolkit.jwt.servlet.WebRequest;
import eu.webtoolkit.jwt.servlet.WebResponse;

public class GraphToolsBarWidget extends WCompositeWidget {

	private IGraphToolsBar graphToolsBar;
	
	private List<WPushButton> graphToolButtonList = new ArrayList<WPushButton>();
	
	private static final String UNSELECTED_GRAPHTOOL_CSS_CLASS = "btn-info";
	
	private static final String SELECTED_GRAPHTOOL_CSS_CLASS = "btn-primary";

	public GraphToolsBarWidget(final IGraphToolsBar graphToolsBar,
			WContainerWidget parent) {
		super(parent);
		this.graphToolsBar = graphToolsBar;
		
		
	    WContainerWidget container = new WContainerWidget();
	    container.resize(new WLength(150), new WLength(450));
	    WVBoxLayout vbox = new WVBoxLayout();
	    container.setLayout(vbox);
		for (final GraphTool aGraphTool : this.graphToolsBar.getNodeTools()) {
			WPushButton graphToolButton = getButtonFromGraphTool(graphToolsBar,	aGraphTool);
			vbox.addWidget(graphToolButton);
			this.graphToolButtonList.add(graphToolButton);
		}
		for (final GraphTool aGraphTool : this.graphToolsBar.getEdgeTools()) {
			WPushButton graphToolButton = getButtonFromGraphTool(graphToolsBar,	aGraphTool);
			vbox.addWidget(graphToolButton);
			this.graphToolButtonList.add(graphToolButton);
		}
		setImplementation(container);
	}

	private WPushButton getButtonFromGraphTool(
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
		WPushButton graphToolButton = new WPushButton(aGraphTool.getLabel());
		graphToolButton.setIcon(iconResource.generateUrl());
		graphToolButton.setStyleClass(UNSELECTED_GRAPHTOOL_CSS_CLASS);
		graphToolButton.clicked().addListener(graphToolButton, new Signal1.Listener<WMouseEvent>() {
		    public void trigger(WMouseEvent e1) {
		        graphToolsBar.setSelectedTool(aGraphTool);
		        for (WPushButton aButton : graphToolButtonList) {
		        	aButton.setStyleClass(UNSELECTED_GRAPHTOOL_CSS_CLASS);
		        	setStyleClass(SELECTED_GRAPHTOOL_CSS_CLASS);
		        }
		    }
		});
		graphToolButton.setWidth(new WLength(120));
		graphToolButton.setHeight(new WLength(30));
		return graphToolButton;
	}
}
