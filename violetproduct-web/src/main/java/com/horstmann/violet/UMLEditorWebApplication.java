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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.dialog.DialogFactoryMode;
import com.horstmann.violet.framework.file.GraphFile;
import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.LocalFile;
import com.horstmann.violet.framework.file.chooser.IFileChooserService;
import com.horstmann.violet.framework.file.chooser.JFileChooserService;
import com.horstmann.violet.framework.file.persistence.IFilePersistenceService;
import com.horstmann.violet.framework.file.persistence.XHTMLPersistenceService;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanFactory;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.plugin.PluginLoader;
import com.horstmann.violet.framework.theme.ClassicMetalTheme;
import com.horstmann.violet.framework.theme.ITheme;
import com.horstmann.violet.framework.theme.ThemeManager;
import com.horstmann.violet.framework.userpreferences.DefaultUserPreferencesDao;
import com.horstmann.violet.framework.userpreferences.IUserPreferencesDao;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.property.PropertyEditorWidget;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.Workspace;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.editorpart.behavior.AbstractEditorPartBehavior;

import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WBootstrapTheme;
import eu.webtoolkit.jwt.WContainerWidget;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WHBoxLayout;
import eu.webtoolkit.jwt.WPanel;

/**
 * A program for editing UML diagrams.
 */
public class UMLEditorWebApplication extends WApplication
{

    private static boolean FACTORY_INITIALIZED = false;

    /**
     * Default constructor
     * 
     * @param filesToOpen
     * @throws IOException
     */
    public UMLEditorWebApplication(WEnvironment env) throws IOException
    {
        super(env);
        if (!FACTORY_INITIALIZED)
        {
            initBeanFactory();
            BeanInjector.getInjector().inject(this);
            installPlugins();
            FACTORY_INITIALIZED = true;
        }
        createDefaultWorkspace();
    }

    private void initBeanFactory()
    {
        IUserPreferencesDao userPreferencesDao = new DefaultUserPreferencesDao();
        BeanFactory.getFactory().register(IUserPreferencesDao.class, userPreferencesDao);

        ThemeManager themeManager = new ThemeManager();
        ITheme theme1 = new ClassicMetalTheme();
        List<ITheme> themeList = new ArrayList<ITheme>();
        themeList.add(theme1);
        themeManager.setInstalledThemes(themeList);
        themeManager.switchToTheme(theme1);
        BeanFactory.getFactory().register(ThemeManager.class, themeManager);

        DialogFactory dialogFactory = new DialogFactory(DialogFactoryMode.DELEGATED);
        BeanFactory.getFactory().register(DialogFactory.class, dialogFactory);

        IFilePersistenceService filePersistenceService = new XHTMLPersistenceService();
        BeanFactory.getFactory().register(IFilePersistenceService.class, filePersistenceService);

        IFileChooserService fileChooserService = new JFileChooserService();
        BeanFactory.getFactory().register(IFileChooserService.class, fileChooserService);
    }

    private void createDefaultWorkspace() throws IOException
    {
    	setTheme(new WBootstrapTheme());
    	
    	
    	URL resource = getClass().getResource("test.class.violet.html");
		IFile aFile = new LocalFile(new File(resource.getFile()));
        GraphFile graphFile = new GraphFile(aFile);
		IWorkspace workspace = new Workspace(graphFile);
		workspace.getAWTComponent().setSize(800, 600);
		workspace.getAWTComponent().prepareLayout();
    	
		WContainerWidget containerWidget = new WContainerWidget();
		WHBoxLayout layout = new WHBoxLayout();
		containerWidget.setLayout(layout);
		getRoot().addWidget(containerWidget);
    	
        final EditorPartWidget editorPartWidget = new EditorPartWidget(workspace.getEditorPart());
        GraphToolsBarWidget graphToolsBarWidget = new GraphToolsBarWidget(workspace.getSideBar().getGraphToolsBar(), containerWidget);
        editorPartWidget.resize(1024, 768);
        layout.addWidget(graphToolsBarWidget);
        layout.addWidget(editorPartWidget);
        final WPanel propertyPanel = new WPanel();
        layout.addWidget(propertyPanel);
        IEditorPartBehaviorManager behaviorManager = workspace.getEditorPart().getBehaviorManager();
        behaviorManager.addBehavior(new AbstractEditorPartBehavior() {
        	@Override
        	public void onNodeSelected(INode node) {
        		PropertyEditorWidget editorWidget = new PropertyEditorWidget(node, editorPartWidget);
        		System.out.println("selected");
        		propertyPanel.setCentralWidget(editorWidget);
        	}
        	@Override
        	public void onEdgeSelected(IEdge edge) {
        		PropertyEditorWidget editorWidget = new PropertyEditorWidget(edge, editorPartWidget);
        		propertyPanel.setCentralWidget(editorWidget);
        	}
        	@Override
        	public void afterRemovingSelectedElements() {
        		propertyPanel.getCentralWidget().remove();
        	}
        });
        
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