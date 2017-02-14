package com.horstmann.violet.framework.language;

public class Language
{
    private String shortcut;
    private String name;

    public Language(String shortcut, String name)
    {
        this.shortcut = shortcut;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getShortcut()
    {
        return shortcut;
    }

    public void setShortcut(String shortcut)
    {
        this.shortcut = shortcut;
    }
}
