package com.horstmann.violet.framework.graphics.content;

import com.horstmann.violet.framework.property.text.SingleLineText;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * This ...
 *
 * @author Adrian Bobrowski
 * @date 12.01.2016
 */
public class TextContentTest
{
    @Test
    public void testOnChange() throws Exception
    {
        SingleLineText singleLineText = new SingleLineText();
        TextContent textContent = new TextContent(singleLineText);
        TestingRefreshContent testingRefreshContent = new TestingRefreshContent();
        testingRefreshContent.setAsParent(textContent);

        assertEquals(0, testingRefreshContent.refreshUpCount);
        assertEquals(0, testingRefreshContent.refreshDownCount);

        singleLineText.setText("test");
        assertEquals(1, testingRefreshContent.refreshUpCount);
        assertEquals(0, testingRefreshContent.refreshDownCount);
    }
}