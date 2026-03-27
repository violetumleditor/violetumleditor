package com.horstmann.violet.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.horstmann.violet.framework.injection.bean.ManiocFramework.ManagedBean;

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


    private boolean isResetUserPreferences;
    private boolean isKioskMode;
    private boolean isEnglishLanguageForced;
    private boolean isHelpRequested;
    private List<String> filesToOpen = new ArrayList<>();

}
