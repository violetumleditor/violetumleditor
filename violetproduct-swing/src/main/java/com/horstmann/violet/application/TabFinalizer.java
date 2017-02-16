package com.horstmann.violet.application;

import com.horstmann.violet.application.gui.MainFrame;
import com.horstmann.violet.framework.dialog.DialogFactory;
import com.horstmann.violet.framework.file.IGraphFile;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.BeanInjector;
import com.horstmann.violet.framework.injection.bean.ManiocFramework.InjectedBean;
import com.horstmann.violet.framework.injection.resources.ResourceBundleInjector;
import com.horstmann.violet.framework.injection.resources.annotation.ResourceBundleBean;
import com.horstmann.violet.framework.userpreferences.UserPreferencesService;
import com.horstmann.violet.workspace.IWorkspace;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * A class that prepares tabs to close and prevents user from losing changes by closing tab.
 */
public class TabFinalizer {

    /**
     * Constructs a tab finalizer.
     *
     * @param mainFrame frame for which finalizer should be created
     */
    public TabFinalizer(final MainFrame mainFrame)
    {
        BeanInjector.getInjector().inject(this);
        ResourceBundleInjector.getInjector().inject(this);
        this.mainFrame = mainFrame;
    }

    /**
     * Perform tab finalization and return if the tab is ready to close.
     *
     * @param workspace workspace of a tab that should be finalized
     * @return true if the tab is ready to close.
     */
    public boolean isReadyToClose(final IWorkspace workspace)
    {
        final IGraphFile currentGraphFile = workspace.getGraphFile();
        final int userDecision = askUserForSaveFile();
        return performUserOption(currentGraphFile, userDecision);
    }

    /**
     * Create and show dialog window with question, if save file.
     *
     * @return User decision as int value
     * @see JOptionPane
     */
    private int askUserForSaveFile()
    {
        final JOptionPane optionPane = new JOptionPane(this.dialogExitMessage,
                JOptionPane.CLOSED_OPTION, JOptionPane.YES_NO_CANCEL_OPTION, this.dialogExitIcon);
        dialogFactory.showDialog(optionPane, this.dialogExitTitle, true);
        final int decision = getOptionFromPane(optionPane);
        return decision;
    }

    /**
     * Returns user's choice from option panel
     *
     * @param optionPane option panel
     * @return number representing user's choice
     */
    private int getOptionFromPane(final JOptionPane optionPane)
    {
        int result = JOptionPane.YES_OPTION;
        if (!JOptionPane.UNINITIALIZED_VALUE.equals(optionPane.getValue()))
        {
            result = (Integer) optionPane.getValue();
        }
        return result;
    }

    /**
     *  Performs operation depending on users decision
     *
     * @param currentFile current file
     * @return state if file ready to close
     */
    private boolean performUserOption(final IGraphFile currentFile, final int userDecision)
    {
        switch (userDecision)
        {
            case JOptionPane.CANCEL_OPTION:
                return false;
            case JOptionPane.YES_OPTION:
                currentFile.save();
                setActiveDiagramPreference();
                return true;
            case JOptionPane.NO_OPTION:
                setActiveDiagramPreference();
                return true;
            default:
                return false;
        }
    }

    /**
     * Set active diagram in user preferences
     */
    private void setActiveDiagramPreference()
    {
        final IWorkspace activeWorkspace = mainFrame.getActiveWorkspace();
        final IGraphFile activeWorkspaceGraphFile = activeWorkspace.getGraphFile();
        this.userPreferencesService.setActiveDiagramFile(activeWorkspaceGraphFile);
    }

    private final MainFrame mainFrame;

    @ResourceBundleBean(key = "dialog.exit.icon")
    private ImageIcon dialogExitIcon;

    @ResourceBundleBean(key = "dialog.exit.ok")
    private String dialogExitMessage;

    @ResourceBundleBean(key = "dialog.exit.title")
    private String dialogExitTitle;

    @InjectedBean
    private DialogFactory dialogFactory;

    @InjectedBean
    private UserPreferencesService userPreferencesService;
}
