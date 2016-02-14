package com.horstmann.violet.framework.injection.resources;

import java.util.HashMap;

/**
 * A singleton class, where stores all shortcut, that was injected by ResourceFactory
 */
public class ResourceShortcutProvider
{
    private HashMap<String, String> shortcutsMap;

    private ResourceShortcutProvider()
    {
        shortcutsMap = new HashMap<String, String>();
    }

    private static final class SingletonHolder
    {
        private static ResourceShortcutProvider INSTANCE = new ResourceShortcutProvider();
    }

    /**
     * Return instance of ResourceShortcutProvider.
     * @return instance of ResourceShortcutProvider
     */
    public static ResourceShortcutProvider getInstance()
    {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Adding shortcut to HashMap if behavior name didn't exist
     * @param behaviorName shortcut name
     * @param behaviorShortcut shortcut
     */
    public void addShortcut(String behaviorName, String behaviorShortcut)
    {
        if(!shortcutsMap.containsKey(behaviorName))
        {
            shortcutsMap.put(behaviorName, behaviorShortcut);
        }
    }

    /**
     * Return HashMap of all register shortcuts
     * @return HashMap of shortcuts
     */
    public HashMap<String, String> getAllShortcuts()
    {
        return shortcutsMap;
    }
}
