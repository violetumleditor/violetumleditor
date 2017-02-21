package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

import com.horstmann.violet.product.diagram.property.text.decorator.OneLineText;
import com.horstmann.violet.product.diagram.property.text.decorator.PrefixDecorator;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 12.01.2016
 */
public class PrefixDecoratorTest
{
    @Test
    public void testSetPrefix() throws Exception
    {
        PrefixDecorator prefixDecorator = new PrefixDecorator(new OneLineText("test"),"XYZ");
        assertEquals("XYZ test", prefixDecorator.toDisplay());
        assertEquals("test", prefixDecorator.toEdit());
        assertEquals("test", prefixDecorator.toString());
    }

    @Test
    public void testToDisplay() throws Exception
    {
        PrefixDecorator prefixDecorator = new PrefixDecorator(new OneLineText("test"),"<prefix>");
        assertEquals("<prefix> test", prefixDecorator.toDisplay());
    }
}