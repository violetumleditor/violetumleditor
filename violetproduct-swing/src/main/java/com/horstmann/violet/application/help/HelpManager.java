package com.horstmann.violet.application.help;

import java.awt.Toolkit;
import java.text.MessageFormat;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.framework.util.BrowserLauncher;
import com.horstmann.violet.framework.util.ClipboardPipe;

public class HelpManager
{
    private static HelpManager instance;
    
    private HelpManager() {
        
    }

    public static HelpManager getInstance() {
        if (instance == null) {
            instance = new HelpManager();
        }
        return instance;
    }
    




    /**
     * Opens online help
     */
    public void openUserGuide()
    {
        boolean isOK = BrowserLauncher.openURL(this.userGuideURL);
        if (!isOK)
        {
            openBrowser(this.userGuideURL);
        }

    }

    /**
     * Goes to homepage
     */
    public void openHomepage()
    {
        boolean isOK = BrowserLauncher.openURL(this.homePageURL);
        if (!isOK)
        {
            openBrowser(this.homePageURL);
        }

    }

    /**
     * Launch web browser or copy url to clipoard if failed
     * 
     * @param url
     */
    private void openBrowser(String url)
    {
        String message = MessageFormat.format(this.errorMessageTemplate, new Object[]
        {
            url
        });
        JOptionPane optionPane = new JOptionPane();
        optionPane.setMessage(message);
        optionPane.setIcon(this.errorImageIcon);
        ClipboardPipe pipe = new ClipboardPipe(url);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(pipe, null);
        DialogFactory.getInstance().showDialog(optionPane, this.errorDialogBoxTitle, true);
    }

    @ResourceBundleBean(key = "help.userguide.url")
    private String userGuideURL;
    
    @ResourceBundleBean(key = "help.homepage.url")
    private String homePageURL;
    
    @ResourceBundleBean(key = "dialog.open_url_failed.ok")
    private String errorMessageTemplate;
    
    @ResourceBundleBean(key = "dialog.open_url_failed.title")
    private String errorDialogBoxTitle;
    
    @ResourceBundleBean(key = "dialog.open_url_failed.icon")
    private ImageIcon errorImageIcon;
    
}
