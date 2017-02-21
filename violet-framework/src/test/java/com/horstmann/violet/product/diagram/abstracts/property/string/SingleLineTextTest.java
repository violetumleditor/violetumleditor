package com.horstmann.violet.product.diagram.abstracts.property.string;

import com.horstmann.violet.product.diagram.property.text.SingleLineText;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 12.01.2016
 */
public class SingleLineTextTest {

    @Test
    public void testClone() throws Exception {
        final SingleLineText singleLineText = new SingleLineText();
        singleLineText.setText("test");

        final SingleLineText cloned = singleLineText.clone();
        assertEquals(singleLineText.toDisplay(), cloned.toDisplay());
        assertEquals(singleLineText.toEdit(), cloned.toEdit());
        assertEquals(singleLineText.toString(), cloned.toString());
    }

    @Test
    public void testSetText() throws Exception {

    }

    @Test
    @Ignore
    public void testToDisplay() throws Exception {
        final SingleLineText singleLineText = new SingleLineText();
        singleLineText.setText("<b><<test>></b>");

        assertEquals("&lt;b&gt;«test»&lt;&#x2F;b&gt;", singleLineText.toDisplay());
    }

    @Test
    public void testToEdit() throws Exception {
        final SingleLineText singleLineText = new SingleLineText();
        singleLineText.setText("<b><<test>></b>");

        assertEquals("<b><<test>></b>", singleLineText.toEdit());
    }

    @Test
    public void testToString() throws Exception {
        final SingleLineText singleLineText = new SingleLineText();
        singleLineText.setText("<b><<test>></b>");

        assertEquals("<b><<test>></b>", singleLineText.toString());
    }
}