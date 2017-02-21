package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

import com.horstmann.violet.product.diagram.property.text.decorator.OneLineText;
import com.horstmann.violet.product.diagram.property.text.decorator.UnderlineDecorator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        UnderlineDecorator underlineDecorator = new UnderlineDecorator(new OneLineText("test"));
        assertEquals("<u>test</u>", underlineDecorator.toDisplay());
    }
}