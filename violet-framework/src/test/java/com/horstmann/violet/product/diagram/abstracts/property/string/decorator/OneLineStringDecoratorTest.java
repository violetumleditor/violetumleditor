package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

import com.horstmann.violet.framework.property.text.decorator.OneLineText;
import com.horstmann.violet.framework.property.text.decorator.OneLineTextDecorator;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 12.01.2016
 */
public class OneLineStringDecoratorTest {

    @Test
    public void testClone() throws Exception
    {
        OneLineTextDecorator oneLineStringDecorator = new OneLineTextDecorator(new OneLineText("test"));

        OneLineTextDecorator cloned = oneLineStringDecorator.clone();
        assertEquals(oneLineStringDecorator.toDisplay(), cloned.toDisplay());
        assertEquals(oneLineStringDecorator.toEdit(), cloned.toEdit());
        assertEquals(oneLineStringDecorator.toString(), cloned.toString());
    }

    @Test
    public void testToDisplay() throws Exception
    {
        OneLineTextDecorator oneLineStringDecorator = new OneLineTextDecorator(new OneLineText("test"));
        assertEquals("test", oneLineStringDecorator.toEdit());

    }

    @Test
    public void testToEdit() throws Exception
    {
        OneLineTextDecorator oneLineStringDecorator = new OneLineTextDecorator(new OneLineText("test"));
        assertEquals("test", oneLineStringDecorator.toEdit());
    }
}