package com.horstmann.violet.product.diagram.sequence;

import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.node.IResizableNode;
import com.horstmann.violet.product.diagram.abstracts.node.RectangularNode;
import com.horstmann.violet.product.diagram.abstracts.property.MultiLineString;
import com.horstmann.violet.product.diagram.abstracts.property.IntegrationFrameType;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * A package node in a UML diagram.
 */
public class IntegrationFrameNode extends RectangularNode implements IResizableNode {

    /**
     * Construct a package node with a default size
     */
    public IntegrationFrameNode() {
        type = IntegrationFrameType.ALT;
        name = new MultiLineString();
        name.setText(type.getName());
        name.setSize(MultiLineString.LARGE);
        content = new MultiLineString();
        content.setJustification(MultiLineString.LEFT);
    }

    @Override
    public Point2D getConnectionPoint(IEdge e) {
        return super.getConnectionPoint(e);
    }

    @Override
    public void setWantedSize(Rectangle2D size) {
        this.wantedSize = size;
    }

    @Override
    public Rectangle2D getResizablePoint() {
        Rectangle2D nodeBounds = getBounds();
        int pointSize = 10;

        double x = nodeBounds.getMaxX() - (pointSize / 2);
        double y = nodeBounds.getMaxY() - (pointSize / 2);

        return new Rectangle2D.Double(x, y, pointSize, pointSize);
    }

    /**
     * @return the shape of type name field
     */
    private Shape getTypeShapePath() {
        Rectangle2D typeBounds = getTypeRectangleBounds();

        double d_p2_p3 = typeBounds.getHeight() / 2;

        Point2D p1 = new Point2D.Double(typeBounds.getMinX(), typeBounds.getMinY());
        Point2D p2 = new Point2D.Double(typeBounds.getMaxX(), typeBounds.getMinY());
        Point2D p3 = new Point2D.Double(typeBounds.getMaxX(), typeBounds.getMinY() + d_p2_p3);
        Point2D p4 = new Point2D.Double(typeBounds.getMaxX() - d_p2_p3, typeBounds.getMaxY());
        Point2D p5 = new Point2D.Double(typeBounds.getMinX(), typeBounds.getMaxY());

        GeneralPath typePath = new GeneralPath();
        typePath.moveTo(p1.getX(), p1.getY());
        typePath.lineTo(p2.getX(), p2.getY());
        typePath.lineTo(p3.getX(), p3.getY());
        typePath.lineTo(p4.getX(), p4.getY());
        typePath.lineTo(p5.getX(), p5.getY());
        typePath.lineTo(p1.getX(), p1.getY());
        typePath.closePath();

        return typePath;
    }


    private Rectangle2D getTypeRectangleBounds() {
        Rectangle2D nameBounds = name.getBounds();
        Rectangle2D bodyBounds = getBodyRectangleBounds();

        double x = bodyBounds.getMinX();
        double y = bodyBounds.getMinY();
        double w = Math.max(DEFAULT_TYPE_WIDTH, nameBounds.getWidth());
        double h = Math.max(DEFAULT_TYPE_HEIGHT, nameBounds.getHeight());

        Rectangle2D typeBounds = new Rectangle2D.Double(x, y, w, h);
        return getGraph().getGridSticker().snap(typeBounds);
    }

    private Rectangle2D getBodyRectangleBounds() {
        Point2D currentLocation = getLocation();

        double x = currentLocation.getX();
        double y = currentLocation.getY();
        double w = this.wantedSize.getWidth();
        double h = this.wantedSize.getHeight();

        Rectangle2D bodyBounds = new Rectangle2D.Double(x, y, w, h);
        return getGraph().getGridSticker().snap(bodyBounds);
    }

    @Override
    public Rectangle2D getBounds() {
        return getGraph().getGridSticker().snap(getBodyRectangleBounds());
    }

    @Override
    public void draw(Graphics2D g2) {
        // Backup current color;
        Color oldColor = g2.getColor();

        // Translate g2 if node has parent
        Point2D nodeLocationOnGraph = getLocationOnGraph();
        Point2D nodeLocation = getLocation();
        Point2D g2Location = new Point2D.Double(nodeLocationOnGraph.getX() - nodeLocation.getX(), nodeLocationOnGraph.getY()
                - nodeLocation.getY());
        g2.translate(g2Location.getX(), g2Location.getY());

        // Perform drawing
        super.draw(g2);
        Rectangle2D bodyBounds = getBodyRectangleBounds();
        Shape typeBounds = getTypeShapePath();

        g2.setColor(getBackgroundColor());
        g2.fill(typeBounds);

        g2.setColor(getBorderColor());
        g2.draw(typeBounds);
        g2.draw(bodyBounds);

        g2.setColor(getTextColor());
        name.setText(type.getName());
        name.draw(g2, getTypeRectangleBounds());
        content.draw(g2, bodyBounds);

        // Restore g2 original location
        g2.translate(-g2Location.getX(), -g2Location.getY());

        // Restore first color
        g2.setColor(oldColor);
    }

    @Override
    public Color getBackgroundColor() {
        return new Color(255, 255, 153, 255);
    }

    @Override
    public Shape getShape() {
        GeneralPath path = new GeneralPath();
        path.append(getTypeRectangleBounds(), false);
        path.append(getBodyRectangleBounds(), false);
        return path;
    }

    @Override
    public boolean addChild(INode n, Point2D p) {
        return false;
    }

    public IntegrationFrameNode clone() {
        IntegrationFrameNode cloned = (IntegrationFrameNode) super.clone();
        cloned.type = type;
        cloned.name = name.clone();
        cloned.content = content.clone();
        return cloned;
    }


    /**
     * Sets the name property value.
     *
     * @param newValue the class name
     */
    public void setName(MultiLineString newValue) {
        name = newValue;
    }

    /**
     * Gets the name property value.
     *
     * @return the class name
     */
    public MultiLineString getName() {
        return name;
    }

    /**
     * Sets the contents property value.
     *
     * @param newValue the contents of this class
     */
    public void setContent(MultiLineString newValue) {
        content = newValue;
    }

    /**
     * Gets the contents property value.
     *
     * @return the contents of this class
     */
    public MultiLineString getContent() {
        return content;
    }

    /**
     * Gets the type property value.
     *
     * @return the type of this frame
     */
    public IntegrationFrameType getType() {
        return type;
    }

    /**
     * Sets the type property value.
     *
     * @param type the type of this frame
     */
    public void setType(IntegrationFrameType type) {
        this.type = type;
    }


    private IntegrationFrameType type;
    private MultiLineString name;
    private MultiLineString content;
    private Rectangle2D wantedSize = new Rectangle2D.Double(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);

    private static int DEFAULT_TYPE_WIDTH = 60;
    private static int DEFAULT_TYPE_HEIGHT = 20;
    private static int DEFAULT_WIDTH = 200;
    private static int DEFAULT_HEIGHT = 150;

}