package com.horstmann.violet.application.autosave;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.Timer;

import com.horstmann.violet.application.gui.MainFrame;
import com.horstmann.violet.framework.file.IGraphFile;
import com.horstmann.violet.workspace.IWorkspace;

public class AutoSave implements ActionListener {

	private MainFrame mainFrame;
	private List<IWorkspace> workspaceList;

    private Timer saveTimer;
    private String autoSaveDirectory = System.getProperty("user.home") + "/VioletUML";
	
	public AutoSave(MainFrame mainFrame)
	{
		this.mainFrame = mainFrame;
		createVioletDirectory();
		initializeTimer();
	}
	
	public String getDirectory()
	{
		return autoSaveDirectory;
	}
	
	private void createVioletDirectory()
	{
		new File(autoSaveDirectory).mkdir();
	}
	
	private void initializeTimer()
	{
		saveTimer = new Timer(30000, (ActionListener) this);
		saveTimer.setInitialDelay(0);
		saveTimer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    workspaceList = mainFrame.getWorkspaceList();
	    
	    if (workspaceList.size() == 0) return;
	    for (IWorkspace aWorkspacel : workspaceList)
	    {
	    	IGraphFile graphFile = aWorkspacel.getGraphFile();
	    	if (graphFile.isSaveRequired())
	    	{
	    		graphFile.autoSave();
	    	}
	    }
	}
}
