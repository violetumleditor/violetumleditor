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

package com.horstmann.violet.workspace.sidebar.optionaltools;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.sidebar.ISideBarElement;
import com.horstmann.violet.workspace.sidebar.SideBar;

@ResourceBundleBean(resourceReference=SideBar.class)
public class OptionalToolsPanel extends JPanel implements ISideBarElement
{

    /**
     * Default contructor
     */
    public OptionalToolsPanel()
    {
        ResourceBundleInjector.getInjector().inject(this);
        this.setUI(new OptionalToolsPanelUI(this));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.display.clipboard.sidebar.ISideBarElement#install(com.horstmann.violet.framework.display.clipboard.IDiagramPanel)
     */
    public void install(IWorkspace diagramPanel)
    {
        this.diagramPanel = diagramPanel;
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


    protected JButton getExportToClipboardButton()
    {
        bExportToClipboard.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                diagramPanel.getGraphFile().exportToClipboard();
            }
        });
        return bExportToClipboard;
    }

    protected JButton getPrintButton()
    {
        bPrint.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                diagramPanel.getGraphFile().exportToPrinter();
            }
        });
        return bPrint;
    }

    protected JButton getHelpButton()
    {
        bHelp.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                //TODO : open online support page
            }
        });
        return bHelp;
    }

    /**
     * Current diagram panel
     */
    private IWorkspace diagramPanel;

    @ResourceBundleBean(key="share_document")
    private JButton bShareDocument;
    
    @ResourceBundleBean(key="export_to_clipboard")
    private JButton bExportToClipboard;
    
    @ResourceBundleBean(key="print")
    private JButton bPrint;

    @ResourceBundleBean(key="help")
    private JButton bHelp;



}
