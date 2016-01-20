package com.horstmann.violet.framework.injection.resources;

import java.util.HashMap;

/**
 * Created by piter on 02.01.16.
 */
public class ResourceShortcutProvider {
    private HashMap<String, String> shortcutsMap;

    private ResourceShortcutProvider(){
        shortcutsMap = new HashMap<String, String>();
    }

    private final static class SingletonHolder{
        private static ResourceShortcutProvider INSTANCE = new ResourceShortcutProvider();
    }

    public static ResourceShortcutProvider getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public void addShortcut(String behaviorName, String behaviorShortcut){
        if(!shortcutsMap.containsKey(behaviorName)){
            shortcutsMap.put(behaviorName, behaviorShortcut.replace(' ', '-').toUpperCase());
        }
    }

    public HashMap<String, String> getAllShortcuts(){
        return shortcutsMap;
    }
}
