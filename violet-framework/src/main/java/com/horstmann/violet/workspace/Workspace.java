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

package com.horstmann.violet.workspace;

import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.IGraphFile;
import com.horstmann.violet.framework.file.IGraphFileListener;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.plugin.IDiagramPlugin;
import com.horstmann.violet.framework.plugin.PluginRegistry;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.Id;
import com.horstmann.violet.workspace.editorpart.EditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.editorpart.behavior.*;
import com.horstmann.violet.workspace.sidebar.ISideBar;
import com.horstmann.violet.workspace.sidebar.SideBar;
import com.horstmann.violet.workspace.sidebar.colortools.IColorChoiceBar;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBar;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBarListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Diagram workspace. It is a kind of package composed by a diagram put in a scroll panel, a side bar for tools and a status bar.
 * This the class to use when you want to work with diagrams outside from Violet (in Eclipse or NetBeans for example)
 *
 * @author Alexandre de Pellegrin
 */
public class Workspace implements IWorkspace
{
    /**
     * Constructs a diagram panel with the specified graph
     *
     * @param graphFile
     */
    public Workspace(final IGraphFile graphFile)
    {
        this.graphFile = graphFile;
        init();
    }

    /**
     * Constructs a diagram panel with the specified graph and a specified id
     *
     * @param graphFile
     * @param id unique id
     */
    public Workspace(final IGraphFile graphFile, final Id id)
    {
        this.graphFile = graphFile;
        this.id = id;
        init();
    }

    private void init()
    {
        BeanInjector.getInjector().inject(this);
        setTitle(getGraphName());
        this.graphFile.addListener(new IGraphFileListener()
        {
            public void onFileModified()
            {
                updateTitle(true);
                fireSaveNeeded();
            }

            public void onFileSaved()
            {
                setTitle(getGraphName());
                updateTitle(false);
            }
        });
        getAWTComponent().prepareLayout();
    }

    /**
     * @return graph filename or the corresponding diagram name if the graph <br/>
     * hasn't been saved yet.
     */
    private String getGraphName()
    {
        final String filename = this.graphFile.getFilename();
        if (filename != null)
        {
            return filename;
        }
        final List<IDiagramPlugin> diagramPlugins = this.pluginRegistry.getDiagramPlugins();
        final Class<? extends IGraph> searchedClass = this.graphFile.getGraph().getClass();
        for (final IDiagramPlugin aDiagramPlugin : diagramPlugins)
        {
            if (aDiagramPlugin.getGraphClass().equals(searchedClass))
            {
                return aDiagramPlugin.getName();
            }
        }
        return resourceBundle.getString("workspace.unknown");
    }

    @Override
    public IGraphFile getGraphFile()
    {
        return this.graphFile;
    }

    @Override
    public IEditorPart getEditorPart()
    {
        if (this.graphEditor == null)
        {
            synchronized (Workspace.class)
            {
                if (this.graphEditor == null)
                {
                    initEditorPart();
                }
            }
        }
        return this.graphEditor;
    }

    private void initEditorPart()
    {
        this.graphEditor = new EditorPart(this.graphFile.getGraph());
        final IEditorPartBehaviorManager behaviorManager = this.graphEditor.getBehaviorManager();
        final IGraphToolsBar graphToolsBar = this.getSideBar().getGraphToolsBar();
        final IColorChoiceBar colorChoiceBar = this.getSideBar().getColorChoiceBar();

        behaviorManager.addBehavior(new SelectByLassoBehavior(this.graphEditor, graphToolsBar));
        behaviorManager.addBehavior(new SelectByClickBehavior(this.graphEditor, graphToolsBar));
        behaviorManager.addBehavior(new SelectByDistanceBehavior(this.graphEditor));
        behaviorManager.addBehavior(new SelectAllBehavior(this.graphEditor, graphToolsBar));
        behaviorManager.addBehavior(new AddNodeBehavior(this.graphEditor, graphToolsBar));
        behaviorManager.addBehavior(new AddEdgeBehavior(this.graphEditor, graphToolsBar));
        behaviorManager.addBehavior(new AddTransitionPointBehavior(this.graphEditor, graphToolsBar));
        behaviorManager.addBehavior(new DragSelectedBehavior(this.graphEditor, graphToolsBar));
        behaviorManager.addBehavior(new DragTransitionPointBehavior(this.graphEditor, graphToolsBar));
        behaviorManager.addBehavior(new DragGraphBehavior(this));
        behaviorManager.addBehavior(new DragEditorPartBehavior(this));
        behaviorManager.addBehavior(new EditSelectedBehavior(this.graphEditor));
        behaviorManager.addBehavior(new FileCouldBeSavedBehavior(this.getGraphFile()));
        behaviorManager.addBehavior(new ResizeNodeBehavior(this.graphEditor, graphToolsBar));
        behaviorManager.addBehavior(new ZoomByWheelBehavior(this.getEditorPart()));
        behaviorManager.addBehavior(new ChangeToolByWeelBehavior(graphToolsBar));
        behaviorManager.addBehavior(new ShowMenuOnRightClickBehavior(this.graphEditor));
        behaviorManager.addBehavior(new UndoRedoCompoundBehavior(this.graphEditor));
        behaviorManager.addBehavior(new CutCopyPasteBehavior(this.graphEditor));
        behaviorManager.addBehavior(new SwingRepaintingBehavior(this.graphEditor));
        behaviorManager.addBehavior(new ColorizeBehavior(this, colorChoiceBar));
        behaviorManager.addBehavior(new FindBehavior(this.graphEditor));
    }

