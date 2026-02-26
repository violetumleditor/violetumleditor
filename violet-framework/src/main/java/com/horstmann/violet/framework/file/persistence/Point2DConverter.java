package com.horstmann.violet.framework.file.persistence;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * Custom XStream converter for {@link Point2D}.
 * Inspired from Mikle Garin source code
 *
 * @author Alexandre de Pellegrin  
 */
public class Point2DConverter extends AbstractSingleValueConverter {
	@Override
	@SuppressWarnings("rawtypes")
	public boolean canConvert(final Class type) {
		return Point2D.class.isAssignableFrom(type);
	}

	@Override
	public String toString(final Object obj) {
		return pointToString((Point2D) obj);
	}

	@Override
	public Object fromString(final String str) {
		return pointFromString(str);
	}

	/**
	 * Returns {@link Point2D} converted into string.
	 *
	 * @param point {@link Point2D} to convert
	 * @return {@link Point2D} converted into string
	 */
	public static String pointToString(final Point2D point) {
		return point.getX() + "," + point.getY();
	}

	/**
	 * Returns {@link Point2D} read from string.
	 *
	 * @param point {@link Point2D} string
	 * @return {@link Point2D} read from string
	 */
	public static Point2D.Double pointFromString(final String point) {
		try {
			final List<String> points = Arrays.asList(point.split(","));
			final double x = Double.parseDouble(points.get(0));
			final double y = Double.parseDouble(points.get(1));
			return new Point2D.Double(x, y);
		} catch (Exception e) {
			throw new RuntimeException("Unable to parse Point2D: " + point, e);
		}
	}
}
