package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 12.01.2016
 */
public class OneLineStringTest {

    @Test
    public void testClone() throws Exception {
        OneLineString oneLineString = new OneLineString("test");

        OneLineString cloned = oneLineString.clone();
        assertEquals(oneLineString.toDisplay(), cloned.toDisplay());
        assertEquals(oneLineString.toEdit(), cloned.toEdit());
        assertEquals(oneLineString.toString(), cloned.toString());
    }

    @Test
    public void testSetText() throws Exception {
        OneLineString oneLineString = new OneLineString();
        oneLineString.setText("<b><<test>></b>");

        assertEquals("&lt;b&gt;«test»&lt;&#x2F;b&gt;", oneLineString.toDisplay());
        assertEquals("<b><<test>></b>", oneLineString.toEdit());
        assertEquals("<b><<test>></b>", oneLineString.toString());
    }

    @Test
    public void testToDisplay() throws Exception {
        OneLineString oneLineString = new OneLineString("<b><<test>></b>");
        assertEquals("&lt;b&gt;«test»&lt;&#x2F;b&gt;", oneLineString.toDisplay());
    }

    @Test
    public void testToEdit() throws Exception {
        OneLineString oneLineString = new OneLineString("<b><<test>></b>");
        assertEquals("<b><<test>></b>", oneLineString.toEdit());
    }

    @Test
    public void testToString() throws Exception {
        OneLineString oneLineString = new OneLineString("<b><<test>></b>");
        assertEquals("<b><<test>></b>", oneLineString.toString());
    }

    @Test
    public void testContains() throws Exception {
        OneLineString oneLineString = new OneLineString("<b><<test>></b>");
        assertTrue(oneLineString.contains("t"));
    }
    @Test
    public void testContains2() throws Exception {
        OneLineString oneLineString = new OneLineString("<b><<test>></b>");
        assertTrue(oneLineString.contains(">><"));
    }
    @Test
    public void testContains3() throws Exception {
        OneLineString oneLineString = new OneLineString("<b><<test>></b>");
        assertFalse(oneLineString.contains("XYZ"));
    }

    @Test
    public void testFind() throws Exception {
        OneLineString oneLineString = new OneLineString("<b><<test>></b>");
        assertEquals(4,oneLineString.find("t"));
    }
    @Test
    public void testFind2() throws Exception {
        OneLineString oneLineString = new OneLineString("<b><test>></b>");
        assertEquals(4,oneLineString.find("t"));
    }
    @Test
    public void testFind3() throws Exception {
        OneLineString oneLineString = new OneLineString("<b><<test>></b>");
        assertEquals(8, oneLineString.find(">><"));
    }
    @Test
    public void testFind4() throws Exception {
        OneLineString oneLineString = new OneLineString("<b><test>></b>");
        assertEquals(8, oneLineString.find(">><"));
    }
    @Test
    public void testFind5() throws Exception {
        OneLineString oneLineString = new OneLineString("<b><<test>></b>");
        assertEquals(-1, oneLineString.find("XYZ"));
    }
}