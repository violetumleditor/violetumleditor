package com.horstmann.violet.product.diagram.common.edge;

import com.horstmann.violet.product.diagram.abstracts.edge.ShapeEdge;
import com.horstmann.violet.product.diagram.property.BentStyleChoiceList;
import com.horstmann.violet.product.diagram.property.LineStyleChoiceList;
import com.horstmann.violet.product.diagram.property.choiceList.ChoiceList;
import com.horstmann.violet.product.diagram.abstracts.Direction;
import com.horstmann.violet.product.diagram.abstracts.edge.bentstyle.BentStyle;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

/**
 * An edge that is composed of multiple line segments
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 21.02.2016
 */
public abstract class LineEdge extends ShapeEdge
{
    public LineEdge()
    {
        super();
        lineStyleChoiceList = new LineStyleChoiceList();
        bentStyleChoiceList = new BentStyleChoiceList();

        setBentStyle(BentStyle.AUTO);
        setLineStyle(LineStyleChoiceList.SOLID);
    }

    protected LineEdge(LineEdge lineEdge)
    {
        this.bentStyleChoiceList = lineEdge.bentStyleChoiceList.clone();
        this.lineStyleChoiceList = lineEdge.lineStyleChoiceList.clone();
        this.selectedBentStyle = this.bentStyleChoiceList.getSelectedPos();
        this.selectedLineStyle = this.lineStyleChoiceList.getSelectedPos();
    }

    @Override
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();
        lineStyleChoiceList = new LineStyleChoiceList();
        bentStyleChoiceList = new BentStyleChoiceList();

