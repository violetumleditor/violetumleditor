package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 12.01.2016
 */
public class LargeSizeDecoratorTest {

    @Test
    public void testToDisplay() throws Exception {
        LargeSizeDecorator largeSizeDecorator = new LargeSizeDecorator(new OneLineString("test"));
        assertEquals("<font size=+1>test</font>", largeSizeDecorator.toDisplay());
    }

    @Test
    public void testToDisplay1() throws Exception {
        LargeSizeDecorator largeSizeDecorator = new LargeSizeDecorator(new OneLineString("test"), 1);
        assertEquals("<font size=+1>test</font>", largeSizeDecorator.toDisplay());
    }

    @Test
    public void testToDisplay3() throws Exception {
        LargeSizeDecorator largeSizeDecorator = new LargeSizeDecorator(new OneLineString("test"), 3);
        assertEquals("<font size=+3>test</font>", largeSizeDecorator.toDisplay());
    }
}