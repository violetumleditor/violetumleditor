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

package com.horstmann.violet.product.diagram.state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;
import java.util.List;

import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.RectangularNode;
import com.horstmann.violet.product.diagram.abstracts.property.MultiLineString;
import com.horstmann.violet.product.diagram.common.CompartmentBoundsManager;

/**
 * A node in a state diagram.
 */
public class StateNode extends RectangularNode
{
    /**
     * Construct a state node with a default size
     */
    public StateNode()
    {
        name = new MultiLineString();
        onEntry = new MultiLineString();
        onExit = new MultiLineString();
        manager = new CompartmentBoundsManager(getFields(), DEFAULT_HEIGHT, DEFAULT_COMPARTMENT_HEIGHT, DEFAULT_WIDTH);
    }
    
    @Override
    public boolean addConnection(IEdge e) {
    	if (e.getEnd() == null) {
    		return false;
    	}
    	if (this.equals(e.getEnd())) {
    		return false;
    	}
    	return super.addConnection(e);
    }

    @Override
    public Rectangle2D getBounds()
    {
    	Rectangle2D top = getTopRectangleBounds();
        Rectangle2D mid = getMiddleRectangleBounds();
        Rectangle2D bot = getBottomRectangleBounds();
        top.add(mid);
        top.add(bot);
        Rectangle2D snappedBounds = getGraph().getGridSticker().snap(top);
        return snappedBounds;
    }
    
    private Rectangle2D getTopRectangleBounds() {
    	return manager.getFirstRectangleBounds(getLocation(), name, getGraph());
    }
       
    private Rectangle2D getMiddleRectangleBounds() {
    	return manager.getRectangleBounds(getTopRectangleBounds(), onEntry, getGraph());
    }
    
    private Rectangle2D getBottomRectangleBounds() {
    	return manager.getRectangleBounds(getMiddleRectangleBounds(), onExit, getGraph());
    }

    @Override
    public void draw(Graphics2D g2)
    {
        super.draw(g2);

        // Backup current color;
        Color oldColor = g2.getColor();

        // Perform drawing
        Shape shape = getShape();
        g2.setColor(getBackgroundColor());
        g2.fill(shape);
        g2.setColor(getBorderColor());
        g2.draw(shape);
        g2.setColor(getTextColor());
        name.draw(g2,  getTopRectangleBounds());
        onEntry.draw(g2, getMiddleRectangleBounds());
        onExit.draw(g2, getBottomRectangleBounds());

        // Restore first color
        g2.setColor(oldColor);
    }

    @Override
    public Shape getShape()
    {
        return new RoundRectangle2D.Double(getBounds().getX(), getBounds().getY(), getBounds().getWidth(), getBounds().getHeight(),
                ARC_SIZE, ARC_SIZE);
    }

    /**
     * Sets the name property value.
     * 
     * @param newValue the new state name
     */
    public void setName(MultiLineString newValue)
    {
        name = newValue;
        update();
    }

    /**
     * Gets the name property value.
     * 
     * @param the state name
     */
    public MultiLineString getName()
    {
        return name;
    }
    
    /**
     * Sets the entry property value.
     * 
     * @param newValue the new entry action
     */
    public void setOnEntry(MultiLineString newValue)
    {
    	entryValue = newValue.getText().toString();
        onEntry = new MultiLineString();
        onEntry.setText(entryValue.isEmpty() ? "" : "entry / " + entryValue);
        update();
    }

    /**
     * Gets the entry property value.
     * 
     * @return the entry action
     */
    public MultiLineString getOnEntry()
    {
        MultiLineString result = new MultiLineString();
        result.setText(entryValue == null ? "" : entryValue);
		return result;
    }
    
    /**
     * Sets the exit property value.
     * 
     * @param newValue the new exit action
     */
    public void setOnExit(MultiLineString newValue)
    {
    	exitValue = newValue.getText().toString();
        onExit = new MultiLineString();
        onExit.setText(exitValue.isEmpty() ? "" : "exit / " + exitValue);
        update();
    }

    /**
     * Gets the exit action property value.
     * 
     * @return the exit action name
     */
    public MultiLineString getOnExit()
    {
    	MultiLineString result = new MultiLineString();
        result.setText(exitValue == null ? "" : exitValue);
 		return result;
    }
    
    private void update() {
    	manager.setFields(getFields());
    }
    
    private List<MultiLineString> getFields() {
		return Arrays.asList(onEntry, onExit);
	}

    public StateNode clone()
    {
        StateNode cloned = (StateNode) super.clone();
        cloned.name = name.clone();
        cloned.onEntry = onEntry.clone();
        cloned.onExit = onExit.clone();
        return cloned;
    }

    private MultiLineString name;
    private MultiLineString onEntry;
   	private MultiLineString onExit;
   	private String entryValue;
	private String exitValue;
   	private CompartmentBoundsManager manager;

    private static int ARC_SIZE = 20;
    private static int DEFAULT_WIDTH = 80;
    private static int DEFAULT_HEIGHT = 40;
    private final int DEFAULT_COMPARTMENT_HEIGHT = 5;
}
