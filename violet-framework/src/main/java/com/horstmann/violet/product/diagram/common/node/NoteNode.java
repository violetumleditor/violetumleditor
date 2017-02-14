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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;

import com.horstmann.violet.framework.graphics.content.ContentBackground;
import com.horstmann.violet.framework.graphics.content.ContentBorder;
import com.horstmann.violet.framework.graphics.content.ContentInsideShape;
import com.horstmann.violet.framework.graphics.content.TextContent;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.framework.injection.resources.ResourceBundleConstant;
import com.horstmann.violet.framework.theme.ThemeManager;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.MultiLineText;
import com.horstmann.violet.workspace.sidebar.colortools.ColorToolsBarPanel;

public class NoteNode extends ColorableNode
{
    /**
     * Construct a note node with default content structure.
     */
    public NoteNode()
    {
        text = new MultiLineText();
        createContentStructure();
    }

    /**
     * Construct a note node from another note node.
     *
     * @param node a node to copy from.
     * @throws CloneNotSupportedException when node cannot be cloned.
     */
    private NoteNode(NoteNode node) throws CloneNotSupportedException
    {
        super(node);
        text = node.text.clone();
        createContentStructure();
    }

    @Override
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();
        text.reconstruction();
    }

    /**
     * Creates and returns a copy of NoteNode instance.
     *
     * @return a cloned instance.
     * @throws CloneNotSupportedException when instance cannot be cloned.
     */
    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new NoteNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        TextContent textContent = new TextContent(text);
        textContent.setMinHeight(DEFAULT_HEIGHT);
        textContent.setMinWidth(DEFAULT_WIDTH);

        ContentInsideShape contentInsideShape = initializeContentInsideShape(textContent);
        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());
        setDefaultNoteColors();
    }

    /**
     * Draws a NoteNode.
     *
     * @param graphics the graphics context
     */
    @Override
    public void draw(Graphics2D graphics)
    {
        super.draw(graphics);

        Color oldColor = graphics.getColor();
        GeneralPath fold = new GeneralPath();
        Rectangle2D bounds = getBounds();
        fold.moveTo((float) (bounds.getMaxX() - FOLD_X), (float) bounds.getY());
        fold.lineTo((float) bounds.getMaxX() - FOLD_X, (float) bounds.getY() + FOLD_X);
        fold.lineTo((float) bounds.getMaxX(), (float) (bounds.getY() + FOLD_Y));
        fold.closePath();
        graphics.setColor(ThemeManager.getInstance().getTheme().getWhiteColor());
        graphics.fill(fold);
        graphics.setColor(getBorderColor());
        graphics.draw(fold);
        graphics.setColor(oldColor);
    }

    @Override
    public void setTextColor(Color textColor)
    {
        text.setTextColor(textColor);
        super.setTextColor(textColor);
    }

    @Override
    public String getToolTip()
    {
        return ResourceBundleConstant.NODE_AND_EDGE_RESOURCE.getString("note_node.tooltip");
    }

    @Override
    public LineText getName() {
        return text;
    }

    @Override
    public LineText getAttributes() {
        return null;
    }

    @Override
    public LineText getMethods() {
        return null;
    }

    @Override
    public int getZ()
    {
        for (IEdge e : getGraph().getAllEdges())
        {
            if (e.getStartNode() == this)
            {
                INode end = e.getEndNode();
                Point2D endPoint = end.getLocation();
                INode n = getGraph().findNode(endPoint);
                if (n != end)
                    return end.getZ() + 1;
            }
        }
        return 1;
    }

    @Override
    public boolean addConnection(IEdge edge)
    {
        return edge.getStartNode() != edge.getEndNode() && super.addConnection(edge);
    }

    public MultiLineText getText()
    {
        return text;
    }

    public void setText(MultiLineText newValue)
    {
        text = newValue;
    }

    /**
     * Overrides default node colors
     */
    private void setDefaultNoteColors()
    {
        boolean isDefaultBackgroundColor = getBackgroundColor() == ColorToolsBarPanel.DEFAULT_COLOR.getBackgroundColor();
        if (isDefaultBackgroundColor)
        {
            setBackgroundColor(ColorToolsBarPanel.PASTEL_YELLOW_ORANCE.getBackgroundColor());
            setBorderColor(ColorToolsBarPanel.PASTEL_YELLOW_ORANCE.getBorderColor());
            setTextColor(ColorToolsBarPanel.PASTEL_YELLOW_ORANCE.getTextColor());
        }
    }

    private ContentInsideShape initializeContentInsideShape(TextContent textContent)
    {
        return new ContentInsideCustomShape(textContent, new ContentInsideCustomShape.ShapeCreator()
        {
            @Override
            public Shape createShape(double contentWidth, double contentHeight)
            {
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
    }

    private MultiLineText text;

    private static final int DEFAULT_WIDTH = 60;
    private static final int DEFAULT_HEIGHT = 40;
    private static final int FOLD_X = 8;
    private static final int FOLD_Y = 8;
}