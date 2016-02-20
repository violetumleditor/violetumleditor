package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

import com.horstmann.violet.framework.property.string.decorator.ItalicsDecorator;
import com.horstmann.violet.framework.property.string.decorator.OneLineString;
import org.junit.Test;

import static org.junit.Assert.*;

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
        ItalicsDecorator italicsDecorator = new ItalicsDecorator(new OneLineString("test"));
        assertEquals("<i>test</i>", italicsDecorator.toDisplay());
    }
}