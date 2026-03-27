package com.horstmann.violet.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.horstmann.violet.framework.injection.bean.ManiocFramework.ManagedBean;

/**
 * Bean containing the preferences used at application launch, such as the list of files to open, or whether to start in kiosk mode.
 * These preferences are set by parsing the command line arguments passed to the application.
 * The parsing is done in the constructor of this bean, and the resulting preferences are stored in the fields of this bean.
 * This bean is then injected into the UMLEditorApplication bean, and can be used to access the launch preferences throughout the application.
 */
@ManagedBean
public class LaunchingPreferences {



    public LaunchingPreferences(String[] args)
    {
        List<String> argsList = new ArrayList<String>(Arrays.asList(args));
        List<String> optionArgList = new ArrayList<String>();
        for (String arg : argsList)
        {
            if ("-reset".equals(arg))
            {
                this.isResetUserPreferences = true;
                optionArgList.add(arg);
            }
            if ("-english".equals(arg))
            {
                this.isEnglishLanguageForced = true;
                optionArgList.add(arg);
            }
            if ("-help".equals(arg) || "-?".equals(arg))
            {
                this.isHelpRequested = true;
                optionArgList.add(arg);
            }
            if ("-kioskMode".equals(arg))
            {
                this.isKioskMode = true;
                optionArgList.add(arg);
            }
            if (arg.startsWith("-adminPassword="))
            {
                this.adminPassword = arg.substring("-adminPassword=".length());
                optionArgList.add(arg);
            }
        }
        List<String> fileArgList = new ArrayList<String>(argsList);
        fileArgList.removeAll(optionArgList);
        this.filesToOpen = fileArgList;
    }


    public boolean isResetUserPreferences()
    {
        return isResetUserPreferences;
    }

    public boolean isKioskMode()
    {
        return isKioskMode;
    }

    public boolean isEnglishLanguageForced()
    {
        return isEnglishLanguageForced;
    }

    public boolean isHelpRequested()
    {
        return isHelpRequested;
    }

    public List<String> getFilesToOpen()
    {
        return filesToOpen;
    }

    public String getAdminPassword()
    {
        return adminPassword;
    }


    private boolean isResetUserPreferences;
    private boolean isKioskMode;
    private String adminPassword = "admin";
    private boolean isEnglishLanguageForced;
    private boolean isHelpRequested;
    private List<String> filesToOpen = new ArrayList<>();

}
