package com.horstmann.violet.product.diagram.classes.node;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.AbstractNode;
import com.horstmann.violet.product.diagram.common.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.classes.ClassDiagramConstant;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.MultiLineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;

/**
 * A package node in a class diagram.
 */
public class PackageNode extends ColorableNode
{
    public PackageNode()
    {
        name = new SingleLineText();
        name.setAlignment(LineText.CENTER);
        context = new MultiLineText();
        createContentStructure();
    }

    protected PackageNode(PackageNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        context = node.context.clone();
        createContentStructure();
    }

    @Override
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();
        if(null == name)
        {
            name = new SingleLineText();
        }
        if(null == context)
        {
            context = new MultiLineText();
        }
        name.reconstruction();
        context.reconstruction();
    }

    @Override
    protected void afterReconstruction()
    {
        for(INode child : getChildren())
        {
            if (isSupportedNode(child))
            {
                nodesGroup.add(((ColorableNode) child).getContent(), getChildRelativeLocation(child));
            }
        }

        super.afterReconstruction();
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new PackageNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        nameContent = new TextContent(name);
        nameContent.setMinHeight(DEFAULT_TOP_HEIGHT);
        nameContent.setMinWidth(DEFAULT_TOP_WIDTH);

        TextContent textContent = new TextContent(context);

        nodesGroup = new RelativeLayout();
        nodesGroup.setMinHeight(DEFAULT_HEIGHT);
        nodesGroup.setMinWidth(DEFAULT_WIDTH);

        RelativeLayout relativeGroupContent = new RelativeLayout();

        EmptyContent emptyContent = new EmptyContent();
        emptyContent.setMinWidth(NAME_GAP);

        HorizontalLayout horizontalName = new HorizontalLayout();
        horizontalName.add(nameContent);
        horizontalName.add(emptyContent);

        relativeGroupContent.add(
                new PaddingContent(nodesGroup, CHILD_GAP), new Point2D.Double(0, DEFAULT_TOP_HEIGHT)
        );
        relativeGroupContent.add(textContent, new Point2D.Double(0, DEFAULT_TOP_HEIGHT));
        relativeGroupContent.add(horizontalName);

        ContentInsideShape contentInsideShape = new ContentInsideCustomShape(
                relativeGroupContent, new PackageShape(nameContent)
        );

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());
    }

    @Override
    public void setTextColor(Color textColor)
    {
        name.setTextColor(textColor);
        context.setTextColor(textColor);
        super.setTextColor(textColor);
    }

    @Override
    public String getToolTip()
    {
        return ClassDiagramConstant.CLASS_DIAGRAM_RESOURCE.getString("tooltip.package_node");
    }

    @Override
    public Point2D getConnectionPoint(IEdge edge)
    {
        Point2D connectionPoint = super.getConnectionPoint(edge);
        Direction direction = edge.getDirection(this).getNearestCardinalDirection();

        if (Direction.SOUTH.equals(direction))
        {
            if(connectionPoint.getX() >= getLocation().getX() + nameContent.getWidth())
            {
                return new Point2D.Double(connectionPoint.getX(), connectionPoint.getY() + nameContent.getHeight());
            }
        }
        else if(Direction.WEST.equals(direction))
        {
            if(connectionPoint.getY() < getLocation().getY() + nameContent.getHeight())
            {
                return new Point2D.Double(connectionPoint.getX(), connectionPoint.getY() + nameContent.getHeight());
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

        return new Point2D.Double(childLocation.getX()-CHILD_GAP, childLocation.getY()-DEFAULT_TOP_HEIGHT-CHILD_GAP);
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
    public void setContext(MultiLineText newValue)
    {
        context = newValue;
    }

    /**
     * Gets the contents property value.
     * 
     * @return the contents of this class
     */
    public MultiLineText getContext()
    {
        return context;
    }

    private SingleLineText name;
    private MultiLineText context;

    private transient TextContent nameContent;
    private transient RelativeLayout nodesGroup;

    private static int DEFAULT_TOP_WIDTH = 60;
    private static int DEFAULT_TOP_HEIGHT = 20;
    private static int DEFAULT_WIDTH = 100;
    private static int DEFAULT_HEIGHT = 60;
    private static final int NAME_GAP = 30;
    private static final int CHILD_GAP = 20;

    static protected class PackageShape implements ContentInsideCustomShape.ShapeCreator
    {
        PackageShape(TextContent nameContent)
        {
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
}
