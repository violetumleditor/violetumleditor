package com.horstmann.violet.framework.file.persistence;

import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.List;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * Custom XStream converter for {@link Rectangle2D}.
 * Inspired from Mikle Garin source code
 *
 * @author Alexandre de Pellegrin  
 */
public class Rectangle2DConverter extends AbstractSingleValueConverter {
	@Override
	public boolean canConvert(final Class type) {
		return Rectangle2D.class.isAssignableFrom(type);
	}

	@Override
	public String toString(final Object obj) {
		return pointToString((Rectangle2D) obj);
	}

	@Override
	public Object fromString(final String str) {
		return pointFromString(str);
	}

	/**
	 * Returns {@link Rectangle2D} converted into string.
	 *
	 * @param rectangle {@link Rectangle2D} to convert
	 * @return {@link Rectangle2D} converted into string
	 */
	public static String pointToString(final Rectangle2D rectangle) {
		return rectangle.getX() + "," + rectangle.getY() + "," + rectangle.getWidth() + "," + rectangle.getHeight();
	}

	/**
	 * Returns {@link Rectangle2D} read from string.
	 *
	 * @param rectangle {@link Rectangle2D} string
	 * @return {@link Rectangle2D} read from string
	 */
	public static Rectangle2D.Double pointFromString(final String rectangle) {
		try {
			final List<String> points = Arrays.asList(rectangle.split(","));
			final double x = Double.parseDouble(points.get(0));
			final double y = Double.parseDouble(points.get(1));
			final double w = Double.parseDouble(points.get(2));
			final double h = Double.parseDouble(points.get(3));
			return new Rectangle2D.Double(x, y, w, h);
		} catch (Exception e) {
			throw new RuntimeException("Unable to parse Rectangle2D: " + rectangle, e);
		}
	}
}
