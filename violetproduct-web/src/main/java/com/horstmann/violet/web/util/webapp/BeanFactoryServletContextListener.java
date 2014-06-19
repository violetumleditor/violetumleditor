package com.horstmann.violet.web.util.webapp;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

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

public class BeanFactoryServletContextListener implements ServletContextListener {

	@InjectedBean
	private PluginLoader pluginLoader;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		initBeanFactory();
		BeanInjector.getInjector().inject(this);
		installPlugins();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		BeanFactory.finalizeManagedBeans();
	}

	private void initBeanFactory() {
		BeanFactory.delegatesBeanFinalization(true);
		
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

	/**
	 * Install plugins
	 */
	private void installPlugins() {

		this.pluginLoader.installPlugins();
	}

}
