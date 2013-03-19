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

package com.horstmann.violet.eclipseplugin.tools;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.horstmann.violet.framework.file.IGraphFile;
import com.horstmann.violet.framework.file.export.FileExportService;
import com.horstmann.violet.product.diagram.abstracts.IGraph;
import com.horstmann.violet.workspace.IWorkspace;

/**
 * Allows to print a diagram through Eclipse
 * 
 * @author Alexandre de Pellegrin
 */
public class DiagramPrinter
{

    /**
     * Default constructor
     * 
     * @param newDiagramPanel
     * @param newShell
     */
    public DiagramPrinter(IWorkspace workspace, Shell newShell)
    {
        this.diagramPanel = workspace;
        this.shell = newShell;
    }

    /**
     * Starts printing
     */
    public void print()
    {
        Display display = this.shell.getDisplay();

        try
        {
            IGraphFile graphFile = this.diagramPanel.getGraphFile();
            IGraph graph = graphFile.getGraph();
            FileExportService.getImage(graph);
            BufferedImage bufferedImage = FileExportService.getImage(graph);
            ImageData imageData = this.convertToSWT(bufferedImage);

            if (imageData == null)
            {
                throw new Exception("Error while converting AWT BufferedImage to SWT ImageData");
            }

            // Show the Choose Printer dialog
            PrintDialog dialog = new PrintDialog(shell, SWT.NULL);
            PrinterData printerData = dialog.open();

            if (printerData != null)
            {
                // Create the printer object
                Printer printer = new Printer(printerData);
                // Calculate the scale factor between the screen resolution and
                // printer
                // resolution in order to correctly size the image for the
                // printer
                Point screenDPI = display.getDPI();
                Point printerDPI = printer.getDPI();
                int scaleFactor = printerDPI.x / screenDPI.x;
                // Determine the bounds of the entire area of the printer
                Rectangle trim = printer.computeTrim(0, 0, 0, 0);

                // Start the print job
                if (printer.startJob(this.diagramPanel.getFilePath()))
                {
                    if (printer.startPage())
                    {
                        GC gc = new GC(printer);
                        Image printerImage = new Image(printer, imageData);
                        // Draw the image
                        gc.drawImage(printerImage, 0, 0, imageData.width, imageData.height, -trim.x, -trim.y, scaleFactor * imageData.width, scaleFactor * imageData.height);
                        // Clean up
                        printerImage.dispose();
                        gc.dispose();
                        printer.endPage();
                    }
                }
                // End the job and dispose the printerIDiagramPanel
                printer.endJob();
                printer.dispose();
            }
        }
        catch (Exception e)
        {
            MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
            messageBox.setMessage("Error printing UML Diagram : " + e.getMessage());
            messageBox.open();
        }
    }

    public BufferedImage convertToAWT(ImageData data)
    {
        ColorModel colorModel = null;
        PaletteData palette = data.palette;
        if (palette.isDirect)
        {
            colorModel = new DirectColorModel(data.depth, palette.redMask, palette.greenMask, palette.blueMask);
            BufferedImage bufferedImage = new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(data.width, data.height), false, null);
            WritableRaster raster = bufferedImage.getRaster();
            int[] pixelArray = new int[3];
            for (int y = 0; y < data.height; y++)
            {
                for (int x = 0; x < data.width; x++)
                {
                    int pixel = data.getPixel(x, y);
                    RGB rgb = palette.getRGB(pixel);
                    pixelArray[0] = rgb.red;
                    pixelArray[1] = rgb.green;
                    pixelArray[2] = rgb.blue;
                    raster.setPixels(x, y, 1, 1, pixelArray);
                }
            }
            return bufferedImage;
        }
        else
        {
            RGB[] rgbs = palette.getRGBs();
            byte[] red = new byte[rgbs.length];
            byte[] green = new byte[rgbs.length];
            byte[] blue = new byte[rgbs.length];
            for (int i = 0; i < rgbs.length; i++)
            {
                RGB rgb = rgbs[i];
                red[i] = (byte) rgb.red;
                green[i] = (byte) rgb.green;
                blue[i] = (byte) rgb.blue;
            }
            if (data.transparentPixel != -1)
            {
                colorModel = new IndexColorModel(data.depth, rgbs.length, red, green, blue, data.transparentPixel);
            }
            else
            {
                colorModel = new IndexColorModel(data.depth, rgbs.length, red, green, blue);
            }
            BufferedImage bufferedImage = new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(data.width, data.height), false, null);
            WritableRaster raster = bufferedImage.getRaster();
            int[] pixelArray = new int[1];
            for (int y = 0; y < data.height; y++)
            {
                for (int x = 0; x < data.width; x++)
                {
                    int pixel = data.getPixel(x, y);
                    pixelArray[0] = pixel;
                    raster.setPixel(x, y, pixelArray);
                }
            }
            return bufferedImage;
        }
    }

    private ImageData convertToSWT(BufferedImage bufferedImage)
    {
        if (bufferedImage.getColorModel() instanceof DirectColorModel)
        {
            DirectColorModel colorModel = (DirectColorModel) bufferedImage.getColorModel();
            PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(), colorModel.getBlueMask());
            ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
            WritableRaster raster = bufferedImage.getRaster();
            int[] pixelArray = new int[3];
            for (int y = 0; y < data.height; y++)
            {
                for (int x = 0; x < data.width; x++)
                {
                    raster.getPixel(x, y, pixelArray);
                    int pixel = palette.getPixel(new RGB(pixelArray[0], pixelArray[1], pixelArray[2]));
                    data.setPixel(x, y, pixel);
                }
            }
            return data;
        }
        else if (bufferedImage.getColorModel() instanceof IndexColorModel)
        {
            IndexColorModel colorModel = (IndexColorModel) bufferedImage.getColorModel();
            int size = colorModel.getMapSize();
            byte[] reds = new byte[size];
            byte[] greens = new byte[size];
            byte[] blues = new byte[size];
            colorModel.getReds(reds);
            colorModel.getGreens(greens);
            colorModel.getBlues(blues);
            RGB[] rgbs = new RGB[size];
            for (int i = 0; i < rgbs.length; i++)
            {
                rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
            }
            PaletteData palette = new PaletteData(rgbs);
            ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
            data.transparentPixel = colorModel.getTransparentPixel();
            WritableRaster raster = bufferedImage.getRaster();
            int[] pixelArray = new int[1];
            for (int y = 0; y < data.height; y++)
            {
                for (int x = 0; x < data.width; x++)
                {
                    raster.getPixel(x, y, pixelArray);
                    data.setPixel(x, y, pixelArray[0]);
                }
            }
            return data;
        }
        return null;
    }

    IWorkspace diagramPanel;
    Shell shell;

}
