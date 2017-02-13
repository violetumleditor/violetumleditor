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
import javax.swing.*;

import com.horstmann.violet.application.gui.*;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.framework.userpreferences.UserPreferencesService;
import com.horstmann.violet.product.diagram.classes.node.ClassNode;

/**
 * Represents the setting menu on the editor frame
 */
@ResourceBundleBean(resourceReference = MenuFactory.class)
public class SettingMenu extends JMenu
{

    /**
     * Default constructor
     *
     * @param mainFrame
     */
    @ResourceBundleBean(key = "setting")
    public SettingMenu(final MainFrame mainFrame)
    {
        BeanInjector.getInjector().inject(this);
        ResourceBundleInjector.getInjector().inject(this);
        this.mainFrame = mainFrame;
        this.createMenu();

    }

    /**
     * Initialize the menu
     */
    private void createMenu()
    {
        changeClassNameJBox.addActionListener(new ActionListener()
                                              {
                                                  @Override
                                                  public void actionPerformed(ActionEvent e)
                                                  {
                                                      if (changeClassNameJBox.isSelected())
                                                      {
                                                          ClassNode.classNameChange = true;
                                                          userPreferencesServices.setSelectedClassNameOption("enabled");
                                                      }
                                                      else
                                                          {
                                                          ClassNode.classNameChange = false;
                                                          userPreferencesServices.setSelectedClassNameOption("disabled");
                                                          }
                                                  }
                                              }
        );

        if (userPreferencesServices.getSelectedClassNameOption().equals("enabled"))
        {
            changeClassNameJBox.setSelected(true);
        }

        this.add(changeClassNameJBox);
    }

    /**
     * Application frame
     */
    private MainFrame mainFrame;

    @ResourceBundleBean(key = "setting.dialog.name.class")
    private JCheckBoxMenuItem changeClassNameJBox;

    @InjectedBean
    private UserPreferencesService userPreferencesServices;

}
