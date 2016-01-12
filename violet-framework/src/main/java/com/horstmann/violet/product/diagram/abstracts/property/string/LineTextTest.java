package com.horstmann.violet.product.diagram.abstracts.property.string;

import org.junit.Test;
import org.junit.runners.JUnit4;

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
        public TestLineText()
        {
            super();
        }
        public TestLineText(Converter converter)
        {
            super(converter);
        }
        @Override
        public String toDisplay() {return null;}
        @Override
        public String toEdit() {return null;}
        @Override
        public void setText(String text) {}
    }

    @Test
    public void testClone() throws Exception {

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

    }

    @Test
    public void testSetPadding1() throws Exception {
        LineText lineText = new TestLineText();

    }

    @Test
    public void testSetPadding2() throws Exception {
        LineText lineText = new TestLineText();

    }

    @Test(expected=IllegalArgumentException.class)
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

    }
}