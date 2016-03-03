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
     * Returns a unique id of this node_old to make it easier to identify
     * 
     * @return a unique id
     */
    Id getId();

    /**
     * Sets unique id to this node_old to make it easier to identify
     * 
     * @param id new unique id
     */
    void setId(Id id);

    /**
     * Returns current node_old revision
     */
    Integer getRevision();

    /**
     * Updates current node_old revision number
     * 
     * @param newRevisionNumber n
     */
    void setRevision(Integer newRevisionNumber);
    
    /**
     * Increments revision number
     */
    void incrementRevision();

}
