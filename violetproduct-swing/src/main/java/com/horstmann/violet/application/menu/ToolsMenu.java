package com.horstmann.violet.application.menu;

import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.horstmann.violet.application.gui.MainFrame;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;

@ResourceBundleBean(resourceReference = MenuFactory.class)
public class ToolsMenu extends JMenu {

	/**
	 * Default constructor
	 * 
	 * @param mainFrame
	 *            where is attached this menu
	 * @param factory
	 *            for accessing to external resources
	 */
	@ResourceBundleBean(key = "tools")
	public ToolsMenu(final MainFrame mainFrame)
	{
		ResourceBundleInjector.getInjector().inject(this);
		this.mainFrame = mainFrame;
		List<IWorkspace> workspaceList = this.mainFrame.getWorkspaceList();
		setEnabled(!workspaceList.isEmpty());
	}

	public void updateMenuItem()
	{
		List<IWorkspace> workspaceList = this.mainFrame.getWorkspaceList();
		setEnabled(!workspaceList.isEmpty());
		final IWorkspace activeWorkspace = this.mainFrame.getActiveWorkspace();
		removeAll();
		addTool(GraphTool.SELECTION_TOOL);
		addSeparator();
		add(new Label("Nodes"));
		addSeparator();
		for (final GraphTool tool : activeWorkspace.getSideBar().getGraphToolsBar().getNodeTools()) {
			if (tool == GraphTool.SELECTION_TOOL) {
				continue;
			}
			addTool(tool);
		};
		addSeparator();
		add(new Label("Edges"));
		addSeparator();
		for (final GraphTool tool : activeWorkspace.getSideBar().getGraphToolsBar().getEdgeTools()) {
			addTool(tool);
		};
	}

	private void addTool(final GraphTool tool)
	{
		JMenuItem menuItem = tool.getMenuItem();
		add(menuItem);
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				mainFrame.getActiveWorkspace().getSideBar().getGraphToolsBar().setSelectedTool(tool);
			}
		});
	}

	/**
	 * Current editor frame
	 */
	private MainFrame mainFrame;

	@ResourceBundleBean(key = "tools.active.icon")
	private ImageIcon activeWorkspaceIcon;
}
