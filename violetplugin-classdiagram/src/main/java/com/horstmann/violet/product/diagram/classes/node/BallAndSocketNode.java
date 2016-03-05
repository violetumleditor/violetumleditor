package com.horstmann.violet.product.diagram.classes.node;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.classes.ClassDiagramConstant;
import com.horstmann.violet.product.diagram.property.choiceList.ChoiceList;
import com.horstmann.violet.product.diagram.property.choiceList.TextChoiceList;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;
import com.horstmann.violet.product.diagram.common.node.PointNode;

import java.awt.*;
import java.awt.geom.*;

import static com.horstmann.violet.product.diagram.classes.node.BallAndSocketNode.Types.BALL;

/**
 * @author Jakub Homlala This class represents ball and socket (Ball and Socket interface notification)
 */
public class BallAndSocketNode extends ColorableNode
{
    protected enum Types
    {
        BALL,
        SOCKET,
        BALL_AND_SOCKET
    }

    protected class BallAndSocketShape implements ContentInsideCustomShape.ShapeCreator
    {
        @Override
        public Shape createShape(double contentWidth, double contentHeight)
        {
            GeneralPath path = new GeneralPath();

            switch ((Types)type.getSelectedValue())
            {
                case BALL:
                {
                    return new Ellipse2D.Double(DEFAULT_GAP,DEFAULT_GAP,DEFAULT_DIAMETER-2*DEFAULT_GAP,DEFAULT_DIAMETER-2*DEFAULT_GAP);
                }

                case SOCKET:
                {
                    path.append(new Arc2D.Double(0, DEFAULT_GAP, DEFAULT_DIAMETER, DEFAULT_DIAMETER, 0, 180, Arc2D.OPEN),false);
                }break;

                case BALL_AND_SOCKET:
                {
                    path.append(new Ellipse2D.Double(DEFAULT_GAP,DEFAULT_GAP,DEFAULT_DIAMETER-2*DEFAULT_GAP,DEFAULT_DIAMETER-2*DEFAULT_GAP),false);
                    path.append(new Arc2D.Double(0, 0, DEFAULT_DIAMETER, DEFAULT_DIAMETER, 0, 180, Arc2D.OPEN),false);
                }break;
            }

            AffineTransform af = new AffineTransform();
            af.rotate(
                    Math.toRadians((Integer)orientation.getSelectedValue()),
                    path.getBounds().getX() + path.getBounds().width/2,
                    path.getBounds().getY() + path.getBounds().height/2
            );
            path.transform(af);

            return path;
        }
    }

    /**
     * Constructor, setting default values for properties, setting colors of
     * node.
     */
    public BallAndSocketNode()
    {
        super();
        name = new SingleLineText();
        type = new TextChoiceList<Types>(TYPE_KEYS, TYPE_VALUES);
        orientation = new TextChoiceList<Integer>(ORIENTATION_KEYS, ORIENTATION_VALUES);
    }

    protected BallAndSocketNode(BallAndSocketNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        type = node.type.clone();
        orientation = node.orientation.clone();
        createContentStructure();
    }

    @Override
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();

        name.reconstruction();
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new BallAndSocketNode(this);
    }

    @Override
    public void createContentStructure()
    {
        TextContent nameContent = new TextContent(name);

        ContentInsideCustomShape shape = new ContentInsideCustomShape(null, new BallAndSocketShape());
        shape.setMinWidth(DEFAULT_DIAMETER);
        shape.setMinHeight(DEFAULT_DIAMETER);

        setBorder(new ContentBorder(shape, getBorderColor()));
//        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));

        CenterContent centeredShape = new CenterContent(getBorder());

        VerticalLayout verticalGroupContent = new VerticalLayout();
        verticalGroupContent.add(centeredShape);
        verticalGroupContent.add(nameContent);

        setContent(verticalGroupContent);

        setTextColor(super.getTextColor());
    }

    @Override
    public String getToolTip()
    {
        return ClassDiagramConstant.CLASS_DIAGRAM_RESOURCE.getString("ball_and_socket_node.tooltip");
    }

    /**
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
     * @return orientation choice list
     */
    public ChoiceList getOrientation()
    {
        return orientation;
    }

    /**
     * This setter is used for violet framework in order to make type property
     * visible in property editor. Set node type choice list.
     *
     * @param orientation
     */
    public void setOrientation(ChoiceList orientation)
    {
        this.orientation = orientation;
        getContent().refresh();
    }

    public ChoiceList getType()
    {
        return type;
    }

    public void setType(ChoiceList type)
    {
        this.type = type;
        getContent().refresh();
    }


    private SingleLineText name;
    private ChoiceList orientation;
    private ChoiceList type;

    /**
     * Diameter of circle used for creating bounds
     */
    private static final int DEFAULT_DIAMETER = 30;

    /**
     * Gap of circle used for creating bounds
     */
    private static final int DEFAULT_GAP = 5;

    public static final String[] ORIENTATION_KEYS = new String[]{"top", "bottom", "left", "right"};
    public static final Integer[] ORIENTATION_VALUES = new Integer[]{0, 180, 270, 90};
    public static final String[] TYPE_KEYS = new String[]{"Ball and Socket", "Only ball", "Only socket" };
    public static final Types[] TYPE_VALUES = new Types[]{Types.BALL_AND_SOCKET, BALL, Types.SOCKET};
}
