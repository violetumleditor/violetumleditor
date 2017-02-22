package com.horstmann.violet.product.diagram.abstracts.node;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.horstmann.violet.product.diagram.common.node.ColorableNode;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;

/**
 * The Class SwitchableNodeTest.
 */
public class SwitchableNodeTest {

	/**
	 * The Class TestNodeOne.
	 */
	private class TestNodeOne extends ColorableNode implements ISwitchableNode {

		/** The data to copy. */
		private SingleLineText dataToCopy;

		/**
		 * Instantiates a new test node one.
		 *
		 * @param initialData
		 *            the initial data
		 */
		public TestNodeOne(SingleLineText initialData) {
			dataToCopy = initialData;
		}

		/**
		 * Instantiates a new test node one.
		 *
		 * @param node
		 */
		public TestNodeOne(TestNodeTwo node) {
			dataToCopy = node.dataToCopy.clone();
		}

		/**
		 * Gets the data to copy.
		 *
		 * @return the data to copy
		 */
		public LineText getDataToCopy() {
			return dataToCopy;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.horstmann.violet.product.diagram.abstracts.node.SwitchableNode#
		 * switchNode()
		 */
		@Override
		public INode switchNode() {
			return new TestNodeTwo(this);
		}

		@Override
		public LineText getName() {
			return null;
		}

		@Override
		public LineText getAttributes() {
			return null;
		}

		@Override
		public LineText getMethods() {
			return null;
		}
	}

	/**
	 * The Class TestNodeTwo.
	 */
	private class TestNodeTwo extends ColorableNode implements ISwitchableNode {

		/** The data to copy. */
		private SingleLineText dataToCopy;

		/**
		 * Instantiates a new test node two.
		 *
		 * @param initialData
		 *            the initial data
		 */
		public TestNodeTwo(SingleLineText initialData) {
			dataToCopy = initialData;
		}

		/**
		 * Instantiates a new test node two.
		 *
		 * @param node
		 */
		public TestNodeTwo(TestNodeOne node) {
			dataToCopy = node.dataToCopy.clone();
		}

		/**
		 * Gets the data to copy.
		 *
		 * @return the data to copy
		 */
		public LineText getDataToCopy() {
			return dataToCopy;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.horstmann.violet.product.diagram.abstracts.node.SwitchableNode#
		 * switchNode()
		 */
		@Override
		public INode switchNode() {
			return new TestNodeOne(this);
		}

		@Override
		public LineText getName() {
			return null;
		}

		@Override
		public LineText getAttributes() {
			return null;
		}

		@Override
		public LineText getMethods() {
			return null;
		}
	}

	/**
	 * Test to check if conversion between nodes is working
	 *
	 * @throws Exception
	 */
	@Test
	public void shouldReturnConvertedNodeWithData() throws Exception {
		final String TEST_STRING = "TestString";
		SingleLineText text = new SingleLineText();
		text.setText(TEST_STRING);
		TestNodeOne firstNode = new TestNodeOne(text);
		TestNodeTwo convertedNode = (TestNodeTwo) firstNode.switchNode();
		assertEquals(TEST_STRING, convertedNode.getDataToCopy().toDisplay());
	}

}