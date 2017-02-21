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
import com.horstmann.violet.product.diagram.property.text.PrefixAndSuffixDecorator;
import com.horstmann.violet.product.diagram.property.text.decorator.OneLineText;
import com.horstmann.violet.product.diagram.property.text.decorator.SmallSizeDecorator;
import com.horstmann.violet.workspace.sidebar.colortools.ColorToolsBarPanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

/**
 * A constraint node in a UML diagram.
 */
public class ConstraintNode extends ColorableNode
{
    /**
     * Construct a constraint node with a default size and color
     */
    public ConstraintNode()
    {
        tooltip = ResourceBundleConstant.NODE_AND_EDGE_RESOURCE.getString("constraint_node.tooltip");
        text = new MultiLineText(textConverter);
        createContentStructure();
    }

    /**
     * Construct a constaint node by cloning it.
     * @param node
     * @throws CloneNotSupportedException
     */
    public ConstraintNode(ConstraintNode node) throws CloneNotSupportedException
    {
        super(node);
        tooltip = ResourceBundleConstant.NODE_AND_EDGE_RESOURCE.getString("constraint_node.tooltip");
        text = node.text.clone();
        createContentStructure();
    }

    /**
     * Reconstruction of the text.
     */
    @Override
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();
        text.reconstruction(textConverter);
    }

    /**
     * Copy the node.
     * @return Copy of the node.
     * @throws CloneNotSupportedException
     */
    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new ConstraintNode(this);
    }

    /**
     * Draw node with a cut out corner.
     */
    @Override
    protected void createContentStructure()
    {
        drawNodeWithoutCorner();
    }

    private void drawNodeWithoutCorner()
    {
        TextContent textContent = new TextContent(text);
        textContent.setMinHeight(DEFAULT_HEIGHT);
        textContent.setMinWidth(DEFAULT_WIDTH);

        ContentInsideShape contentInsideShape = new ContentInsideCustomShape(textContent, new ContentInsideCustomShape.ShapeCreator()
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

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());

        setBackgroundColor(ColorToolsBarPanel.DEFAULT_COLOR.getBackgroundColor());
        setBorderColor(ColorToolsBarPanel.DEFAULT_COLOR.getBorderColor());
        setTextColor(ColorToolsBarPanel.DEFAULT_COLOR.getTextColor());
    }

    /**
     * Draw the corner of node.
     * @param graphics
     */
    @Override
    public void draw(Graphics2D graphics)
    {
        super.draw(graphics);
        drawCorner(graphics);
    }

    private void drawCorner(Graphics2D graphics)
    {
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

    /**
     * Sets the node text color.
     * @param textColor
     */
    @Override
    public void setTextColor(Color textColor)
    {
        text.setTextColor(textColor);
        super.setTextColor(textColor);
    }

    /**
     * @return Tooltip string.
     */
    @Override
    public String getToolTip()
    {
        return tooltip;
    }

    @Override
    public LineText getName()
    {
        return text;
    }

    @Override
    public LineText getAttributes()
    {
        return null;
    }

    @Override
    public LineText getMethods()
    {
        return null;
    }

    /**
     * Ensures that this kind of node is always on top
     */
    @Override
    public int getZ() {
        return INFINITE_Z_LEVEL;
    }

    /**
     * Adds connection edge.
     * @param edge
     */
    @Override
    public boolean addConnection(IEdge edge)
    {
        if (edge.getStartNode() == edge.getEndNode())
        {
            return false;
        }
        return super.addConnection(edge);
    }

    /**
     * @return the text inside the note
     */
    public MultiLineText getText()
    {
        return text;
    }

    /**
     * @return the text with prefix and suffix
     */
    private static LineText.Converter textConverter = new LineText.Converter()
    {
        @Override
        public OneLineText toLineString(String text)
        {
            OneLineText decoratedText;

            decoratedText = new OneLineText(text);
            decoratedText = new SmallSizeDecorator(decoratedText);
            decoratedText = new PrefixAndSuffixDecorator(decoratedText, "{", "}");

            return decoratedText;
        }
    };

    /**
     * Sets the value of the text property.
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
    private String tooltip;

    private static int DEFAULT_WIDTH = 60;
    private static int DEFAULT_HEIGHT = 40;
    private static int FOLD_X = 8;
    private static int FOLD_Y = 8;
    private static int INFINITE_Z_LEVEL = 10000;
}
