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

    @ResourceBundleBean(key = "dialog.autosave.title")
    private String saveRecoverFrameTitle;

    @ResourceBundleBean(key = "dialog.autosave.recover")
    private String buttonRecovery;

    @ResourceBundleBean(key = "dialog.autosave.startnew")
    private String buttonNew;

    /**
     * Open autosave frame
     */
    public AutoSaveRecover(MainFrame mainFrame)
    {

        this.mainFrame = mainFrame;

        ResourceBundleInjector.getInjector().inject(this);
        ManiocFramework.BeanInjector.getInjector().inject(this);
        this.setTitle(this.saveRecoverFrameTitle);
        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());

        JPanel buttonPanel = getButtonPanel();
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);
        setLocation();
    }

    private JPanel getButtonPanel()
    {

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));


        JButton buttonRecovery = new JButton(this.buttonRecovery);
        buttonRecovery.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAutoSaveFile();
                dispose();

            }
        }
        );

        JButton buttonNew = new JButton(this.buttonNew);
        buttonNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeAutoSaveFile();
                dispose();

            }
        }
        );
        this.getRootPane().setDefaultButton(buttonNew);
        buttonPanel.add(buttonNew);
        buttonPanel.add(buttonRecovery);
        return buttonPanel;
    }

    /**
     * Set JFrame position
     */
    private void setLocation()
    {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        int w = this.getSize().width;
        int h = this.getSize().height;
        int x = (dimension.width - w) / 2;
        int y = (dimension.height - h) / 2;
        this.setLocation(x, y);
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