        lineStyleChoiceList.setSelectedIndex(selectedLineStyle);
        bentStyleChoiceList.setSelectedIndex(selectedBentStyle);
    }

    @Override
    public void setTransitionPoints(Point2D[] transitionPoints)
    {
    	if(transitionPoints != null && transitionPoints.length > 0)
    	{
    		setBentStyle(BentStyle.FREE);
    	}
        super.setTransitionPoints(transitionPoints);
    }

    @Override
    public boolean isTransitionPointsSupported()
    {
        return true;
    }

    /**
     * Draws the edge.
     *
     * @param graphics the graphics context
     */
    public void draw(Graphics2D graphics)
    {
        updateContactPoints();

        Color oldColor = graphics.getColor();
        Stroke oldStroke = graphics.getStroke();

        graphics.setColor(getBorderColor());
        graphics.setStroke(getLineStyle());
        graphics.draw(getPath());
        graphics.setStroke(oldStroke);
        graphics.setColor(oldColor);
    }

    @Override
    public Shape getShape()
    {
        return getPath();
    }

    protected GeneralPath getPath()
    {
        GeneralPath path = new GeneralPath();
        path.moveTo(contactPoints[0].getX(), contactPoints[0].getY());

        for (int i = 1; i < contactPoints.length; ++i)
        {
            path.lineTo(contactPoints[i].getX(), contactPoints[i].getY());
        }
        return path;
    }

    protected void updateContactPoints()
    {

        if (getStartNode().equals(getEndNode()))
        {
            Rectangle2D nodeBounds = getStartNode().getBounds();
            Point2D nodeLocation = getStartNode().getLocationOnGraph();
            contactPoints = new Point2D[5];
            contactPoints[0] = new Point2D.Double(nodeLocation.getX() + nodeBounds.getWidth(), nodeLocation.getY() + nodeBounds.getHeight()/2);
            contactPoints[1] = new Point2D.Double(contactPoints[0].getX()+SELF_LOOP_GAP_X, contactPoints[0].getY());
            contactPoints[2] = new Point2D.Double(contactPoints[1].getX(), nodeLocation.getY() + nodeBounds.getHeight() + SELF_LOOP_GAP_Y);
            contactPoints[3] = new Point2D.Double(contactPoints[0].getX()- nodeBounds.getWidth()/2, contactPoints[2].getY());
            contactPoints[4] = new Point2D.Double(contactPoints[3].getX(), contactPoints[2].getY()-SELF_LOOP_GAP_Y);
        }
        else
        {
        	// Step 1 : update contacts points with node centers (to get correct edge directions)
        	List<Point2D> points = new ArrayList<Point2D>();
            Rectangle2D startBounds = getStartNode().getBounds();
            Rectangle2D endBounds = getEndNode().getBounds();
            Point2D startLocationOnGraph = getStartNode().getLocationOnGraph();
            Point2D endLocationOnGraph = getEndNode().getLocationOnGraph();
            Point2D startCenter = new Point2D.Double(startLocationOnGraph.getX() + startBounds.getWidth() / 2, startLocationOnGraph.getY() + startBounds.getHeight() / 2);
            Point2D endCenter = new Point2D.Double(endLocationOnGraph.getX() + endBounds.getWidth() / 2, endLocationOnGraph.getY() + endBounds.getHeight() / 2);
            points.add(startCenter);
            points.addAll(Arrays.asList(getTransitionPoints()));
            points.add(endCenter);
            Point2D[] bentStylePointsAsArray = points.toArray(new Point2D[points.size()]);
            points = getBentStyle().getPath(bentStylePointsAsArray);
            contactPoints = new Point2D[points.size()];
            points.toArray(contactPoints);
            
            // Step 2 : As direction are ok with previous step, connection points are also ok. Get real contact points.
            Line2D connectionPoints = getConnectionPoints();
        	Point2D startingPoint = connectionPoints.getP1();
        	Point2D endingPoint = connectionPoints.getP2();
        	points.clear();
        	points.add(startingPoint);
            points.addAll(Arrays.asList(getTransitionPoints()));
            points.add(endingPoint);
            bentStylePointsAsArray = points.toArray(new Point2D[points.size()]);
            points = getBentStyle().getPath(bentStylePointsAsArray);
            contactPoints = new Point2D[points.size()];
            points.toArray(contactPoints);
        }
    }

    /**
     * Gets the bentStyle property
     *
     * @return the bent style list
     */
    public final ChoiceList getBentStyleChoiceList()
    {
        return bentStyleChoiceList;
    }

    /**
     * Sets the bentStyle property
     *
     * @param bentStyleChoiceList the bent style list
     */
    public final void setBentStyleChoiceList(ChoiceList bentStyleChoiceList)
    {
        this.bentStyleChoiceList = (BentStyleChoiceList)bentStyleChoiceList;
        this.selectedBentStyle = this.bentStyleChoiceList.getSelectedPos();
    }

    /**
     * Gets the line style property.
     *
     * @return the line style list
     */
    public final ChoiceList getLineStyleChoiceList()
    {
        return lineStyleChoiceList;
    }

    /**
     * Sets the line style property.
     *
     * @param lineStyleChoiceList the line style list
     */
    public final void setLineStyleChoiceList(ChoiceList lineStyleChoiceList)
    {
        this.lineStyleChoiceList = (LineStyleChoiceList)lineStyleChoiceList;
        this.selectedLineStyle = this.lineStyleChoiceList.getSelectedPos();
    }

    /**
     * Gets the line style.
     *
     * @return the line style
     */
    public final Stroke getLineStyle()
    {
        return lineStyleChoiceList.getSelectedValue();
    }

    protected final void setLineStyle(Stroke stroke)
    {
        if(lineStyleChoiceList.setSelectedValue(stroke))
        {
            this.selectedLineStyle = lineStyleChoiceList.getSelectedPos();
        }
    }

    /**
     * Gets the bent style.
     *
     * @return the bent style
     */
    public final BentStyle getBentStyle()
    {
    	if (!bentStyleChoiceList.getSelectedValue().equals(BentStyle.FREE)) {
    		clearTransitionPoints();
    	}
    	if (!bentStyleChoiceList.getSelectedValue().equals(BentStyleChoiceList.AUTO))
        {
            return bentStyleChoiceList.getSelectedValue();
        }

        Direction startDirection = getDirection(getStartNode()).getNearestCardinalDirection();
        Direction endDirection = getDirection(getEndNode()).getNearestCardinalDirection();

        if ((Direction.NORTH.equals(startDirection) || Direction.SOUTH.equals(startDirection)) &&
            (Direction.NORTH.equals(endDirection)   || Direction.SOUTH.equals(endDirection)))
        {
            return BentStyleChoiceList.VHV;
        }
        else if ((Direction.NORTH.equals(startDirection) || Direction.SOUTH.equals(startDirection)) &&
                 (Direction.EAST.equals(endDirection)    || Direction.WEST.equals(endDirection)))
        {
            return BentStyleChoiceList.VH;
        }
        else if ((Direction.EAST.equals(startDirection) || Direction.WEST.equals(startDirection)) &&
                 (Direction.NORTH.equals(endDirection)  || Direction.SOUTH.equals(endDirection)))
        {
            return BentStyleChoiceList.HV;
        }
        else if ((Direction.EAST.equals(startDirection) || Direction.WEST.equals(startDirection)) &&
                 (Direction.EAST.equals(endDirection)   || Direction.WEST.equals(endDirection)))
        {
            return BentStyleChoiceList.HVH;
        }
        return BentStyleChoiceList.STRAIGHT;
    }

    protected final void setBentStyle(BentStyle bentStyle)
    {
        if(bentStyleChoiceList.setSelectedValue(bentStyle))
        {
        	this.selectedBentStyle = bentStyleChoiceList.getSelectedPos();
        }
    }

    private transient LineStyleChoiceList lineStyleChoiceList;
    private transient BentStyleChoiceList bentStyleChoiceList;

    private int selectedBentStyle;
    private int selectedLineStyle;

    public static final int SELF_LOOP_GAP_X = 20;
    public static final int SELF_LOOP_GAP_Y = 22;
}
