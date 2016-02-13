package com.horstmann.violet.application.autosave;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import com.horstmann.violet.application.gui.MainFrame;
import com.horstmann.violet.framework.file.GraphFile;
import com.horstmann.violet.framework.file.LocalFile;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.framework.file.IGraphFile;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.Workspace;

/**
 * Violet's auto save
 * 
 * @author Pawe³ Majka
 * 
 */
public class AutoSave implements ActionListener {

	private MainFrame mainFrame;
	private Timer saveTimer;

    private final int second = 1000;
    private final int saveInterval = 30 * second;
    private final String autoSaveDirectory = System.getProperty("user.home") + File.separator + "VioletUML";

	public AutoSave(MainFrame mainFrame)
	{
		if (mainFrame != null)
		{
			this.mainFrame = mainFrame;
			if (createVioletDirectory())
			{
				openAutoSaveProjects();
				initializeTimer();
			}
		}
	}

	private boolean createVioletDirectory()
	{
		File directory = new File(autoSaveDirectory);
		if (directory.isDirectory())
		{
			return true;
		}
		else
		{
			return directory.mkdir();
		}
	}

	private void openAutoSaveProjects() {
		File directory = new File(autoSaveDirectory);
		if (directory.isDirectory())
		{
			for (File file: directory.listFiles())
			{
				try {
					LocalFile autoSaveFile = new LocalFile(file);
					IGraphFile graphFile = new GraphFile(autoSaveFile);
					
					IWorkspace workspace = new Workspace(graphFile);
					mainFrame.addWorkspace(workspace);
					
					file.delete();
				} catch (IOException e) {
                    String message = MessageFormat.format(fileImportErrorMessage, e.getMessage());
                    JOptionPane.showMessageDialog(null, message, fileImportError, JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	private void initializeTimer()
	{
		saveTimer = new Timer(saveInterval, (ActionListener) this);
		saveTimer.setInitialDelay(0);
		saveTimer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    for (IWorkspace workspace: mainFrame.getWorkspaceList())
	    {
	    	IGraphFile graphFile = workspace.getGraphFile();
	    	if (graphFile.isSaveRequired())
	    	{
	    		graphFile.autoSave();
	    	}
	    }
	}

	@ResourceBundleBean(key = "autosave.import.error")
	private String fileImportError;

	@ResourceBundleBean(key = "autosave.import.error.message")
	private String fileImportErrorMessage;
}
