package com.horstmann.violet.product.diagram.abstracts;

import java.awt.geom.Point2D;
import java.util.List;

public interface ISelectable {
	
	List<Point2D> getSelectionPoints();
	
	ISelectable getParent();
	
}
