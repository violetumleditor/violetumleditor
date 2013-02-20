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

package com.horstmann.violet.framework.file.export;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

import com.horstmann.violet.framework.util.ClipboardPipe;
import com.horstmann.violet.product.diagram.abstracts.IGraph;

public class FileExportService
{

    /**
     * Return the image correspondiojng to the graph
     * 
     * @param graph
     * @author Alexandre de Pellegrin
     * @return bufferedImage. To convert it into an image, use the syntax :
     *         Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
     */
    public static BufferedImage getImage(IGraph graph)
    {
        Rectangle2D bounds = graph.getClipBounds();
        BufferedImage image = new BufferedImage((int) bounds.getWidth() + 1, (int) bounds.getHeight() + 1,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.translate(-bounds.getX(), -bounds.getY());
        g2.setColor(Color.WHITE);
        g2.fill(new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth() + 1, bounds.getHeight() + 1));
        g2.setColor(Color.BLACK);
        g2.setBackground(Color.WHITE);
        graph.draw(g2);
        return image;
    }

    /**
     * Export graph to clipboard (Do not merge with exportToClipBoard(). Used in Eclipse plugin)
     * 
     * @author Alexandre de Pellegrin
     * @param graph
     */
    public static void exportToclipBoard(IGraph graph)
    {
        BufferedImage bImage = getImage(graph);
        ClipboardPipe pipe = new ClipboardPipe(bImage);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(pipe, null);
    }

    /**
     * Auteur : a.depellegrin<br>
     * D�finition : Exports class diagram graph to xmi <br>
     * 
     * @param graph to export
     * @param out to write result
     */
    public static void exportToXMI(IGraph graph, OutputStream out)
    {
        // if (!(graph instanceof ClassDiagramGraph))
        // {
        // // Only exports class diagrams
        // return;
        // }
        // try
        // {
        // // Gets xsl files
        // ResourceBundle fileResourceBundle = ResourceBundle.getBundle(ResourceBundleConstant.FILE_STRINGS, Locale.getDefault());
        // URL xslResource1 = FileExportService.class.getResource(fileResourceBundle.getString("files.xmi.step1.xsl"));
        // URL xslResource2 = FileExportService.class.getResource(fileResourceBundle.getString("files.xmi.step2.xsl"));
        // // Converts graph to Violet's XML
        // ByteArrayOutputStream graphOut = new ByteArrayOutputStream();
        // FileExportService.write(graph, graphOut);
        // ByteArrayInputStream graphIn = new ByteArrayInputStream(graphOut.toByteArray());
        // // XSL transform - step 1
        // ByteArrayOutputStream xmiOut = new ByteArrayOutputStream();
        // InputStream xslResource1InputStream = xslResource1.openStream();
        // TransformerFactory factory = TransformerFactory.newInstance();
        // Transformer transformer = factory.newTransformer(new StreamSource(xslResource1InputStream));
        // transformer.transform(new StreamSource(graphIn), new StreamResult(xmiOut));
        // // XSL transform - step 2
        // ByteArrayInputStream xmiIn = new ByteArrayInputStream(xmiOut.toByteArray());
        // InputStream xslResource2InputStream = xslResource2.openStream();
        // transformer = factory.newTransformer(new StreamSource(xslResource2InputStream));
        // transformer.transform(new StreamSource(xmiIn), new StreamResult(out));
        // // Closes unused streams
        // xslResource1InputStream.close();
        // xslResource2InputStream.close();
        // graphOut.close();
        // graphIn.close();
        // xmiOut.close();
        // xmiIn.close();
        // }
        // catch (Exception e)
        // {
        // // Well... we tried!
        // }
    }

}
