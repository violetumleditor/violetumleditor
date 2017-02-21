package com.horstmann.violet.product.diagram.property.text.decorator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OneLineTextTest {

    @Test
    public void cloneTest() {
        oneLineText = new OneLineText("test");
        OneLineText clone = oneLineText.clone();
        assertEquals(oneLineText.toString(), clone.toString());
    }

    @Test
    public void setText_ShouldRemoveMultipleWhitespaces() {
        oneLineText = new OneLineText("Sample     text   with     whitespaces");
        assertEquals("Sample text with whitespaces", oneLineText.toString());
    }

    @Test
    public void toDisplay_ShouldReturnUnificationText() {
        oneLineText = new OneLineText("<<abstract>>");
        assertEquals("«abstract»", oneLineText.toDisplay());
    }

    @Test
    public void toDisplay_ShouldReturnTextWithHTMLTags() {
        oneLineText = new OneLineText("<b>Text</b>");
        assertEquals("<b>Text</b>", oneLineText.toDisplay());
    }

    @Test
    public void contains_ShouldReturnWhetherTextContainsSentenceIgnoringCase() {
        oneLineText = new OneLineText("This is sample text");
        assertTrue(oneLineText.contains("SAMPLE"));
        assertFalse(oneLineText.contains("test"));
    }

    @Test
    public void find_ShouldReturnIndexOfFirstTextOccurrenceIgnoringCase() {
        oneLineText = new OneLineText("Sample text");
        assertEquals(7, oneLineText.find("TEXT"));
        assertEquals(0, oneLineText.find("sample"));
        assertEquals(-1, oneLineText.find("test"));
    }

    private OneLineText oneLineText;
}