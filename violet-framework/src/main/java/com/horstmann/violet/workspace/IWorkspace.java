package com.horstmann.violet.workspace;

import java.awt.event.KeyEvent;
import java.util.Optional;

import com.horstmann.violet.framework.file.IGraphFile;
import com.horstmann.violet.product.diagram.abstracts.Id;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.sidebar.ISideBar;

public interface IWorkspace
{
    public static interface KeyListenerDelegate
    {
        void handleKeyEvent(KeyEvent keyEvent);
    }

    /**
     * @return graph file
     */
    public IGraphFile getGraphFile();

    /**
     * @return graph editor
     */
    public IEditorPart getEditorPart();

    /**
     * @return current side bar
     */
    public ISideBar getSideBar();

    /**
     * @return current diagram's title
     */
    public String getTitle();

    /**
     * Sets current diagram's title
     */
    public void setTitle(String newTitle);

    /**
     * Gets the fileName property.
     *
     * @return the file path
     */
    public String getFilePath();

    /**
     * Sets the fileName property.
     *
     * @param path the file path
     */
    public void setFilePath(String path);

    /**
     * Registers a listener on this diagram panel to capture events
     *
     * @param l
     */
    public void addListener(IWorkspaceListener l);

    /**
     * @return unique id
     */
    public Id getId();

    /**
     * @return the awt component representing this workspace
     */
    public WorkspacePanel getAWTComponent();

    /**
     * Force a specific awt component to representing graphically this workspace
     *
     * @param panel
     */
    public void setAWTComponent(WorkspacePanel panel);

    /**
     * @return active workspace key listener;
     */
    public Optional<KeyListenerDelegate> getKeyListenerDelegate();

}
