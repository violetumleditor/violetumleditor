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

package com.horstmann.violet.eclipseplugin.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

import com.horstmann.violet.eclipseplugin.editors.VioletUMLEditor;
import com.horstmann.violet.eclipseplugin.tools.DiagramPrinter;
import com.horstmann.violet.workspace.IWorkspace;

/**
 * Eclipse plugin print action
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public class PrintAction implements IEditorActionDelegate
{

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorActionDelegate#setActiveEditor(org.eclipse.jface .action.IAction, org.eclipse.ui.IEditorPart)
     */
    public void setActiveEditor(IAction action, IEditorPart targetEditor)
    {
        this.setActiveEditor((VioletUMLEditor) targetEditor);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action)
    {
        IWorkspace workspace = this.getActiveEditor().getUMLDiagramPanel();
        Shell shell = this.getActiveEditor().getSite().getShell();
        DiagramPrinter printer = new DiagramPrinter(workspace, shell);
        printer.print();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action .IAction, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection)
    {
        // TODO Auto-generated method stub
    }

    /**
     * @return editor instance
     */
    private VioletUMLEditor getActiveEditor()
    {
        return activeEditor;
    }

    /**
     * Sets editor instance
     * 
     * @param activeEditor
     */
    private void setActiveEditor(VioletUMLEditor activeEditor)
    {
        this.activeEditor = activeEditor;
    }

    /**
     * Editor instance
     */
    private VioletUMLEditor activeEditor;

}
