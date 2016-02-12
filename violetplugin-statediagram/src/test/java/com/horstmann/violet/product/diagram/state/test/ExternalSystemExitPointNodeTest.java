package com.horstmann.violet.product.diagram.state.test;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Graphics2D;

import org.junit.Test;
import org.mockito.Mockito;
import com.horstmann.violet.product.diagram.state.ExternalSystemExitPointNode;


public class ExternalSystemExitPointNodeTest {

	@Test
	public void testClonedNodeValues() {
		ExternalSystemExitPointNode node = new ExternalSystemExitPointNode();
		node.getName().setText("Jakub");
		ExternalSystemExitPointNode node2 = node.clone();
		assertEquals(node.getName().toString(), node2.getName().toString());
	}
	
	@Test
	public void testDraw()
	{
		ExternalSystemExitPointNode node = new ExternalSystemExitPointNode();
		Color oldColor = node.getBackgroundColor();
		Graphics2D gMock = Mockito.mock(Graphics2D.class);
		node.draw(gMock);
		Mockito.verify(gMock).setColor(oldColor);
		
	}
}
