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

package com.horstmann.violet.product.diagram.common;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.framework.theme.ThemeManager;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.framework.property.text.MultiLineText;
import com.horstmann.violet.workspace.sidebar.colortools.ColorToolsBarPanel;

/**
 * A note node_old in a UML diagram.
 * 
 * FIXME : manage Z order
 * 
 * for (IEdge e : getGraph().getEdges()) { if (e.getStartNode() == this) { INode end = e.getEndNode(); Point2D endPoint = end.getLocation();
 * INode n = getGraph().findNode(endPoint); if (n != end) end.setZ(n.getZ() + 1); } }
 * 
 */
public class NoteNode extends ColorableNode
{
    /**
     * Construct a note node_old with a default size and color
     */
    public NoteNode()
    {
        super();
        text = new MultiLineText();
        createContentStructure();
    }

    public NoteNode(NoteNode node) throws CloneNotSupportedException
    {
        super(node);
        text = node.text.clone();
        createContentStructure();
    }

    @Override
    public void deserializeSupport()
    {
        super.deserializeSupport();
        text.deserializeSupport();
    }

    @Override
    protected INode copy() throws CloneNotSupportedException {
        return new NoteNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        TextContent textContent = new TextContent(text);
        textContent.setMinHeight(DEFAULT_HEIGHT);
        textContent.setMinWidth(DEFAULT_WIDTH);

        ContentInsideShape contentInsideShape = new ContentInsideCustomShape(textContent, new ContentInsideCustomShape.ShapeCreator()
        {
            @Override
            public Shape createShape(double contentWidth, double contentHeight) {
                GeneralPath path = new GeneralPath();
                path.moveTo(0, 0);
                path.lineTo(contentWidth - FOLD_X, 0);
                path.lineTo(contentWidth, FOLD_Y);
                path.lineTo(contentWidth, contentHeight);
                path.lineTo(0, contentHeight);
                path.closePath();
                return path;
            }
        });

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());

        setBackgroundColor(ColorToolsBarPanel.PASTEL_YELLOW_ORANCE.getBackgroundColor());
        setBorderColor(ColorToolsBarPanel.PASTEL_YELLOW_ORANCE.getBorderColor());
        setTextColor(ColorToolsBarPanel.PASTEL_YELLOW_ORANCE.getTextColor());
    }

    @Override
    public void draw(Graphics2D g2)
    {
        super.draw(g2);

        Color oldColor = g2.getColor();
        GeneralPath fold = new GeneralPath();
        Rectangle2D bounds = getBounds();
        fold.moveTo((float) (bounds.getMaxX() - FOLD_X), (float) bounds.getY());
        fold.lineTo((float) bounds.getMaxX() - FOLD_X, (float) bounds.getY() + FOLD_X);
        fold.lineTo((float) bounds.getMaxX(), (float) (bounds.getY() + FOLD_Y));
        fold.closePath();
        g2.setColor(ThemeManager.getInstance().getTheme().getWhiteColor());
        g2.fill(fold);
        g2.setColor(getBorderColor());
        g2.draw(fold);
        g2.setColor(oldColor);
    }

    @Override
    public void setTextColor(Color textColor)
    {
        text.setTextColor(textColor);
        super.setTextColor(textColor);
    }









    @Override
    public int getZ()
    {
        // Ensures that this kind of nodes is always on top
        return INFINITE_Z_LEVEL;
    }

    @Override
    public boolean addConnection(IEdge e)
    {
        if (e.getStartNode() == e.getEndNode())
        {
            return false;
        }
        return super.addConnection(e);
    }

    /**
     * Gets the value of the text property.
     * 
     * @return the text inside the note
     */
    public MultiLineText getText()
    {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param newValue the text inside the note
     */
    public void setText(MultiLineText newValue)
    {
        text = newValue;
    }

    /**
     * Kept for compatibility
     */
    public void setColor(Color newValue)
    {
        // Nothing to do
    }

    private MultiLineText text;

    private static int DEFAULT_WIDTH = 60;
    private static int DEFAULT_HEIGHT = 40;
    private static int FOLD_X = 8;
    private static int FOLD_Y = 8;
    private static int INFINITE_Z_LEVEL = 10000;
}
