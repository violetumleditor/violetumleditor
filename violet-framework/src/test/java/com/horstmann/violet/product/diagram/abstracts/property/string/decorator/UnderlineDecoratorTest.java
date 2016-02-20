package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

import com.horstmann.violet.framework.property.string.decorator.OneLineString;
import com.horstmann.violet.framework.property.string.decorator.UnderlineDecorator;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 12.01.2016
 */
public class UnderlineDecoratorTest
{
    @Test
    public void testToDisplay() throws Exception
    {
        UnderlineDecorator underlineDecorator = new UnderlineDecorator(new OneLineString("test"));
        assertEquals("<u>test</u>", underlineDecorator.toDisplay());
    }
}