package com.horstmann.violet.application.menu;

import javax.swing.JMenu;

import com.horstmann.violet.application.gui.MainFrame;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;

@ResourceBundleBean(resourceReference = MenuFactory.class)
public class WindowMenu extends JMenu
{

    /**
     * Default constructor
     * 
     * @param mainFrame where is attached this menu
     * @param factory for accessing to external resources
     */
    @ResourceBundleBean(key = "window")
    public WindowMenu(final MainFrame mainFrame)
    {
        ResourceBundleInjector.getInjector().inject(this);
        this.mainFrame = mainFrame;
    }
    
    /**
     * Current editor frame
     */
    private MainFrame mainFrame;
    
}
