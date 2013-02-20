package com.horstmann.violet.product.diagram.classes.nodes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.RectangularNode;
import com.horstmann.violet.product.diagram.abstracts.property.MultiLineString;
import com.horstmann.violet.product.diagram.common.PointNode;

/**
 * A class node in a class diagram.
 */
public class ClassNode extends RectangularNode
{
    /**
     * Construct a class node with a default size
     */
    public ClassNode()
    {
        name = new MultiLineString();
        name.setSize(MultiLineString.LARGE);
        attributes = new MultiLineString();
        attributes.setJustification(MultiLineString.LEFT);
        methods = new MultiLineString();
        methods.setJustification(MultiLineString.LEFT);
    }
    
    private Rectangle2D getTopRectangleBounds() {
        Rectangle2D globalBounds = new Rectangle2D.Double(0, 0, 0, 0);
        Rectangle2D nameBounds = name.getBounds();
        globalBounds.add(nameBounds);
        boolean isMethodsEmpty = (methods.getText().length() == 0);
        boolean isAttributesEmpty = (attributes.getText().length() == 0);
        double defaultHeight = DEFAULT_HEIGHT;
        if (!isMethodsEmpty || !isAttributesEmpty) {
            defaultHeight = DEFAULT_COMPARTMENT_HEIGHT;
        }
        globalBounds.add(new Rectangle2D.Double(0, 0, DEFAULT_WIDTH, defaultHeight));
        Point2D currentLocation = getLocation();
        double x = currentLocation.getX();
        double y = currentLocation.getY();
        double w = globalBounds.getWidth();
        double h = globalBounds.getHeight();
        globalBounds.setFrame(x, y, w, h);
        Rectangle2D snappedBounds = getGraph().getGrid().snap(globalBounds);
        return snappedBounds;
    }
    
    private Rectangle2D getMiddleRectangleBounds() {
        Rectangle2D globalBounds = new Rectangle2D.Double(0, 0, 0, 0);
        Rectangle2D attributesBounds = attributes.getBounds();
        globalBounds.add(attributesBounds);
        boolean isMethodsEmpty = (methods.getText().length() == 0);
        boolean isAttributesEmpty = (attributes.getText().length() == 0);
        if (!isMethodsEmpty || !isAttributesEmpty) {
            globalBounds.add(new Rectangle2D.Double(0, 0, DEFAULT_WIDTH, DEFAULT_COMPARTMENT_HEIGHT));
        }
        Rectangle2D topBounds = getTopRectangleBounds();
        double x = topBounds.getX();
        double y = topBounds.getMaxY();
        double w = globalBounds.getWidth();
        double h = globalBounds.getHeight();
        globalBounds.setFrame(x, y, w, h);
        Rectangle2D snappedBounds = getGraph().getGrid().snap(globalBounds);
        return snappedBounds;
    }
    
    private Rectangle2D getBottomRectangleBounds() {
        Rectangle2D globalBounds = new Rectangle2D.Double(0, 0, 0, 0);
        Rectangle2D methodsBounds = methods.getBounds();
        globalBounds.add(methodsBounds);
        boolean isMethodsEmpty = (methods.getText().length() == 0);
        boolean isAttributesEmpty = (attributes.getText().length() == 0);
        if (!isMethodsEmpty || !isAttributesEmpty) {
            globalBounds.add(new Rectangle2D.Double(0, 0, DEFAULT_WIDTH, DEFAULT_COMPARTMENT_HEIGHT));
        }
        Rectangle2D middleBounds = getMiddleRectangleBounds();
        double x = middleBounds.getX();
        double y = middleBounds.getMaxY();
        double w = globalBounds.getWidth();
        double h = globalBounds.getHeight();
        globalBounds.setFrame(x, y, w, h);
        Rectangle2D snappedBounds = getGraph().getGrid().snap(globalBounds);
        return snappedBounds;
    }

