package com.horstmann.violet.product.diagram.abstracts.node;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


/**
 * Created by Wojtek on 2016-01-31.
 */
public abstract class RoundRectangularNode extends RectangularNode {

    @Override
    public Rectangle2D getBounds()
    {
        Point2D currentLocation = getLocation();
        double x = currentLocation.getX();
        double y = currentLocation.getY();
        double w = getWidth();
        double h = getHeight();
        Rectangle2D.Double globalBounds = new Rectangle2D.Double(x, y, w, h);
        Rectangle2D snappedBounds = getGraph().getGridSticker().snap(globalBounds);
        return snappedBounds;
    }

    public void draw(Graphics2D g2)
    {
        super.draw(g2);
        Color oldColor = g2.getColor();
        Shape shape = getShape();
        g2.setColor(getBackgroundColor());
        g2.fill(shape);
        g2.setColor(getBorderColor());
        g2.draw(shape);
        g2.setColor(getTextColor());
        selfDraw(g2, getBounds());
        g2.setColor(oldColor);
    }

    public abstract void selfDraw(Graphics2D g2, Rectangle2D bounds);
    public abstract Double getWidth();
    public abstract Double getHeight();

}
