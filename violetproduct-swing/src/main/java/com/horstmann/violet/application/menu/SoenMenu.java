package com.horstmann.violet.application.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.horstmann.violet.application.gui.MainFrame;
import com.horstmann.violet.application.help.AboutDialog;
import com.horstmann.violet.application.help.HelpManager;
import com.horstmann.violet.application.help.ShortcutDialog;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;

@ResourceBundleBean(resourceReference = MenuFactory.class)
public class SoenMenu extends JMenu{
	
	 /**
     * Default constructor
     * 
     * @param mainFrame where this menu is atatched
     * @param factory to access to external resources such as texts, icons
     */
    @ResourceBundleBean(key = "soen")
    public SoenMenu(MainFrame mainFrame)
    {
        ResourceBundleInjector.getInjector().inject(this);
        this.mainFrame = mainFrame;
        this.createMenu();
    }

    /**
     * Initializes menu
     */
    private void createMenu()
    {

        enableF1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
//                HelpManager.getInstance().openUserGuide();
            }

        });
        this.add(enableF1);

        enableF2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
//                HelpManager.getInstance().openHomepage();
            }

        });
        this.add(enableF2);
        
        enableF3.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
//                AboutDialog dialog = new AboutDialog(mainFrame);
//                dialog.setVisible(true);
            }

        });
        this.add(enableF3);

//        shortcutItem.addActionListener(new ActionListener()
//        {
//            @Override
//            public void actionPerformed(ActionEvent e)
//            {
//                ShortcutDialog dialog = new ShortcutDialog(mainFrame);
//                dialog.setVisible(true);
//            }
//        });
//        this.add(shortcutItem);

    }

    

    /**
     * Main app frame where this menu is attached to
     */
    private JFrame mainFrame;
    
    @ResourceBundleBean(key = "soen.enableF1")
    private JMenuItem enableF1;
    
    @ResourceBundleBean(key = "soen.enableF2")
    private JMenuItem enableF2;
    
    @ResourceBundleBean(key = "soen.enableF3")
    private JMenuItem enableF3;
//
//    @ResourceBundleBean(key = "help.shortcut")
//    private JMenuItem shortcutItem;



}
