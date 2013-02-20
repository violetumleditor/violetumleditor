package com.horstmann.violet.workspace.sidebar;

import java.awt.Component;

import com.horstmann.violet.workspace.IWorkspace;

/**
 * An element displayed on a side bar. Usually, this is a JPanel
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public interface ISideBarElement
{

    /**
     * Method invoked when this element is added to a sidebar
     * 
     * @param workspace
     */
    public void install(IWorkspace workspace);


    /**
     * @return the AWT component representing this side bar
     */
    public Component getAWTComponent();

}
