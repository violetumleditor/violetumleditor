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

import java.io.File;
import java.util.List;
import java.util.Vector;

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
import com.horstmann.violet.workspace.editorpart.behavior.AddEdgeBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.AddNodeBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.AddTransitionPointBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.ChangeToolByWeelBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.ColorizeBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.CutCopyPasteBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.DragGraphBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.DragSelectedBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.DragTransitionPointBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.EditSelectedBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.FileCouldBeSavedBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.ResizeNodeBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.SelectAllBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.SelectByClickBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.SelectByDistanceBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.SelectByLassoBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.ShowMenuOnRightClickBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.SwingRepaintingBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.UndoRedoCompoundBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.ZoomByWheelBehavior;
import com.horstmann.violet.workspace.sidebar.ISideBar;
import com.horstmann.violet.workspace.sidebar.SideBar;
import com.horstmann.violet.workspace.sidebar.graphtools.GraphTool;
import com.horstmann.violet.workspace.sidebar.graphtools.IGraphToolsBarListener;

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
    public Workspace(IGraphFile graphFile)
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
    public Workspace(IGraphFile graphFile, Id id)
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
    private String getGraphName() {
       String filename = this.graphFile.getFilename();
       if (filename != null) {
           return filename;
       }
       List<IDiagramPlugin> diagramPlugins = this.pluginRegistry.getDiagramPlugins();
       Class<? extends IGraph> searchedClass = this.graphFile.getGraph().getClass();
       for (IDiagramPlugin aDiagramPlugin : diagramPlugins)
       {
           if (aDiagramPlugin.getGraphClass().equals(searchedClass))
           {
               return aDiagramPlugin.getName();
           }
       }
       return "Unknown";
    }
    
    
    @Override
    public IGraphFile getGraphFile() {
        return this.graphFile;
    }

    @Override
    public IEditorPart getEditorPart()
    {
        if (this.graphEditor == null)
        {
            this.graphEditor = new EditorPart(this.graphFile.getGraph());
            IEditorPartBehaviorManager behaviorManager = this.graphEditor.getBehaviorManager();
            behaviorManager.addBehavior(new SelectByLassoBehavior(this.graphEditor, this.getSideBar().getGraphToolsBar()));
            behaviorManager.addBehavior(new SelectByClickBehavior(this.graphEditor, this.getSideBar().getGraphToolsBar()));
            behaviorManager.addBehavior(new SelectByDistanceBehavior(this.graphEditor));
            behaviorManager.addBehavior(new SelectAllBehavior(this.graphEditor, this.getSideBar().getGraphToolsBar()));
            behaviorManager.addBehavior(new AddNodeBehavior(this.graphEditor, this.getSideBar().getGraphToolsBar()));
            behaviorManager.addBehavior(new AddEdgeBehavior(this.graphEditor, this.getSideBar().getGraphToolsBar()));
            behaviorManager.addBehavior(new AddTransitionPointBehavior(this.graphEditor, this.getSideBar().getGraphToolsBar()));
            behaviorManager.addBehavior(new DragSelectedBehavior(this.graphEditor, this.getSideBar().getGraphToolsBar()));
            behaviorManager.addBehavior(new DragTransitionPointBehavior(this.graphEditor, this.getSideBar().getGraphToolsBar()));
            behaviorManager.addBehavior(new DragGraphBehavior(this));
            behaviorManager.addBehavior(new EditSelectedBehavior(this.graphEditor));
            behaviorManager.addBehavior(new FileCouldBeSavedBehavior(this.getGraphFile()));
            behaviorManager.addBehavior(new ResizeNodeBehavior(this.graphEditor, this.getSideBar().getGraphToolsBar()));
            behaviorManager.addBehavior(new ZoomByWheelBehavior(this.getEditorPart()));
            behaviorManager.addBehavior(new ChangeToolByWeelBehavior(this.getSideBar().getGraphToolsBar()));
            behaviorManager.addBehavior(new ShowMenuOnRightClickBehavior(this.graphEditor));
            behaviorManager.addBehavior(new UndoRedoCompoundBehavior(this.graphEditor));
            behaviorManager.addBehavior(new CutCopyPasteBehavior(this.graphEditor));
            behaviorManager.addBehavior(new SwingRepaintingBehavior(this.graphEditor));
            behaviorManager.addBehavior(new ColorizeBehavior(this, this.getSideBar().getColorChoiceBar()));
        }
        return this.graphEditor;
    }

    @Override
    public ISideBar getSideBar()
    {
        if (this.sideBar == null)
        {

            this.sideBar = new SideBar(this);
            this.sideBar.getGraphToolsBar().addListener(new IGraphToolsBarListener()
            {
                public void toolSelectionChanged(GraphTool tool)
                {
                    getEditorPart().getSelectionHandler().setSelectedTool(tool);
                }
            });
        }
        return this.sideBar;
    }


    @Override
    public String getTitle()
    {
        return title;
    }

    /**
     * Set graph title
     * 
     * @param newValue
     */
    private void setTitle(String newValue)
    {
        title = newValue;
        fireTitleChanged(newValue);
    }

    /**
     * Fires a event to indicate that the title has been changed
     * 
     * @param newTitle
     */
    private void fireTitleChanged(String newTitle)
    {
        Vector<IWorkspaceListener> tl = cloneListeners();
        int size = tl.size();
        if (size == 0) return;

        for (int i = 0; i < size; ++i)
        {
            IWorkspaceListener aListener = (IWorkspaceListener) tl.elementAt(i);
            aListener.titleChanged(newTitle);
        }
    }


    /**
     * Set a status indicating that the graph needs to be saved
     * 
     * @param isSaveNeeded
     */
    private void updateTitle(boolean isSaveNeeded)
    {
        String aTitle = getTitle();
        if (isSaveNeeded)
        {
            if (!aTitle.endsWith("*"))
            {
                setTitle(aTitle + "*");
            }
        }
        if (!isSaveNeeded)
        {
            if (aTitle.endsWith("*"))
            {
                setTitle(aTitle.substring(0, aTitle.length() - 1));
            }
        }
    }

    @Override
    public String getFilePath()
    {
        return filePath;
    }

    @Override
    public void setFilePath(String path)
    {
        filePath = path;
        File file = new File(path);
        setTitle(file.getName());
    }


    @Override
    public synchronized void addListener(IWorkspaceListener l)
    {
        if (!this.listeners.contains(l))
        {
            this.listeners.addElement(l);
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized Vector<IWorkspaceListener> cloneListeners()
    {
        return (Vector<IWorkspaceListener>) this.listeners.clone();
    }

    /**
     * Fire an event to all listeners by calling
     */
    public void fireMustOpenFile(IFile aFile)
    {
        Vector<IWorkspaceListener> tl = cloneListeners();
        int size = tl.size();
        if (size == 0) return;
        for (int i = 0; i < size; ++i)
        {
            IWorkspaceListener l = (IWorkspaceListener) tl.elementAt(i);
            l.mustOpenfile(aFile);
        }
    }

    /**
     * Fire an event to all listeners by calling
     */
    private void fireSaveNeeded()
    {
        Vector<IWorkspaceListener> tl = cloneListeners();
        int size = tl.size();
        if (size == 0) return;
        for (int i = 0; i < size; ++i)
        {
            IWorkspaceListener l = (IWorkspaceListener) tl.elementAt(i);
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
    public void setAWTComponent(WorkspacePanel workspacePanel) {
    	
    	this.workspacePanel = workspacePanel;
    }

    public WorkspacePanel workspacePanel;
    private IGraphFile graphFile;
    private IEditorPart graphEditor;
    private ISideBar sideBar;
    private String filePath;
    private String title;
    private Vector<IWorkspaceListener> listeners = new Vector<IWorkspaceListener>();
    private Id id;
    
    @InjectedBean
    private PluginRegistry pluginRegistry;

}
