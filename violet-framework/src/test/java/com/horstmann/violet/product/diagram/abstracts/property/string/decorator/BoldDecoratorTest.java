package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

import com.horstmann.violet.framework.property.text.decorator.BoldDecorator;
import com.horstmann.violet.framework.property.text.decorator.OneLineText;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 12.01.2016
 */
public class BoldDecoratorTest
{
    @Test
    public void testToDisplay() throws Exception
    {
        BoldDecorator boldDecorator = new BoldDecorator(new OneLineText("test"));
        assertEquals("<b>test</b>", boldDecorator.toDisplay());
    }
}