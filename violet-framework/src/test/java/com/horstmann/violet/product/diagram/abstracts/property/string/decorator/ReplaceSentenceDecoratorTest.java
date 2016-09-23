package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

import com.horstmann.violet.product.diagram.property.text.decorator.OneLineText;
import com.horstmann.violet.product.diagram.property.text.decorator.ReplaceSentenceDecorator;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 12.01.2016
 */
public class ReplaceSentenceDecoratorTest
{
    @Test
    public void testToDisplay() throws Exception
    {
        ReplaceSentenceDecorator replaceSentenceDecorator = new ReplaceSentenceDecorator(new OneLineText("testing"),"ing", "xyz");
        assertEquals("testxyz", replaceSentenceDecorator.toDisplay());
    }

    @Test
    public void testToDisplay2() throws Exception
    {
        ReplaceSentenceDecorator replaceSentenceDecorator = new ReplaceSentenceDecorator(new OneLineText("testing"),"test", "xyz");
        assertEquals("xyzing", replaceSentenceDecorator.toDisplay());
    }

    @Test
    public void testToDisplay3() throws Exception
    {
        ReplaceSentenceDecorator replaceSentenceDecorator = new ReplaceSentenceDecorator(new OneLineText("testing"),"t", "xyz");
        assertEquals("xyzesting", replaceSentenceDecorator.toDisplay());
    }

    @Test
    public void testToDisplay4() throws Exception
    {
        ReplaceSentenceDecorator replaceSentenceDecorator = new ReplaceSentenceDecorator(new OneLineText("testing"),"s", "xyz");
        assertEquals("texyzting", replaceSentenceDecorator.toDisplay());
    }

    @Test
    public void testToDisplay5() throws Exception
    {
        ReplaceSentenceDecorator replaceSentenceDecorator = new ReplaceSentenceDecorator(new OneLineText("testing"), "@", "xyz");
        assertEquals("testing", replaceSentenceDecorator.toDisplay());
    }
}