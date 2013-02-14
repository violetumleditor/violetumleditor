package com.horstmann.violet.framework.userpreferences;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.ManagedBean;
import com.horstmann.violet.framework.theme.VistaBlueTheme;


/**
 * Manages all user preferences
 * 
 * @author alexandre de pellegrin
 *
 */
@ManagedBean
public class UserPreferencesService
{


    public void setPreferedLookAndFeel(String className)
    {
        IUserPreferencesDao pService = PreferencesServiceFactory.getInstance();
        pService.put(PreferencesConstant.LOOK_AND_FEEL, className);
    }

    public String getPreferedLookAndFeel()
    {
        IUserPreferencesDao pService = PreferencesServiceFactory.getInstance();
        String preferedLAF = pService.get(PreferencesConstant.LOOK_AND_FEEL, VistaBlueTheme.class.getName());
        return preferedLAF;
    }
    
  
    /**
     * @return the list of lastest opened files (as path strings)
     */
    public Set<IFile> getRecentFiles()
    {
        Set<PreferredFile> recentFiles = new HashSet<PreferredFile>();
        String recent = this.dao.get(PreferencesConstant.RECENT_FILES, "").trim();
        String[] strings = recent.split(PreferencesConstant.FILE_SEPARATOR.toString());
        for (String anEntry : strings)
        {
            try
            {
                PreferredFile aRecentFile  = new PreferredFile(anEntry);
                recentFiles.add(aRecentFile);
            }
            catch (IOException e)
            {
                // Nothing to do, will be automatically deleted on dao.put() action
            }
        }
        updateRecentFileList(recentFiles);
        return new HashSet<IFile>(recentFiles);
    }
    
    
    /**
     * add recently opened file into user preferences
     * 
     * @param opened file
     */
    public void addRecentFile(IFile aFile)
    {
        PreferredFile newPreferredFile = new PreferredFile(aFile);
        Set<PreferredFile> recentFileList = new HashSet<PreferredFile>();
        for (IFile file : getRecentFiles()) {
            recentFileList.add(new PreferredFile(file));
        }
        recentFileList.add(newPreferredFile);
        while (recentFileList.size() > DEFAULT_MAX_RECENT_FILES) {
            recentFileList.remove(0);
        }
        updateRecentFileList(recentFileList);
    }
    
    /**
     * Update user preferences
     * @param recentFiles
     */
    private void updateRecentFileList(Set<PreferredFile> recentFiles) {
        StringBuilder result = new StringBuilder("");
        for (IFile aFile : recentFiles) {
            PreferredFile aPreferredFile = new PreferredFile(aFile);
            result.append(aPreferredFile.toString()).append(PreferencesConstant.FILE_SEPARATOR.toString());
        }
        this.dao.put(PreferencesConstant.RECENT_FILES, result.toString());
    }
    
    /**
     * Gets opened files on last session. Used to restore workspace after restart
     * 
     * @return file list
     */
    public Set<IFile> getOpenedFilesDuringLastSession()
    {
        String list = this.dao.get(PreferencesConstant.OPENED_FILES_ON_WORKSPACE, "");
        String[] strings = list.split(PreferencesConstant.FILE_SEPARATOR.toString());
        Set<PreferredFile> result = new HashSet<PreferredFile>();
        for (String anEntry : strings)
        {
            try {
				PreferredFile aFile = new PreferredFile(anEntry);
                result.add(aFile);
            } catch (IOException a) {
                // We should purge list from unparsable entries
            }
        }
        updateOpenedFileList(result);
        return new HashSet<IFile>(result);
    }

    /**
     * Saves newly opened file path into user preferences
     * 
     * @param path file path (should be relative or absolute)
     */
    public void addOpenedFile(IFile aFile)
    {
        PreferredFile newPreferredFile = new PreferredFile(aFile);
        Set<PreferredFile> openedFileList = new HashSet<PreferredFile>();
        for (IFile file : getOpenedFilesDuringLastSession()) {
            openedFileList.add(new PreferredFile(file));
        }
        openedFileList.add(newPreferredFile);
        updateOpenedFileList(openedFileList);
    }
    
    
    /**
     * Removes newly closed file from user preferences
     * 
     * @param path file path (could be relative or absolute)
     */
    public void removeOpenedFile(IFile aFile)
    {
        PreferredFile newPreferredFile = new PreferredFile(aFile);
        Set<PreferredFile> openedFileList = new HashSet<PreferredFile>();
        for (IFile file : getOpenedFilesDuringLastSession()) {
            openedFileList.add(new PreferredFile(file));
        }
        openedFileList.remove(newPreferredFile);
        updateOpenedFileList(openedFileList);
    }

    
    /**
     * Update user preferences
     * @param recentFiles
     */
    private void updateOpenedFileList(Set<PreferredFile> openedFiles) {
        StringBuilder result = new StringBuilder("");
        for (PreferredFile aPreferredFile : openedFiles) {
            result.append(aPreferredFile.toString()).append(PreferencesConstant.FILE_SEPARATOR.toString());
        }
        this.dao.put(PreferencesConstant.OPENED_FILES_ON_WORKSPACE, result.toString());
    }
    


    



    /**
     * Indicates which diagram is currently focused on workspace and saves it into user preferences
     * 
     * @param path file path (could be relative or absolute)
     */
    public void setActiveDiagramFile(IFile aFile)
    {
        if (aFile != null)
        {
            PreferredFile preferredFile = new PreferredFile(aFile);
            this.dao.put(PreferencesConstant.ACTIVE_FILE, preferredFile.toString());
        }
    }

    /**
     * Gets from user preferences which diagram was setted as focused
     * 
     * @return file path (could be relative or absolute). Returns null by default.
     */
    public IFile getActiveDiagramFile()
    {
        String entry = this.dao.get(PreferencesConstant.ACTIVE_FILE, "");
        IFile aFile = null;
        try
        {
            aFile = new PreferredFile(entry);
        }
        catch (IOException e)
        {
            // TODO : logger needed
        }
        return aFile;
    }
    
    /**
     * Clear user preferences
     */
    public void reset() {
        this.dao.reset();
    }
    
    /**
     * Allows to store and retrieve preferences
     */
    @InjectedBean
    private IUserPreferencesDao dao;
    
    /**
     * Recent opened files list capacity
     */
    private static final int DEFAULT_MAX_RECENT_FILES = 5;
}
