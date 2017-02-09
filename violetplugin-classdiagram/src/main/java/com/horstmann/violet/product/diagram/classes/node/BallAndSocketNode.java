package com.horstmann.violet.product.diagram.classes.node;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.framework.graphics.shape.ContentInsideEllipse;
import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.classes.ClassDiagramConstant;
import com.horstmann.violet.product.diagram.common.node.ColorableNode;
import com.horstmann.violet.product.diagram.property.choiceList.ChoiceList;
import com.horstmann.violet.product.diagram.property.choiceList.TextChoiceList;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * This class represents ball and socket (Ball and Socket interface notification)
 *
 * @author Jakub Homlala
 */
public class BallAndSocketNode extends ColorableNode
{
    private enum Types
    {
        BALL,
        SOCKET,
        BALL_AND_SOCKET
    }

    private class SocketShape implements ContentInsideCustomShape.ShapeCreator
    {
        @Override
        public Shape createShape(double contentWidth, double contentHeight)
        {
            int angle = orientation.getSelectedValue();
            return new Arc2D.Double(0, 0, DEFAULT_DIAMETER, DEFAULT_DIAMETER, angle, DIRECTION_SOUTH, Arc2D.OPEN);
        }
    }

    /**
     * Initializes new instance with default values.
     */
    public BallAndSocketNode()
    {
        super();
        name = new SingleLineText();
        name.setPadding(5, 5, 5, 5);
        type = new TextChoiceList<Types>(TYPE_KEYS, TYPE_VALUES);
        orientation = new TextChoiceList<Integer>(ORIENTATION_KEYS, ORIENTATION_VALUES);

        selectedType = type.getSelectedPos();
        selectedOrientation = orientation.getSelectedPos();
    }

    /**
     * Creates copy of an instance
     *
     * @param node a node to copy
     * @throws CloneNotSupportedException when instance cannot be cloned.
     */
    private BallAndSocketNode(BallAndSocketNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        type = node.type.clone();
        orientation = node.orientation.clone();
        name.setPadding(5, 5, 5, 5);

        selectedType = type.getSelectedPos();
        selectedOrientation = orientation.getSelectedPos();

        createContentStructure();
    }

    @Override
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();

        if (null == name)
        {
            name = new SingleLineText();
        }
        name.reconstruction();
        name.setPadding(5, 5, 5, 5);

        type = new TextChoiceList<Types>(TYPE_KEYS, TYPE_VALUES);
        orientation = new TextChoiceList<Integer>(ORIENTATION_KEYS, ORIENTATION_VALUES);

