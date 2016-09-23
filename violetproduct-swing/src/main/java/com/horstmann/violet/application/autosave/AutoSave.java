package com.horstmann.violet.application.autosave;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import com.horstmann.violet.application.gui.MainFrame;
import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.file.GraphFile;
import com.horstmann.violet.framework.file.IFile;
import com.horstmann.violet.framework.file.LocalFile;
import com.horstmann.violet.framework.file.persistence.IFilePersistenceService;
import com.horstmann.violet.framework.file.persistence.IFileReader;
import com.horstmann.violet.framework.file.persistence.JFileReader;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.framework.file.IGraphFile;
import com.horstmann.violet.workspace.IWorkspace;
import com.horstmann.violet.workspace.Workspace;

/**
 * Violet's auto save
 * 
 * @author Pawel Majka
 * 
 */
public class AutoSave implements ActionListener {

	private MainFrame mainFrame;
	private Timer saveTimer;

    private final int second = 1000;
    private final int saveInterval = 60 * second;
    private final String autoSaveDirectory = System.getProperty("user.home") + File.separator + "VioletUML";

	public AutoSave(MainFrame mainFrame)
	{
		BeanInjector.getInjector().inject(this);

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

	private void openAutoSaveProjects()
	{
		File directory = new File(autoSaveDirectory);
		if (directory.isDirectory())
		{
			File[] files = directory.listFiles();
			if (files.length == 0)
				return;

			for (File file: files)
			{
				try {
					IFile autoSaveFile = new LocalFile(file);
					IFileReader readFile = new JFileReader(file);
					InputStream in = readFile.getInputStream();
					if (in != null)
					{
						IGraph graph = this.filePersistenceService.read(in);
						IGraphFile graphFile = new GraphFile(autoSaveFile);
					
						IWorkspace workspace = new Workspace(graphFile);
						mainFrame.addWorkspace(workspace);
					
						file.delete();
					}
				} catch (IOException e) {
					file.delete();
				} catch (Exception e) {
					file.delete();
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

	@InjectedBean
	private IFilePersistenceService filePersistenceService;
}
