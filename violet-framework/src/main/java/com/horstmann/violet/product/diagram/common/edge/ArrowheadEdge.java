package com.horstmann.violet.product.diagram.common.edge;

import com.horstmann.violet.product.diagram.property.ArrowheadChoiceList;
import com.horstmann.violet.product.diagram.property.choiceList.ChoiceList;
import com.horstmann.violet.product.diagram.abstracts.edge.arrowhead.Arrowhead;

import java.awt.*;

/**
 * TODO javadoc
 * This ...
 *
 * @author Adrian Bobrowski <adrian071993@gmail.com>
 * @date 21.02.2016
 */
public abstract class ArrowheadEdge extends LineEdge
{
    public ArrowheadEdge()
    {
        super();

        startArrowheadChoiceList = new ArrowheadChoiceList();
        endArrowheadChoiceList = new ArrowheadChoiceList();

        setStartArrowhead(ArrowheadChoiceList.NONE);
        setEndArrowhead(ArrowheadChoiceList.NONE);
    }

    protected ArrowheadEdge(ArrowheadEdge arrowheadEdge)
    {
        super(arrowheadEdge);
        this.startArrowheadChoiceList = arrowheadEdge.startArrowheadChoiceList.clone();
        this.endArrowheadChoiceList = arrowheadEdge.endArrowheadChoiceList.clone();
        this.selectedStartArrowhead = arrowheadEdge.startArrowheadChoiceList.getSelectedPos();
        this.selectedEndArrowhead = arrowheadEdge.endArrowheadChoiceList.getSelectedPos();
    }

    @Override
    protected void beforeReconstruction()
    {
        super.beforeReconstruction();

        startArrowheadChoiceList = new ArrowheadChoiceList();
        endArrowheadChoiceList = new ArrowheadChoiceList();

        startArrowheadChoiceList.setSelectedIndex(selectedStartArrowhead);
        endArrowheadChoiceList.setSelectedIndex(selectedEndArrowhead);
    }
    
    @Override
    public void setBorderColor(Color borderColor) {
    	super.setBorderColor(borderColor);
    	getStartArrowhead().setBorderColor(borderColor);
    	getEndArrowhead().setBorderColor(borderColor);
    }
    
    @Override
    public void setBackgroundColor(Color bgColor) {
    	super.setBackgroundColor(bgColor);
    	getStartArrowhead().setFilledColor(bgColor);
    	getEndArrowhead().setFilledColor(bgColor);
    }
    

    /**
     * Draws the edge.
     *
     * @param graphics the graphics context
     */
    public void draw(Graphics2D graphics)
    {
        super.draw(graphics);
        getStartArrowhead().draw(graphics, contactPoints[1], contactPoints[0]);
        getEndArrowhead().draw(graphics, contactPoints[contactPoints.length-2], contactPoints[contactPoints.length-1]);
    }

//    @Override
//    public Shape getShape()
//    {
//        GeneralPath path = getPath();
//        path.append(getStartArrowhead().getPath(), false);
//        path.append(getEndArrowhead().getPath(), false);
//        return path;
//    }

    /**
     * Gets the start arrow head property
     *
     * @return the start arrow head style list
     */
    public final ChoiceList getStartArrowheadChoiceList()
    {
        return startArrowheadChoiceList;
    }

    /**
     * Sets the start arrow head property
     *
     * @param startArrowheadChoiceList the start arrow head list
     */
    public final void setStartArrowheadChoiceList(ChoiceList startArrowheadChoiceList)
    {
        this.startArrowheadChoiceList = (ArrowheadChoiceList)startArrowheadChoiceList;
        this.selectedStartArrowhead = this.startArrowheadChoiceList.getSelectedPos();
    }

    /**
     * Gets the end arrow head property
     *
     * @return the end arrow head style list
     */
    public final ChoiceList getEndArrowheadChoiceList()
    {
        return endArrowheadChoiceList;
    }

    /**
     * Sets the end arrow head property
     *
     * @param endArrowheadChoiceList the end arrow head list
     */
    public final void setEndArrowheadChoiceList(ChoiceList endArrowheadChoiceList)
    {
        this.endArrowheadChoiceList = (ArrowheadChoiceList)endArrowheadChoiceList;
        this.selectedEndArrowhead = this.endArrowheadChoiceList.getSelectedPos();
    }

    /**
     * Gets the start arrowhead style.
     *
     * @return the start arrowhead style
     */
    public final Arrowhead getStartArrowhead()
    {
        return startArrowheadChoiceList.getSelectedValue();
    }

    protected final void setStartArrowhead(Arrowhead startArrowhead)
    {
    	if(startArrowheadChoiceList.setSelectedValue(startArrowhead))
        {
            this.selectedStartArrowhead = startArrowheadChoiceList.getSelectedPos();
        }
    }

    /**
     * Gets the end arrowhead style.
     *
     * @return the end arrowhead style
     */
    public final Arrowhead getEndArrowhead()
    {
    	return endArrowheadChoiceList.getSelectedValue();
    }

    protected final void setEndArrowhead(Arrowhead endArrowhead)
    {
        if(endArrowheadChoiceList.setSelectedValue(endArrowhead))
        {
            this.selectedEndArrowhead = endArrowheadChoiceList.getSelectedPos();
        }
    }

    private transient ArrowheadChoiceList startArrowheadChoiceList = new ArrowheadChoiceList();
    private transient ArrowheadChoiceList endArrowheadChoiceList = new ArrowheadChoiceList();

    private int selectedStartArrowhead;
    private int selectedEndArrowhead;
}
