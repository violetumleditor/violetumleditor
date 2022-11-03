package com.horstmann.violet.framework.file.persistence;

import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;
import java.util.List;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * Custom XStream converter for {@link Rectangle2D}.
 * Inspired from Mikle Garin source code
 *
 * @author Alexandre de Pellegrin  
 */
public class RoundRectangle2DConverter extends AbstractSingleValueConverter {
	@Override
	public boolean canConvert(final Class type) {
		return RoundRectangle2D.class.isAssignableFrom(type);
	}

	@Override
	public String toString(final Object obj) {
		return pointToString((RoundRectangle2D) obj);
	}

	@Override
	public Object fromString(final String str) {
		return pointFromString(str);
	}

	/**
	 * Returns {@link RoundRectangle2D} converted into string.
	 *
	 * @param rectangle {@link RoundRectangle2D} to convert
	 * @return {@link RoundRectangle2D} converted into string
	 */
	public static String pointToString(final RoundRectangle2D rectangle) {
		return rectangle.getX() + "," + rectangle.getY() + "," + rectangle.getWidth() + "," + rectangle.getHeight() + "," + rectangle.getArcWidth() + "," + rectangle.getArcHeight();
	}

	/**
	 * Returns {@link RoundRectangle2D} read from string.
	 *
	 * @param rectangle {@link RoundRectangle2D} string
	 * @return {@link RoundRectangle2D} read from string
	 */
	public static RoundRectangle2D.Double pointFromString(final String rectangle) {
		try {
			final List<String> points = Arrays.asList(rectangle.split(","));
			final double x = Double.parseDouble(points.get(0));
			final double y = Double.parseDouble(points.get(1));
			final double w = Double.parseDouble(points.get(2));
			final double h = Double.parseDouble(points.get(3));
			final double arcw = Double.parseDouble(points.get(4));
			final double arch = Double.parseDouble(points.get(5));
			return new RoundRectangle2D.Double(x, y, w, h, arcw, arch);
		} catch (Exception e) {
			throw new RuntimeException("Unable to parse RoundRectangle2D: " + rectangle, e);
		}
	}
}
