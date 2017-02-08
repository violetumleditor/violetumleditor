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

package com.horstmann.violet.application.menu;

import com.horstmann.violet.application.gui.MainFrame;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.editorpart.behavior.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Edit menu
 *
 * @author Alexandre de Pellegrin
 */
@ResourceBundleBean(resourceReference = MenuFactory.class)
public class EditMenu extends JMenu {

    /**
     * Default constructor
     *
     * @param mainFrame where is attached this menu
     */
    @ResourceBundleBean(key = "edit")
    public EditMenu(final MainFrame mainFrame) {
        ResourceBundleInjector.getInjector().inject(this);
        this.mainFrame = mainFrame;
        this.createMenu();
    }

    /**
     * Initializes menu
     */
    private void createMenu() {
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed()) {
                    IEditorPart activeEditorPart = getActiveEditorPart();
                    IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
                    List<UndoRedoCompoundBehavior> found = behaviorManager.getBehaviors(UndoRedoCompoundBehavior.class);
                    if (found.size() != 1) {
                        return;
                    }
                    found.get(0).undo();
                }
            }
        });
        this.add(undo);

        redo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed()) {
                    IEditorPart activeEditorPart = getActiveEditorPart();
                    IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
                    List<UndoRedoCompoundBehavior> found = behaviorManager.getBehaviors(UndoRedoCompoundBehavior.class);
                    if (found.size() != 1) {
                        return;
                    }
                    found.get(0).redo();
                }
            }
        });
        this.add(redo);

        properties.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed()) {
                    IEditorPart activeEditorPart = getActiveEditorPart();
                    IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
                    List<EditSelectedBehavior> found = behaviorManager.getBehaviors(EditSelectedBehavior.class);
                    if (found.size() != 1) {
                        return;
                    }
                    found.get(0).editSelected();
                }
            }
        });
        this.add(properties);

        cut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed()) {
                    IEditorPart activeEditorPart = getActiveEditorPart();
                    IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
                    List<CutCopyPasteBehavior> found = behaviorManager.getBehaviors(CutCopyPasteBehavior.class);
                    if (found.size() != 1) {
                        return;
                    }
                    found.get(0).cut();
                }
            }
        });
        this.add(cut);

        copy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed()) {
                    IEditorPart activeEditorPart = getActiveEditorPart();
                    IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
                    List<CutCopyPasteBehavior> found = behaviorManager.getBehaviors(CutCopyPasteBehavior.class);
                    if (found.size() != 1) {
                        return;
                    }
                    found.get(0).copy();
                }
            }
        });
        this.add(copy);

        paste.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed()) {
                    IEditorPart activeEditorPart = getActiveEditorPart();
                    IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
                    List<CutCopyPasteBehavior> found = behaviorManager.getBehaviors(CutCopyPasteBehavior.class);
                    if (found.size() != 1) {
                        return;
                    }
                    found.get(0).paste();
                }
            }
        });
        this.add(paste);

        find.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed()) {
                    IEditorPart activeEditorPart = getActiveEditorPart();
                    IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
                    List<FindBehavior> found = behaviorManager.getBehaviors(FindBehavior.class);
                    if (found.size() != 1) {
                        return;
                    }
                    found.get(0).find();
                }
            }
        });
        this.add(find);

        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed()) getActiveEditorPart().removeSelected();
            }
        });
        this.add(delete);

        selectAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed()) {
                    IEditorPart activeEditorPart = getActiveEditorPart();
                    IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
                    List<SelectAllBehavior> found = behaviorManager.getBehaviors(SelectAllBehavior.class);
                    if (found.size() != 1) {
                        return;
                    }
                    found.get(0).selectAllGraphElements();
                }
            }
        });
        this.add(selectAll);

        selectNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed()) {
                    IEditorPart activeEditorPart = getActiveEditorPart();
                    IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
                    List<SelectByDistanceBehavior> found = behaviorManager.getBehaviors(SelectByDistanceBehavior.class);
                    if (found.size() != 1) {
                        return;
                    }
                    found.get(0).selectAnotherGraphElement(1);
                }
            }
        });
        this.add(selectNext);

        selectPrevious.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed()) {
                    IEditorPart activeEditorPart = getActiveEditorPart();
                    IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
                    List<SelectByDistanceBehavior> found = behaviorManager.getBehaviors(SelectByDistanceBehavior.class);
                    if (found.size() != 1) {
                        return;
                    }
                    found.get(0).selectAnotherGraphElement(-1);
                }
            }
        });
        this.add(selectPrevious);

    }

    /**
     * @return current editor
     */
    private IEditorPart getActiveEditorPart() {
        return this.mainFrame.getActiveWorkspace().getEditorPart();
    }

    /**
     * @return true id at least one workspace is reachable
     */
    private boolean isThereAnyWorkspaceDisplayed() {
        return mainFrame.getWorkspaceList().size() > 0;
    }

    /**
     * Application frame
     */
    private MainFrame mainFrame;

    @ResourceBundleBean(key = "edit.undo")
    private JMenuItem undo;

    @ResourceBundleBean(key = "edit.redo")
    private JMenuItem redo;

    @ResourceBundleBean(key = "edit.properties")
    private JMenuItem properties;

    @ResourceBundleBean(key = "edit.cut")
    private JMenuItem cut;

    @ResourceBundleBean(key = "edit.copy")
    private JMenuItem copy;

    @ResourceBundleBean(key = "edit.paste")
    private JMenuItem paste;

    @ResourceBundleBean(key = "edit.find")
    private JMenuItem find;

    @ResourceBundleBean(key = "edit.delete")
    private JMenuItem delete;

    @ResourceBundleBean(key = "edit.select_all")
    private JMenuItem selectAll;

    @ResourceBundleBean(key = "edit.select_next")
    private JMenuItem selectNext;

    @ResourceBundleBean(key = "edit.select_previous")
    private JMenuItem selectPrevious;
}
