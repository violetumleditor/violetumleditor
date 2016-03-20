package com.horstmann.violet.product.diagram.classes.node;

import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideCustomShape;
import com.horstmann.violet.framework.graphics.shape.ContentInsideEllipse;
import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.common.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.classes.ClassDiagramConstant;
import com.horstmann.violet.product.diagram.property.choiceList.ChoiceList;
import com.horstmann.violet.product.diagram.property.choiceList.TextChoiceList;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;

import java.awt.*;
import java.awt.geom.*;

import static com.horstmann.violet.product.diagram.classes.node.BallAndSocketNode.Types.BALL;

/**
 * This class represents ball and socket (Ball and Socket interface notification)
 *
 * @author Jakub Homlala
 */
public class BallAndSocketNode extends ColorableNode
{
    protected enum Types
    {
        BALL,
        SOCKET,
        BALL_AND_SOCKET
    }

    protected class SocketShape implements ContentInsideCustomShape.ShapeCreator
    {
        @Override
        public Shape createShape(double contentWidth, double contentHeight)
        {
            int angle = orientation.getSelectedValue();
            return new Arc2D.Double(0, 0, DEFAULT_DIAMETER, DEFAULT_DIAMETER, angle, 180, Arc2D.OPEN);
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
        name.setPadding(5,5,5,5);
        type = new TextChoiceList<Types>(TYPE_KEYS, TYPE_VALUES);
        orientation = new TextChoiceList<Integer>(ORIENTATION_KEYS, ORIENTATION_VALUES);

        this.selectedType = this.type.getSelectedPos();
        this.selectedOrientation = this.orientation.getSelectedPos();
    }

    protected BallAndSocketNode(BallAndSocketNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        type = node.type.clone();
        orientation = node.orientation.clone();
        name.setPadding(5,5,5,5);

        this.selectedType = this.type.getSelectedPos();
        this.selectedOrientation = this.orientation.getSelectedPos();

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
        name.reconstruction();
        name.setPadding(5,5,5,5);

        type = new TextChoiceList<Types>(TYPE_KEYS, TYPE_VALUES);
        orientation = new TextChoiceList<Integer>(ORIENTATION_KEYS, ORIENTATION_VALUES);

        type.setSelectedIndex(selectedType);
        orientation.setSelectedIndex(selectedOrientation);
    }

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
        ballBackground.setBackgroundColor(bgColor);
    }

    @Override
    public void setBorderColor(Color borderColor)
    {
        super.setBorderColor(borderColor);
        socketBorder.setBorderColor(borderColor);
        ballBorder.setBorderColor(borderColor);
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

        if(!name.toEdit().isEmpty() && Direction.NORTH.equals(direction))
        {
            return new Point2D.Double(
                    selfBounds.getCenterX(),
                    selfBounds.getMaxY()
            );
        }

        Types type = (Types)getType().getSelectedValue();
        int orientationAngle = orientation.getSelectedValue();
        int directionAngle = 0;

        if(Direction.SOUTH.equals(direction))
        {
            directionAngle = 180;
        }
        else if(Direction.WEST.equals(direction))
        {
            directionAngle = 90;
        }
        else if(Direction.EAST.equals(direction))
        {
            directionAngle = 270;
        }

        boolean supportDirection =(Types.BALL_AND_SOCKET == type && (directionAngle +180)%360 == orientationAngle);
        if(supportDirection)
        {
            orientationAngle +=180;
        }

        if(Types.SOCKET == type || supportDirection)
        {
            double radians = Math.toRadians(orientationAngle);
            return new Point2D.Double(
                    selfBounds.getCenterX() + Math.sin(radians) * DEFAULT_DIAMETER/2,
                    selfBounds.getY() + (1+Math.cos(radians)) * (DEFAULT_DIAMETER)/2
            );
        }
        double topGap = 0;
        if(0 == directionAngle)
        {
            topGap = -DEFAULT_GAP;
        }
        else if(180 == directionAngle)
        {
            topGap = DEFAULT_GAP;
        }
        double radians = Math.toRadians(directionAngle);

        return new Point2D.Double(
                selfBounds.getCenterX() + Math.sin(radians) * (DEFAULT_DIAMETER - DEFAULT_GAP)/2,
                selfBounds.getY() + topGap + (1+Math.cos(radians)) * DEFAULT_DIAMETER/2
        );
    }

    private void refreshBallAndSocketLayout()
    {
        ballAndSocketLayout.remove(ballBackground);
        ballAndSocketLayout.remove(socketBorder);

        Types type = (Types)getType().getSelectedValue();

        if(Types.BALL_AND_SOCKET == type || Types.SOCKET == type)
        {
            Content content = new EmptyContent();
            content.setMinWidth(DEFAULT_DIAMETER);
            content.setMinHeight(DEFAULT_DIAMETER);

            socketBorder = new ContentBorder(
                    new ContentInsideCustomShape(content, new SocketShape()), getBorderColor()
            );
            ballAndSocketLayout.add(socketBorder);
        }
        if(Types.BALL_AND_SOCKET == type || Types.BALL == type)
        {
            Content content = new EmptyContent();
            content.setMinWidth((DEFAULT_DIAMETER-2*DEFAULT_GAP)/Math.sqrt(2));
            content.setMinHeight((DEFAULT_DIAMETER-2*DEFAULT_GAP)/Math.sqrt(2));

            ballBorder = new ContentBorder(new ContentInsideEllipse(content, 1), getBorderColor());
            ballBackground = new ContentBackground(ballBorder, getBackgroundColor());
            ballAndSocketLayout.add(ballBackground, new Point2D.Double(DEFAULT_GAP,DEFAULT_GAP));
        }
    }

    /**
     * Sets the name property value.
     *
     * @param newValue
     *            the new state name
     */
    public void setName(LineText newValue) {
        name.setText(newValue);
    }

    /**
     * Gets the name property value.
     *
     * @return the state name
     */
    public LineText getName() {
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
        if(this.orientation.setSelectedIndex(orientation.getSelectedPos()))
        {
            this.selectedOrientation = orientation.getSelectedPos();
            refreshBallAndSocketLayout();
        }
    }

    public ChoiceList getType()
    {
        return type;
    }

    public void setType(ChoiceList type)
    {
        if(this.type.setSelectedIndex(type.getSelectedPos()))
        {
            this.selectedType = type.getSelectedPos();
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

    public static String[] ORIENTATION_KEYS = new String[]{"top", "bottom", "left", "right"};
    public static final Integer[] ORIENTATION_VALUES = new Integer[]{0, 180, 270, 90};
    public static String[] TYPE_KEYS = new String[]{"ball_and_socket", "ball", "socket" };
    public static final Types[] TYPE_VALUES = new Types[]{Types.BALL_AND_SOCKET, BALL, Types.SOCKET};

    static
    {
        for(int i=0; i< ORIENTATION_KEYS.length; ++i)
        {
            try
            {
                ORIENTATION_KEYS[i] = ClassDiagramConstant.CLASS_DIAGRAM_RESOURCE.getString(
                        ("orientation." + ORIENTATION_KEYS[i]).toLowerCase()
                );
            }catch (Exception ignored){}
        }

        for(int i=0; i< TYPE_KEYS.length; ++i)
        {
            try
            {
                TYPE_KEYS[i] = ClassDiagramConstant.CLASS_DIAGRAM_RESOURCE.getString(
                        ("type." + TYPE_KEYS[i]).toLowerCase()
                );
            }catch (Exception ignored){}
        }
    }
}
