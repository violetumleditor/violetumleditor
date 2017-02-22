package com.horstmann.violet.application.autosave;

import com.horstmann.violet.application.gui.MainFrame;
import com.horstmann.violet.framework.file.GraphFile;
import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.IGraphFile;
import com.horstmann.violet.framework.file.LocalFile;
import com.horstmann.violet.framework.file.persistence.IFileReader;
import com.horstmann.violet.framework.file.persistence.JFileReader;
import com.horstmann.violet.framework.injection.bean.ManiocFramework;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.Workspace;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.InputStream;

public class AutoSaveRecover extends JFrame
{

    public static boolean isAutoSaveFileLoad = false;
    private MainFrame mainFrame;
    private AutoSave autoSave = new AutoSave(mainFrame);

    /**
     * Open autosave frame
     */
    public AutoSaveRecover(MainFrame mainFrame)
    {

        this.mainFrame = mainFrame;
        loadAutoSaveFile();
    }
    
    /**
     * Load autosave file
     */
    private void loadAutoSaveFile()
    {
        File directory = new File(autoSave.getAutoSaveDirectory());

        File[] files = directory.listFiles();

        for (File file : files) {
            try {

                IFile autoSaveFile = new LocalFile(file);
                IFileReader readFile = new JFileReader(file);
                InputStream in = readFile.getInputStream();

                if (in != null)
                {
                    IGraphFile graphFile = new GraphFile(autoSaveFile);
                    IWorkspace workspace = new Workspace(graphFile);
                    mainFrame.addWorkspace(workspace);
                    in.close();
                    removeAutoSaveFile();
                    isAutoSaveFileLoad = true;
                }
            }
            catch (Exception e)
            {
                file.delete();
            }
        }
    }

    /**
     * Remove autosave file
     */
    private void removeAutoSaveFile()
    {
        File directory = new File(autoSave.getAutoSaveDirectory());
        File[] files = directory.listFiles();
        for (File file : files)
        {
            file.delete();
        }
    }
}