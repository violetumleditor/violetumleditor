package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

import com.horstmann.violet.framework.property.text.decorator.OneLineText;
import com.horstmann.violet.framework.property.text.decorator.SmallSizeDecorator;
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
        SmallSizeDecorator smallSizeDecorator = new SmallSizeDecorator(new OneLineText("test"));
        assertEquals("<font size=-1>test</font>", smallSizeDecorator.toDisplay());
    }

    @Test
    public void testToDisplay_should_decrease_font_size_to_specified_value() throws Exception
    {
        SmallSizeDecorator smallSizeDecorator = new SmallSizeDecorator(new OneLineText("test"), 1);
        assertEquals("<font size=-1>test</font>", smallSizeDecorator.toDisplay());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testToDisplay_should_decrease_font_size_be_negative_value() throws Exception
    {
        SmallSizeDecorator smallSizeDecorator = new SmallSizeDecorator(new OneLineText("test"), -3);
        assertEquals("<font size=+3>test</font>", smallSizeDecorator.toDisplay());
    }
}