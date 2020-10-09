package com.horstmann.violet.product.diagram.abstracts.node;

import java.awt.geom.Rectangle2D;

/**
 * Node size is automatic. However, by implementing this interface,
 * the user isable to add extra width and extra height to this diagram element 
 * 
 * @author Alexandre de Pellegrin
 *
 */
public interface IResizableNode
{
    
	
    static final int RESIZABLE_POINT_SIZE = 20;
	
	void setPreferredSize(Rectangle2D size);
	
	Rectangle2D getPreferredSize();
	
	Rectangle2D getBounds();
	
	
    /**
     * Methods returns point which allow to change size of node
     *
     * @return point which allow to change size of node
     */
    default Rectangle2D getResizableDragPoint() {
    	Rectangle2D nodeBounds = getBounds();
    	
    	double x = nodeBounds.getMaxX() - RESIZABLE_POINT_SIZE;
    	double y = nodeBounds.getMaxY() - RESIZABLE_POINT_SIZE;
    	
    	return new Rectangle2D.Double(x + RESIZABLE_POINT_SIZE / 2, y + RESIZABLE_POINT_SIZE / 2, RESIZABLE_POINT_SIZE, RESIZABLE_POINT_SIZE);
    }
    
    

}