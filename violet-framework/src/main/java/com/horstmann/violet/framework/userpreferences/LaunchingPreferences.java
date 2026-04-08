package com.horstmann.violet.framework.userpreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.horstmann.violet.framework.injection.bean.ManiocFramework.ManagedBean;

/**
 * Bean containing preferences parsed from command line arguments.
 */
@ManagedBean
public class LaunchingPreferences
{

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
            if ("-autoSave".equals(arg))
            {
                this.isAutoSave = true;
                optionArgList.add(arg);
            }
            if (arg.startsWith("-adminPassword="))
            {
                this.adminPassword = arg.substring("-adminPassword=".length());
                optionArgList.add(arg);
            }
            if ("-cheerpjMode".equals(arg))
            {
                this.isCheerpjMode = true;
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

    public boolean isAutoSave()
    {
        return isAutoSave;
    }

    public boolean isCheerpjMode()
    {
        return isCheerpjMode;
    }

    private boolean isResetUserPreferences;
    private boolean isKioskMode;
    private String adminPassword = "admin";
    private boolean isEnglishLanguageForced;
    private boolean isHelpRequested;
    private boolean isAutoSave;
    private boolean isCheerpjMode;
    private List<String> filesToOpen = new ArrayList<>();

}
