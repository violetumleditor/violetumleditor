package com.horstmann.violet.framework.util;

import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import org.freehep.graphicsio.pdf.PDFGraphics2D;

/**
 * Created by Michał Leśniak on 28.01.16.
 */
public class PDFGraphics2DStringWriter extends PDFGraphics2D
{

    public PDFGraphics2DStringWriter(OutputStream outputStream, Dimension size)
    {
        super(outputStream, size);
    }

    @Override
    public void drawString(String s, float x, float y)
    {
        writeString(s, x, y);
    }

    @Override
    public void drawString(String str, int x, int y)
    {
        writeString(str, x, y);
    }

    @Override
    public void drawString(String string, double x, double y)
    {
        writeString(string, x, y);
    }

    @Override
    protected void writeString(String str, double x, double y)
    {
        Font oldFont = getFont();
        try
        {
            // workaround for Bug in PDFFontTable class (wrong order of Font constructor parameters)
            if (oldFont.isBold() && "SansSerif".equals(oldFont.getName()))
            {
                Font newFont = new Font("Helvetica", Font.BOLD, oldFont.getSize());
                setFont(newFont);
                x = x + 8; // workaround for different between fonts width
            }

            super.writeString(str, x, y);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            setFont(oldFont);
        }
    }
}
