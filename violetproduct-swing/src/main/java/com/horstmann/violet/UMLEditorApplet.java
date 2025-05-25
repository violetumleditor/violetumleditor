/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2002 Cay S. Horstmann (http://horstmann.com)

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

package com.horstmann.violet;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JApplet; // deprecated
import javax.swing.JFrame;

import com.horstmann.violet.application.gui.MainFrame;
import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.dialog.DialogFactoryMode;
import com.horstmann.violet.framework.file.chooser.IFileChooserService;
import com.horstmann.violet.framework.file.chooser.JFileChooserService;
import com.horstmann.violet.framework.file.persistence.IFilePersistenceService;
import com.horstmann.violet.framework.file.persistence.XHTMLPersistenceService;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanFactory;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.plugin.PluginLoader;
import com.horstmann.violet.framework.theme.ClassicMetalTheme;
import com.horstmann.violet.framework.theme.DarkBlueTheme;
import com.horstmann.violet.framework.theme.ITheme;
import com.horstmann.violet.framework.theme.ThemeManager;
import com.horstmann.violet.framework.theme.BlueAmbianceTheme;
import com.horstmann.violet.framework.userpreferences.AppletUserPreferencesDao;
import com.horstmann.violet.framework.userpreferences.IUserPreferencesDao;

/**
 * A program for editing UML diagrams.
 */
@SuppressWarnings("deprecation")
public class UMLEditorApplet extends JApplet
{

    /*
     * Applet entry point (non-Javadoc)
     * 
     * @see java.applet.Applet#init()
     */
    public void init()
    {
        initBeanFactory();
        BeanInjector.getInjector().inject(this);
        createAppletWorkspace();
    }
    
    private void initBeanFactory() {
        IUserPreferencesDao userPreferencesDao = new AppletUserPreferencesDao();
        BeanFactory.getFactory().register(IUserPreferencesDao.class, userPreferencesDao);
        
        ThemeManager themeManager = new ThemeManager();
        ITheme theme1 = new ClassicMetalTheme();
        ITheme theme2 = new BlueAmbianceTheme();
        ITheme theme3 = new DarkBlueTheme();
        List<ITheme> themeList = new ArrayList<ITheme>();
        themeList.add(theme1);
        themeList.add(theme2);
        themeList.add(theme3);
        themeManager.setInstalledThemes(themeList);
        BeanFactory.getFactory().register(ThemeManager.class, themeManager);
        themeManager.applyPreferedTheme();

        DialogFactory dialogFactory = new DialogFactory(DialogFactoryMode.INTERNAL);
        BeanFactory.getFactory().register(DialogFactory.class, dialogFactory);
        
        IFilePersistenceService filePersistenceService = new XHTMLPersistenceService();
        BeanFactory.getFactory().register(IFilePersistenceService.class, filePersistenceService);
        
        IFileChooserService fileChooserService = new JFileChooserService();
        BeanFactory.getFactory().register(IFileChooserService.class, fileChooserService);

    }


    /**
     * Creates workspace when application works as an applet. It contains :<br>
     * + plugins loading + GUI theme management + launging argments to open diagram<br>
     */
    private void createAppletWorkspace()
    {
        installPlugins();
        MainFrame mainFrame = new MainFrame();
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.addWindowListener(new WindowAdapter()
        {
            public void windowClosed(WindowEvent e)
            {
                System.out.println("editor closed");
            }
        });
        setContentPane(mainFrame.getContentPane());
        setJMenuBar(mainFrame.getJMenuBar());
    }

    /**
     * Install plugins
     */
    private void installPlugins()
    {
        this.pluginLoader.installPlugins();
    }

    @InjectedBean
    private PluginLoader pluginLoader;

}