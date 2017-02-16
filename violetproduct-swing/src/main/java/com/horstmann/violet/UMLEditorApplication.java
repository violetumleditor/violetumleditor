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
import com.horstmann.violet.framework.language.LanguageManager;
import com.horstmann.violet.framework.plugin.PluginLoader;
import com.horstmann.violet.framework.theme.BlueAmbianceTheme;
import com.horstmann.violet.framework.theme.ClassicMetalTheme;
import com.horstmann.violet.framework.theme.DarkAmbianceTheme;
import com.horstmann.violet.framework.theme.DarkBlueTheme;
import com.horstmann.violet.framework.theme.ITheme;
import com.horstmann.violet.framework.theme.NightTheme;
import com.horstmann.violet.framework.theme.ThemeManager;
import com.horstmann.violet.framework.userpreferences.DefaultUserPreferencesDao;
import com.horstmann.violet.framework.userpreferences.IUserPreferencesDao;
import com.horstmann.violet.framework.userpreferences.UserPreferencesService;
import com.horstmann.violet.framework.util.VersionChecker;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.Workspace;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JFrame;

/**
 * A program for editing UML diagrams.
 */
public class UMLEditorApplication {

    /**
     * Standalone application entry point
     *
     * @param args (could contains file to open)
     */
    public static void main(final String[] args) {
        for (int i = 0; i < args.length; i++) {
            final String arg = args[i];
            if ("-reset".equals(arg)) {
                initBeanFactory();
                final UserPreferencesService service = BeanFactory.getFactory()
                        .getBean(UserPreferencesService.class);
                service.reset();
                System.out.println("User preferences reset done.");
            }
            if ("-english".equals(arg)) {
                Locale.setDefault(Locale.ENGLISH);
                System.out.println("Language forced to english.");
            }
            if ("-help".equals(arg) || "-?".equals(arg)) {
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
     */
    private UMLEditorApplication(final String[] filesToOpen) {
        initBeanFactory();
        BeanInjector.getInjector().inject(this);
        createDefaultWorkspace(filesToOpen);
    }
    /**
     * Initialize theme
     */
    private static void initBeanFactory() {
        final IUserPreferencesDao userPreferencesDao = new DefaultUserPreferencesDao();
        BeanFactory.getFactory().register(IUserPreferencesDao.class, userPreferencesDao);

        final ThemeManager themeManager = new ThemeManager();
        final ITheme theme1 = new ClassicMetalTheme();
        final ITheme theme2 = new BlueAmbianceTheme();
        final ITheme theme3 = new DarkAmbianceTheme();
        final ITheme theme4 = new DarkBlueTheme();
        final ITheme theme5 = new NightTheme();
        final List<ITheme> themeList = new ArrayList<ITheme>();
        themeList.add(theme1);
        themeList.add(theme2);
        themeList.add(theme3);
        themeList.add(theme4);
        themeList.add(theme5);
        themeManager.setInstalledThemes(themeList);
        themeManager.applyPreferedTheme();
        BeanFactory.getFactory().register(ThemeManager.class, themeManager);
        themeManager.applyPreferedTheme();
        final LanguageManager languageManager = new LanguageManager();
        languageManager.applyPreferedLanguage();

        final DialogFactory dialogFactory = new DialogFactory(DialogFactoryMode.INTERNAL);
        BeanFactory.getFactory().register(DialogFactory.class, dialogFactory);

        final IFilePersistenceService filePersistenceService = new XHTMLPersistenceService();
        BeanFactory.getFactory().register(IFilePersistenceService.class, filePersistenceService);

        final IFileChooserService fileChooserService = new JFileChooserService();
        BeanFactory.getFactory().register(IFileChooserService.class, fileChooserService);
    }

    /**
     * Creates workspace when application works as a standalone one. It contains :<br>
     * + plugins loading + GUI theme management + a spash screen<br>
     * + jvm checking<br>
     * + command line args<br>
     * + last workspace restore<br>
     */
    private void createDefaultWorkspace(final String[] filesToOpen) {
        installPlugins();
        final SplashScreen splashScreen = new SplashScreen();
        splashScreen.setVisible(true);
        this.versionChecker.checkJavaVersion();
        final MainFrame mainFrame = new MainFrame();
        mainFrame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        SplashScreen.displayOverEditor(mainFrame, 1000);
        final List<IFile> fullList = new ArrayList<IFile>();
        final List<IFile> lastSessionFiles = this.userPreferencesService
                .getOpenedFilesDuringLastSession();
        fullList.addAll(lastSessionFiles);
        for (final String aFileToOpen : filesToOpen) {
            try {
                final LocalFile localFile = new LocalFile(new File(aFileToOpen));
                fullList.add(localFile);
            } catch (final IOException e) {
                // There's nothing to do. We're starting the program
                // Some logs should be nive
                e.printStackTrace();
            }
        }
        // Open files
        for (final IFile aFile : lastSessionFiles) {
            try {
                final IGraphFile graphFile = new GraphFile(aFile);
                final IWorkspace workspace = new Workspace(graphFile);
                mainFrame.addWorkspace(workspace);
            } catch (final Exception e) {
                System.err.println(
                        "Unable to open file " + aFile.getFilename() + "from location " + aFile
                                .getDirectory());
                userPreferencesService.removeOpenedFile(aFile);
                System.err.println("Removed from user preferences!");
            }
        }
        final IFile activeFile = this.userPreferencesService.getActiveDiagramFile();
        mainFrame.openActiveWorkspace(activeFile);
        mainFrame.setVisible(true);
        splashScreen.setVisible(false);
        splashScreen.dispose();
    }

    /**
     * Install plugins
     */
    private void installPlugins() {

        this.pluginLoader.installPlugins();
    }


    @InjectedBean
    private VersionChecker versionChecker;

    @InjectedBean
    private PluginLoader pluginLoader;

    @InjectedBean
    private UserPreferencesService userPreferencesService;


}