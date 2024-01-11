package com.horstmann.violet.product.diagram.abstracts.edge;

import java.awt.geom.Point2D;

import com.horstmann.violet.product.diagram.abstracts.ISelectable;

public interface ITransitionPoint extends ISelectable {
	
	void setX(double x);
	void setY(double y);
	double getX(); 	
	double getY();
	Point2D toPoint2D();
	

}
