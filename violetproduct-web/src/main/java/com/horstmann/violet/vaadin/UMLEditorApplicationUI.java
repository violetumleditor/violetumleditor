package com.horstmann.violet.vaadin;

import java.util.ArrayList;
import java.util.List;

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
import com.horstmann.violet.framework.theme.ITheme;
import com.horstmann.violet.framework.theme.ThemeManager;
import com.horstmann.violet.framework.userpreferences.DefaultUserPreferencesDao;
import com.horstmann.violet.framework.userpreferences.IUserPreferencesDao;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

public class UMLEditorApplicationUI extends UI {

    private static boolean FACTORY_INITIALIZED = false;

	
	public UMLEditorApplicationUI() {
		super();
        if (!FACTORY_INITIALIZED)
        {
            initBeanFactory();
            BeanInjector.getInjector().inject(this);
            installPlugins();
            FACTORY_INITIALIZED = true;
        }
	}
	

	@Override
	protected void init(VaadinRequest request) {
		createDefaultWorkspace();
	}

	private void initBeanFactory() {
		IUserPreferencesDao userPreferencesDao = new DefaultUserPreferencesDao();
		BeanFactory.getFactory().register(IUserPreferencesDao.class, userPreferencesDao);

		ThemeManager themeManager = new ThemeManager();
		ITheme theme1 = new ClassicMetalTheme();
		List<ITheme> themeList = new ArrayList<ITheme>();
		themeList.add(theme1);
		themeManager.setInstalledThemes(themeList);
		themeManager.switchToTheme(theme1);
		BeanFactory.getFactory().register(ThemeManager.class, themeManager);

		DialogFactory dialogFactory = new DialogFactory(DialogFactoryMode.INTERNAL);
		BeanFactory.getFactory().register(DialogFactory.class, dialogFactory);

		IFilePersistenceService filePersistenceService = new XHTMLPersistenceService();
		BeanFactory.getFactory().register(IFilePersistenceService.class, filePersistenceService);

		IFileChooserService fileChooserService = new JFileChooserService();
		BeanFactory.getFactory().register(IFileChooserService.class, fileChooserService);
	}

	private void createDefaultWorkspace() {
		WorkspacePanel workspacePanel = new WorkspacePanel();
		setContent(workspacePanel);
	}

	/**
	 * Install plugins
	 */
	private void installPlugins() {

		this.pluginLoader.installPlugins();
	}

	@InjectedBean
	private PluginLoader pluginLoader;

}
