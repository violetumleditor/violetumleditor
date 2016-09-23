package com.horstmann.violet.product.diagram.abstracts.property.string;

import com.horstmann.violet.product.diagram.property.text.LineText;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 12.01.2016
 */
public class LineTextTest
{
    private static class TestLineText extends LineText
    {
        public TestLineText() {
            super();
        }
        protected TestLineText(TestLineText lineText) throws CloneNotSupportedException {
            super(lineText);
        }
        @Override
        protected TestLineText copy() throws CloneNotSupportedException {
            return new TestLineText(this);
        }
        @Override
        public String toDisplay() {
            return null;
        }
        @Override
        public String toEdit() {
            return null;
        }
        @Override
        public void setText(String text) {
            setLabelText(text);
            notifyAboutChange();
        }
    }

    private static class TestChangeListener implements LineText.ChangeListener
    {
        @Override
        public void onChange() {
            ++count;
        }
        public int count = 0;
    }

    @Test
    public void testClone() throws Exception {
        LineText lineText = new TestLineText();
        lineText.setAlignment(LineText.CENTER);
        lineText.setTextColor(Color.RED);

        LineText cloned = lineText.clone();
        assertEquals(lineText.getTextColor(), cloned.getTextColor());
        assertEquals(lineText.getAlignment(), cloned.getAlignment());
        assertEquals(lineText.getBounds().getX(), cloned.getBounds().getX(), 0.01);
        assertEquals(lineText.getBounds().getY(), cloned.getBounds().getY(), 0.01);
        assertEquals(lineText.getBounds().getWidth(), cloned.getBounds().getWidth(), 0.01);
        assertEquals(lineText.getBounds().getHeight(), cloned.getBounds().getHeight(), 0.01);
    }

    @Test
    public void testGetBoundsEmpty() throws Exception {
        LineText lineText = new TestLineText();

        assertEquals(0, lineText.getBounds().getX(), 0.01);
        assertEquals(0, lineText.getBounds().getY(), 0.01);
        assertEquals(0, lineText.getBounds().getWidth(), 0.01);
        assertEquals(0, lineText.getBounds().getHeight(), 0.01);
    }

    @Test
    public void testGetBounds() throws Exception {
        LineText lineText = new TestLineText();
        lineText.setText("test");

        assertEquals(0, lineText.getBounds().getX(), 0.01);
        assertEquals(0, lineText.getBounds().getY(), 0.01);
        assertEquals(22, lineText.getBounds().getWidth(), 0.01);
        assertEquals(16, lineText.getBounds().getHeight(), 0.01);
    }

    @Test
    public void testGetTextColor() throws Exception {
        LineText lineText = new TestLineText();

        lineText.setTextColor(Color.RED);
        assertEquals(Color.RED, lineText.getTextColor());
    }

    @Test
    public void testSetTextColor() throws Exception {
        LineText lineText = new TestLineText();

        lineText.setTextColor(Color.BLUE);
        assertEquals(Color.BLUE, lineText.getTextColor());
    }

    @Test
    public void testSetPadding() throws Exception {
        LineText lineText = new TestLineText();
        lineText.setPadding(5);
        lineText.setText("test");

        assertEquals(0, lineText.getBounds().getX(), 0.01);
        assertEquals(0, lineText.getBounds().getY(), 0.01);
        assertEquals(22+2*5, lineText.getBounds().getWidth(), 0.01);
        assertEquals(16+2*5, lineText.getBounds().getHeight(), 0.01);
    }

    @Test
    public void testSetPadding1() throws Exception {
        LineText lineText = new TestLineText();
        lineText.setPadding(10,5);
        lineText.setText("test");

        assertEquals(0, lineText.getBounds().getX(), 0.01);
        assertEquals(0, lineText.getBounds().getY(), 0.01);
        assertEquals(22+2*5, lineText.getBounds().getWidth(), 0.01);
        assertEquals(16+2*10, lineText.getBounds().getHeight(), 0.01);
    }

    @Test
    public void testSetPadding2() throws Exception {
        LineText lineText = new TestLineText();
        lineText.setPadding(5,10, 6,11);
        lineText.setText("test");

        assertEquals(0, lineText.getBounds().getX(), 0.01);
        assertEquals(0, lineText.getBounds().getY(), 0.01);
        assertEquals(22+11+10, lineText.getBounds().getWidth(), 0.01);
        assertEquals(16+6+5, lineText.getBounds().getHeight(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAlignmentIllegal() throws Exception {
        LineText lineText = new TestLineText();
        lineText.setAlignment(100);
    }

    @Test
    public void testGetAlignmentCenter() throws Exception {
        LineText lineText = new TestLineText();
        lineText.setAlignment(LineText.CENTER);

        assertEquals(LineText.CENTER, lineText.getAlignment());
    }

    @Test
    public void testGetAlignmentLeft() throws Exception {
        LineText lineText = new TestLineText();
        lineText.setAlignment(LineText.LEFT);

        assertEquals(LineText.LEFT, lineText.getAlignment());
    }

    @Test
    public void testGetAlignmentRight() throws Exception {
        LineText lineText = new TestLineText();
        lineText.setAlignment(LineText.RIGHT);

        assertEquals(LineText.RIGHT, lineText.getAlignment());
    }

    @Test
    public void testAddChangeListener() throws Exception {
        TestChangeListener testChangeListener = new TestChangeListener();

        LineText lineText = new TestLineText();
        lineText.addChangeListener(testChangeListener);

        assertEquals(0, testChangeListener.count);

        lineText.setText("test");
        assertEquals(1, testChangeListener.count);
    }
}