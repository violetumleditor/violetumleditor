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

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.horstmann.violet.application.gui.MainFrame;
import com.horstmann.violet.application.gui.SplashScreen;
import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.dialog.DialogFactoryMode;
import com.horstmann.violet.framework.file.GraphFile;
import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.IGraphFile;
import com.horstmann.violet.framework.file.LocalFile;
import com.horstmann.violet.framework.file.chooser.IFileChooserService;
import com.horstmann.violet.framework.file.chooser.JFileChooserService;
import com.horstmann.violet.framework.file.persistence.IFilePersistenceService;
import com.horstmann.violet.framework.file.persistence.XHTMLPersistenceService;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanFactory;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.plugin.PluginLoader;
import com.horstmann.violet.framework.theme.BlueAmbianceTheme;
import com.horstmann.violet.framework.theme.ClassicMetalTheme;
import com.horstmann.violet.framework.theme.DarkAmbianceTheme;
import com.horstmann.violet.framework.theme.DarkBlueTheme;
import com.horstmann.violet.framework.theme.ITheme;
import com.horstmann.violet.framework.theme.ThemeManager;
import com.horstmann.violet.framework.userpreferences.DefaultUserPreferencesDao;
import com.horstmann.violet.framework.userpreferences.IUserPreferencesDao;
import com.horstmann.violet.framework.userpreferences.UserPreferencesService;
import com.horstmann.violet.framework.util.VersionChecker;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.Workspace;

/**
 * A program for editing UML diagrams.
 */
public class UMLEditorApplication
{

    /**
     * Standalone application entry point
     * 
     * @param args (could contains file to open)
     */
    public static void main(String[] args)
    {
        for (int i = 0; i < args.length; i++)
        {
            String arg = args[i];
            if ("-reset".equals(arg))
            {
                initBeanFactory();
                UserPreferencesService service = BeanFactory.getFactory().getBean(UserPreferencesService.class);
                service.reset();
                System.out.println("User preferences reset done.");
            }
            if ("-english".equals(arg))
            {
                Locale.setDefault(Locale.ENGLISH);
                System.out.println("Language forced to english.");
            }
            if ("-help".equals(arg) || "-?".equals(arg))
            {
                System.out.println("Violet UML Editor command line help. Options are :");
                System.out.println("-reset to reset user preferences,");
                System.out.println("-english to force language to english.");
                return;
            }
        }
        new UMLEditorApplication(args);
    }

    /**
     * Default constructor
     * 
     * @param filesToOpen
     */
    private UMLEditorApplication(String[] filesToOpen)
    {
        initBeanFactory();
        BeanInjector.getInjector().inject(this);
        createDefaultWorkspace(filesToOpen);
    }
    
    private static void initBeanFactory() {
        IUserPreferencesDao userPreferencesDao = new DefaultUserPreferencesDao();
        BeanFactory.getFactory().register(IUserPreferencesDao.class, userPreferencesDao);

        ThemeManager themeManager = new ThemeManager();
        ITheme theme1 = new ClassicMetalTheme();
        ITheme theme2 = new BlueAmbianceTheme();
        ITheme theme3 = new DarkAmbianceTheme();
        ITheme theme4 = new DarkBlueTheme();
        List<ITheme> themeList = new ArrayList<ITheme>();
        themeList.add(theme1);
        themeList.add(theme2);
        themeList.add(theme3);
        themeList.add(theme4);
        themeManager.setInstalledThemes(themeList);
        themeManager.applyPreferedTheme();
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
     * Creates workspace when application works as a standalone one. It contains :<br>
     * + plugins loading + GUI theme management + a spash screen<br>
     * + jvm checking<br>
     * + command line args<br>
     * + last workspace restore<br>
     */
    private void createDefaultWorkspace(String[] filesToOpen)
    {
        installPlugins();
        SplashScreen splashScreen = new SplashScreen();
        splashScreen.setVisible(true);
        this.versionChecker.checkJavaVersion();
        MainFrame mainFrame = new MainFrame();
        mainFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        SplashScreen.displayOverEditor(mainFrame, 1000);
        List<IFile> fullList = new ArrayList<IFile>();
        List<IFile> lastSessionFiles = this.userPreferencesService.getOpenedFilesDuringLastSession();
        fullList.addAll(lastSessionFiles);
        for (String aFileToOpen : filesToOpen)
        {
            try
            {
                LocalFile localFile = new LocalFile(new File(aFileToOpen));
                fullList.add(localFile);
            }
            catch (IOException e)
            {
                // There's nothing to do. We're starting the program
                // Some logs should be nive
                e.printStackTrace();
            }
        }
        // Open files
        for (IFile aFile : lastSessionFiles)
        {
            try
            {
                IGraphFile graphFile = new GraphFile(aFile);
                IWorkspace workspace = new Workspace(graphFile);
                mainFrame.addTabbedPane(workspace);
            }
            catch (Exception e)
            {
                System.err.println("Unable to open file " + aFile.getFilename() + "from location " + aFile.getDirectory());
                userPreferencesService.removeOpenedFile(aFile);
                System.err.println("Removed from user preferences!");
            }
        }
        IFile activeFile = this.userPreferencesService.getActiveDiagramFile();
        mainFrame.setActiveDiagramPanel(activeFile);
        mainFrame.setVisible(true);
        splashScreen.setVisible(false);
        splashScreen.dispose();
    }

    /**
     * Install plugins
     */
    private void installPlugins()
    {

        this.pluginLoader.installPlugins();
    }


    @InjectedBean
    private VersionChecker versionChecker;

    @InjectedBean
    private PluginLoader pluginLoader;

    @InjectedBean
    private UserPreferencesService userPreferencesService;


}