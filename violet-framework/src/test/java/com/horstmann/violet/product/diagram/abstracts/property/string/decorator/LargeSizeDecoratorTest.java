package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

import com.horstmann.violet.framework.property.text.decorator.LargeSizeDecorator;
import com.horstmann.violet.framework.property.text.decorator.OneLineText;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 12.01.2016
 */
public class LargeSizeDecoratorTest
{
    @Test
    public void testToDisplay() throws Exception
    {
        LargeSizeDecorator largeSizeDecorator = new LargeSizeDecorator(new OneLineText("test"));
        assertEquals("<font size=+1>test</font>", largeSizeDecorator.toDisplay());
    }

    @Test
    public void testToDisplay_should_increase_font_size_to_specified_value() throws Exception
    {
        LargeSizeDecorator largeSizeDecorator = new LargeSizeDecorator(new OneLineText("test"), 1);
        assertEquals("<font size=+1>test</font>", largeSizeDecorator.toDisplay());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testToDisplay_should_increase_font_size_be_negative_value() throws Exception
    {
        LargeSizeDecorator largeSizeDecorator = new LargeSizeDecorator(new OneLineText("test"), -3);
        assertEquals("<font size=+3>test</font>", largeSizeDecorator.toDisplay());
    }
}