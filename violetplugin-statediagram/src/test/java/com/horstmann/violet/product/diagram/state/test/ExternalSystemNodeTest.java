package com.horstmann.violet.product.diagram.state.test;
import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.junit.Test;
import org.mockito.Mockito;

import com.horstmann.violet.product.diagram.abstracts.property.MultiLineString;

import com.horstmann.violet.product.diagram.state.ExternalSystemNode;

public class ExternalSystemNodeTest {
	@Test
	public void testStartingValues() {
		ExternalSystemNode node = new ExternalSystemNode();
		assertEquals(node.getName().getText(),"");
		assertEquals(node.getBackgroundColor(),new Color(240,240,240));
		assertEquals(node.getBorderColor(),new Color(91,91,91));
		assertEquals(node.getTextColor(),new Color(51,51,51));
	}
	
	@Test
	public void testGetBounds() {
		ExternalSystemNode node = new ExternalSystemNode();
		assertEquals(node.getBounds(),new Rectangle2D.Double(0,0,20,20));
	}
	
	
	@Test
	public void testSetName() {
		ExternalSystemNode node = new ExternalSystemNode();
		MultiLineString multiLineString = new MultiLineString();
		node.setName(multiLineString);
		assertEquals(node.getName(), multiLineString);
	}
	
	@Test
	public void testDraw() {
		ExternalSystemNode node = new ExternalSystemNode();

		Color oldColor = node.getBackgroundColor();
		Graphics2D gMock = Mockito.mock(Graphics2D.class);
		node.draw(gMock);
		
		//Mockito.verify(gMock).setColor(oldColor);
		
	}
}
