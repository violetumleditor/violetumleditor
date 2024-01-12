package com.horstmann.violet.framework.file;

import java.io.OutputStream;

import com.horstmann.violet.product.diagram.abstracts.IGraph;

public interface IGraphFile extends IFile
{

    /**
     * @return associated graph
     */
    public abstract IGraph getGraph();
    
    
    /**
     * Must be triggered to allow file saving
     */
    public abstract void setSaveRequired();
    
    /**
     * To know if the graph needs to be saved
     * @return true if save is required
     */
    public abstract boolean isSaveRequired();
    
    
    /**
     * Saves the graph
     */
    public abstract void save();

    /**
     * Saves the graph to a new URI (thiw will open a file chooser)
     */
    public abstract void saveToNewLocation();
    
    /**
     * Adds a listener to be informed each time the graph is modified or saved
     * 
     * @param listener
     */
    public abstract void addListener(IGraphFileListener listener);

    /**
     * Removes a listener
     * 
     * @param listener
     */
    public abstract void removeListener(IGraphFileListener listener);

    /**
     * Exports the graph as an image to the clipboard and shows a dialog box on the parent frame when it is done.
     * 
     */
    public abstract void exportToClipboard();

    /**
     * Exports the current graph to an image file.
     * 
     * @param out the output stream
     * @param format the desired file format
     */
    public abstract void exportImage(OutputStream out, String format);

    /**
     * Exports the current graph to a PDF file.
     *
     * @param out the output stream
     */
    public abstract void exportToPdf(OutputStream out);

    /**
     * Prints the graph
     * 
     */
    public abstract void exportToPrinter();
    

    /**
     * @return current file name (or null if not saved yet)
     */
    public String getFilename();
    
    /**
     * @return graph file location (or null if not saved yet)
     */
    public String getDirectory();
    
}