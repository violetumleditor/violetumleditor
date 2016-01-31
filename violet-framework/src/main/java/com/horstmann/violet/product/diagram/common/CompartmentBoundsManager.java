package com.horstmann.violet.product.diagram.common;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.product.diagram.abstracts.property.MultiLineString;

/**
 * Manager is responsible for calculating bounds for field compartments in
 * nodes.
 */
public class CompartmentBoundsManager {

	private static final double DEFAULT_HEIGHT = 60;
	private static final double DEFAULT_COMPARTMENT_HEIGHT = 20;
	private static final double DEFAULT_WIDTH = 100;

	private List<MultiLineString> fields = new ArrayList<MultiLineString>();
	private double compartmentHeight;
	private double height;
	private double width;

	/**
	 * Construct manager for list of fields and default sizes
	 * 
	 * @param fields
	 *            list of fields
	 */
	public CompartmentBoundsManager(List<MultiLineString> fields) {
		this(fields, DEFAULT_HEIGHT, DEFAULT_COMPARTMENT_HEIGHT, DEFAULT_WIDTH);
	}

	/**
	 * Construct manager for list of fields and given sizes
	 * 
	 * @param fields
	 *            list of fields
	 * @param height
	 *            default height of a node
	 * @param compartmentHeight
	 *            default height of one compartment
	 * @param defaultWidth
	 *            default width of a node
	 */
	public CompartmentBoundsManager(List<MultiLineString> fields, double height, double compartmentHeight,
			double defaultWidth) {
		this.fields = fields;
		this.height = height;
		this.compartmentHeight = compartmentHeight;
		this.width = defaultWidth;
	}

	/**
	 * @param currentLocation
	 *            location of the node
	 * @param field
	 *            field with which this compartment is associated
	 * @param graph
	 *            graph containing node
	 * @return bounds of the first (top) compartment
	 */
	public Rectangle2D getFirstRectangleBounds(Point2D currentLocation, MultiLineString field, IGraph graph) {
		double x = currentLocation.getX();
		double y = currentLocation.getY();
		return getBounds(field, height, x, y, graph);
	}

	/**
	 * @param upperField
	 *            compartment which is directly above this one
	 * @param field
	 *            field with which this compartment is associated
	 * @param graph
	 *            graph containing node
	 * @return bounds of the compartment
	 */
	public Rectangle2D getRectangleBounds(Rectangle2D upperField, MultiLineString field, IGraph graph) {
		double x = upperField.getX();
		double y = upperField.getMaxY();
		return getBounds(field, 0, x, y, graph);
	}

	private Rectangle2D getBounds(MultiLineString field, double optionValue, double x, double y, IGraph graph) {
		Rectangle2D globalBounds = new Rectangle2D.Double(0, 0, 0, 0);
		globalBounds.add(field.getBounds());

		boolean areFieldsNotEmpty = areFieldsNotEmpty();
		double defaultHeight = areFieldsNotEmpty ? compartmentHeight : optionValue;
		globalBounds.add(new Rectangle2D.Double(0, 0, width, defaultHeight));

		double w = globalBounds.getWidth();
		double h = globalBounds.getHeight();
		globalBounds.setFrame(x, y, w, h);
		Rectangle2D snappedBounds = graph.getGridSticker().snap(globalBounds);
		return snappedBounds;
	}

	private boolean areFieldsNotEmpty() {
		boolean result = false;
		for (MultiLineString each : fields) {
			result = result || !(each.getText().length() == 0);
			if (result) {
				return result;
			}
		}
		return result;
	}

	public void setFields(List<MultiLineString> newFields) {
		fields = newFields;
	}

}