        type.setSelectedIndex(selectedType);
        orientation.setSelectedIndex(selectedOrientation);
    }

    /**
     * Creates and returns a copy of BallAndSocketNode instance.
     *
     * @return a cloned instance.
     * @throws CloneNotSupportedException when instance cannot be cloned.
     */
    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new BallAndSocketNode(this);
    }

    @Override
    public void createContentStructure()
    {
        VerticalLayout verticalGroupContent = new VerticalLayout();

        ballAndSocketLayout = new RelativeLayout();
        ballAndSocketLayout.setMinWidth(DEFAULT_DIAMETER);
        ballAndSocketLayout.setMinHeight(DEFAULT_DIAMETER);
        refreshBallAndSocketLayout();

        TextContent nameContent = new TextContent(name);

        verticalGroupContent.add(new CenterContent(ballAndSocketLayout));
        verticalGroupContent.add(nameContent);

        setContent(verticalGroupContent);
        setTextColor(super.getTextColor());
    }

    @Override
    public void setBackgroundColor(Color bgColor)
    {
        super.setBackgroundColor(bgColor);
        if (ballBackground != null)
        {
            ballBackground.setBackgroundColor(bgColor);
        }
    }

    @Override
    public void setBorderColor(Color borderColor)
    {
        super.setBorderColor(borderColor);
        if (socketBorder != null)
        {
            socketBorder.setBorderColor(borderColor);
        }
        if (ballBorder != null)
        {
            ballBorder.setBorderColor(borderColor);
        }
    }

    @Override
    public String getToolTip()
    {
        return ClassDiagramConstant.CLASS_DIAGRAM_RESOURCE.getString("tooltip.ball_and_socket_node");
    }

    @Override
    public Point2D getConnectionPoint(IEdge edge)
    {
        Direction direction = edge.getDirection(this).getNearestCardinalDirection();
        Rectangle2D selfBounds = getBounds();

        if (!name.toEdit().isEmpty() && Direction.NORTH.equals(direction))
        {
            return new Point2D.Double(
                    selfBounds.getCenterX(),
                    selfBounds.getMaxY()
            );
        }

        Types type = (Types) getType().getSelectedValue();
        int orientationAngle = orientation.getSelectedValue();
        int directionAngle = getDirectionAngle(direction);

        boolean isSupportedDirection = (Types.BALL_AND_SOCKET == type &&
                (directionAngle + DIRECTION_SOUTH) % 360 == orientationAngle);
        if (isSupportedDirection)
        {
            orientationAngle += DIRECTION_SOUTH;
        }

        if (Types.SOCKET == type || isSupportedDirection)
        {
            double radians = Math.toRadians(orientationAngle);
            return new Point2D.Double(
                    selfBounds.getCenterX() + Math.sin(radians) * DEFAULT_DIAMETER / 2,
                    selfBounds.getY() + (1 + Math.cos(radians)) * (DEFAULT_DIAMETER) / 2
            );
        }

        double topGap = getTopGap(directionAngle);
        double radians = Math.toRadians(directionAngle);

        return new Point2D.Double(
                selfBounds.getCenterX() + Math.sin(radians) * (DEFAULT_DIAMETER - DEFAULT_GAP) / 2,
                selfBounds.getY() + topGap + (1 + Math.cos(radians)) * DEFAULT_DIAMETER / 2
        );
    }

    private int getDirectionAngle(Direction direction)
    {
        if (Direction.SOUTH.equals(direction))
        {
            return DIRECTION_SOUTH;
        }
        else if (Direction.WEST.equals(direction))
        {
            return DIRECTION_WEST;
        }
        else if (Direction.EAST.equals(direction))
        {
            return DIRECTION_EAST;
        }

        return DIRECTION_NORTH;
    }

    private double getTopGap(int directionAngle)
    {
        if (DIRECTION_NORTH == directionAngle)
        {
            return -DEFAULT_GAP;
        }
        else if (DIRECTION_SOUTH == directionAngle)
        {
            return DEFAULT_GAP;
        }

        return DIRECTION_NORTH;
    }

    private void refreshBallAndSocketLayout()
    {
        Types type = (Types) getType().getSelectedValue();

        if (Types.BALL_AND_SOCKET == type || Types.SOCKET == type)
        {
            refreshSocket();
        }
        if (Types.BALL_AND_SOCKET == type || Types.BALL == type)
        {
            refreshBall();
        }
    }

    /**
     * Refresh socket in layout
     */
    private void refreshSocket()
    {
        ballAndSocketLayout.remove(socketBorder);
        Content content = new EmptyContent();
        content.setMinWidth(DEFAULT_DIAMETER);
        content.setMinHeight(DEFAULT_DIAMETER);

        socketBorder = new ContentBorder(new ContentInsideCustomShape(content, new SocketShape()), getBorderColor());
        ballAndSocketLayout.add(socketBorder);
    }

    /**
     * Refresh ball in layout.
     */
    private void refreshBall()
    {
        ballAndSocketLayout.remove(ballBackground);
        Content content = new EmptyContent();
        content.setMinWidth((DEFAULT_DIAMETER - 2 * DEFAULT_GAP) / Math.sqrt(2));
        content.setMinHeight((DEFAULT_DIAMETER - 2 * DEFAULT_GAP) / Math.sqrt(2));

        ballBorder = new ContentBorder(new ContentInsideEllipse(content, 1), getBorderColor());
        ballBackground = new ContentBackground(ballBorder, getBackgroundColor());
        ballAndSocketLayout.add(ballBackground, new Point2D.Double(DEFAULT_GAP, DEFAULT_GAP));
    }

    public void setName(LineText newValue)
    {
        name.setText(newValue);
    }

    public LineText getName()
    {
        return name;
    }

    public ChoiceList getType()
    {
        return type;
    }

    public void setType(ChoiceList type)
    {
        if (this.type.setSelectedIndex(type.getSelectedPos()))
        {
            selectedType = type.getSelectedPos();
            refreshBallAndSocketLayout();
        }

    @Override
    public LineText getAttributes() {
        return null;
    }

    @Override
    public LineText getMethods() {
        return null;

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
     * @param orientation orientation choice list
     */
    public void setOrientation(ChoiceList orientation)
    {
        if (this.orientation.setSelectedIndex(orientation.getSelectedPos()))
        {
            selectedOrientation = orientation.getSelectedPos();
            refreshBallAndSocketLayout();
        }
    }

    private SingleLineText name;

    private int selectedOrientation;
    private int selectedType;

    private transient TextChoiceList<Integer> orientation;
    private transient TextChoiceList<Types> type;

    private transient RelativeLayout ballAndSocketLayout;
    private transient ContentBorder socketBorder;
    private transient ContentBorder ballBorder;
    private transient ContentBackground ballBackground;

    /**
     * Diameter of circle used for creating bounds
     */
    private static final int DEFAULT_DIAMETER = 30;

    /**
     * Gap of circle used for creating bounds
     */
    private static final int DEFAULT_GAP = 5;

    private static final int DIRECTION_NORTH = 0;
    private static final int DIRECTION_SOUTH = 180;
    private static final int DIRECTION_WEST = 90;
    private static final int DIRECTION_EAST = 270;

    private static String[] ORIENTATION_KEYS = new String[] { "top", "bottom", "left", "right" };
    private static final Integer[] ORIENTATION_VALUES = new Integer[] {
            DIRECTION_NORTH, DIRECTION_SOUTH,
            DIRECTION_EAST, DIRECTION_WEST
    };
    private static String[] TYPE_KEYS = new String[] { "ball_and_socket", "ball", "socket" };
    private static final Types[] TYPE_VALUES = new Types[] { Types.BALL_AND_SOCKET, Types.BALL, Types.SOCKET };

    static
    {
        for (int i = 0; i < ORIENTATION_KEYS.length; ++i)
        {
            ORIENTATION_KEYS[i] = ClassDiagramConstant.CLASS_DIAGRAM_RESOURCE.getString(
                    ("orientation." + ORIENTATION_KEYS[i]).toLowerCase());
        }

        for (int i = 0; i < TYPE_KEYS.length; ++i)
        {
            TYPE_KEYS[i] = ClassDiagramConstant.CLASS_DIAGRAM_RESOURCE.getString(
                    ("type." + TYPE_KEYS[i]).toLowerCase());
        }
    }
}
