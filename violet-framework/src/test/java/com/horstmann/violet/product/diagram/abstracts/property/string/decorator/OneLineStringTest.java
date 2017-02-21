package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

import com.horstmann.violet.product.diagram.property.text.decorator.OneLineText;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 12.01.2016
 */
public class OneLineStringTest {

    @Test
    public void testClone() throws Exception {
        final OneLineText oneLineString = new OneLineText("test");

        final OneLineText cloned = oneLineString.clone();
        assertEquals(oneLineString.toDisplay(), cloned.toDisplay());
        assertEquals(oneLineString.toEdit(), cloned.toEdit());
        assertEquals(oneLineString.toString(), cloned.toString());
    }

    @Test
    public void testSetText_should_change_the_text_to_plain_text() throws Exception {
        final OneLineText oneLineString = new OneLineText();
        oneLineString.setText("test");

        assertEquals("test", oneLineString.toDisplay());
        assertEquals("test", oneLineString.toEdit());
        assertEquals("test", oneLineString.toString());
    }

    @Test
    public void testSetText_should_change_the_text_on_the_text_containing_2gt_or_2lt()
            throws Exception {
        final OneLineText oneLineString = new OneLineText();
        oneLineString.setText("<<test>>");

        assertEquals("«test»", oneLineString.toDisplay());
        assertEquals("<<test>>", oneLineString.toEdit());
        assertEquals("<<test>>", oneLineString.toString());
    }

    @Test
    @Ignore
    public void testSetText_should_change_the_text_on_the_text_containing_HTML_code()
            throws Exception {
        final OneLineText oneLineString = new OneLineText();
        oneLineString.setText("<b><<test>></b>");

        assertEquals("&lt;b&gt;«test»&lt;&#x2F;b&gt;", oneLineString.toDisplay());
        assertEquals("<b><<test>></b>", oneLineString.toEdit());
        assertEquals("<b><<test>></b>", oneLineString.toString());
    }

    @Test
    @Ignore
    public void testToDisplay() throws Exception {
        final OneLineText oneLineString = new OneLineText("<b><<test>></b>");
        assertEquals("&lt;b&gt;«test»&lt;&#x2F;b&gt;", oneLineString.toDisplay());
    }

    @Test
    public void testToEdit() throws Exception {
        final OneLineText oneLineString = new OneLineText("<b><<test>></b>");
        assertEquals("<b><<test>></b>", oneLineString.toEdit());
    }

    @Test
    public void testToString() throws Exception {
        final OneLineText oneLineString = new OneLineText("<b><<test>></b>");
        assertEquals("<b><<test>></b>", oneLineString.toString());
    }

    @Test
    public void testContains_should_search_sign_that_contains_the_text_in() throws Exception {
        final OneLineText oneLineString = new OneLineText("<b><<test>></b>");
        assertTrue(oneLineString.contains("t"));
    }

    @Test
    public void testContains_should_search_sign_which_does_not_contains_the_text_in()
            throws Exception {
        final OneLineText oneLineString = new OneLineText("<b><<test>></b>");
        assertFalse(oneLineString.contains("x"));
    }

    @Test
    public void testContains_should_search_text_that_contains_the_text_in() throws Exception {
        final OneLineText oneLineString = new OneLineText("<b><<test>></b>");
        assertTrue(oneLineString.contains(">><"));
    }

    @Test
    public void testContains_should_search_text_which_does_not_contains_the_text_in()
            throws Exception {
        final OneLineText oneLineString = new OneLineText("<b><<test>></b>");
        assertFalse(oneLineString.contains("XYZ"));
    }

    @Test
    public void testFind_should_find_the_entry_on_which_the_character_in_the_text()
            throws Exception {
        final OneLineText oneLineString = new OneLineText("<b><test>></b>");
        assertEquals(4, oneLineString.find("t"));
    }

    @Test
    public void testFind_should_find_the_entry_on_which_the_text_in_the_text() throws Exception {
        final OneLineText oneLineString = new OneLineText("Lorem ipsum dolor");
        assertEquals(6, oneLineString.find("ipsum"));
    }

    @Test
    public void testFind_should_not_find_the_entry_on_which_the_character_in_the_text()
            throws Exception {
        final OneLineText oneLineString = new OneLineText("<b><test>></b>");
        assertEquals(-1, oneLineString.find("x"));
    }

    @Test
    public void testFind_should_not_find_the_entry_on_which_the_text_in_the_text()
            throws Exception {
        final OneLineText oneLineString = new OneLineText("Lorem ipsum dolor");
        assertEquals(-1, oneLineString.find("xyz"));
    }
}