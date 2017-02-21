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

package com.horstmann.violet.workspace.sidebar.editortools;

import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.editorpart.behavior.CutCopyPasteBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.FindBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.UndoRedoCompoundBehavior;
import com.horstmann.violet.workspace.sidebar.ISideBarElement;
import com.horstmann.violet.workspace.sidebar.SideBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

@ResourceBundleBean(resourceReference = SideBar.class)
public class EditorToolsPanel extends JPanel implements ISideBarElement {

    public EditorToolsPanel() {
        super();
        ResourceBundleInjector.getInjector().inject(this);
        this.setUI(new EditorToolsPanelUI(this));
        this.bZoomIn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                workspace.getEditorPart().zoomIn();
            }
        });
        this.bZoomOut.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                workspace.getEditorPart().zoomOut();
            }
        });
        this.bUndo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UndoRedoCompoundBehavior undoRedoBehavior = getUndoRedoBehavior();
                if (undoRedoBehavior != null) {
                    undoRedoBehavior.undo();
                }
            }
        });
        this.bRedo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UndoRedoCompoundBehavior undoRedoBehavior = getUndoRedoBehavior();
                if (undoRedoBehavior != null) {
                    undoRedoBehavior.redo();
                }
            }
        });
        this.bDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                workspace.getEditorPart().removeSelected();
            }
        });
        this.bCut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CutCopyPasteBehavior cutCopyPasteBehavior = getCutCopyPasteBehavior();
                if (cutCopyPasteBehavior != null) {
                    cutCopyPasteBehavior.cut();
                }
            }
        });
        this.bCopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CutCopyPasteBehavior cutCopyPasteBehavior = getCutCopyPasteBehavior();
                if (cutCopyPasteBehavior != null) {
                    cutCopyPasteBehavior.copy();
                }
            }
        });
        this.bPaste.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CutCopyPasteBehavior cutCopyPasteBehavior = getCutCopyPasteBehavior();
                if (cutCopyPasteBehavior != null) {
                    cutCopyPasteBehavior.paste();
                }
            }
        });
        this.bFind.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FindBehavior findBehavior = getFindBehavior();
                if (findBehavior != null) {
                    findBehavior.find();
                }
            }
        });
    }

    /**
     * Looks for UndoRedoBehavior on the current editor part
     *
     * @return the first UndoRedoBehavior object found or null
     */
    private UndoRedoCompoundBehavior getUndoRedoBehavior()
    {
    	IEditorPart activeEditorPart = workspace.getEditorPart();
        IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
        List<UndoRedoCompoundBehavior> found = behaviorManager.getBehaviors(UndoRedoCompoundBehavior.class);
        if (found.size() != 1) {
            return null;
        }
        return found.get(0);
    }

    /**
     * Looks for CutCopyPasteBehavior on the current editor part
     *
     * @return the first CutCopyPasteBehavior object found or null
     */
    private CutCopyPasteBehavior getCutCopyPasteBehavior()
    {
    	IEditorPart activeEditorPart = workspace.getEditorPart();
        IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
        List<CutCopyPasteBehavior> found = behaviorManager.getBehaviors(CutCopyPasteBehavior.class);
        if (found.size() != 1) {
            return null;
        }
        return found.get(0);
    }

    /**
     * Looks for FindBehavior on the current editor part
     *
     * @return the first FindBehavior object found or null
     */
    private FindBehavior getFindBehavior()
    {
        IEditorPart activeEditorPart = workspace.getEditorPart();
        IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
        List<FindBehavior> found = behaviorManager.getBehaviors(FindBehavior.class);
        if (found.size() != 1) {
            return null;
        }
        return found.get(0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.display.clipboard.sidebar.ISideBarElement#getTitle()
     */
    public String getTitle()
    {
        return this.title;
    }


    /* (non-Javadoc)
     * @see com.horstmann.violet.product.workspace.sidebar.ISideBarElement#install(com.horstmann.violet.product.workspace.IWorkspace)
     */
    public void install(IWorkspace workspace)
    {
        this.workspace = workspace;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.display.clipboard.sidebar.ISideBarElement#getAWTComponent()
     */
    public Component getAWTComponent()
    {
        return this;
    }

    /**
     * @return zoom in button
     */
    public JButton getZoomInButton()
    {
        return this.bZoomIn;
    }

    /**
     * @return zoom out button
     */
    public JButton getZoomOutButton()
    {
        return this.bZoomOut;
    }

    /**
     * @return undo button
     */
    public JButton getUndoButton()
    {
        return this.bUndo;
    }

    /**
     * @return redo button
     */
    public JButton getRedoButton()
    {
        return this.bRedo;
    }

    /**
     * @return delete button
     */
    public JButton getDeleteButton()
    {
        return this.bDelete;
    }

    /**
     * @return cut button
     */
    public JButton getCutButton()
    {
        return this.bCut;
    }

    /**
     * @return copy button
     */
    public JButton getCopyButton()
    {
        return this.bCopy;
    }

    /**
     * @return paste button
     */
    public JButton getPasteButton()
    {
        return this.bPaste;
    }

    /**
     * @return find button
     */
    public JButton getFindButton()
    {
        return this.bFind;
    }

    /**
     * current workspace
     */
    private IWorkspace workspace;

    @ResourceBundleBean(key = "zoomin")
    private JButton bZoomIn;
    @ResourceBundleBean(key = "zoomout")
    private JButton bZoomOut;
    @ResourceBundleBean(key = "undo")
    private JButton bUndo;
    @ResourceBundleBean(key = "redo")
    private JButton bRedo;
    @ResourceBundleBean(key = "delete")
    private JButton bDelete;
    @ResourceBundleBean(key = "cut")
    private JButton bCut;
    @ResourceBundleBean(key = "copy")
    private JButton bCopy;
    @ResourceBundleBean(key = "paste")
    private JButton bPaste;
    @ResourceBundleBean(key = "find")
    private JButton bFind;
    @ResourceBundleBean(key = "title.standardbuttons.text")
    private String title;

}
