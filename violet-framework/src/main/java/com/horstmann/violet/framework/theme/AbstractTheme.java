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

import java.awt.Frame;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.userpreferences.UserPreferencesService;
import com.l2fprod.common.swing.plaf.LookAndFeelAddons;
import com.l2fprod.common.swing.plaf.windows.WindowsLookAndFeelAddons;

/**
 * Abstract GUI theme with commons operations
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public abstract class AbstractTheme implements ITheme
{

    /**
     * Configure the look and feel here
     */
    protected abstract void configure();
	
    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.display.clipboard.laf.CustomLookAndFeel#activate()
     */
    public void activate()
    {
        configure();
        try
        {
            String laf = getThemeInfo().getLookAndFeelClass().getName();
            UIManager.setLookAndFeel(laf);

            Frame[] frames = Frame.getFrames();
            for (int i = 0; i < frames.length; i++)
            {
                SwingUtilities.updateComponentTreeUI(frames[i]);
            }

        }
        catch (UnsupportedLookAndFeelException e)
        {
            abortWithError(e);
        }
        catch (ClassNotFoundException e)
        {
            abortWithError(e);
        }
        catch (InstantiationException e)
        {
            abortWithError(e);
        }
        catch (IllegalAccessException e)
        {
            abortWithError(e);
        }
        updateTaskPaneUI();
        updateTabbedPaneUI();
    }

    /**
     * Inits tabbed pane look and feel
     */
    private void updateTabbedPaneUI() {
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(1, 1, 1, 1));
    }

    /**
     * Inits collapsible side panel look and feel
     */
    private void updateTaskPaneUI()
    {
    	LookAndFeelAddons addons = new WindowsLookAndFeelAddons();
    	if (LookAndFeelAddons.getAddon() == null) {
    		LookAndFeelAddons.setAddon(addons);
    	}

        UIDefaults defaults = UIManager.getDefaults();
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("TaskPaneGroup.background", getSidebarElementBackgroundColor());
        m.put("TaskPaneGroup.titleBackgroundGradientStart", getSidebarElementTitleBackgroundStartColor());
        m.put("TaskPaneGroup.titleBackgroundGradientEnd", getSidebarElementTitleBackgroundEndColor());
        m.put("TaskPaneGroup.titleForeground", getSidebarElementForegroundColor());
        m.put("TaskPaneGroup.titleOver", getSidebarElementTitleOverColor());
        m.put("TaskPaneGroup.borderColor", getSidebarElementBackgroundColor());
        m.put("TaskPane.useGradient", Boolean.TRUE);
        m.put("TaskPane.backgroundGradientStart", getSidebarBackgroundStartColor());
        m.put("TaskPane.backgroundGradientEnd", getSidebarBackgroundEndColor());

        defaults.putAll(m);
    }



    /**
     * Informs on error and exits
     * 
     * @param e exception raised
     */
    private void abortWithError(Exception e)
    {
        System.err.println("Fatal error when loading look and feel! -> " + this.lafClassName);
        e.printStackTrace();
        System.err.println("Stopping...");
        exitWithErrors();
    }
    
    /**
     * Exit with error status ad, before, reset preferences
     */
    private void exitWithErrors()
    {
        BeanInjector.getInjector().inject(this);
        this.userPreferencesService.reset();
        System.exit(-1);
    }

    private String lafClassName;
    
    @InjectedBean
    private UserPreferencesService userPreferencesService;

}
