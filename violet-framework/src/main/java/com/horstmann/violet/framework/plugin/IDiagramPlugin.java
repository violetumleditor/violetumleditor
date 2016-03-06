package com.horstmann.violet.framework.plugin;

import com.horstmann.violet.product.diagram.abstracts.IGraph;

/**
 * Describes a Violet's plugin embedding a new kind of diagram.
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public interface IDiagramPlugin extends IPlugin
{
    /**
     * @return diagram type name (ex : Class Diagram)
     */
    String getName();
    
    /**
     * @return the category of this diagram (ex : Static diagram)
     */
    String getCategory();
    
    /**
     * @return file extension associated to this graph (ex : .class.violet)
     */
    String getFileExtension();

    /**
     * @return file extension textual name (ex : Class Diagram Files)
     */
    String getFileExtensionName();
    
    /**
     * @return path of a diagram file used to introduce this kind of diagram
     */
    String getSampleFilePath();

    /**
     * @return corresponding graph class
     */
    Class<? extends IGraph> getGraphClass();

}