    @Override
    public Rectangle2D getBounds()
    {
        Rectangle2D top = getTopRectangleBounds();
        Rectangle2D mid = getMiddleRectangleBounds();
        Rectangle2D bot = getBottomRectangleBounds();
        top.add(mid);
        top.add(bot);
        Rectangle2D snappedBounds = getGraph().getGrid().snap(top);
        return snappedBounds;
    }

    @Override
    public void draw(Graphics2D g2)
    {
        // Backup current color;
        Color oldColor = g2.getColor();
        // Translate g2 if node has parent
        Point2D nodeLocationOnGraph = getLocationOnGraph();
        Point2D nodeLocation = getLocation();
        Point2D g2Location = new Point2D.Double(nodeLocationOnGraph.getX() - nodeLocation.getX(), nodeLocationOnGraph.getY() - nodeLocation.getY());
        g2.translate(g2Location.getX(), g2Location.getY());
        // Perform drawing
        super.draw(g2);
        Rectangle2D currentBounds = getBounds();
        Rectangle2D topBounds = getTopRectangleBounds();
        Rectangle2D midBounds = getMiddleRectangleBounds(); 
        Rectangle2D bottomBounds = getBottomRectangleBounds();
        g2.setColor(getBackgroundColor());
        g2.fill(currentBounds);
        g2.setColor(getBorderColor());
        g2.draw(currentBounds);
        g2.drawLine((int) topBounds.getX(),(int) topBounds.getMaxY(),(int) currentBounds.getMaxX(),(int) topBounds.getMaxY());
        g2.drawLine((int) bottomBounds.getX(),(int) bottomBounds.getY(),(int) currentBounds.getMaxX(),(int) bottomBounds.getY());
        g2.setColor(getTextColor());
        name.draw(g2, topBounds);
        attributes.draw(g2, midBounds);
        methods.draw(g2, bottomBounds);
        // Restore g2 original location
        g2.translate(-g2Location.getX(), -g2Location.getY());
        // Restore first color
        g2.setColor(oldColor);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.framework.Node#addNode(com.horstmann.violet.framework.Node, java.awt.geom.Point2D)
     */
    public boolean addChild(INode n, Point2D p)
    {
        // TODO : where is it added?
        if (n instanceof PointNode)
        {
            return true;
        }
        return false;
    }

    /**
     * Sets the name property value.
     * 
     * @param newValue the class name
     */
    public void setName(MultiLineString newValue)
    {
        name = newValue;
    }

    /**
     * Gets the name property value.
     * 
     * @return the class name
     */
    public MultiLineString getName()
    {
        return name;
    }

    /**
     * Sets the attributes property value.
     * 
     * @param newValue the attributes of this class
     */
    public void setAttributes(MultiLineString newValue)
    {
        attributes = newValue;
    }

    /**
     * Gets the attributes property value.
     * 
     * @return the attributes of this class
     */
    public MultiLineString getAttributes()
    {
        return attributes;
    }

    /**
     * Sets the methods property value.
     * 
     * @param newValue the methods of this class
     */
    public void setMethods(MultiLineString newValue)
    {
        methods = newValue;
    }

    /**
     * Gets the methods property value.
     * 
     * @return the methods of this class
     */
    public MultiLineString getMethods()
    {
        return methods;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.horstmann.violet.product.diagram.abstracts.RectangularNode#clone()
     */
    public ClassNode clone()
    {
        ClassNode cloned = (ClassNode) super.clone();
        cloned.name = (MultiLineString) name.clone();
        cloned.methods = (MultiLineString) methods.clone();
        cloned.attributes = (MultiLineString) attributes.clone();
        return cloned;
    }


    private MultiLineString name;
    private MultiLineString attributes;
    private MultiLineString methods;

    private static int DEFAULT_COMPARTMENT_HEIGHT = 20;
    private static int DEFAULT_WIDTH = 100;
    private static int DEFAULT_HEIGHT = 60;

}
