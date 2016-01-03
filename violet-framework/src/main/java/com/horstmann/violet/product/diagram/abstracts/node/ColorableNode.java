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

package com.horstmann.violet.product.diagram.abstracts.node;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.horstmann.violet.framework.graphics.content.Content;
import com.horstmann.violet.framework.graphics.content.ContentBackground;
import com.horstmann.violet.framework.graphics.content.ContentBorder;
import com.horstmann.violet.framework.graphics.content.EmptyContent;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRoundRectangle;
import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.IColorable;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.workspace.sidebar.colortools.ColorToolsBarPanel;

/**
 * A node_old that has a rectangular shape.
 */
public abstract class ColorableNode extends AbstractNode implements IColorable
{
    public ColorableNode()
    {
        super();
    }

    public ColorableNode(ColorableNode node) throws CloneNotSupportedException
    {
        super(node);
    }

//  protected abstract void createContentStructure();
    protected void createContentStructure()
    {
        setBorder(new ContentBorder(new ContentInsideRoundRectangle(new EmptyContent()), getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());
    }

    @Override
    public void draw(Graphics2D g2)
    {
        getContent().draw(g2, getLocationOnGraph());
    }

    @Override
    public Rectangle2D getBounds()
    {
        Point2D location = getLocation();
        Rectangle2D contentBounds = getContent().getBounds();
        return new Rectangle2D.Double(location.getX(), location.getY(), contentBounds.getWidth(), contentBounds.getHeight());
    }

    public boolean contains(Point2D p)
    {
//        return getContent().contains(p);
        return getBackground().contains(p);
    }
    
    @Override
    public boolean addConnection(IEdge e) {
    	// Self call (loop)
    	INode endingNode = e.getEnd();
    	if (endingNode == null) {
    		e.setEnd(e.getStart());
    		e.setEndlocation(e.getStartLocation());
    	}
    	// Back to default behavior
    	return super.addConnection(e);
    }

    
    /**
     * List edges connected to the same side
     * 
     * @param edge
     * @return ordered list of edges 
     */
    private List<IEdge> getEdgesOnSameSide(IEdge edge) {
        // Step 1 : look for edges
        List<IEdge> result = new ArrayList<IEdge>();
        Direction d = edge.getDirection(this);

        if (d == null) return result;
        Direction cardinalDirectionToSearch = d.getNearestCardinalDirection();
        for (IEdge anEdge : getConnectedEdges()) {
            Direction edgeDirection = anEdge.getDirection(this);
            Direction nearestCardinalDirection = edgeDirection.getNearestCardinalDirection();
            if (cardinalDirectionToSearch.equals(nearestCardinalDirection)) {
                result.add(anEdge);
            }
            if (anEdge.getStart().equals(anEdge.getEnd()) && anEdge.getStart().equals(this)) {
            	// self loop
            	result.add(anEdge);
            }
        }
        // Step 2: sort them
        if (Direction.NORTH.equals(cardinalDirectionToSearch) || Direction.SOUTH.equals(cardinalDirectionToSearch)) {
            Collections.sort(result, new Comparator<IEdge>() {
                @Override
                public int compare(IEdge e1, IEdge e2) {
                    Direction d1 = e1.getDirection(ColorableNode.this);
                    Direction d2 = e2.getDirection(ColorableNode.this);
                    double x1 = d1.getX();
                    double x2 = d2.getX();
                    return Double.compare(x1, x2);
                }
            });
        }
        if (Direction.EAST.equals(cardinalDirectionToSearch) || Direction.WEST.equals(cardinalDirectionToSearch)) {
            Collections.sort(result, new Comparator<IEdge>() {
                @Override
                public int compare(IEdge e1, IEdge e2) {
                    Direction d1 = e1.getDirection(ColorableNode.this);
                    Direction d2 = e2.getDirection(ColorableNode.this);
                    double y1 = d1.getY();
                    double y2 = d2.getY();
                    return Double.compare(y1, y2);
                }
            });
        }
        return result;
    }
    
    
    
    public Point2D getConnectionPoint(IEdge e)
    {
        List<IEdge> edgesOnSameSide = getEdgesOnSameSide(e);
        int position = edgesOnSameSide.indexOf(e);
        int size = edgesOnSameSide.size();
        Rectangle2D b = getBounds();
        
        double x = b.getCenterX();
        double y = b.getCenterY();

        Direction d = e.getDirection(this);
        Direction nearestCardinalDirection = d.getNearestCardinalDirection();
        if (Direction.NORTH.equals(nearestCardinalDirection)) {
            x = b.getMaxX() - (b.getWidth() / (size + 1)) * (position + 1);
            y = b.getMaxY();
        }
        if (Direction.SOUTH.equals(nearestCardinalDirection)) {
            x = b.getMaxX() - (b.getWidth() / (size + 1)) * (position + 1);
            y = b.getMinY();
        }
        if (Direction.EAST.equals(nearestCardinalDirection)) {
            x = b.getMinX();
            y = b.getMaxY() - (b.getHeight() / (size + 1)) * (position + 1);
        }
        if (Direction.WEST.equals(nearestCardinalDirection)) {
            x = b.getMaxX();
            y = b.getMaxY() - (b.getHeight() / (size + 1)) * (position + 1);
        }
        Point2D rawPoint = new Point2D.Double(x, y);
        return rawPoint;
    }

    public Shape getShape()
    {
        return getBounds();
    }


    


    public final Content getContent() {
        return content;
    }

    protected final void setContent(Content content) {
        this.content = content;
    }

    protected final ContentBackground getBackground() {
        return background;
    }

    protected final void setBackground(ContentBackground background) {
        this.background = background;
    }

    protected final ContentBorder getBorder() {
        return border;
    }

    protected final void setBorder(ContentBorder border) {
        this.border = border;
    }

    public final void setBackgroundColor(Color bgColor)
    {
        if(null != background)
        {
            background.setBackgroundColor(bgColor);
        }
    }

    public final Color getBackgroundColor()
    {
        if(null == background)
        {
            return ColorToolsBarPanel.DEFAULT_COLOR.getBackgroundColor();
        }
        return background.getBackgroundColor();
    }

    public final void setBorderColor(Color borderColor)
    {
        if(null != border)
        {
            border.setBorderColor(borderColor);
        }
    }

    public final Color getBorderColor()
    {
        if(null == border)
        {
            return ColorToolsBarPanel.DEFAULT_COLOR.getBorderColor();
        }
        return border.getBorderColor();
    }

    public void setTextColor(Color textColor)
    {}

    public Color getTextColor()
    {
        return ColorToolsBarPanel.DEFAULT_COLOR.getTextColor();
    }


    private Content content = null;
    private ContentBackground background = null;
    private ContentBorder border = null;
}
