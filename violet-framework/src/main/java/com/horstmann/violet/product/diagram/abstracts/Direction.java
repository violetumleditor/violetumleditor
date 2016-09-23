/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.product.diagram.abstracts;

import java.awt.geom.Point2D;

/**
 * TODO need refactor
 * This class describes a direction in the 2D plane. A direction is a vector of length 1 with an angle between 0 (inclusive) and 360
 * degrees (exclusive). There is also a degenerate direction of length 0.
 */
public class Direction
{
    /**
     * Constructs a direction (normalized to length 1).
     * 
     * @param dx the x-value of the direction
     * @param dy the corresponding y-value of the direction
     */
    public Direction(double dx, double dy)
    {
        x = dx;
        y = dy;
        double length = Math.sqrt(x * x + y * y);
        if (length == 0) return;
        x = x / length;
        y = y / length;
    }

    /**
     * Constructs a direction between two points
     * 
     * @param p the starting point
     * @param q the ending point
     */
    public Direction(Point2D p, Point2D q)
    {
        this(q.getX() - p.getX(), q.getY() - p.getY());
    }

    /**
     * Turns this direction by an angle.
     * 
     * @param angle the angle in degrees
     */
    public Direction turn(double angle)
    {
        double a = Math.toRadians(angle);
        return new Direction(x * Math.cos(a) - y * Math.sin(a), x * Math.sin(a) + y * Math.cos(a));
    }

    /**
     * Gets the x-component of this direction
     * 
     * @return the x-component (between -1 and 1)
     */
    public double getX()
    {
        return x;
    }

    /**
     * Gets the y-component of this direction
     * 
     * @return the y-component (between -1 and 1)
     */
    public double getY()
    {
        return y;
    }

    /**
     * Gets the nearest direction corresponding to a cardinal one (NORTH, SOUTH, EAST, WEST)
     * 
     * @return Direction.NORTH or Direction.SOUTH or Direction.EAST or Direction.WEST
     */
    public Direction getNearestCardinalDirection()
    {
        long privateX = Math.round(this.getX());
        long privateY = Math.round(this.getY());
        if (Math.abs(privateX) == 1 && Math.abs(privateY) == 1)
        {
            if (Math.abs(this.getX()) >= Math.abs(this.getY()))
            {
                privateY = 0;
            }
            if (Math.abs(this.getX()) < Math.abs(this.getY()))
            {
                privateX = 0;
            }
        }
        if (privateX == 0 && privateY == -1)
        {
            return Direction.NORTH;
        }
        if (privateX == 0 && privateY == 1)
        {
            return Direction.SOUTH;
        }
        if (privateX == 1 && privateY == 0)
        {
            return Direction.EAST;
        }
        if (privateX == -1 && privateY == 0)
        {
            return Direction.WEST;
        }
        // Default case returns no change (should never heppen)
        return this;
    }
    
    

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Direction other = (Direction) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}



	private double x;
    private double y;

    public static final Direction NORTH = new Direction(0, -1);
    public static final Direction SOUTH = new Direction(0, 1);
    public static final Direction EAST = new Direction(1, 0);
    public static final Direction WEST = new Direction(-1, 0);
}
