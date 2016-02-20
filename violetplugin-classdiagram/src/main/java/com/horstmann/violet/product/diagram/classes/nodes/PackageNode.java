package com.horstmann.violet.product.diagram.classes.nodes;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.framework.property.string.LineText;
import com.horstmann.violet.framework.property.string.MultiLineText;
import com.horstmann.violet.framework.property.string.SingleLineText;

/**
 * A package nodes in a class diagram.
 */
public class PackageNode extends ColorableNode
{
    static protected class PackageShape implements ContentInsideCustomShape.ShapeCreator
    {
        PackageShape(TextContent nameContent) {
            this.nameContent = nameContent;
        }

        @Override
        public Shape createShape(double contentWidth, double contentHeight)
        {
            GeneralPath path = new GeneralPath();
            path.moveTo(nameContent.getWidth(), nameContent.getHeight());
            path.lineTo(nameContent.getWidth(), 0);
            path.lineTo(0, 0);
            path.lineTo(0, contentHeight);
            path.lineTo(contentWidth, contentHeight);
            path.lineTo(contentWidth, nameContent.getHeight());
            path.lineTo(0, nameContent.getHeight());
            path.closePath();
            return path;
        }

        private TextContent nameContent;
    }

    public PackageNode()
    {
        name = new SingleLineText();
        name.setAlignment(LineText.CENTER);
        text = new MultiLineText();
        createContentStructure();
    }

    protected PackageNode(PackageNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        text = node.text.clone();
        createContentStructure();
    }

    @Override
    public void deserializeSupport()
    {
        name.deserializeSupport();
        text.deserializeSupport();

        super.deserializeSupport();

        for(INode child : getChildren())
        {
            if (isSupportedNode(child))
            {
                nodesGroup.add(((ColorableNode) child).getContent(), getChildRelativeLocation(child));
            }
        }
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new PackageNode(this);
    }

    @Override
    protected void createContentStructure() {
        RelativeLayout relativeGroupContent = new RelativeLayout();

        nodesGroup = new RelativeLayout();
        nodesGroup.setMinHeight(DEFAULT_HEIGHT);
        nodesGroup.setMinWidth(DEFAULT_WIDTH);

        TextContent nameContent = new TextContent(name);
        nameContent.setMinHeight(DEFAULT_TOP_HEIGHT);
        nameContent.setMinWidth(DEFAULT_TOP_WIDTH);
        TextContent textContent = new TextContent(text);
        EmptyContent emptyContent = new EmptyContent();
        emptyContent.setMinWidth(CHILD_GAP);
        EmptyContent emptyWidthContent = new EmptyContent();
        emptyWidthContent.setMinWidth(CHILD_GAP);
        EmptyContent emptyHeightContent = new EmptyContent();
        emptyHeightContent.setMinHeight(CHILD_GAP);

        HorizontalLayout horizontal = new HorizontalLayout();
        horizontal.add(nodesGroup);
        horizontal.add(emptyWidthContent);
        VerticalLayout vertical = new VerticalLayout();
        vertical.add(horizontal);
        vertical.add(emptyHeightContent);

        HorizontalLayout horizontalGroupContent = new HorizontalLayout();
        horizontalGroupContent.add(nameContent);
        horizontalGroupContent.add(emptyContent);

        relativeGroupContent.add(horizontalGroupContent);
        relativeGroupContent.add(vertical, new Point2D.Double(0, DEFAULT_TOP_HEIGHT));
        relativeGroupContent.add(textContent, new Point2D.Double(0, DEFAULT_TOP_HEIGHT));

        ContentInsideShape contentInsideShape = new ContentInsideCustomShape(relativeGroupContent, new PackageShape(nameContent));

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());
    }

    @Override
    public void setTextColor(Color textColor) {
        name.setTextColor(textColor);
        text.setTextColor(textColor);
        super.setTextColor(textColor);
    }

    @Override
    public Point2D getConnectionPoint(IEdge e) {
        Point2D connectionPoint = super.getConnectionPoint(e);

        // Fix location to stick to shape (because of the top rectangle)
        Direction d = e.getDirection(this);
        Direction nearestCardinalDirection = d.getNearestCardinalDirection();
        if (Direction.SOUTH.equals(nearestCardinalDirection)) {
            Rectangle2D topRectangleBounds = name.getBounds();
            if (!topRectangleBounds.contains(connectionPoint)) {
                double x = connectionPoint.getX();
                double y = connectionPoint.getY();
                double h = topRectangleBounds.getHeight();
                connectionPoint = new Point2D.Double(x, y + h);
            }
        }
        return connectionPoint;
    }

    @Override
    public void removeChild(INode node)
    {
        nodesGroup.remove(((AbstractNode) node).getContent());
        super.removeChild(node);
    }

    @Override
    public boolean addChild(INode node, Point2D p)
    {
        if(DEFAULT_TOP_HEIGHT > p.getY())
        {
            return false;
        }
        if (isSupportedNode(node))
        {
            node.setParent(this);
            node.setGraph(this.getGraph());
            node.setLocation(p);
            addChild(node, getChildren().size());

            ColorableNode colorableNode = (ColorableNode) node;
            colorableNode.setTextColor(getTextColor());
            colorableNode.setBackgroundColor(getBackgroundColor());
            colorableNode.setBorderColor(getBorderColor());
            nodesGroup.add(colorableNode.getContent(), getChildRelativeLocation(node));

            return true;
        }
        return false;
    }

    protected Point2D getChildRelativeLocation(INode child)
    {
        Point2D childLocation = child.getLocation();
        if(DEFAULT_TOP_HEIGHT+CHILD_GAP > childLocation.getY() || CHILD_GAP > childLocation.getX())
        {
            childLocation.setLocation(Math.max(childLocation.getX(), CHILD_GAP), Math.max(childLocation.getY(), DEFAULT_TOP_HEIGHT+CHILD_GAP));
            child.setLocation(childLocation);
        }

        return new Point2D.Double(childLocation.getX(), childLocation.getY()-DEFAULT_TOP_HEIGHT);
    }

    /**
     * Ensure that child node_old respects the minimum gap with package borders
     *
     * @param child
     */
    protected void onChildChangeLocation(INode child)
    {
        nodesGroup.setPosition(((AbstractNode) child).getContent(), getChildRelativeLocation(child));
    }

    private boolean isSupportedNode(INode node)
    {
        return (node instanceof ClassNode || node instanceof InterfaceNode || node instanceof PackageNode || node instanceof EnumNode ||  node instanceof BallAndSocketNode);
    }

    /**
     * Sets the name property value.
     * 
     * @param newValue the class name
     */
    public void setName(SingleLineText newValue)
    {
        name.setText(newValue.toEdit());
    }

    /**
     * Gets the name property value.
     * 
     * @return the class name
     */
    public SingleLineText getName()
    {
        return name;
    }

    /**
     * Sets the contents property value.
     * 
     * @param newValue the contents of this class
     */
    public void setText(MultiLineText newValue)
    {
        text = newValue;
    }

    /**
     * Gets the contents property value.
     * 
     * @return the contents of this class
     */
    public MultiLineText getText()
    {
        return text;
    }

    private SingleLineText name;
    private MultiLineText text;

    private transient RelativeLayout nodesGroup = null;

    private static int DEFAULT_TOP_WIDTH = 60;
    private static int DEFAULT_TOP_HEIGHT = 20;
    private static int DEFAULT_WIDTH = 100;
    private static int DEFAULT_HEIGHT = 60;
    private static final int NAME_GAP = 3;
    private static final int CHILD_GAP = 20;

}
