package com.horstmann.violet.product.diagram.state.test;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Graphics2D;

import org.junit.Test;
import org.mockito.Mockito;
import com.horstmann.violet.product.diagram.state.ExternalSystemEntryPointNode;


public class ExternalSystemEntryPointNodeTest {

	@Test
	public void testClonedNodeValues() {
		ExternalSystemEntryPointNode node = new ExternalSystemEntryPointNode();
		node.getName().setText("Jakub");
		ExternalSystemEntryPointNode node2 = node.clone();
		assertEquals(node.getName().toString(), node2.getName().toString());
	}
	
	@Test
	public void testDraw()
	{
		ExternalSystemEntryPointNode node = new ExternalSystemEntryPointNode();
		Color oldColor = node.getBackgroundColor();
		Graphics2D gMock = Mockito.mock(Graphics2D.class);
		node.draw(gMock);
		Mockito.verify(gMock).setColor(oldColor);
		
	}
}
