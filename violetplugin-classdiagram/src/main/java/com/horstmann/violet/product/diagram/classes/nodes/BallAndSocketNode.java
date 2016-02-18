package com.horstmann.violet.product.diagram.classes.nodes;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.property.ChoiceList;
import com.horstmann.violet.product.diagram.abstracts.property.string.SingleLineText;
import com.horstmann.violet.product.diagram.common.PointNode;
import com.horstmann.violet.workspace.sidebar.colortools.ColorToolsBarPanel;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

/**
 * @author Jakub Homlala This class represents ball and socket (Ball and Socket interface notification)
 */
public class BallAndSocketNode extends ColorableNode
{

    public static final String[] ORIENTATION_ARRAY = new String[]{"top", "bottom", "left", "right"};

    protected static class BallAndSocketShape implements ContentInsideCustomShape.ShapeCreator
    {
        public BallAndSocketShape(TextContent nameContent, BallAndSocketNode owner )
        {
            super();

            this.owner = owner;
            this.nameContent = nameContent;
        }

        @Override
        public Shape createShape(double contentWidth, double contentHeight)
        {
            int degrees = this.owner.getDegrees();

            double x = (this.nameContent.getWidth() / 2);

            return new Arc2D.Double(x, 0, RADIUS, RADIUS, degrees, 180, Arc2D.OPEN);
        }

        private BallAndSocketNode owner;
        private TextContent nameContent;
    }

    /**
     * Constructor, setting default values for properties, setting colors of
     * node.
     */
    public BallAndSocketNode()
    {
        super();
        name = new SingleLineText();
        type = new ChoiceList(ORIENTATION_ARRAY);

        setBackgroundColor(ColorToolsBarPanel.PASTEL_GREY.getBackgroundColor());
        setBorderColor(ColorToolsBarPanel.PASTEL_GREY.getBorderColor());
        setTextColor(ColorToolsBarPanel.PASTEL_GREY.getTextColor());
    }

    protected BallAndSocketNode(BallAndSocketNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        type = node.type.clone();
        createContentStructure();
    }

    @Override
    public void deserializeSupport()
    {
        name.deserializeSupport();

        super.deserializeSupport();
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new BallAndSocketNode(this);
    }

    /*
	 * (non-Javadoc) This method is used for drawing half of circle and text below.
	 *
	 * @see
	 * com.horstmann.violet.product.diagram.abstracts.node.RectangularNode#draw(
	 * java.awt.Graphics2D)
	 */
    @Override
    public void createContentStructure() {

        TextContent nameContent = new TextContent(this.name);
        EmptyContent empty = new EmptyContent();

        ContentInsideShape contentInsideShape = new ContentInsideCustomShape(empty, new BallAndSocketShape(nameContent, this));

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));

        setTextColor(super.getTextColor());

        VerticalLayout verticalGroupContent = new VerticalLayout();
        verticalGroupContent.add(getBorder());
        verticalGroupContent.add(nameContent);

        setContent(verticalGroupContent);
    }

    /*
     * (non-Javadoc) This method is used for setting right starting point for
     * wire
     *
     * @see
     * com.horstmann.violet.product.diagram.abstracts.node.AbstractNode#addChild
     * (com.horstmann.violet.product.diagram.abstracts.node.INode,
     * java.awt.geom.Point2D)
     */
    @Override
    public boolean addChild(INode n, Point2D p) {
        if (n instanceof PointNode) {
            return true;
        }
        return false;
    }

    /**
     * This method returns degree based on socket type selected in properties
     *
     * @return degree of socket node
     */
    private int getDegrees() {
        int degrees = 0;
        if (type != null) {
            String selected = (String)type.getSelectedValue();
            if (selected != null) {
                if (selected.equals("top"))
                    degrees = 180;
                else if (selected.equals("bottom"))
                    degrees = 0;
                else if (selected.equals("right"))
                    degrees = 90;
                else if (selected.equals("left"))
                    degrees = 270;
            }
        }

        return degrees;
    }

    /**
     * Sets the name property value.
     *
     * @param newValue
     *            the new state name
     */
    public void setName(SingleLineText newValue) {
        name = newValue;
    }

    /**
     * Gets the name property value.
     *
     * @return the state name
     */
    public SingleLineText getName() {
        return name;
    }

    /**
     * This getter is used for violet framework in order to make type property
     * visible in property editor. Get choice type list used in node.
     *
     * @return type choice list
     */
    public ChoiceList getType() {
        return type;
    }

    /**
     * This setter is used for violet framework in order to make type property
     * visible in property editor. Set node type choice list.
     *
     * @param type
     *            -
     */
    public void setType(ChoiceList type) {
        this.type = type;
        getContent().refresh();
    }

    private SingleLineText name;
    private ChoiceList type;

    /**
     * Diameter of circle used for creating bounds
     */
    private static int DEFAULT_DIAMETER = 31;

    /**
     * Gap of circle used for creating bounds
     */
    private static int DEFAULT_GAP = 3;

    private static int RADIUS = DEFAULT_DIAMETER + 4 * DEFAULT_GAP;
}
