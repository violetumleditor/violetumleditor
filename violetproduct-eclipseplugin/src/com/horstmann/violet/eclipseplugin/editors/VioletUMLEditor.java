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

package com.horstmann.violet.eclipseplugin.editors;

import java.io.File;
import java.net.URI;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.ResourceTransfer;

import com.horstmann.violet.eclipseplugin.file.EclipseFileChooserService;
import com.horstmann.violet.eclipseplugin.tools.EclipseUtils;
import com.horstmann.violet.framework.file.GraphFile;
import com.horstmann.violet.framework.file.IGraphFile;
import com.horstmann.violet.framework.file.LocalFile;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.theme.ITheme;
import com.horstmann.violet.framework.theme.ThemeManager;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.IWorkspaceListener;
import com.horstmann.violet.workspace.Workspace;

/**
 * Main Editor Part
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class VioletUMLEditor extends EditorPart
{



    /**
     * Performs saving
     */
	@Override
    public void doSave(IProgressMonitor monitor)
    {
		this.fileChooserService.changeProgressMonitor(monitor);
		this.getUMLDiagramPanel().getGraphFile().save();
		firePropertyChange(EditorPart.PROP_DIRTY);
    }

    /**
     * @see org.eclipse.ui.ISaveablePart#doSaveAs()
     */
	@Override
    public void doSaveAs()
    {
        // Nothing to do here. Files are always created with the wizard
    }

    /**
     * Initializes editor
     */
	@Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException
    {
        BeanInjector.getInjector().inject(this);
        setInput(input);
        setSite(site);
        // Theme is initialized from wizard when creating a new diagram file
        // and from EditorPart when opening an existing diagram file
        EclipseColorPicker eclipseColorPicker = new EclipseColorPicker(site.getWorkbenchWindow().getWorkbench().getDisplay());
        ITheme eclipseTheme = new EclipseTheme(eclipseColorPicker);
        ThemeManager.getInstance().switchToTheme(eclipseTheme);
        // Retreive file input
        if (input instanceof IFileEditorInput)
        {
            IFileEditorInput fe = (IFileEditorInput) input;
            IFile file = fe.getFile();
            this.fileChooserService.setEclipseFile(file);
            // Update part editor title
            this.setPartName(file.getName());
        }

    }

    /**
     * @see org.eclipse.ui.ISaveablePart#isDirty()
     */
	@Override
    public boolean isDirty()
    {
        IWorkspace umlDiagramPanel = this.getUMLDiagramPanel();
		IGraphFile graphFile = umlDiagramPanel.getGraphFile();
		return graphFile.isSaveRequired();
    }

    /**
     * @see org.eclipse.ui.IWorkbenchPart#setFocus()
     */
	@Override
    public void setFocus()
    {
        // Nothing to do here.
    }

    /**
     * 'Save As' is disabled
     */
	@Override
    public boolean isSaveAsAllowed()
    {
        return false;
    }

    /**
     * Builds editor with embedded JPanels
     */
	@Override
    public void createPartControl(Composite parent)
    {
        // Set parent layout
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        parent.setLayout(gridLayout);

        IWorkspace workspacePanel = this.getUMLDiagramPanel();
		final DiagramComposite diagramComposite = new DiagramComposite(parent, workspacePanel);
        int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
        Transfer[] types = new Transfer[]
        {
            ResourceTransfer.getInstance()
        };
        DropTarget target = new DropTarget(parent, operations);
        target.setTransfer(types);
        target.addDropListener(new FileDropTargetListener(workspacePanel.getEditorPart()));

    }

    /**
     * Return master Violet Frame. Usefull to retreive graph and toolbar
     * 
     * @return
     */
    public IWorkspace getUMLDiagramPanel()
    {
        if (this.UMLWorkspace == null)
        {
            try {
            	IGraphFile graphFile = new GraphFile(fileChooserService.chooseAndGetFileReader().getFileDefinition());
                this.UMLWorkspace = new Workspace(graphFile);
                this.UMLWorkspace.addListener(new IWorkspaceListener() {
    				@Override
    				public void titleChanged(String newTitle) {
    				}
    				
    				@Override
    				public void mustOpenfile(com.horstmann.violet.framework.file.IFile file) {
    					try {
    						IEditorSite site = getEditorSite();
    						Display d = site.getShell().getDisplay();
    						LocalFile localFile = new LocalFile(file);
    						File javaFile = localFile.toFile();
    						URI uri = javaFile.toURI();
    						URL url = uri.toURL();
    						EclipseUtils.openUMLDiagram(url, d);
    					} catch (Exception e) {
    						throw new RuntimeException(e);
    					}
    				}
    				
    				@Override
    				public void graphCouldBeSaved() {
    					IEditorSite site = getEditorSite();
                        if (site != null)
                        {
                            Display d = site.getShell().getDisplay();
                            d.asyncExec(new Runnable()
                            {
                                public void run()
                                {
                                    firePropertyChange(EditorPart.PROP_DIRTY);
                                }
                            });
                        }
    				}
    			});
            } catch (Exception e) {
            	throw new RuntimeException(e);
            }
        }
        return this.UMLWorkspace;
    }
    
    public static final String ID = "com.horstmann.violet.eclipseplugin.editors.VioletUMLEditor";

    /** UML diagram swing panel */
    private IWorkspace UMLWorkspace;
    
    @InjectedBean
    private EclipseFileChooserService fileChooserService;

}
