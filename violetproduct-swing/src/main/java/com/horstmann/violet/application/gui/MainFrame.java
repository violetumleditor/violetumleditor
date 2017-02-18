/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2008 Cay S. Horstmann (http://horstmann.com)
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

package com.horstmann.violet.application.gui;

import com.horstmann.violet.application.TabFinalizer;
import com.horstmann.violet.application.autosave.AutoSave;
import com.horstmann.violet.application.autosave.IAutoSave;
import com.horstmann.violet.application.help.AboutDialog;
import com.horstmann.violet.application.menu.MenuFactory;
import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.file.GraphFile;
import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.IGraphFile;
import com.horstmann.violet.framework.file.chooser.IFileChooserService;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.framework.theme.ThemeManager;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.product.diagram.property.ArrowheadChoiceList;
import com.horstmann.violet.product.diagram.property.BentStyleChoiceList;
import com.horstmann.violet.product.diagram.property.LineStyleChoiceList;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.IWorkspaceListener;
import com.horstmann.violet.workspace.Workspace;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This desktop frame contains panes that show graphs.
 */
@ResourceBundleBean(resourceReference = AboutDialog.class)
public class MainFrame extends JFrame implements IAutoSave
{
    /**
     * Constructs a blank frame with a tabbed panel.
     */
    public MainFrame()
    {
        getContentPane().setLayout(new BorderLayout());
        BeanInjector.getInjector().inject(this);
        ResourceBundleInjector.getInjector().inject(this);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.dialogFactory.setDialogOwner(this);
        decorateFrame();
        setInitialSize();
        createMenuBar();
        startAutoSave();
    }

    /**
     * Add tab with title and close button
     */
    private void addCloseableTab(final String title, final IWorkspace workspace)
    {
        initTabbedPane();
        final Component component = workspace.getAWTComponent();
        this.tabbedPane.addTab(title, component);
        final int newTabIndex = tabbedPane.indexOfComponent(component);
        tabbedPane.setTabComponentAt(newTabIndex, new CloseableTabComponent(tabbedPane));
        tabbedPane.setSelectedIndex(newTabIndex);
        tabbedPane.revalidate();
        tabbedPane.repaint();
    }

    /**
     * Close tab with specified workspace
     *
     * @param workspace workspace to close
     */
    public void closeTabWithWorkspace(final IWorkspace workspace)
    {
            removeWorkspace(workspace);
            tabbedPane.remove(workspace.getAWTComponent());
    }

    /**
     * Return workspace from tab with given index
     */
    private IWorkspace getWorkspaceAt(final int index)
    {
        final Component component = tabbedPane.getComponentAt(index);
        final IWorkspace workspace = findWorkspaceByComponent(component);
        return workspace;
    }

    /**
     * Find workspace in workspace collection by specified component
     */
    private IWorkspace findWorkspaceByComponent(final Component component)
    {
        if (component != null)
        {
            for (final IWorkspace workspace : workspaceList)
            {
                if (workspace.getAWTComponent().equals(component))
                {
                    return workspace;
                }
            }
        }
        throw new RuntimeException("Workspace by component not found");
    }

    /**
     * Sets initial size on startup
     */
    private void setInitialSize()
    {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final int screenWidth = (int) screenSize.getWidth();
        final int screenHeight = (int) screenSize.getHeight();
        setBounds(screenWidth / 16, screenHeight / 16, screenWidth * 7 / 8,
                screenHeight * 7 / 8);
        setLocation(0, 0);
        // For screenshots only -> setBounds(50, 50, 850, 650);
    }

    /**
     * Set title and icon
     */
    private void decorateFrame()
    {
        setTitle(this.applicationName);
        setIconImage(this.applicationIcon);
    }

    /**
     * Creates menu bar
     */
    private void createMenuBar()
    {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.setFont(this.themeManager.getTheme().getMenubarFont());
        final MenuFactory menuFactory = getMenuFactory();
        menuBar.add(menuFactory.getFileMenu(this));
        menuBar.add(menuFactory.getEditMenu(this));
        menuBar.add(menuFactory.getViewMenu(this));
        menuBar.add(menuFactory.getDocumentMenu(this));
        menuBar.add(menuFactory.getHelpMenu(this));
        menuBar.add(menuFactory.getSettingMenu(this));
        setJMenuBar(menuBar);
    }

    /**
     * Start autosave feature
     */
    private void startAutoSave()
    {
    	this.autoSave = new AutoSave(this);
        

    }

    /**
     * Add a listener to perform action when something happens on this diagram
     */
    private void listenToWorkspaceEvents(final IWorkspace workspace)
    {
        workspace.addListener(new IWorkspaceListener()
        {
            @Override
            public void titleChanged(final String newTitle)
            {
                setTitle(newTitle);
                tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), newTitle);
            }

            @Override
            public void graphCouldBeSaved()
            {
                // nothing to do here
            }

            @Override
            public void mustOpenfile(final IFile file)
            {
                try {
                    final IGraphFile graphFile = new GraphFile(file);
                    final IWorkspace newWorkspace = new Workspace(graphFile);
                    addWorkspace(newWorkspace);
                } catch (final IOException e) {
                    final DialogFactory dialogFactory = DialogFactory.getInstance();
                    dialogFactory.showErrorDialog(e.getMessage());
                }
            }

        });
    }

    /**
     * Removes a diagram panel from workspace collection
     *
     * @param workspace workspace to remove
     */
    private void removeWorkspace(final IWorkspace workspace)
    {
        if (workspaceList.contains(workspace))
        {
            workspaceList.remove(workspace);
            menuFactory.getDocumentMenu(this).updateMenuItem();
        }
    }

    /**
     * Add new workspace to collection and tabbed panel
     *
     * @param newWorkspace new workspace
     */
    public void addWorkspace(final IWorkspace newWorkspace)
    {
        workspaceList.add(newWorkspace);
        addCloseableTab(newWorkspace.getTitle(), newWorkspace);
        setActiveWorkspace(newWorkspace);
    }

    /**
     * Looks for an opened diagram from its file path and focus it
     *
     * @param graphFile diagram file path
     */
    public void openActiveWorkspace(final IFile graphFile)
    {
        final IWorkspace workspace = findWorkspaceByFile(graphFile);
        if (workspace != null)
        {
            setActiveWorkspace(workspace);
            addCloseableTab(workspace.getTitle(), workspace);
        }
    }

    /**
     * Find workspace in workspace collection by file
     */
    private IWorkspace findWorkspaceByFile(final IFile graphFile)
    {
        if (graphFile != null)
        {
            for (final IWorkspace workspace : this.workspaceList)
            {
                final String inputFileName = graphFile.getFilename();
                final String currentFileName = workspace.getGraphFile().getFilename();
                if (inputFileName.equals(currentFileName))
                {
                    return workspace;
                }
            }
        }
        return null;
    }

    /**
     * Looks for an opened diagram from its file path and focus it
     *
     * @param workspace workspace
     */
    public void setActiveWorkspace(final IWorkspace workspace)
    {
        if (workspace != null && this.workspaceList.contains(workspace))
        {
            listenToWorkspaceEvents(workspace);
            menuFactory.getDocumentMenu(this).updateMenuItem();
            setTitle(workspace.getTitle());
            this.activeWorkspace = workspace;
            tabbedPane.setSelectedComponent(workspace.getAWTComponent());
        }
    }

    /**
     * Initialize new tabbed pane
     */
    private void initTabbedPane()
    {
        if (this.tabbedPane == null)
        {
            this.tabbedPane = new JTabbedPane();
            this.tabbedPane.addChangeListener(new TabSwitchActionHandler());
            getContentPane().add(this.tabbedPane);
        }
    }

    /**
     * @return true if at least a diagram is displayed
     */
    public boolean isThereAnyDiagramDisplayed()
    {
        return !this.workspaceList.isEmpty();
    }

    /**
     * Return workspace collection
     *
     * @return workspace list
     */
    public List<IWorkspace> getWorkspaceList()
    {
        return workspaceList;
    }

    /**
     * @return selected diagram file path (or null if not one is selected)
     */
    public IWorkspace getActiveWorkspace()
    {
        return this.activeWorkspace;
    }

    /**
     * @return the menu factory instance
     */
    private MenuFactory getMenuFactory()
    {
        if (this.menuFactory == null)
        {
            menuFactory = new MenuFactory();
        }
        return this.menuFactory;
    }

    /**
     * Listener for tab switch actions
     */
    private class TabSwitchActionHandler implements ChangeListener
    {
        @Override
        public void stateChanged(final ChangeEvent e)
        {
            final int index = tabbedPane.getSelectedIndex();
            if (index != -1)
            {
                final IWorkspace workspace = getWorkspaceAt(index);
                setActiveWorkspace(workspace);
            }
            tabbedPane.revalidate();
            tabbedPane.repaint();
        }
    }

    /**
     * A class that represents tab component with title and close button.
     */
    private class CloseableTabComponent extends JPanel
    {
        private final JTabbedPane tabbedPane;

        CloseableTabComponent(final JTabbedPane tabbedPane)
        {
            super(new FlowLayout());
            this.tabbedPane = tabbedPane;
            setOpaque(true);
            add(createTabLabel());
            add(new CloseButton());
        }

        /**
         * Creates label with title from tabbed panel
         */
        private JLabel createTabLabel()
        {
            final JLabel label = new JLabel()
            {
                @Override
                public String getText()
                {
                    final int newTabIndex = tabbedPane
                            .indexOfTabComponent(CloseableTabComponent.this);
                    if (newTabIndex != -1)
                    {
                        return tabbedPane.getTitleAt(newTabIndex);
                    }
                    return null;
                }
            };
            return label;
        }

        /**
         * A class that represents close button for tab
         */
        private class CloseButton extends JButton
        {
            CloseButton()
            {
                this.setIcon(new ImageIcon(tabCloseImage));
                this.setBorder(BorderFactory.createEmptyBorder());
                setFocusPainted(false);
                setContentAreaFilled(false);
                addActionListener(new ButtonClickHandler());
            }

            private class ButtonClickHandler implements ActionListener
            {
                @Override
                public void actionPerformed(final ActionEvent e)
                {
                    final int clickedTabIndex = tabbedPane
                            .indexOfTabComponent(CloseableTabComponent.this);
                    closeTab(clickedTabIndex);
                    tabbedPane.revalidate();
                    tabbedPane.repaint();
                }
            }
        }

        /**
         * Close tab with specified index
         */
        private void closeTab(final int index)
        {
            if (index != -1)
            {
                final IWorkspace workspace = getWorkspaceAt(index);
                if (tabFinalizer.isReadyToClose(workspace))
                {
                    removeWorkspace(workspace);
                    tabbedPane.removeTabAt(index);
                }
            }
        }
    }

    private JTabbedPane tabbedPane;

    /**
     * Finalizer that prepares tabs to close.
     */
    private final TabFinalizer tabFinalizer = new TabFinalizer(this);

    private MenuFactory menuFactory;

    /**
     * GUI Theme manager
     */
    @InjectedBean
    private ThemeManager themeManager;

    /**
     * Needed to display dialog boxes
     */
    @InjectedBean
    private DialogFactory dialogFactory;

    /**
     * Needed to open files
     */
    @InjectedBean
    private IFileChooserService fileChooserService;

    @ResourceBundleBean(key = "app.name")
    private String applicationName;

    @ResourceBundleBean(key = "app.icon")
    private Image applicationIcon;
    
    private AutoSave autoSave;

    @ResourceBundleBean(key = "delete.icon")
    private Image tabCloseImage;


    /**
     * All disgram workspaces
     */
    private final List<IWorkspace> workspaceList = new ArrayList<IWorkspace>();

    private IWorkspace activeWorkspace;

    // workaround for bug #4646747 in J2SE SDK 1.4.0
    private static final java.util.HashMap<Class<?>, BeanInfo> beanInfos;

    static {
        beanInfos = new java.util.HashMap<Class<?>, BeanInfo>();
        final Class<?>[] cls = new Class<?>[]
                {
                        Point2D.Double.class,
                        BentStyleChoiceList.class,
                        ArrowheadChoiceList.class,
                        LineStyleChoiceList.class,
                        IGraph.class,
                        AbstractNode.class,
                };
        for (final Class<?> cl : cls)
        {
            try {
                beanInfos.put(cl, Introspector.getBeanInfo(cl));
            } catch (final IntrospectionException ignored) {
            }
        }
    }
	@Override
	public void reloadSettings() {
		this.autoSave.reloadSettings();
	}
}
