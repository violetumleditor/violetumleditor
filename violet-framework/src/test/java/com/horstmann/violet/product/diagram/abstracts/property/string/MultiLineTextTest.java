package com.horstmann.violet.product.diagram.abstracts.property.string;

import com.horstmann.violet.product.diagram.property.text.MultiLineText;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 12.01.2016
 */
public class MultiLineTextTest {

    @Test
    public void testClone() throws Exception {
        final MultiLineText multiLineText = new MultiLineText();
        multiLineText.setText("test");

        final MultiLineText cloned = multiLineText.clone();
        assertEquals(multiLineText.toDisplay(), cloned.toDisplay());
        assertEquals(multiLineText.toEdit(), cloned.toEdit());
        assertEquals(multiLineText.toString(), cloned.toString());
    }

    @Test
    public void testSetText() throws Exception {

    }

    @Test
    @Ignore
    public void testToDisplayOneLine() throws Exception {
        final MultiLineText multiLineText = new MultiLineText();
        multiLineText.setText("<b><<test>></b>");

        assertEquals("&lt;b&gt;«test»&lt;&#x2F;b&gt;", multiLineText.toDisplay());
    }

    @Test
    @Ignore
    public void testToDisplayMoreLines() throws Exception {
        final MultiLineText multiLineText = new MultiLineText();
        multiLineText.setText("<b\n><<\ntest>><\n/b>");

        assertEquals("&lt;b<br>&gt;«<br>test»&lt;<br>&#x2F;b&gt;", multiLineText.toDisplay());
    }

    @Test
    public void testToEditOneLine() throws Exception {
        final MultiLineText multiLineText = new MultiLineText();
        multiLineText.setText("<b><<test>></b>");

        assertEquals("<b><<test>></b>", multiLineText.toEdit());
    }

    @Test
    public void testToEditMoreLines() throws Exception {
        final MultiLineText multiLineText = new MultiLineText();
        multiLineText.setText("<b\n><<te\nst>></b>");

        assertEquals("<b\n><<te\nst>></b>", multiLineText.toEdit());
    }

    @Test
    public void testToStringOneLine() throws Exception {
        final MultiLineText multiLineText = new MultiLineText();
        multiLineText.setText("<b><<test>></b>");

        assertEquals("<b><<test>></b>", multiLineText.toString());
    }

    @Test
    public void testToStringMoreLines() throws Exception {
        final MultiLineText multiLineText = new MultiLineText();
        multiLineText.setText("\n<b><<test>></b>\n");

        assertEquals("|<b><<test>></b>|", multiLineText.toString());
    }
}