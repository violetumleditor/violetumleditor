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

package com.horstmann.violet.product.diagram.propertyeditor.customeditor;

import com.horstmann.violet.framework.file.GraphFile;
import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.IGraphFile;
import com.horstmann.violet.framework.file.chooser.IFileChooserService;
import com.horstmann.violet.framework.file.naming.ExtensionFilter;
import com.horstmann.violet.framework.file.naming.FileNamingService;
import com.horstmann.violet.framework.file.persistence.IFileReader;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.product.diagram.common.DiagramLink;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.Workspace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditorSupport;

/**
 * A PropertyEditor for FileLink objects that lets the user select a file and/or open it.
 * 
 * @author Alexandre de Pellegrin
 */
public abstract class AbstractDiagramLinkEditor extends PropertyEditorSupport
{

    /** The file chooser used for selecting files */
    protected JFileChooser m_FileChooser;

    /** The panel displayed */
    private JPanel m_Panel;

    /** The file chooser to use with with menu */
    @InjectedBean
    private IFileChooserService fileChooserService;

    /** File services */
    @InjectedBean
    private FileNamingService fileNamingService;
    
    
    
    /**
     * Returns a representation of the current property value as java source.
     * 
     * @return a value of type 'String'
     */
    public String getJavaInitializationString()
    {

        DiagramLink fl = (DiagramLink) getValue();
        if (fl == null)
        {
            return "null";
        }
        return "new File(\"" + fl.getFile() + "\")";
    }

    /**
     * Returns true because we do support a custom editor.
     * 
     * @return true
     */
    public boolean supportsCustomEditor()
    {
        return true;
    }

    /**
     * Gets the custom editor component.
     * 
     * @return a value of type 'java.awt.Component'
     */
    public java.awt.Component getCustomEditor()
    {
        if (this.m_Panel == null)
        {
            ResourceBundleInjector.getInjector().inject(this);
            JButton chooseButton = new JButton();
            chooseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                	ExtensionFilter[] filters = fileNamingService.getFileFilters();
                        IFileReader fileOpener = fileChooserService.chooseAndGetFileReader(filters);
                        IFile selectedFile = fileOpener.getFileDefinition();
                        if (selectedFile == null) return;
                        DiagramLink diagramLink = (DiagramLink) AbstractDiagramLinkEditor.this.getValue();
                        diagramLink.setFile(selectedFile);
                        AbstractDiagramLinkEditor.this.setValue(diagramLink);
                        firePropertyChange();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            
            //this.getResourceBundle().getString("file.link.open.text")
            JButton goButton = new JButton();
            goButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    try {
                        DiagramLink diagramLink = (DiagramLink) AbstractDiagramLinkEditor.this.getValue();
                        IFile file = diagramLink.getFile();
                        if (file == null) return;
                        IGraphFile graphFile = new GraphFile(file);
                        IWorkspace workspace = new Workspace(graphFile);
                        show(workspace);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            this.m_Panel = new JPanel();
            this.m_Panel.add(chooseButton);
            this.m_Panel.add(goButton);
        }
        return this.m_Panel;

    }
    
    /**
     * Needs to be implemented to display the workspace created when a file is opened
     * @param workspace
     */
    public abstract void show(IWorkspace workspace);

    /**
     * Returns true since this editor is paintable.
     * 
     * @return true.
     */
    public boolean isPaintable()
    {
        return true;
    }

    /**
     * Paints a representation of the current Object.
     * 
     * @param gfx the graphics context to use
     * @param box the area we are allowed to paint into
     */
    public void paintValue(java.awt.Graphics gfx, java.awt.Rectangle box)
    {

        FontMetrics fm = gfx.getFontMetrics();
        int vpad = (box.height - fm.getHeight()) / 2;
        DiagramLink fl = (DiagramLink) getValue();
        String val = "No file";
        IFile file = fl.getFile();
        if (fl != null && file != null)
        {
            val = file.getFilename();
        }
        gfx.drawString(val, 2, fm.getHeight() + vpad);
    }




}
