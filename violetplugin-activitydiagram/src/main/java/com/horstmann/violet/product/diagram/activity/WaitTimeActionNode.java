package com.horstmann.violet.product.diagram.activity;

import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.RoundRectangularNode;
import com.horstmann.violet.product.diagram.abstracts.property.MultiLineString;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

/**
 * An receive event node in an activity diagram.
 */
public class WaitTimeActionNode extends RoundRectangularNode
{

    private static Double DEFAULT_WIDTH = 40.0;
    private static Double DEFAULT_HEIGHT = 40.0;
    private static int EDGE_WIDTH = 20;

    private MultiLineString waitTimeAction;

    /**
     * Construct an receive event node with a default size
     */
    public WaitTimeActionNode()
    {
        waitTimeAction = new MultiLineString();
    }


    @Override
    public boolean addConnection(IEdge e)
    {
        if (e.getEnd() != null && this != e.getEnd())
        {
            return true;
        }
        return false;
    }

    @Override
    public void selfDraw(Graphics2D g2, Rectangle2D bounds) {
        waitTimeAction.draw(g2, getTextBounds());
    }

    @Override
    public Double getWidth() {
        return DEFAULT_WIDTH;
    }

    @Override
    public Double getHeight() {
        return DEFAULT_HEIGHT;
    }

    @Override
    public Shape getShape()
    {
        Rectangle2D b = getBounds();
        float x1 = (float) b.getX();
        float y1 = (float) b.getY();
        float x2 = x1 + (float) b.getWidth();
        float y2 = y1 + (float) b.getHeight();
        float x3 = x2 - (float) b.getWidth();
        float x4 = x3 + EDGE_WIDTH;
        float y3 = y2 - (float) b.getHeight() / 2;
        GeneralPath path = new GeneralPath();

        path.moveTo(x1, y1);
        path.lineTo(x2, y1);
        path.lineTo(x4, y3);
        path.lineTo(x2, y2);
        path.lineTo(x3, y2);
        path.lineTo(x4, y3);
        path.lineTo(x1, y1);
        return path;
    }

    private Rectangle2D getTextBounds()
    {
        Rectangle2D bounds = getBounds();
        return new Rectangle2D.Double(bounds.getX(), bounds.getY()+bounds.getHeight(), bounds.getWidth(), bounds.getHeight());
    }


    /**
     * Sets the waitTimeAction property value.
     *
     * @param newValue the new waitTimeAction description
     */
    public void setWaitTimeAction(MultiLineString newValue)
    {
        waitTimeAction = newValue;
    }

    /**
     * Gets the waitTimeAction property value.
     *
     * @param the waitTimeAction description
     */
    public MultiLineString getWaitTimeAction()
    {
        return waitTimeAction;
    }

    /**
     * @see Object#clone()
     */
    @Override
    public WaitTimeActionNode clone()
    {
        WaitTimeActionNode cloned = (WaitTimeActionNode) super.clone();
        cloned.waitTimeAction = (MultiLineString) waitTimeAction.clone();
        return cloned;
    }

}
