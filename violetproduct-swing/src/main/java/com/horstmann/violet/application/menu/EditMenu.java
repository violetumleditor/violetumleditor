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

import com.horstmann.violet.application.gui.MagnifierFrame;
import com.horstmann.violet.application.gui.MainFrame;
import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.dialog.DialogFactoryMode;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.framework.util.nodeusage.NodeUsage;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.editorpart.behavior.CutCopyPasteBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.EditSelectedBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.FindBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.SelectAllBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.SelectByDistanceBehavior;
import com.horstmann.violet.workspace.editorpart.behavior.UndoRedoCompoundBehavior;
import java.awt.AWTException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

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
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed())
                {
                    final IEditorPart activeEditorPart = getActiveEditorPart();
                    final IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
                    final List<UndoRedoCompoundBehavior> found = behaviorManager.getBehaviors(UndoRedoCompoundBehavior.class);
                    if (found.size() >= 1)
                    {
                      found.get(0).undo();
                    }
                }
            }
        });
        this.add(undo);

        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed())
                {
                    final IEditorPart activeEditorPart = getActiveEditorPart();
                    final IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
                    final List<UndoRedoCompoundBehavior> found = behaviorManager.getBehaviors(UndoRedoCompoundBehavior.class);
                    if (found.size() >= 1)
                    {
                        found.get(0).redo();
                    }
                }
            }
        });
        this.add(redo);

        properties.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed())
                {
                    final IEditorPart activeEditorPart = getActiveEditorPart();
                    final IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
                    final List<EditSelectedBehavior> found = behaviorManager.getBehaviors(EditSelectedBehavior.class);
                    if (found.size() >= 1)
                    {
                        found.get(0).createSelectedItemEditMenu();
                    }
                }
            }
        });
        this.add(properties);

        cut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed())
                {
                    final IEditorPart activeEditorPart = getActiveEditorPart();
                    final IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
                    final List<CutCopyPasteBehavior> found = behaviorManager.getBehaviors(CutCopyPasteBehavior.class);
                    if (found.size() >= 1)
                    {
                        found.get(0).cut();
                    }
                }
            }
        });
        this.add(cut);

        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed())
                {
                    final IEditorPart activeEditorPart = getActiveEditorPart();
                    final IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
                    final List<CutCopyPasteBehavior> found = behaviorManager.getBehaviors(CutCopyPasteBehavior.class);
                    if (found.size() >= 1)
                    {
                        found.get(0).copy();
                    }
                }
            }
        });
        this.add(copy);

        paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed())
                {
                    final IEditorPart activeEditorPart = getActiveEditorPart();
                    final IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
                    final List<CutCopyPasteBehavior> found = behaviorManager.getBehaviors(CutCopyPasteBehavior.class);
                    if (found.size() >= 1)
                    {
                        found.get(0).paste();
                    }
                }
            }
        });
        this.add(paste);

        find.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed())
                {
                    final IEditorPart activeEditorPart = getActiveEditorPart();
                    final IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
                    final List<FindBehavior> found = behaviorManager.getBehaviors(FindBehavior.class);
                    if (found.size() >= 1)
                    {
                        found.get(0).find();
                    }
                }
            }
        });
        this.add(find);

        delete.addActionListener(event ->
        {
            final IEditorPart editorPart = getActiveEditorPart();
            if (isThereAnyWorkspaceDisplayed())
            {
                final List<NodeUsage> selectedNodesUsages = editorPart.getSelectedNodesUsages();
                removeSelectedNodes(editorPart, selectedNodesUsages);

            }
        });
        this.add(delete);

        selectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed())
                {
                    final IEditorPart activeEditorPart = getActiveEditorPart();
                    final IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
                    final List<SelectAllBehavior> found = behaviorManager.getBehaviors(SelectAllBehavior.class);
                    if (found.size() >= 1)
                    {
                        found.get(0).selectAllGraphElements();
                    }
                }
            }
        });
        this.add(selectAll);

        selectNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed())
                {
                    final IEditorPart activeEditorPart = getActiveEditorPart();
                    final IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
                    final List<SelectByDistanceBehavior> found = behaviorManager.getBehaviors(SelectByDistanceBehavior.class);
                    if (found.size() >= 1)
                    {
                        found.get(0).selectAnotherGraphElement(1);
                    }
                }
            }
        });
        this.add(selectNext);

        selectPrevious.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (isThereAnyWorkspaceDisplayed())
                {
                    final IEditorPart activeEditorPart = getActiveEditorPart();
                    final IEditorPartBehaviorManager behaviorManager = activeEditorPart.getBehaviorManager();
                    final List<SelectByDistanceBehavior> found = behaviorManager.getBehaviors(SelectByDistanceBehavior.class);
                    if (found.size() >= 1)
                    {
                        found.get(0).selectAnotherGraphElement(-1);
                    }
                }
            }
        });
        this.add(selectPrevious);
        
		magnifier.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(final ActionEvent event) {
				SwingUtilities.invokeLater(new Runnable() 
				{
					@Override
					public void run() {
						if (isMagnifierOpened()) 
						{
							closeMagnifierFrame();
						} 
						else 
						{
							createMagnifierFrame();
						}
					}
				});
			}
		});
        this.add(magnifier);
    }
    
    /**
     * Is magnifier open
     * @return true if is magnifier window
     */
    private boolean isMagnifierOpened() {
		return (magnifierWindow != null);
	}
    
    /**
     * Close magnifier frame
     */
    private void closeMagnifierFrame() {
    	magnifierFrame.getMouseMoveTimer().stop();
    	magnifierWindow.dispose();
    	magnifierFrame = null;
    	magnifierWindow = null;
    }
    
    /**
     * Create magnifier frame
     */
	private void createMagnifierFrame() {
		try {
			magnifierWindow = new JWindow();
			magnifierFrame = new MagnifierFrame();
			magnifierWindow.add(magnifierFrame.getZoomPanel());
			magnifierWindow.setLocationByPlatform(true);
			magnifierWindow.setLocation(getMagnifierStartupX(),getMagnifierStartupY());
			magnifierWindow.setAlwaysOnTop(true);
            magnifierWindow.addWindowListener(createMagnifierWindowListener());
			magnifierWindow.setVisible(true);
			magnifierWindow.pack();
		} catch (final AWTException e) {
			System.err.println("Failed create magnifier window");
		}
	}

    /**
	 * Window listener
	 * @return magnifier window
	 */
    private WindowListener createMagnifierWindowListener() {
		return new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                magnifierWindow.dispose();
            }
        };
	}

    private void removeSelectedNodes(final IEditorPart editorPart, final List<NodeUsage> selectedNodesUsages)
    {
        if (selectedNodesUsages.isEmpty())
        {
            editorPart.removeSelected();
        }
        else
        {
            final DialogFactory dialogFactory = new DialogFactory(DialogFactoryMode.INTERNAL);
            final int answer = dialogFactory
                    .showConfirmationDialog(confirmationDialogTitle, createNodesInUseMessage(selectedNodesUsages));

            if (answer == JOptionPane.YES_OPTION)
            {
                editorPart.removeSelected();
            }
        }
    }

    private String createNodesInUseMessage(final List<NodeUsage> selectedNodesUsages)
    {
        final String separator = System.getProperty("line.separator");
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(confirmationDialogInformation);
        selectedNodesUsages.forEach((nodeUsage) -> stringBuilder.append(separator).append(nodeUsage));

        return stringBuilder.toString();
    }

    /**
     * Get magnifier startup Y
     * @return magnifierStartaupTop
     */
	private int getMagnifierStartupY() {
    	return mainFrame.getY() + MagnifierFrame.PADDING_TOP;
	}

	/**
	 * Get magnifier startup X
	 * @return magnifierStartaupRight
	 */
	private int getMagnifierStartupX() {
    	return mainFrame.getX() + mainFrame.getWidth() - MagnifierFrame.PADDING_RIGHT;
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
    private final MainFrame mainFrame;
    
    private JWindow magnifierWindow;
    private MagnifierFrame magnifierFrame;

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
    
    @ResourceBundleBean(key = "edit.magnifier")
    private JMenuItem magnifier;

    @ResourceBundleBean(key = "edit.delete.confirmation_dialog.title.text")
    private String confirmationDialogTitle;

    @ResourceBundleBean(key = "edit.delete.confirmation_dialog.information.text")
    private String confirmationDialogInformation;
}
