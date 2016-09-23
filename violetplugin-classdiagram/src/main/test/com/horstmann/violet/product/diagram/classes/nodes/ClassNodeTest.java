package com.horstmann.violet.product.diagram.classes.node;

import static org.junit.Assert.*;

import java.awt.geom.Rectangle2D;

import org.junit.Test;

import com.horstmann.violet.product.diagram.abstracts.property.MultiLineString;

public class ClassNodeTest {

	@Test
	public void testEmptyClassNodeBounds() {
		ClassNode node = new ClassNode();

		assertBounds(getEmptyClassNodeBounds(), node.getBounds());
	}

	@Test
	public void testClassNodeWithNameBounds() {
		ClassNode node = new ClassNode();
		node.setName(createMultiLineString("TestClass"));
		assertBounds(getClassNodeWithNameBounds(), node.getBounds());
	}

	@Test
	public void testFullClassNodeBounds() {
		ClassNode node = new ClassNode();
		node.setName(createMultiLineString("TestClass"));
		node.setAttributes(createMultiLineString("attributes"));
		node.setMethods(createMultiLineString("methods"));

		assertBounds(getFullClassNodeBounds(), node.getBounds());
	}

	@Test
	public void testClassNodeWithAttributesBounds() {
		ClassNode node = new ClassNode();
		node.setAttributes(createMultiLineString("attributes"));
		assertBounds(getClassNodeWithAttributesBounds(), node.getBounds());
	}

	@Test
	public void testClassNodeWithNameAndMethodsBounds() {
		ClassNode node = new ClassNode();
		node.setName(createMultiLineString("TestClass"));
		node.setMethods(createMultiLineString("methods"));

		assertBounds(getClassNodeWithNameAndMethodsBounds(), node.getBounds());
	}

	@Test
	public void testClassNodeWithMethodsBounds() {
		ClassNode node = new ClassNode();
		node.setMethods(createMultiLineString("methods"));

		assertBounds(getClassNodeWithMethodsBounds(), node.getBounds());
	}
	
	private Rectangle2D getClassNodeWithAttributesBounds() {
		return new Rectangle2D.Double(0.0, 0.0, 100.0, 76.0);
	}

	private Rectangle2D getClassNodeWithNameBounds() {
		return new Rectangle2D.Double(0.0, 0.0, 100.0, 60.0);
	}

	private Rectangle2D getClassNodeWithMethodsBounds() {
		return new Rectangle2D.Double(0.0, 0.0, 100.0, 76.0);
	}

	private Rectangle2D getClassNodeWithNameAndMethodsBounds() {
		return new Rectangle2D.Double(0.0, 0.0, 100.0, 92.0);
	}

	private Rectangle2D getFullClassNodeBounds() {
		return new Rectangle2D.Double(0.0, 0.0, 100.0, 108.0);
	}

	private Rectangle2D getEmptyClassNodeBounds() {
		return new Rectangle2D.Double(0.0, 0.0, 100.0, 60.0);
	}

	private void assertBounds(Rectangle2D expected, Rectangle2D actual) {
		assertEquals(expected.getX(), actual.getX(), 0.0);
		assertEquals(expected.getY(), actual.getY(), 0.0);
		assertEquals(expected.getHeight(), actual.getHeight(), 0.0);
		assertEquals(expected.getWidth(), actual.getWidth(), 0.0);
	}

	private MultiLineString createMultiLineString(String text) {
		MultiLineString result = new MultiLineString();
		result.setText(text);
		return result;
	}

}