    @Override
    public ISideBar getSideBar()
    {
        if (this.sideBar == null)
        {
            synchronized (Workspace.class)
            {
                if (this.sideBar == null)
                {
                    initSideBar();
                }
            }
        }
        return this.sideBar;
    }

    private void initSideBar()
    {
        this.sideBar = new SideBar(this);
        this.sideBar.getGraphToolsBar().addListener(new IGraphToolsBarListener()
        {
            public void toolSelectionChanged(final GraphTool tool)
            {
                getEditorPart().getSelectionHandler().setSelectedTool(tool);
            }
        });
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    @Override
    public void setTitle(final String newValue)
    {
        title = newValue;
        fireTitleChanged(newValue);
    }

    /**
     * Fires a event to indicate that the title has been changed
     *
     * @param newTitle
     */
    private void fireTitleChanged(final String newTitle)
    {
        final List<IWorkspaceListener> tl = cloneListeners();
        final int size = tl.size();
        if (size == 0) return;

        for (int i = 0; i < size; ++i)
        {
            final IWorkspaceListener aListener = tl.get(i);
            aListener.titleChanged(newTitle);
        }
    }

    /**
     * Set a status indicating that the graph needs to be saved
     *
     * @param isSaveNeeded
     */
    private void updateTitle(final boolean isSaveNeeded)
    {
        final String aTitle = getTitle();
        final String prefix = resourceBundle.getString("workspace.unsaved") + " ";
        if (isSaveNeeded)
        {
            if (!aTitle.startsWith(prefix))
            {
                setTitle(prefix + aTitle);
            }
        }
        if (!isSaveNeeded)
        {
            if (aTitle.startsWith(prefix))
            {
                setTitle(aTitle.substring(prefix.length(), aTitle.length() - 1));
            }
        }
    }

    @Override
    public String getFilePath()
    {
        return filePath;
    }

    @Override
    public void setFilePath(final String path)
    {
        filePath = path;
        final File file = new File(path);
        setTitle(file.getName());
    }

    @Override
    public synchronized void addListener(final IWorkspaceListener l)
    {
        if (!this.listeners.contains(l))
        {
            this.listeners.add(l);
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized List<IWorkspaceListener> cloneListeners()
    {
        return (List<IWorkspaceListener>) new ArrayList<IWorkspaceListener>(this.listeners);
    }

    /**
     * Fire an event to all listeners by calling
     */
    public void fireMustOpenFile(final IFile aFile)
    {
        final List<IWorkspaceListener> tl = cloneListeners();
        final int size = tl.size();
        if (size == 0) return;
        for (int i = 0; i < size; ++i)
        {
            final IWorkspaceListener l = tl.get(i);
            l.mustOpenfile(aFile);
        }
    }

    /**
     * Fire an event to all listeners by calling
     */
    private void fireSaveNeeded()
    {
        final List<IWorkspaceListener> tl = cloneListeners();
        final int size = tl.size();
        if (size == 0) return;
        for (int i = 0; i < size; ++i)
        {
            final IWorkspaceListener l = tl.get(i);
            l.graphCouldBeSaved();
        }
    }

    @Override
    public Id getId()
    {
        if (this.id == null)
        {
            this.id = new Id();
        }
        return this.id;
    }

    @Override
    public WorkspacePanel getAWTComponent()
    {
        if (this.workspacePanel == null)
        {
            this.workspacePanel = new WorkspacePanel(this);
        }
        return this.workspacePanel;
    }

    @Override
    public void setAWTComponent(final WorkspacePanel workspacePanel)
    {
        this.workspacePanel = workspacePanel;
    }

    public WorkspacePanel workspacePanel;
    private final IGraphFile graphFile;
    private volatile IEditorPart graphEditor;
    private volatile ISideBar sideBar;
    private String filePath;
    private String title;
    private final List<IWorkspaceListener> listeners = new ArrayList<IWorkspaceListener>();
    private Id id;

    protected static ResourceBundle resourceBundle = ResourceBundle.getBundle("properties.OtherStrings", Locale.getDefault());

    @InjectedBean
    private PluginRegistry pluginRegistry;

}
