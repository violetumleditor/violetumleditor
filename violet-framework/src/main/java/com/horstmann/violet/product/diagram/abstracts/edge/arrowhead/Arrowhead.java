package com.horstmann.violet.product.diagram.abstracts.edge.arrowhead;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.Objects;

/**
 * This class defines arrow heads of various shapes.
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 20.02.2016
 */
public class Arrowhead implements Cloneable
{
    public Arrowhead(String name)
    {
        this.name = name;
    	this.borderColor = Color.BLACK;
        this.filledColor = null;
    }

    public Arrowhead(String name, Color filledColor)
    {
    	this.name = name;
        this.borderColor = Color.BLACK;
        this.filledColor = filledColor;
    }

    /**
     * Draws the arrowhead.
     *
     * @param graphics the graphics context
     * @param p a point on the axis of the arrow head
     * @param q the end point of the arrow head
     */
    public final void draw(Graphics2D graphics, Point2D p, Point2D q)
    {
        Color oldColor = graphics.getColor();

        GeneralPath path = getPath();
        rotatePath(path, calculateAngle(q, p));

        graphics.translate(q.getX(), q.getY());
        if(null != filledColor)
        {
            graphics.setColor(filledColor);
            graphics.fill(path);
        }

        graphics.setColor(borderColor);
        graphics.draw(path);
        graphics.translate(-q.getX(), -q.getY());
        graphics.setColor(oldColor);
    }

    /**
     * Calculates the angle between two points
     * @param p
     * @param q
     * @return angle
     */
    private double calculateAngle(Point2D p, Point2D q)
    {
        return Math.atan2(q.getY() - p.getY(), q.getX() - p.getX());
    }

    /**
     * The path is rotating on the angle
     * @param basePath
     * @param angle
     */
    private void rotatePath(GeneralPath basePath, double angle)
    {
        AffineTransform af = new AffineTransform();
        af.rotate(angle);
        basePath.transform(af);
    }

    /**
     * @return path type a empty/none
     */
    public GeneralPath getPath()
    {
        return new GeneralPath();
    }
    
    

    public void setFilledColor(Color filledColor) {
    	if (this.filledColor != null && !Color.WHITE.equals(this.filledColor)) {
    		this.filledColor = filledColor;
    	}
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}
	
	public Color getFilledColor() {
		return this.filledColor;
	}
	
	public Color getBorderColor() {
		return this.borderColor;
	}
	
	@Override
	public Arrowhead clone() {
		Arrowhead ah = new Arrowhead(this.name);
		ah.setFilledColor(this.filledColor);
		ah.setBorderColor(this.borderColor);
		return ah;
	}

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Arrowhead other = (Arrowhead) obj;
        return toString().equals(other.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(toString());
    }



	private Color filledColor;
    private Color borderColor;
    private String name;

    protected static final double ARROW_ANGLE = Math.PI / 6;
    protected static final double ARROW_LENGTH = 10;
}
