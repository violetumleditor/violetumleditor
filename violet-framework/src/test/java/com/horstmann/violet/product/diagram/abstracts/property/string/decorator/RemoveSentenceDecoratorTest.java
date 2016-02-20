package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

import com.horstmann.violet.framework.property.string.decorator.OneLineString;
import com.horstmann.violet.framework.property.string.decorator.RemoveSentenceDecorator;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 12.01.2016
 */
public class RemoveSentenceDecoratorTest
{
    @Test
    public void testToDisplay() throws Exception
    {
        RemoveSentenceDecorator removeSentenceDecorator = new RemoveSentenceDecorator(new OneLineString("testing"),"ing");
        assertEquals("test", removeSentenceDecorator.toDisplay());
    }

    @Test
    public void testToDisplay2() throws Exception
    {
        RemoveSentenceDecorator removeSentenceDecorator = new RemoveSentenceDecorator(new OneLineString("testing"),"test");
        assertEquals("ing", removeSentenceDecorator.toDisplay());
    }

    @Test
    public void testToDisplay3() throws Exception
    {
        RemoveSentenceDecorator removeSentenceDecorator = new RemoveSentenceDecorator(new OneLineString("testing"),"t");
        assertEquals("esting", removeSentenceDecorator.toDisplay());
    }

    @Test
    public void testToDisplay4() throws Exception
    {
        RemoveSentenceDecorator removeSentenceDecorator = new RemoveSentenceDecorator(new OneLineString("testing"),"s");
        assertEquals("teting", removeSentenceDecorator.toDisplay());
    }

    @Test
    public void testToDisplay5() throws Exception
    {
        RemoveSentenceDecorator removeSentenceDecorator = new RemoveSentenceDecorator(new OneLineString("testing"),"xyz");
        assertEquals("testing", removeSentenceDecorator.toDisplay());
    }
}