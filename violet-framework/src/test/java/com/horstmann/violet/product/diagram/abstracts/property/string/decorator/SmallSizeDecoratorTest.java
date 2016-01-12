package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 12.01.2016
 */
public class SmallSizeDecoratorTest {

    @Test
    public void testToDisplay() throws Exception {
        SmallSizeDecorator smallSizeDecorator = new SmallSizeDecorator(new OneLineString("test"));
        assertEquals("<font size=-1>test</font>", smallSizeDecorator.toDisplay());
    }

    @Test
    public void testToDisplay1() throws Exception {
        SmallSizeDecorator smallSizeDecorator = new SmallSizeDecorator(new OneLineString("test"), 1);
        assertEquals("<font size=-1>test</font>", smallSizeDecorator.toDisplay());
    }

    @Test
    public void testToDisplay3() throws Exception {
        SmallSizeDecorator smallSizeDecorator = new SmallSizeDecorator(new OneLineString("test"), 3);
        assertEquals("<font size=-3>test</font>", smallSizeDecorator.toDisplay());
    }
}