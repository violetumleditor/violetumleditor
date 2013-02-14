package com.horstmann.violet.product.diagram.abstracts;

/**
 * Indicates that this object can be identified over an unique id and a revision number
 * 
 * 
 * @author Alexandre de Pellegrin
 * 
 */
public interface IIdentifiable
{

    /**
     * Returns a unique id of this node to make it easier to identify
     * 
     * @return a unique id
     */
    Id getId();

    /**
     * Sets unique id to this node to make it easier to identify
     * 
     * @param id new unique id
     */
    void setId(Id id);

    /**
     * Returns current node revision
     */
    Integer getRevision();

    /**
     * Updates current node revision number
     * 
     * @param newRevisionNumber n
     */
    void setRevision(Integer newRevisionNumber);
    
    /**
     * Increments revision number
     */
    void incrementRevision();

}
