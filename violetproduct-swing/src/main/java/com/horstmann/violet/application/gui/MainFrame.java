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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.beans.BeanInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.horstmann.violet.application.autosave.AutoSave;
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
import com.horstmann.violet.framework.property.ArrowheadChoiceList;
import com.horstmann.violet.framework.theme.ITheme;
import com.horstmann.violet.framework.theme.ThemeManager;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.framework.property.BentStyle;
import com.horstmann.violet.framework.property.LineStyleChoiceList;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.IWorkspaceListener;
import com.horstmann.violet.workspace.Workspace;
import com.horstmann.violet.workspace.WorkspacePanel;

/**
 * This desktop frame contains panes that show graphs.
 * 
 * @author Alexandre de Pellegrin
 */
@ResourceBundleBean(resourceReference = AboutDialog.class)
public class MainFrame extends JFrame
{
    /**
     * Constructs a blank frame with a desktop pane but no graph windows.
     * 
     */
    public MainFrame()
    {
        BeanInjector.getInjector().inject(this);
        ResourceBundleInjector.getInjector().inject(this);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.dialogFactory.setDialogOwner(this);
        decorateFrame();
        setInitialSize();
        createMenuBar();
        getContentPane().add(this.getMainPanel());
        startAutoSave();
    }

    /**
     * Sets initial size on startup
     */
    private void setInitialSize()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        setBounds(screenWidth / 16, screenHeight / 16, screenWidth * 7 / 8, screenHeight * 7 / 8);
        setLocation(0, 0);
        // For screenshots only -> setBounds(50, 50, 850, 650);
    }

    /**
     * Decorates the frame (title, icon...)
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
        JMenuBar menuBar = new JMenuBar();
        menuBar.setFont(this.themeManager.getTheme().getMenubarFont());
        MenuFactory menuFactory = getMenuFactory();
        menuBar.add(menuFactory.getFileMenu(this));
        menuBar.add(menuFactory.getEditMenu(this));
        menuBar.add(menuFactory.getViewMenu(this));
        menuBar.add(menuFactory.getDocumentMenu(this));
        menuBar.add(menuFactory.getHelpMenu(this));
        setJMenuBar(menuBar);
    }
    
    private void startAutoSave()
    {
    	new AutoSave(this);
    }


    /**
     * Add a listener to perform action when something happens on this diagram
     * 
     * @param workspace
     */
    private void listenToWorkspaceEvents(final IWorkspace workspace)
    {
        workspace.addListener(new IWorkspaceListener()
        {
            public void titleChanged(String newTitle)
            {
                setTitle(newTitle);
            }

            public void graphCouldBeSaved()
            {
                // nothing to do here
            }

            public void mustOpenfile(IFile file)
            {
                try
                {
                    IGraphFile graphFile = new GraphFile(file);
                    IWorkspace newWorkspace = new Workspace(graphFile);
                    addWorkspace(newWorkspace);
                }
                catch (IOException e)
                {
                    DialogFactory.getInstance().showErrorDialog(e.getMessage());
                }
            }
        });
    }

    /**
     * Removes a diagram panel from this editor frame
     * 
     * @param diagramPanel
     */
    public void removeWorkspace(IWorkspace workspaceToRemove)
    {
        if (!this.workspaceList.contains(workspaceToRemove))
        {
            return;
        }
        int pos = this.workspaceList.indexOf(workspaceToRemove);
        if (pos < 0) {
            return;
        }
        boolean isWorkspaceDisplayed = workspaceToRemove.equals(getActiveWorkspace());
        if (!isWorkspaceDisplayed) {
            // TODO : update window menu here
            return;
        }
        this.workspaceList.remove(workspaceToRemove);
        if (pos >= this.workspaceList.size()) {
            pos = this.workspaceList.size() - 1;
        }
        if (pos < 0) {
            Component currentWorkspaceComponent = ((BorderLayout) getMainPanel().getLayout()).getLayoutComponent(BorderLayout.CENTER);
            getMainPanel().remove(currentWorkspaceComponent);
            getMainPanel().add(new JPanel(), BorderLayout.CENTER);
            setTitle(this.applicationName);
            menuFactory.getDocumentMenu(this).updateMenuItem();
            getMainPanel().revalidate();
            getMainPanel().repaint();
            return;
        }
        IWorkspace workspaceToDisplay = this.workspaceList.get(pos);
        setActiveWorkspace(workspaceToDisplay);
    }
    
    public void addWorkspace(IWorkspace newWorkspace) {
        this.workspaceList.add(newWorkspace);
        setActiveWorkspace(newWorkspace);
    }
    
    
    /**
     * Looks for an opened diagram from its file path and focus it
     * 
     * @param diagramFilePath diagram file path
     */
    public void setActiveWorkspace(IFile aGraphFile)
    {
        if (aGraphFile == null) return;
        for (IWorkspace aWorkspace : this.workspaceList)
        {
            IFile toCompare = aWorkspace.getGraphFile();
            boolean isSameFilename = aGraphFile.getFilename().equals(toCompare.getFilename());
            if (isSameFilename)
            {
                setActiveWorkspace(aWorkspace);
                return;
            }
        }
    }
    
    public void setActiveWorkspace(IWorkspace activeWorkspace) {
        if (!this.workspaceList.contains(activeWorkspace)) {
            return;
        }
        WorkspacePanel activeWorkspaceComponent = activeWorkspace.getAWTComponent();
        Component currentWorkspaceComponent = ((BorderLayout) getMainPanel().getLayout()).getLayoutComponent(BorderLayout.CENTER);
        getMainPanel().remove(currentWorkspaceComponent);
        getMainPanel().add(activeWorkspaceComponent, BorderLayout.CENTER);
        listenToWorkspaceEvents(activeWorkspace);
        menuFactory.getDocumentMenu(this).updateMenuItem();
        setTitle(activeWorkspace.getTitle());
        getMainPanel().revalidate();
        getMainPanel().repaint();
    }
    

    /**
     * @return true if at least a diagram is displayed
     */
    public boolean isThereAnyDiagramDisplayed()
    {
        return !this.workspaceList.isEmpty();
    }

    public List<IWorkspace> getWorkspaceList()
    {
        return workspaceList;
    }

    /**
     * @return selected diagram file path (or null if not one is selected; that should never happen)
     */
    public IWorkspace getActiveWorkspace()
    {
        Component activeWorkspace = ((BorderLayout) getMainPanel().getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (activeWorkspace == null) {
            return null;
        }
        for (IWorkspace aWorkspace : this.workspaceList) {
            if (activeWorkspace.equals(aWorkspace.getAWTComponent())) {
                return aWorkspace;
            }
        }
        return null;
    }


    private JPanel getMainPanel() {
        if (this.mainPanel == null) {
            this.mainPanel = new JPanel(new BorderLayout());
            this.mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
            this.mainPanel.add(new JPanel(), BorderLayout.CENTER);
            JPanel bottomBorderPanel = new JPanel();
            ITheme cLAF = this.themeManager.getTheme();
            bottomBorderPanel.setBackground(cLAF.getMenubarBackgroundColor().darker());
            bottomBorderPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
            bottomBorderPanel.setSize(getContentPane().getWidth(), 8);
            this.mainPanel.add(bottomBorderPanel, BorderLayout.SOUTH);
        }
        return this.mainPanel;
    }
    
    /**
     * @return the menu factory instance
     */
    public MenuFactory getMenuFactory()
    {
        if (this.menuFactory == null)
        {
            menuFactory = new MenuFactory();
        }
        return this.menuFactory;
    }
    
    
    /**
     * Main panel
     */
    private JPanel mainPanel;

    /**
     * Menu factory instance
     */
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
    
    @ResourceBundleBean(key="app.name")
    private String applicationName;
    
    @ResourceBundleBean(key="app.icon")
    private Image applicationIcon;

    /**
     * All disgram workspaces
     */
    private List<IWorkspace> workspaceList = new ArrayList<IWorkspace>();

    // workaround for bug #4646747 in J2SE SDK 1.4.0
    private static java.util.HashMap<Class<?>, BeanInfo> beanInfos;
    static
    {
        beanInfos = new java.util.HashMap<Class<?>, BeanInfo>();
        Class<?>[] cls = new Class<?>[]
        {
                Point2D.Double.class,
                BentStyle.class,
                ArrowheadChoiceList.class,
                LineStyleChoiceList.class,
                IGraph.class,
                AbstractNode.class,
        };
        for (int i = 0; i < cls.length; i++)
        {
            try
            {
                beanInfos.put(cls[i], java.beans.Introspector.getBeanInfo(cls[i]));
            }
            catch (java.beans.IntrospectionException ex)
            {
            }
        }
    }
}
