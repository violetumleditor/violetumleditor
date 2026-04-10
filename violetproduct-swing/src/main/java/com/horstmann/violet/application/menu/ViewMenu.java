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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import com.horstmann.violet.application.gui.MainFrame;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.editorpart.IEditorPart;

/**
 * View menu
 * 
 * @author Alexandre de Pellegrin
 * 
 */
@ResourceBundleBean(resourceReference = MenuFactory.class)
public class ViewMenu extends JMenu
{

    /**
     * Default constructor
     * 
     * @param mainFrame where this menu is attached
     */
    @ResourceBundleBean(key = "view")
    public ViewMenu(MainFrame mainFrame)
    {
        BeanInjector.getInjector().inject(this);
        ResourceBundleInjector.getInjector().inject(this);
        this.mainFrame = mainFrame;
        this.createMenu();
    }

    /**
     * Initializes menu
     */
    private void createMenu()
    {

        zoomOut.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                performZoomOut();
            }
        });
        this.add(zoomOut);

        zoomIn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                performZoomIn();
            }
        });
        this.add(zoomIn);

        smallerGrid.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                performDisplaySmallerGrid();
            }
        });
        this.add(smallerGrid);

        largerGrid.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                performDisplayLargerGrid();
            }
        });
        this.add(largerGrid);

        hideGridItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                performHideGrid(event);
            }
        });

        this.addMenuListener(new MenuListener()
        {
            public void menuSelected(MenuEvent event)
            {
                if (mainFrame.getWorkspaceList().size() == 0) return;
                IWorkspace activeWorkspace = mainFrame.getActiveWorkspace();
                IEditorPart activeEditor = activeWorkspace.getEditorPart();
                hideGridItem.setSelected(!activeEditor.getGrid().isVisible());
            }

            public void menuDeselected(MenuEvent event)
            {
            }

            public void menuCanceled(MenuEvent event)
            {
            }
        });

    }

    /**
     * Performs zoom out action
     */
    private void performZoomOut()
    {
        if (mainFrame.getWorkspaceList().size() == 0) return;
        IWorkspace workspace = mainFrame.getActiveWorkspace();
        workspace.getEditorPart().changeZoom(-1);
    }

    /**
     * Performs zoom in action
     */
    private void performZoomIn()
    {
        if (mainFrame.getWorkspaceList().size() == 0) return;
        IWorkspace workspace = mainFrame.getActiveWorkspace();
        workspace.getEditorPart().changeZoom(1);
    }

    /**
     * Performs display smaller grid action
     */
    private void performDisplaySmallerGrid()
    {
        if (mainFrame.getWorkspaceList().size() == 0) return;
        IWorkspace workspace = mainFrame.getActiveWorkspace();
        IEditorPart editorPart = workspace.getEditorPart();
        editorPart.getGrid().changeGridSize(-1);
        editorPart.getSwingComponent().repaint();
    }

    /**
     * Performs display larger grid action
     */
    private void performDisplayLargerGrid()
    {
        if (mainFrame.getWorkspaceList().size() == 0) return;
        IWorkspace workspace = mainFrame.getActiveWorkspace();
        IEditorPart editorPart = workspace.getEditorPart();
        editorPart.getGrid().changeGridSize(1);
        editorPart.getSwingComponent().repaint();
    }

    /**
     * Performs hist grid action
     * 
     * @param event that handle the checkbox menu item to know if the hide sould be shown or not
     */
    private void performHideGrid(ActionEvent event)
    {
        if (mainFrame.getWorkspaceList().size() == 0) return;
        IWorkspace workspace = mainFrame.getActiveWorkspace();
        boolean isHidden = hideGridItem.isSelected();
        IEditorPart editorPart = workspace.getEditorPart();
        if (isHidden)
        {
            editorPart.getGrid().setVisible(false);
        }
        else
        {
            editorPart.getGrid().setVisible(true);
        }
        editorPart.getSwingComponent().repaint();
    }

    /**
     * Current editor frame
     */
    private MainFrame mainFrame;

    @ResourceBundleBean(key = "view.zoom_out")
    private JMenuItem zoomOut;

    @ResourceBundleBean(key = "view.zoom_in")
    private JMenuItem zoomIn;

    @ResourceBundleBean(key = "view.smaller_grid")
    private JMenuItem smallerGrid;

    @ResourceBundleBean(key = "view.larger_grid")
    private JMenuItem largerGrid;

    @ResourceBundleBean(key = "view.hide_grid")
    private JCheckBoxMenuItem hideGridItem;

}
