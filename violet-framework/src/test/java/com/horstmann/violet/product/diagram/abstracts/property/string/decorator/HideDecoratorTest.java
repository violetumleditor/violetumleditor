package com.horstmann.violet.product.diagram.abstracts.property.string.decorator;

import com.horstmann.violet.product.diagram.property.text.decorator.HideDecorator;
import com.horstmann.violet.product.diagram.property.text.decorator.OneLineText;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HideDecoratorTest
{
    @Test
    public void testToDisplay() throws Exception {
        HideDecorator hideDecorator = new HideDecorator(new OneLineText("test"));
        assertEquals("...()", hideDecorator.toDisplay());
    }
}