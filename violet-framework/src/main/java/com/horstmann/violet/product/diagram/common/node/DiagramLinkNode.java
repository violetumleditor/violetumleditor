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

package com.horstmann.violet.product.diagram.common.node;

import com.horstmann.violet.framework.injection.resources.ResourceBundleConstant;
import com.horstmann.violet.product.diagram.common.DiagramLink;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.MultiLineText;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * An link node_old in a diagram.
 */
public class DiagramLinkNode extends ColorableNode
{

    /**
     * Construct a link ode with a default size
     */
    public DiagramLinkNode()
    {
        this.label = new MultiLineText();
    }

    @Override
    public String getToolTip()
    {
//        return ResourceBundleConstant.NODE_AND_EDGE_RESOURCE.getString("note_node.tooltip");
        return "DiagramLinkNode";
    }

    @Override
    public LineText getName() {
        return label;
    }

    @Override
    public LineText getAttributes() {
        return label;
    }

    @Override
    public LineText getMethods() {
        return label;
    }

    @Override
    public Rectangle2D getBounds()
    {
        Rectangle2D top = new Rectangle2D.Double(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        Rectangle2D bot = getLabel().getBounds();
        Point2D currentLocation = getLocation();
        double x = currentLocation.getX();
        double y = currentLocation.getY();
        double w = Math.max(top.getWidth(), bot.getWidth());
        double h = top.getHeight() + bot.getHeight();
        Rectangle2D currentBounds = new Rectangle2D.Double(x, y, w, h);
        Rectangle2D snapperBounds = getGraph().getGridSticker().snap(currentBounds);
        return snapperBounds;
    }


    public void draw(Graphics2D graphics)
    {
        Rectangle2D bounds = getBounds();
        GeneralPath path1 = new GeneralPath();
        float x1 = (float) (bounds.getCenterX() - DEFAULT_SIZE / 2 + DEFAULT_SIZE / 8);
        float y1 = (float) (bounds.getCenterY() - DEFAULT_SIZE / 2);
        float x2 = x1 + DEFAULT_SIZE / 4;
        float y2 = y1;
        float x3 = x2 + DEFAULT_SIZE / 4;
        float y3 = y2 + DEFAULT_SIZE / 4;
        float x4 = x3;
        float y4 = y3 + DEFAULT_SIZE / 4;
        float x5 = x2;
        float y5 = y4 + DEFAULT_SIZE / 4;
        float x6 = x1;
        float y6 = y5;
        path1.moveTo(x1, y1);
        path1.lineTo(x2, y2);
        path1.lineTo(x3, y3);
        path1.lineTo(x4, y4);
        path1.lineTo(x5, y5);
        path1.lineTo(x6, y6);
        Rectangle2D rec1 = new Rectangle2D.Float();
        rec1.setRect(x1 - DEFAULT_SIZE / 2, y1 - DEFAULT_SIZE / 12, DEFAULT_SIZE / 2, y6 - y1 + 2 * DEFAULT_SIZE / 12);
        x1 = (float) (2 * bounds.getCenterX() - x1);
        x2 = (float) (2 * bounds.getCenterX() - x2);
        x3 = (float) (2 * bounds.getCenterX() - x3);
        x4 = (float) (2 * bounds.getCenterX() - x4);
        x5 = (float) (2 * bounds.getCenterX() - x5);
        x6 = (float) (2 * bounds.getCenterX() - x6);
        GeneralPath path2 = new GeneralPath();
        path2.moveTo(x1, y1);
        path2.lineTo(x2, y2);
        path2.lineTo(x3, y3);
        path2.lineTo(x4, y4);
        path2.lineTo(x5, y5);
        path2.lineTo(x6, y6);
        Rectangle2D rec2 = new Rectangle2D.Float();
        rec2.setRect(x1, y1 - DEFAULT_SIZE / 12, DEFAULT_SIZE / 2, y6 - y1 + 2 * DEFAULT_SIZE / 12);

        Color backupcolor = graphics.getColor();
        graphics.setColor(Color.WHITE);
        graphics.fill(rec1);
        graphics.fill(rec2);
        graphics.setColor(backupcolor);
        graphics.draw(path1);
        graphics.draw(path2);
        graphics.draw(rec1);
        graphics.draw(rec2);

        // Draw name
        Rectangle2D bot = getLabel().getBounds();

        Rectangle2D namebox = new Rectangle2D.Double(bounds.getX() + +(bounds.getWidth() - bot.getWidth()) / 2, bounds.getY()
                + bounds.getHeight() - bot.getHeight(), bot.getWidth(), bot.getHeight());
        getLabel().draw(graphics, namebox);
    }

    public DiagramLink getDiagramLink()
    {
        return diagramLink;
    }

    public void setDiagramLink(DiagramLink fLink)
    {
        this.diagramLink = fLink;
    }

    private MultiLineText getLabel()
    {
        if (this.label == null)
        {
            this.label = new MultiLineText();
        }
        DiagramLink dl = this.getDiagramLink();
        if (dl != null && dl.getFile() != null)
        {
            StringBuffer linktext = new StringBuffer()
                    .append(
                            ResourceBundle.getBundle(ResourceBundleConstant.OTHER_STRINGS, Locale.getDefault()).getString(
                                    "file.link.text")).append(" ").append(dl.getFile().getFilename());
            this.label.setText( linktext.toString() );
        }
        return this.label;
    }

    /** Label */
    private MultiLineText label;

    /** Linked diagram */
    private DiagramLink diagramLink;

    /** Default bounding rectangle width */
    private static int DEFAULT_WIDTH = 48;

    /** Default bounding rectangle height */
    private static int DEFAULT_HEIGHT = 32;

    /** Default tool icon size */
    private static int DEFAULT_SIZE = 32;

}
