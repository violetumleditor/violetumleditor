package com.horstmann.violet.framework.file;

/**
 * GraphFile listener
 * 
 * @author Alexandre de Pellegrin
 *
 */
public interface IGraphFileListener
{
    
    /**
     * Invoked when any modification is made on the graph
     */
    public void onFileModified();
    
    /**
     * Invoked when graph has been saved
     */
    public void onFileSaved();

}
