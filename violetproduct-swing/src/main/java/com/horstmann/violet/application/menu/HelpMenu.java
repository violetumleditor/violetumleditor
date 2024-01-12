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

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.horstmann.violet.application.gui.MainFrame;
import com.horstmann.violet.application.help.AboutDialog;
import com.horstmann.violet.application.help.HelpManager;
import com.horstmann.violet.application.help.ShortcutDialog;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;

/**
 * Help menu
 * 
 * @author Alexandre de Pellegrin
 * 
 */
@ResourceBundleBean(resourceReference = MenuFactory.class)
public class HelpMenu extends JMenu
{

    /**
     * Default constructor
     * 
     * @param mainFrame where this menu is atatched
     * @param factory to access to external resources such as texts, icons
     */
    @ResourceBundleBean(key = "help")
    public HelpMenu(MainFrame mainFrame)
    {
        ResourceBundleInjector.getInjector().inject(this);
        this.mainFrame = mainFrame;
        this.createMenu();
    }

    /**
     * Initializes menu
     */
    private void createMenu()
    {

        userGuideItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                HelpManager.getInstance().openUserGuide();
            }

        });
        this.add(userGuideItem);

        homepageItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                HelpManager.getInstance().openHomepage();
            }

        });
        this.add(homepageItem);
        
        aboutItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                AboutDialog dialog = new AboutDialog(mainFrame);
                dialog.setVisible(true);
            }

        });
        this.add(aboutItem);

        shortcutItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ShortcutDialog dialog = new ShortcutDialog(mainFrame);
                dialog.setVisible(true);
            }
        });
        this.add(shortcutItem);

    }

    

    /**
     * Main app frame where this menu is attached to
     */
    private JFrame mainFrame;
    
    @ResourceBundleBean(key = "help.userguide")
    private JMenuItem userGuideItem;
    
    @ResourceBundleBean(key = "help.homepage")
    private JMenuItem homepageItem;
    
    @ResourceBundleBean(key = "help.about")
    private JMenuItem aboutItem;

    @ResourceBundleBean(key = "help.shortcut")
    private JMenuItem shortcutItem;



}
