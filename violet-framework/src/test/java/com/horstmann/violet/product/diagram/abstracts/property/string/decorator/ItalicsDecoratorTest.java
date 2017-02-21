package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

import com.horstmann.violet.product.diagram.property.text.decorator.ItalicsDecorator;
import com.horstmann.violet.product.diagram.property.text.decorator.OneLineText;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 12.01.2016
 */
public class ItalicsDecoratorTest
{
    @Test
    public void testToDisplay() throws Exception {
        ItalicsDecorator italicsDecorator = new ItalicsDecorator(new OneLineText("test"));
        assertEquals("<i>test</i>", italicsDecorator.toDisplay());
    }
}