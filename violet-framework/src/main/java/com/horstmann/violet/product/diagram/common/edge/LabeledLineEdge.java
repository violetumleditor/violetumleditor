package com.horstmann.violet.product.diagram.common.edge;

import com.horstmann.violet.framework.graphics.content.TextContent;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.edge.bentstyle.BentStyle;
import com.horstmann.violet.product.diagram.property.text.LineText;
import com.horstmann.violet.product.diagram.property.text.SingleLineText;
import com.horstmann.violet.product.diagram.abstracts.Direction;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 22.02.2016
 */
public class LabeledLineEdge extends ArrowheadEdge
{
    public LabeledLineEdge()
    {
        super();

        startLabel = new SingleLineText();
        centerLabel = new SingleLineText();
        endLabel = new SingleLineText();

        startTextContent = new TextContent(startLabel);
        centerTextContent = new TextContent(centerLabel);
        endTextContent = new TextContent(endLabel);
    }

    protected LabeledLineEdge(LabeledLineEdge cloned)
    {
        super(cloned);
        this.startLabel = cloned.startLabel.clone();
        this.centerLabel = cloned.centerLabel.clone();
        this.endLabel = cloned.endLabel.clone();

        startTextContent = new TextContent(startLabel);
        centerTextContent = new TextContent(centerLabel);
        endTextContent = new TextContent(endLabel);
    }

    @Override
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();

        if(null == startLabel)
        {
            startLabel = new SingleLineText();
        }
        if(null == centerLabel)
        {
            centerLabel = new SingleLineText();
        }
        if(null == endLabel)
        {
            endLabel = new SingleLineText();
        }

    }

	@Override
	protected void createContentStructure() {
		super.createContentStructure();
		startTextContent = new TextContent(startLabel);
		centerTextContent = new TextContent(centerLabel);
		endTextContent = new TextContent(endLabel);
		
		startLabel.reconstruction();
		centerLabel.reconstruction();
		endLabel.reconstruction();
	}
	
	@Override
	public void setBorderColor(Color borderColor) {
		super.setBorderColor(borderColor);
		startLabel.setTextColor(borderColor);
		centerLabel.setTextColor(borderColor);
		endLabel.setTextColor(borderColor);
	}
    
    @Override
    protected IEdge copy() throws CloneNotSupportedException
    {
        return new LabeledLineEdge(this);
    }

    /**
     * Draws the edge.
     *
     * @param graphics the graphics context
     */
    @Override
    public void draw(Graphics2D graphics)
    {
        super.draw(graphics);
        //Color oldColor = graphics.getColor();
        //graphics.setColor(getBorderColor());
        drawContent(graphics, startTextContent, contactPoints[0], contactPoints[1], false);
        drawContent(graphics, centerTextContent, contactPoints[contactPoints.length/2-1], contactPoints[contactPoints.length/2], true);
        drawContent(graphics, endTextContent, contactPoints[contactPoints.length-1], contactPoints[contactPoints.length-2], false);
        //graphics.setColor(oldColor);
    }

    private void drawContent(Graphics2D graphics, TextContent textContent, Point2D startPoint, Point2D endPoint, boolean center)
    {
        Rectangle2D textBounds = textContent.getBounds();
        Direction direction = new Direction(startPoint, endPoint);
        Direction nearestDirection = direction.getNearestCardinalDirection();
        double x;
        double y;

        if(center)
        {
            x = (startPoint.getX() + endPoint.getX()) / 2;
            y = (startPoint.getY() + endPoint.getY()) / 2;
        }
        else
        {
            x = startPoint.getX();
            y = startPoint.getY();
        }

        if(BentStyle.FREE == getBentStyle() || BentStyle.STRAIGHT == getBentStyle())
        {
            double tan = Math.atan2(direction.getY(), direction.getX());
            if(0>direction.getX())
            {
                tan+=Math.PI;
            }

            graphics.translate(x,y);
            graphics.rotate(tan);
            if(center)
            {
            	textContent.draw(graphics, new Point2D.Double(-textContent.getWidth() / 2, -textContent.getHeight()));
            }
            else
            {
                if(0>direction.getX())
                {
                    textContent.draw(graphics, new Point2D.Double(-LABEL_GAP - textContent.getWidth(),-textContent.getHeight()));
                }
                else
                {
                    textContent.draw(graphics, new Point2D.Double(LABEL_GAP,-textContent.getHeight()));
                }
            }
            graphics.rotate(-tan);
            graphics.translate(-x,-y);
        }
        else
        {
            if(center)
            {
                if(Direction.NORTH.equals(nearestDirection) || Direction.SOUTH.equals(nearestDirection))
                {
                    y -= textBounds.getHeight()/2;
                }
                else
                {
                    x -= textBounds.getWidth()/2;
                    y -= textBounds.getHeight();
                }
            }
            else
            {
                if(Direction.EAST.equals(nearestDirection))
                {
                    x += LABEL_GAP;
                    y -= textBounds.getHeight();
                }
                else if(Direction.WEST.equals(nearestDirection))
                {
                    x -= textBounds.getWidth() + LABEL_GAP;
                    y -= textBounds.getHeight();
                }
                else if(Direction.SOUTH.equals(nearestDirection))
                {
                    y += LABEL_GAP;
                }
                else if(Direction.NORTH.equals(nearestDirection))
                {
                    y -= textBounds.getHeight() + LABEL_GAP;
                }
            }
            textContent.draw(graphics, new Point2D.Double(x,y));
        }
    }

    public LineText getStartLabel()
    {
        return startLabel;
    }

    public void setStartLabel(LineText startLabel)
    {
        this.startLabel.setText(startLabel.toEdit());
    }

    public void setStartLabel(String startLabel)
    {
        this.startLabel.setText(startLabel);
    }

    public LineText getCenterLabel()
    {
        return centerLabel;
    }

    public void setCenterLabel(LineText centerLabel)
    {
        this.centerLabel.setText(centerLabel.toEdit());
    }

    public void setCenterLabel(String centerLabel)
    {
        this.centerLabel.setText(centerLabel);
    }

    public LineText getEndLabel()
    {
        return endLabel;
    }

    public void setEndLabel(LineText endLabel)
    {
        this.endLabel.setText(endLabel.toEdit());
    }

    public void setEndLabel(String endLabel)
    {
        this.endLabel.setText(endLabel);
    }

    protected TextContent getStartTextContent()
    {
        return startTextContent;
    }

    protected TextContent getCenterTextContent()
    {
        return centerTextContent;
    }

    protected TextContent getEndTextContent()
    {
        return endTextContent;
    }

    private SingleLineText startLabel;
    private SingleLineText centerLabel;
    private SingleLineText endLabel;

    private transient TextContent startTextContent;
    private transient TextContent centerTextContent;
    private transient TextContent endTextContent;

    public static final int LABEL_GAP = 7;


}
