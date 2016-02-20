package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

import com.horstmann.violet.framework.property.string.decorator.OneLineString;
import com.horstmann.violet.framework.property.string.decorator.SmallSizeDecorator;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 12.01.2016
 */
public class SmallSizeDecoratorTest
{
    @Test
    public void testToDisplay() throws Exception
    {
        SmallSizeDecorator smallSizeDecorator = new SmallSizeDecorator(new OneLineString("test"));
        assertEquals("<font size=-1>test</font>", smallSizeDecorator.toDisplay());
    }

    @Test
    public void testToDisplay_should_decrease_font_size_to_specified_value() throws Exception
    {
        SmallSizeDecorator smallSizeDecorator = new SmallSizeDecorator(new OneLineString("test"), 1);
        assertEquals("<font size=-1>test</font>", smallSizeDecorator.toDisplay());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testToDisplay_should_decrease_font_size_be_negative_value() throws Exception
    {
        SmallSizeDecorator smallSizeDecorator = new SmallSizeDecorator(new OneLineString("test"), -3);
        assertEquals("<font size=+3>test</font>", smallSizeDecorator.toDisplay());
    }
}