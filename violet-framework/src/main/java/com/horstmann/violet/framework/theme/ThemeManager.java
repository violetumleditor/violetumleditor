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

package com.horstmann.violet.framework.theme;

import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.ManagedBean;
import com.horstmann.violet.framework.userpreferences.UserPreferencesService;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages GUI themes
 * 
 * @author Alexandre de Pellegrin
 * 
 */
@ManagedBean(registeredManually=true)
public class ThemeManager
{

    /**
     * Private constructor for singleton pattern
     */
    public ThemeManager()
    {
        BeanInjector.getInjector().inject(this);
        ThemeManager.setInstance(this);
    }

    /**
     * Sets unique instance
     * 
     * @param t
     */
    private static void setInstance(ThemeManager t)
    {
        ThemeManager.instance = t;
    }

    /**
     * @return a single ptivate instance
     */
    public static ThemeManager getInstance()
    {
        return ThemeManager.instance;
    }

    /**
     * @return installed look and feels infos
     */
    public ThemeInfo[] getInstalledThemeInfos()
    {
        List<ThemeInfo> infos = new ArrayList<ThemeInfo>();
        for (ITheme aTheme : this.installedThemes)
        {
            infos.add(aTheme.getThemeInfo());
        }
        return infos.toArray(new ThemeInfo[infos.size()]);
    }

    /**
     * Applies prefered theme. Only for standalone mode (not Eclipse plugin mode for example)
     * 
     */
    public void applyPreferedTheme()
    {
        String className = getPreferedLookAndFeel();
        switchToTheme(className);
    }

    public void setPreferedLookAndFeel(String className)
    {
        this.userPreferencesServices.setPreferedLookAndFeel(className);
    }

    public String getPreferedLookAndFeel()
    {
        return this.userPreferencesServices.getPreferedLookAndFeel();
    }

    private void switchToTheme(String themeClassName)
    {
        for (ITheme aTheme : this.installedThemes)
        {
            if (themeClassName.equals(aTheme.getClass().getName()))
            {
                switchToTheme(aTheme);
                return;
            }
        }
        // Default case
        switchToTheme(this.installedThemes.get(this.installedThemes.size() -1));
    }

    /**
     * Switch to a specific theme
     * 
     * @param aTheme t
     */
    public void switchToTheme(ITheme aTheme)
    {
        currentTheme = aTheme;
        currentTheme.activate();
    }

    /**
     * @return current theme
     */
    public ITheme getTheme()
    {
        return currentTheme;
    }

    /**
     * @return installed themes
     */
    public List<ITheme> getInstalledThemes()
    {
        return installedThemes;
    }

    /**
     * @param installedThemes
     */
    public void setInstalledThemes(List<ITheme> installedThemes)
    {
        this.installedThemes = installedThemes;
    }

    public void setUserPreferencesService(UserPreferencesService service) {
        this.userPreferencesServices = service;
    }
    
    /**
     * Single instance
     */
    private static ThemeManager instance;

    /**
     * Current graphical theme
     */
    private ITheme currentTheme;

    /**
     * Installed Themes
     */
    private List<ITheme> installedThemes = new ArrayList<ITheme>();
    
    @InjectedBean
    private UserPreferencesService userPreferencesServices;

}