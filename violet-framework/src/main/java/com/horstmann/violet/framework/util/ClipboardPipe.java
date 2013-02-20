/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.framework.util;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * This class is used to hold an image or a text while on the clipboard.
 * 
 * @author Alexandre de Pellegrin
 */
public class ClipboardPipe implements Transferable
{
    private Image image;
    private String text;

    public ClipboardPipe(Image image)
    {
        this.image = image;
    }

    public ClipboardPipe(String text)
    {
        this.text = text;
    }

    // Returns supported flavors
    public DataFlavor[] getTransferDataFlavors()
    {
        if (this.image != null)
        {
            return new DataFlavor[]
            {
                DataFlavor.imageFlavor
            };
        }
        if (this.text != null)
        {
            return new DataFlavor[]
            {
                DataFlavor.stringFlavor
            };
        }
        return new DataFlavor[] {

        };
    }

    // Returns true if flavor is supported
    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        if (this.image != null)
        {
            return DataFlavor.imageFlavor.equals(flavor);
        }
        if (this.text != null)
        {
            return DataFlavor.stringFlavor.equals(flavor);
        }
        return false;
    }

    // Returns image or text
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
    {
        if (DataFlavor.imageFlavor.equals(flavor) && this.image != null)
        {
            return this.image;
        }
        else if (DataFlavor.stringFlavor.equals(flavor) && this.text != null)
        {
            return this.text;
        }
        else
        {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}