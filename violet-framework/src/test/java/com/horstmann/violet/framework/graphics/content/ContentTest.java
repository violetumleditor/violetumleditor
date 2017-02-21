package com.horstmann.violet.framework.graphics.content;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 12.01.2016
 */
public class ContentTest
{
    private class TestContent extends Content
    {
        TestContent()
        {
            super();
        }

        @Override
        public void draw(Graphics2D graphics) {

        }
    }

    @Test
    public void testContains() throws Exception {
        Content content = new TestContent();

        content.setMinWidth(50);
        content.setMinHeight(20);

        assertTrue(content.contains(new Point(0,0)));
        assertTrue(content.contains(new Point(49,19)));
        assertTrue(content.contains(new Point(30,10)));
        assertTrue(content.contains(new Point(1,1)));
        assertTrue(content.contains(new Point(49,0)));

        assertFalse(content.contains(new Point(50,20)));
        assertFalse(content.contains(new Point(-1,0)));
        assertFalse(content.contains(new Point(100,100)));
    }

    @Test
    public void testGetBounds() throws Exception {
        Content content = new TestContent();

        content.setMinWidth(50);
        content.setMinHeight(20);

        assertEquals(0, content.getBounds().getX(), 0.01);
        assertEquals(0, content.getBounds().getY(), 0.01);
        assertEquals(50, content.getBounds().getWidth(), 0.01);
        assertEquals(20, content.getBounds().getHeight(), 0.01);
    }

    @Test
    public void testGetBoundsEmpty() throws Exception {
        Content content = new TestContent();
        assertEquals(0, content.getBounds().getX(), 0.01);
        assertEquals(0, content.getBounds().getY(), 0.01);
        assertEquals(0, content.getBounds().getWidth(), 0.01);
        assertEquals(0, content.getBounds().getHeight(), 0.01);
    }

    @Test
    public void testGetX() throws Exception {
        Content content = new TestContent();
        assertEquals(0, content.getX(), 0.01);
    }

    @Test
    public void testGetY() throws Exception {
        Content content = new TestContent();
        assertEquals(0, content.getY(), 0.01);
    }

    @Test
    public void testGetWidth() throws Exception {
        Content content = new TestContent();
        assertEquals(0, content.getWidth(), 0.01);

        content.setMinWidth(50);
        assertEquals(50, content.getWidth(), 0.01);
    }

    @Test
    public void testGetHeight() throws Exception {
        Content content = new TestContent();
        assertEquals(0, content.getHeight(), 0.01);

        content.setMinHeight(20);
        assertEquals(20, content.getHeight(), 0.01);
    }

    @Test
    public void testSetMinWidth() throws Exception {
        Content content = new TestContent();
        content.setMinWidth(500);
        assertEquals(500, content.getWidth(), 0.01);
    }

    @Test
    public void testSetMinHeight() throws Exception {
        Content content = new TestContent();
        content.setMinHeight(200);
        assertEquals(200, content.getHeight(), 0.01);
    }

    @Test
    public void testRefreshParent() throws Exception {
        TestingRefreshContent testingRefreshContent = new TestingRefreshContent();
        assertEquals(0, testingRefreshContent.refreshUpCount);
        assertEquals(0, testingRefreshContent.refreshDownCount);

        Content content = new TestContent();
        testingRefreshContent.setAsParent(content);

        content.refresh();
        assertEquals(1, testingRefreshContent.refreshUpCount);
        assertEquals(0, testingRefreshContent.refreshDownCount);
    }

    @Test
    public void testRefreshChildren() throws Exception {
        TestingRefreshContent testingRefreshContent = new TestingRefreshContent();
        assertEquals(0, testingRefreshContent.refreshUpCount);
        assertEquals(0, testingRefreshContent.refreshDownCount);

        Content content = new TestContent();
        testingRefreshContent.setAsChildren(content);

        content.refresh();
        assertEquals(0, testingRefreshContent.refreshUpCount);
        assertEquals(0, testingRefreshContent.refreshDownCount);
    }
}