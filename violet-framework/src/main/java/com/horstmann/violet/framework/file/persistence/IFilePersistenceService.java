package com.horstmann.violet.framework.file.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.horstmann.violet.product.diagram.abstracts.IGraph;

/**
 * Services dedicated to read/save a graph content from/to an input/outputstream
 * @author Alexandre de Pellegrin
 *
 */
public interface IFilePersistenceService
{

    /**
     * Writes the given graph in an outputstream. We use long-term bean persistence to save the program data. See
     * http://java.sun.com/products/jfc/tsc/articles/persistence4/index.html for an overview.
     * 
     * @param out the stream for saving
     */
    public void write(IGraph graph, OutputStream out);
    
    
    /**
     * Reads a graph file
     * 
     * @param in the input stream to read
     * @return the graph that is read in
     */
    public IGraph read(InputStream in) throws IOException;
    
    
}
